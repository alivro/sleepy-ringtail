package com.alivro.spring.sleepyringtail.model.inventory.response;

import com.alivro.spring.sleepyringtail.model.util.response.ProductResponseDto;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryGenericResponseDto {
    // Identificador único en el inventario
    private Integer id;

    // Cantidad disponible
    private Short quantityAvailable;

    // Cantidad mínima
    private Short minimumStock;

    // Cantidad máxima
    private Short maximumStock;

    // Producto
    private ProductResponseDto product;
}
