package com.alivro.spring.sleepyringtail.service;

import com.alivro.spring.sleepyringtail.model.Subcategory;
import com.alivro.spring.sleepyringtail.model.subcategory.request.SubcategorySaveRequestDto;
import com.alivro.spring.sleepyringtail.model.subcategory.response.SubcategoryResponseDto;
import com.alivro.spring.sleepyringtail.util.pagination.CustomPaginationData;
import org.springframework.data.domain.Pageable;

public interface ISubcategoryService {
    /**
     * Método para buscar todas las subcategorías
     *
     * @return Información de todos las subcategorías
     */
    CustomPaginationData<SubcategoryResponseDto, Subcategory> findAll(Pageable pageable);

    /**
     * Método para buscar una subcategoría por su ID
     *
     * @param id Identificador único de la subcategoría
     * @return Información de la subcategoría buscada
     */
    SubcategoryResponseDto findById(Integer id);

    /**
     * Método para guardar una nueva subcategoría
     *
     * @param request Información de la subcategoría a guardar
     * @return Información de la subcategoría guardada
     */
    SubcategoryResponseDto save(SubcategorySaveRequestDto request);

    /**
     * Método para actualizar la información de una subcategoría
     *
     * @param id      Identificador único de la subcategoría
     * @param request Información de la subcategoría a actualizar
     * @return Información de la subcategoría actualizada
     */
    SubcategoryResponseDto update(Integer id, SubcategorySaveRequestDto request);

    /**
     * Método para eliminar una subcategoría por su ID
     *
     * @param id Identificador único de la subcategoría
     */
    void deleteById(Integer id);
}
