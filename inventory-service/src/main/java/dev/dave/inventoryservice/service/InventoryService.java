package dev.dave.inventoryservice.service;

import dev.dave.inventoryservice.dto.InventoryResponse;
import dev.dave.inventoryservice.repository.InventoryRepository;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j

public class InventoryService {

    private final InventoryRepository inventoryRepository;
    @Transactional(readOnly = true)
    @SneakyThrows // do not use this production only. onlyfor developemt to ignore exceptions
    public List<InventoryResponse> isInStock(List<String> skuCode) {
//        log.info("Wait Stated");
//        Thread.sleep(10000);
//        log.info("Wait Ended");
        return inventoryRepository.findBySkuCodeIn(skuCode).stream()
                .map(inventory ->
                        InventoryResponse.builder()
                                .skuCode(inventory.getSkuCode())
                                .isInStock(inventory.getQuantity() > 0)
                                .build()

                ).toList();

    }

}
