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
    @NotNull(message = "El campo cantidad disponible es obligatorio.")
    @PositiveOrZero(message = "El campo cantidad disponible debe ser un número positivo o cero.")
    private Short quantityAvailable;

    @NotNull(message = "El campo stock mínimo es obligatorio.")
    @PositiveOrZero(message = "El campo stock mínimo debe ser un número positivo o cero.")
    private Short minimumStock;

    @NotNull(message = "El campo stock máximo es obligatorio.")
    @PositiveOrZero(message = "El campo stock máximo debe ser un número positivo o cero.")
    private Short maximumStock;

    @Valid
    @NotNull(message = "El campo producto es obligatorio.")
    private ProductRequestDto product;
}
