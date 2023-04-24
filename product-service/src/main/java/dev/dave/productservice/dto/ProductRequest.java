package dev.dave.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder // to create the builder method
@AllArgsConstructor
@NoArgsConstructor

public class ProductRequest {
    private String name;
    private String description;
    private BigDecimal price;
}
