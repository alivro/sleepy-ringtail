package com.alivro.spring.sleepyringtail.model.product.response;

import com.alivro.spring.sleepyringtail.model.Subcategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubcategoryOfProductResponseDto {
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
    public static SubcategoryOfProductResponseDto mapEntityToResponseDto(Subcategory entity) {
        return SubcategoryOfProductResponseDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .build();
    }
}
