package com.alivro.spring.sleepyringtail.service;

import com.alivro.spring.sleepyringtail.model.Product;
import com.alivro.spring.sleepyringtail.model.product.request.ProductGenericRequestDto;
import com.alivro.spring.sleepyringtail.model.product.response.ProductGenericResponseDto;
import com.alivro.spring.sleepyringtail.util.pagination.CustomPaginationData;
import org.springframework.data.domain.Pageable;

public interface IProductService {
    /**
     * Método para buscar todos los productos
     *
     * @return Información de todos los productos
     */
    CustomPaginationData<ProductGenericResponseDto, Product> findAll(Pageable pageable);

    /**
     * Método para buscar los productos que contengan una palabra dada en su nombre
     *
     * @param word     Palabra a buscar en el nombre del producto
     * @param pageable Información de paginación
     * @return Información de los productos que cumplen con el criterio de búsqueda
     */
    CustomPaginationData<ProductGenericResponseDto, Product> findAllByName(String word, Pageable pageable);

    /**
     * Método para buscar los productos que contengan una palabra dada en su descripción
     *
     * @param word     Palabra a buscar en la descripción del producto
     * @param pageable Información de paginación
     * @return Información de los productos que cumplen con el criterio de búsqueda
     */
    CustomPaginationData<ProductGenericResponseDto, Product> findAllByDescription(String word, Pageable pageable);

    /**
     * Método para buscar un producto por su ID
     *
     * @param id Identificador único del producto
     * @return Información del producto buscado
     */
    ProductGenericResponseDto findById(Integer id);

    /**
     * Método para guardar un nuevo producto
     *
     * @param request Información del producto a guardar
     * @return Información del producto guardado
     */
    ProductGenericResponseDto save(ProductGenericRequestDto request);

    /**
     * Método para actualizar la información de un producto
     *
     * @param id      Identificador único del producto
     * @param request Información del producto a actualizar
     * @return Información del producto actualizado
     */
    ProductGenericResponseDto update(Integer id, ProductGenericRequestDto request);

    /**
     * Método para eliminar un producto por su ID
     *
     * @param id Identificador único del producto
     */
    void deleteById(Integer id);
}
