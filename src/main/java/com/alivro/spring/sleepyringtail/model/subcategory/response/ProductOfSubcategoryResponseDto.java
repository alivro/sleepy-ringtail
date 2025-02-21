package com.alivro.spring.sleepyringtail.model.subcategory.response;

import com.alivro.spring.sleepyringtail.model.Product;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductOfSubcategoryResponseDto {
    // Identificador único del producto
    private Integer id;

    // Nombre del producto
    private String name;

    // Tamaño/Presentación del producto
    private String size;

    // Precio del producto
    private BigDecimal price;

    /**
     * Convierte un objeto Entity en un objeto ResponseDto
     *
     * @param entity Información del producto
     * @return Representación ResponseDto de la información del producto
     */
    public static ProductOfSubcategoryResponseDto mapEntityToResponseDto(Product entity) {
        return ProductOfSubcategoryResponseDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .size(entity.getSize())
                .price(entity.getPrice())
                .build();
    }
}
