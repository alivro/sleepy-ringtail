package com.alivro.spring.sleepyringtail.model.subcategory.request;

import com.alivro.spring.sleepyringtail.model.Subcategory;
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
public class SubcategorySaveRequestDto {
    @NotBlank(message = "El campo nombre es obligatorio.")
    @Size(min = 1, max = 50, message = "El campo nombre debe tener como máximo 50 caracteres.")
    private String name;

    @Size(min = 1, max = 150, message = "El campo descripción debe tener como máximo 150 caracteres.")
    private String description;

    /**
     * Convierte un objeto RequestDto en un objeto Entity
     *
     * @param subcategory Información de la subcategoría
     * @return Representación Entity de la información de la subcategoría
     */
    public static Subcategory mapRequestDtoToEntity(SubcategorySaveRequestDto subcategory) {
        return Subcategory.builder()
                .name(subcategory.getName())
                .description(subcategory.getDescription())
                .build();
    }

    /**
     * Convierte un objeto RequestDto en un objeto Entity
     *
     * @param id          Identificador único de la subcategoría
     * @param subcategory Información de la subcategoría
     * @return Representación Entity de la información del subcategoría
     */
    public static Subcategory mapRequestDtoToEntity(Integer id, SubcategorySaveRequestDto subcategory) {
        return mapRequestDtoToEntity(subcategory)
                .toBuilder()
                .id(id)
                .build();
    }
}
