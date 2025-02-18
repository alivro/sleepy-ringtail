package com.alivro.spring.sleepyringtail.model.category.response;

import com.alivro.spring.sleepyringtail.model.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

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
    private List<SubcategoryOfCategoryResponseDto> subcategories;

    /**
     * Convierte un objeto Entity en un objeto ResponseDto
     *
     * @param entity Información de la categoría
     * @return Representación ResponseDto de la información de la categoría
     */
    public static CategoryGetResponseDto mapEntityToResponseDto(Category entity) {
        List<SubcategoryOfCategoryResponseDto> subcategoriesOfCategory = entity.getSubcategories().stream()
                .map(SubcategoryOfCategoryResponseDto::mapEntityToResponseDto)
                .collect(Collectors.toList());

        return CategoryGetResponseDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .subcategories(subcategoriesOfCategory)
                .build();
    }
}
