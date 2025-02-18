package com.alivro.spring.sleepyringtail.model.subcategory.response;

import com.alivro.spring.sleepyringtail.model.Subcategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubcategoryResponseDto {
    // Identificador único de la subcategoría
    private Integer id;

    // Nombre de la subcategoría
    private String name;

    // Descripción de la subcategoría
    private String description;

    // Categoría de la subcategoría
    private CategoryOfSubcategoryResponseDto category;

    /**
     * Convierte un objeto Entity en un objeto ResponseDto
     *
     * @param entity Información de la subcategoría
     * @return Representación ResponseDto de la información de la subcategoría
     */
    public static SubcategoryResponseDto mapEntityToResponseDto(Subcategory entity) {
        CategoryOfSubcategoryResponseDto categoryOfSubcategory =
                CategoryOfSubcategoryResponseDto.mapEntityToResponseDto(entity.getCategory());

        return SubcategoryResponseDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .category(categoryOfSubcategory)
                .build();
    }
}
