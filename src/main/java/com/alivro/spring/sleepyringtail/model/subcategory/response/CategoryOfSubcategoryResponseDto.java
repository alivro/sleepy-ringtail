package com.alivro.spring.sleepyringtail.model.subcategory.response;

import com.alivro.spring.sleepyringtail.model.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryOfSubcategoryResponseDto {
    // Identificador único de la categoría
    private Integer id;

    // Nombre de la categoría
    private String name;

    /**
     * Convierte un objeto Entity en un objeto ResponseDto
     *
     * @param entity Información de la categoría
     * @return Representación ResponseDto de la información de la categoría
     */
    public static CategoryOfSubcategoryResponseDto mapEntityToResponseDto(Category entity) {
        return CategoryOfSubcategoryResponseDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }
}
