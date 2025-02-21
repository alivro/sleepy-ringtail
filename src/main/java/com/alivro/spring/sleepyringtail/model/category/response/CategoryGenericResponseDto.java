package com.alivro.spring.sleepyringtail.model.category.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryGenericResponseDto {
    // Identificador único de la categoría
    private Integer id;

    // Nombre de la categoría
    private String name;

    // Descripción de la categoría
    private String description;
}
