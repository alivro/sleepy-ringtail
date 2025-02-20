package com.alivro.spring.sleepyringtail.model.product.response;

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
public class ProductResponseDto {
    // Identificador único del producto
    private Integer id;

    // Nombre del producto
    private String name;

    // Tamaño/Presentación del producto
    private String size;

    // Descripción del producto
    private String description;

    // Precio del producto
    private BigDecimal price;

    // Código de barras del producto
    private String barcode;

    /**
     * Convierte un objeto Entity en un objeto ResponseDto
     *
     * @param entity Información del producto
     * @return Representación ResponseDto de la información del producto
     */
    public static ProductResponseDto mapEntityToResponseDto(Product entity) {
        return ProductResponseDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .size(entity.getSize())
                .price(entity.getPrice())
                .barcode(entity.getBarcode())
                .build();
    }
}
