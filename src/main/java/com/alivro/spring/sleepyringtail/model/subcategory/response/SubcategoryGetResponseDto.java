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
public class SubcategoryGetResponseDto {
    // Identificador único de la subcategoría
    private Integer id;

    // Nombre de la subcategoría
    private String name;

    // Descripción de la subcategoría
    private String description;

    /**
     * Convierte un objeto Entity en un objeto ResponseDto
     *
     * @param subcategory Información de la subcategoría
     * @return Representación ResponseDto de la información de la subcategoría
     */
    public static SubcategoryGetResponseDto mapEntityToResponseDto(Subcategory subcategory) {
        return SubcategoryGetResponseDto.builder()
                .id(subcategory.getId())
                .name(subcategory.getName())
                .description(subcategory.getDescription())
                .build();
    }
}
