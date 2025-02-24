package com.alivro.spring.sleepyringtail.model.util.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryResponseDto {
    // Identificador único en el inventario
    private Integer id;

    // Cantidad disponible
    private Short quantityAvailable;
}
