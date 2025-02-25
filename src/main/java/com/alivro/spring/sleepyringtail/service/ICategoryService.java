package com.alivro.spring.sleepyringtail.service;

import com.alivro.spring.sleepyringtail.model.Category;
import com.alivro.spring.sleepyringtail.model.category.request.CategoryGenericRequestDto;
import com.alivro.spring.sleepyringtail.model.category.response.CategoryGenericResponseDto;
import com.alivro.spring.sleepyringtail.model.category.response.CategoryGetResponseDto;
import com.alivro.spring.sleepyringtail.util.pagination.CustomPaginationData;
import org.springframework.data.domain.Pageable;

public interface ICategoryService {
    /**
     * Método para buscar todas las categorías
     *
     * @param pageable Información de paginación
     * @return Información de todas las categorías
     */
    CustomPaginationData<CategoryGenericResponseDto, Category> findAll(Pageable pageable);

    /**
     * Método para buscar las categorías que contengan una palabra dada
     *
     * @param word     Palabra a buscar en el nombre de la categoría
     * @param pageable Información de paginación
     * @return Información de las categorías que cumplen con el criterio de búsqueda
     */
    CustomPaginationData<CategoryGenericResponseDto, Category> findAllByName(String word, Pageable pageable);

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
    CategoryGenericResponseDto save(CategoryGenericRequestDto request);

    /**
     * Método para actualizar la información de una categoría
     *
     * @param id      Identificador único de la categoría
     * @param request Información de la categoría a actualizar
     * @return Información de la categoría actualizada
     */
    CategoryGenericResponseDto update(Integer id, CategoryGenericRequestDto request);

    /**
     * Método para eliminar una categoría por su ID
     *
     * @param id Identificador único de la categoría
     */
    void deleteById(Integer id);
}
