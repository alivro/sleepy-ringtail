package com.alivro.spring.sleepyringtail.model.category.request;

import com.alivro.spring.sleepyringtail.model.Category;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CategorySaveRequestDto {
    @NotBlank(message = "El campo nombre es obligatorio.")
    @Size(min = 1, max = 50, message = "El campo nombre debe tener como máximo 50 caracteres.")
    private String name;

    @Size(min = 1, max = 150, message = "El campo descripción debe tener como máximo 150 caracteres.")
    private String description;

    /**
     * Convierte un objeto RequestDto en un objeto Entity
     *
     * @param request Información de la categoría
     * @return Representación Entity de la información de la categoría
     */
    public static Category mapRequestDtoToEntity(CategorySaveRequestDto request) {
        return Category.builder()
                .name(request.getName())
                .description(request.getDescription())
                .build();
    }

    /**
     * Convierte un objeto RequestDto en un objeto Entity
     *
     * @param id      Identificador único de la categoría
     * @param request Información de la categoría
     * @return Representación Entity de la información de la categoría
     */
    public static Category mapRequestDtoToEntity(Integer id, CategorySaveRequestDto request) {
        return mapRequestDtoToEntity(request)
                .toBuilder()
                .id(id)
                .build();
    }
}
