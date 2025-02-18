package com.alivro.spring.sleepyringtail.service;

import com.alivro.spring.sleepyringtail.model.Subcategory;
import com.alivro.spring.sleepyringtail.model.subcategory.request.SubcategorySaveRequestDto;
import com.alivro.spring.sleepyringtail.model.subcategory.response.SubcategoryGetResponseDto;
import com.alivro.spring.sleepyringtail.model.subcategory.response.SubcategorySaveResponseDto;
import com.alivro.spring.sleepyringtail.util.pagination.CustomPaginationData;
import org.springframework.data.domain.Pageable;

public interface ISubcategoryService {
    /**
     * Método para buscar todas las subcategorías
     *
     * @return Información de todos las subcategorías
     */
    CustomPaginationData<SubcategoryGetResponseDto, Subcategory> findAll(Pageable pageable);

    /**
     * Método para buscar una subcategoría por su ID
     *
     * @param id Identificador único de la subcategoría
     * @return Información de la subcategoría buscada
     */
    SubcategoryGetResponseDto findById(Integer id);

    /**
     * Método para guardar una nueva subcategoría
     *
     * @param subcategory Información de la subcategoría a guardar
     * @return Información de la subcategoría guardada
     */
    SubcategorySaveResponseDto save(SubcategorySaveRequestDto subcategory);

    /**
     * Método para actualizar la información de una subcategoría
     *
     * @param id          Identificador único de la subcategoría
     * @param subcategory Información de la subcategoría a actualizar
     * @return Información de la subcategoría actualizada
     */
    SubcategorySaveResponseDto update(Integer id, SubcategorySaveRequestDto subcategory);

    /**
     * Método para eliminar una subcategoría por su ID
     *
     * @param id Identificador único de la subcategoría
     */
    void deleteById(Integer id);
}
