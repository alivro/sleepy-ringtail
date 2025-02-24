package com.alivro.spring.sleepyringtail.model.util.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategoryRequestDto {
    @NotNull(message = "El campo id es obligatorio.")
    @Positive(message = "El campo id debe ser un número positivo.")
    private Integer id;

    @Size(min = 1, max = 50, message = "El campo nombre debe tener entre 1 y 50 caracteres.")
    private String name;
}
