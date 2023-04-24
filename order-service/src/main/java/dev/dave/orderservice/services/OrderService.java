package dev.dave.orderservice.services;

import dev.dave.orderservice.dto.InventoryResponse;
import dev.dave.orderservice.dto.OrderLineItemsDto;
import dev.dave.orderservice.dto.OrderRequest;
import dev.dave.orderservice.event.OrderPlacedEvent;
import dev.dave.orderservice.model.Order;
import dev.dave.orderservice.model.OrderLineItems;
import dev.dave.orderservice.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor // generate required constructors at compiled time
@Transactional
public class OrderService {

    private final OrderRepository orderRepository; // injecting the repository to the service
    private final WebClient.Builder webClientBuilder; // injecting webclient
    private final KafkaTemplate<String, OrderPlacedEvent> kafkaTemplate;
     public String placeOrder(OrderRequest orderRequest){
         Order order = new Order();
         order.setOrderNumber(UUID.randomUUID().toString());

         List<OrderLineItems> orderLineItems = orderRequest.getOrderLineItemsDtoList()
                 .stream()
                 .map(OrderLineItemsDto -> mapToDto(OrderLineItemsDto))
                 .toList();

         order.setOrderLineItemsList(orderLineItems);

         List<String> skuCodes = order.getOrderLineItemsList().stream().map(orderLineItem-> orderLineItem.getSkuCode())
                 .toList();



         // Call Inventory Service, place order if product is in stock
         InventoryResponse[] inventoryResponsesArray = webClientBuilder.build().get()
                 .uri("http://inventory-service/api/inventory",
                         uriBuilder -> uriBuilder.queryParam("skuCode", skuCodes).build())
                         .retrieve()
                                .bodyToMono(InventoryResponse[].class) // to be able to read data from webclient response
                                        .block(); // add this to make webclient synchronous request


         boolean allProductsInStock = Arrays.stream(inventoryResponsesArray)
                 .allMatch(inventoryResponse -> inventoryResponse.isInStock());


         if(allProductsInStock) {
             orderRepository.save(order);
             kafkaTemplate.send("notificationTopic", new OrderPlacedEvent(order.getOrderNumber()) );
             return "Order placed successfully";
         }
         else
             throw new IllegalArgumentException("Product is not in stock. Try again later");
         }

         private OrderLineItems mapToDto(OrderLineItemsDto orderLineItemsDto){
         OrderLineItems orderLineItems = new OrderLineItems();
         orderLineItems.setPrice(orderLineItemsDto.getPrice());
         orderLineItems.setQuantity(orderLineItemsDto.getQuantity());
         orderLineItems.setSkuCode(orderLineItemsDto.getSkuCode());
         return orderLineItems;
    }
}
