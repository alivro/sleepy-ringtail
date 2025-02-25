package com.alivro.spring.sleepyringtail.model.subcategory.response;

import com.alivro.spring.sleepyringtail.model.util.response.CategoryResponseDto;
import com.alivro.spring.sleepyringtail.model.util.response.ProductResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubcategoryGetResponseDto {
    // Identificador único de la subcategoría
    private Integer id;

    // Nombre de la subcategoría
    private String name;

    // Descripción de la subcategoría
    private String description;

    // Categoría de la subcategoría
    private CategoryResponseDto category;

    // Producto(s) de la subcategoría
    private List<ProductResponseDto> products;
}
