package com.alivro.spring.sleepyringtail.model.product.request;

import com.alivro.spring.sleepyringtail.model.util.request.SubcategoryRequestDto;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductGenericRequestDto {
    @NotBlank(message = "El campo nombre es obligatorio.")
    @Size(min = 1, max = 50, message = "El campo nombre debe tener como máximo 50 caracteres.")
    private String name;

    @Size(min = 1, max = 150, message = "El campo descripción debe tener como máximo 150 caracteres.")
    private String description;

    @NotBlank(message = "El campo tamaño es obligatorio.")
    @Size(min = 1, max = 10, message = "El campo tamaño debe tener como máximo 10 caracteres.")
    private String size;

    @Digits(integer = 6, fraction = 2, message = "El campo precio debe seguir el formato ##.##.")
    @DecimalMin(value = "0.0", inclusive = true, message = "El campo precio no puede ser menor a 0.00")
    private BigDecimal price;

    @Pattern(regexp = "^[0-9]{13}$", message = "El campo código de barras debe estar conformado por 13 números.")
    private String barcode;

    @Valid
    @NotNull(message = "El campo subcategoría es obligatorio.")
    private SubcategoryRequestDto subcategory;
}
