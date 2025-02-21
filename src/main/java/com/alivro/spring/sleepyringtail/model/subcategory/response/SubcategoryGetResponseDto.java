package com.alivro.spring.sleepyringtail.model.subcategory.response;

import com.alivro.spring.sleepyringtail.model.Subcategory;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.stream.Collectors;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SubcategoryGetResponseDto {
    // Identificador único de la subcategoría
    private Integer id;

    // Nombre de la subcategoría
    private String name;

    // Descripción de la subcategoría
    private String description;

    // Categoría de la subcategoría
    private CategoryOfSubcategoryResponseDto category;

    // Producto(s) de la subcategoría
    private List<ProductOfSubcategoryResponseDto> products;

    /**
     * Convierte un objeto Entity en un objeto ResponseDto
     *
     * @param entity Información de la subcategoría
     * @return Representación ResponseDto de la información de la subcategoría
     */
    public static SubcategoryGetResponseDto mapEntityToResponseDto(Subcategory entity) {
        CategoryOfSubcategoryResponseDto categoryOfSubcategory =
                CategoryOfSubcategoryResponseDto.mapEntityToResponseDto(entity.getCategory());

        List<ProductOfSubcategoryResponseDto> productsOfSubcategory = entity.getProducts().stream()
                .map(ProductOfSubcategoryResponseDto::mapEntityToResponseDto)
                .collect(Collectors.toList());

        return SubcategoryGetResponseDto.builder()
                .id(entity.getId())
                .name(entity.getName())
                .description(entity.getDescription())
                .category(categoryOfSubcategory)
                .products(productsOfSubcategory)
                .build();
    }
}
