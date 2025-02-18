package com.alivro.spring.sleepyringtail.model.subcategory.request;

import com.alivro.spring.sleepyringtail.model.Category;
import jakarta.validation.constraints.NotBlank;
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
public class CategoryOfSubcategoryRequestDto {
    @Positive(message = "El campo id debe ser un número positivo.")
    private Integer id;

    @NotBlank(message = "El campo nombre es obligatorio.")
    @Size(min = 1, max = 50, message = "El campo nombre debe tener como máximo 50 caracteres.")
    private String name;

    /**
     * Convierte un objeto RequestDto en un objeto Entity
     *
     * @param request Información de la categoría
     * @return Representación Entity de la información de la categoría
     */
    public static Category mapRequestDtoToEntity(CategoryOfSubcategoryRequestDto request) {
        return Category.builder()
                .id(request.getId())
                .name(request.getName())
                .build();
    }
}
