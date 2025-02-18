package com.alivro.spring.sleepyringtail.model.subcategory.request;

import com.alivro.spring.sleepyringtail.model.Category;
import com.alivro.spring.sleepyringtail.model.Subcategory;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
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

    @Valid
    @NotNull(message = "El campo categoría es obligatorio.")
    private CategoryOfSubcategoryRequestDto category;

    /**
     * Convierte un objeto RequestDto en un objeto Entity
     *
     * @param request Información de la subcategoría
     * @return Representación Entity de la información de la subcategoría
     */
    public static Subcategory mapRequestDtoToEntity(SubcategorySaveRequestDto request) {
        Category categoryOfSubcategory =
                CategoryOfSubcategoryRequestDto.mapRequestDtoToEntity(request.getCategory());

        return Subcategory.builder()
                .name(request.getName())
                .description(request.getDescription())
                .category(categoryOfSubcategory)
                .build();
    }

    /**
     * Convierte un objeto RequestDto en un objeto Entity
     *
     * @param id      Identificador único de la subcategoría
     * @param request Información de la subcategoría
     * @return Representación Entity de la información de la subcategoría
     */
    public static Subcategory mapRequestDtoToEntity(Integer id, SubcategorySaveRequestDto request) {
        return mapRequestDtoToEntity(request)
                .toBuilder()
                .id(id)
                .build();
    }
}
