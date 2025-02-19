package com.alivro.spring.sleepyringtail.model.category.response;

import com.alivro.spring.sleepyringtail.model.Category;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategorySaveResponseDto {
    // Identificador único de la categoría
    private Integer id;

    // Nombre de la categoría
    private String name;

    // Descripción de la categoría
    private String description;

    /**
     * Convierte un objeto Entity en un objeto ResponseDto
     *
     * @param entity Información de la categoría
     * @return Representación ResponseDto de la información de la categoría
     */
    public static CategorySaveResponseDto mapEntityToResponseDto(Category entity) {
        return CategorySaveResponseDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .build();
    }
}
