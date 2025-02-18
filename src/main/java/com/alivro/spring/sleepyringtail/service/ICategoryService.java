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
     * @return Información de todas las categorías
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
     * @param request Información de la categoría a guardar
     * @return Información de la categoría guardada
     */
    CategorySaveResponseDto save(CategorySaveRequestDto request);

    /**
     * Método para actualizar la información de una categoría
     *
     * @param id      Identificador único de la categoría
     * @param request Información de la categoría a actualizar
     * @return Información de la categoría actualizada
     */
    CategorySaveResponseDto update(Integer id, CategorySaveRequestDto request);

    /**
     * Método para eliminar una categoría por su ID
     *
     * @param id Identificador único de la categoría
     */
    void deleteById(Integer id);
}
