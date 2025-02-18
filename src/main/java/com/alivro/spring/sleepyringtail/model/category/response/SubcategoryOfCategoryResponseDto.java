package com.alivro.spring.sleepyringtail.model.category.response;

import com.alivro.spring.sleepyringtail.model.Subcategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubcategoryOfCategoryResponseDto {
    // Identificador único de la subcategoría
    private Integer id;

    // Nombre de la subcategoría
    private String name;

    /**
     * Convierte un objeto Entity en un objeto ResponseDto
     *
     * @param entity Información de la subcategoría
     * @return Representación ResponseDto de la información de la subcategoría
     */
    public static SubcategoryOfCategoryResponseDto mapEntityToResponseDto(Subcategory entity) {
        return SubcategoryOfCategoryResponseDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }
}
