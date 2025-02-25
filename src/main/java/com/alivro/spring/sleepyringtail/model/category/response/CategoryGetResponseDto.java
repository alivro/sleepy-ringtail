package com.alivro.spring.sleepyringtail.model.category.response;

import com.alivro.spring.sleepyringtail.model.util.response.SubcategoryResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryGetResponseDto {
    // Identificador único de la categoría
    private Integer id;

    // Nombre de la categoría
    private String name;

    // Descripción de la categoría
    private String description;

    // Subcategoría(s) de la categoría
    private List<SubcategoryResponseDto> subcategories;
}
