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
    @Size(min = 1, max = 50, message = "El campo nombre debe tener entre 1 y 50 caracteres.")
    private String name;

    @Size(min = 1, max = 150, message = "El campo descripción debe tener entre 1 y 150 caracteres.")
    private String description;

    @NotBlank(message = "El campo tamaño es obligatorio.")
    @Size(min = 1, max = 10, message = "El campo tamaño debe tener entre 1 y 10 caracteres.")
    private String size;

    @NotNull(message = "El campo precio es obligatorio.")
    @Digits(integer = 6, fraction = 2, message = "El campo precio debe seguir el formato ##.##.")
    @DecimalMin(value = "0.0", inclusive = true, message = "El campo precio no puede ser menor a 0.00")
    private BigDecimal price;

    @NotBlank(message = "El campo código de barras es obligatorio.")
    @Pattern(regexp = "^[0-9]{13}$", message = "El campo código de barras debe estar conformado por 13 dígitos.")
    private String barcode;

    @Valid
    @NotNull(message = "El campo subcategoría es obligatorio.")
    private SubcategoryRequestDto subcategory;
}
