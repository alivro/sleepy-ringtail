package com.alivro.spring.sleepyringtail.service;

import com.alivro.spring.sleepyringtail.model.Inventory;
import com.alivro.spring.sleepyringtail.model.inventory.request.InventoryGenericRequestDto;
import com.alivro.spring.sleepyringtail.model.inventory.response.InventoryGenericResponseDto;
import com.alivro.spring.sleepyringtail.util.pagination.CustomPaginationData;
import org.springframework.data.domain.Pageable;

public interface IInventoryService {
    /**
     * Método para buscar el inventario de todos los productos
     *
     * @param pageable Información de paginación
     * @return Información del inventario de todos los productos
     */
    CustomPaginationData<InventoryGenericResponseDto, Inventory> findAll(Pageable pageable);

    /**
     * Método para buscar el inventario de un producto por su ID
     *
     * @param id Identificador único del producto en el inventario
     * @return Información del inventario del producto buscado
     */
    InventoryGenericResponseDto findById(Integer id);

    /**
     * Método para guardar la información del inventario de un producto
     *
     * @param request Información del inventario del producto a guardar
     * @return Información del inventario del producto guardado
     */
    InventoryGenericResponseDto save(InventoryGenericRequestDto request);

    /**
     * Método para actualizar la información del inventario de un producto
     *
     * @param id      Identificador único del producto en el inventario
     * @param request Información del inventario del producto a actualizar
     * @return Información del inventario del producto actualizado
     */
    InventoryGenericResponseDto update(Integer id, InventoryGenericRequestDto request);

    /**
     * Método para eliminar del inventario un producto por su ID
     *
     * @param id Identificador único del producto en el inventario
     */
    void deleteById(Integer id);
}
