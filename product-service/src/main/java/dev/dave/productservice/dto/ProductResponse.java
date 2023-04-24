package dev.dave.productservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor

/**
 * Good to separate module class entities and data transsfer objects
 * to not expose any other variable other than the required dta for transfer
 */
public class ProductResponse {
    private String id;
    private String name;
    private String description;
    private BigDecimal price;
}
