package com.alivro.spring.sleepyringtail.service;

import com.alivro.spring.sleepyringtail.model.Category;
import com.alivro.spring.sleepyringtail.model.category.request.CategorySaveRequestDto;
import com.alivro.spring.sleepyringtail.model.category.response.CategoryGetResponseDto;
import com.alivro.spring.sleepyringtail.model.category.response.CategorySaveResponseDto;
import com.alivro.spring.sleepyringtail.util.pagination.CustomPaginationData;
import org.springframework.data.domain.Pageable;

public interface ICategoryService {
    /**
     * Método para buscar todas las categorías
     *
     * @return Información de todos las categorías
     */
    CustomPaginationData<CategoryGetResponseDto, Category> findAll(Pageable pageable);

    /**
     * Método para buscar una categoría por su ID
     *
     * @param id Identificador único de la categoría
     * @return Información de la categoría buscada
     */
    CategoryGetResponseDto findById(Integer id);

    /**
     * Método para guardar una nueva categoría
     *
     * @param category Información de la categoría a guardar
     * @return Información de la categoría guardado
     */
    CategorySaveResponseDto save(CategorySaveRequestDto category);

    /**
     * Método para actualizar la información de una categoría
     *
     * @param id       Identificador único de la categoría
     * @param category Información de la categoría  a actualizar
     * @return Información de la categoría actualizado
     */
    CategorySaveResponseDto update(Integer id, CategorySaveRequestDto category);

    /**
     * Método para eliminar una categoría por su ID
     *
     * @param id Identificador único de la categoría
     */
    void deleteById(Integer id);
}
