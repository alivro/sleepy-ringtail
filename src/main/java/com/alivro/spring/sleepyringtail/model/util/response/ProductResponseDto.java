package com.alivro.spring.sleepyringtail.model.util.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponseDto {
    // Identificador único del producto
    private Integer id;

    // Nombre del producto
    private String name;
}
