package com.alivro.spring.sleepyringtail.model.product.response;

import com.alivro.spring.sleepyringtail.model.util.response.InventoryResponseDto;
import com.alivro.spring.sleepyringtail.model.util.response.SubcategoryResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductGetResponseDto {
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

    // Subcategoría del producto
    private SubcategoryResponseDto subcategory;

    // Inventario del producto
    private InventoryResponseDto inventory;
}
