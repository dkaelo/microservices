package dev.dave.productservice.service;

import dev.dave.productservice.dto.ProductRequest;
import dev.dave.productservice.dto.ProductResponse;
import dev.dave.productservice.model.Product;
import dev.dave.productservice.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor // at compile time it will create constructor with all required parameters
@Slf4j //logs
public class ProductService {

    private final ProductRepository productRepository;


    public void createProduct(ProductRequest productRequest){
        Product product = Product.builder()
                .name(productRequest.getName())
                .description(productRequest.getDescription())
                .price(productRequest.getPrice())
                .build();

        productRepository.save(product);
        log.info("Product {} is saved", product.getId()); // placeholder from Sl4j
    }

    public List<ProductResponse> getAllProducts(){
        List<Product> products = productRepository.findAll();

        return products.stream().map(this::mapTOProductResponse).toList(); // clarify this
    }

    private ProductResponse mapTOProductResponse(Product product){
        return ProductResponse.builder()
                .id(product.getId())
                .name(product.getName())
                .description((product.getDescription()))
                .price(product.getPrice())
                .build();

    }

}
