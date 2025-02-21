package com.alivro.spring.sleepyringtail.model.util.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponseDto {
    // Identificador único del producto
    private Integer id;

    // Nombre del producto
    private String name;

    // Tamaño/Presentación del producto
    private String size;

    // Precio del producto
    private BigDecimal price;
}
