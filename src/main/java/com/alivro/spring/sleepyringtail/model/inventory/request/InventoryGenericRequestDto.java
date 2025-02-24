package com.alivro.spring.sleepyringtail.model.inventory.request;

import com.alivro.spring.sleepyringtail.model.util.request.ProductRequestDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class InventoryGenericRequestDto {
    @PositiveOrZero(message = "El campo cantidad disponible debe ser un número positivo o cero.")
    private Short quantityAvailable;

    @PositiveOrZero(message = "El campo stock mínimo debe ser un número positivo o cero.")
    private Short minimumStock;

    @PositiveOrZero(message = "El campo stock máximo debe ser un número positivo o cero.")
    private Short maximumStock;

    @Valid
    @NotNull(message = "El campo producto es obligatorio.")
    private ProductRequestDto product;
}
