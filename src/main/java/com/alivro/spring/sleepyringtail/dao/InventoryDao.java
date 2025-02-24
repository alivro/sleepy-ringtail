package com.alivro.spring.sleepyringtail.dao;

import com.alivro.spring.sleepyringtail.model.Inventory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryDao extends JpaRepository<Inventory, Integer> {
    /**
     * Método para buscar en el inventario la existencia de un producto
     *
     * @param id Identificador único del producto en el inventario
     * @return true si existe, en caso contrario, false
     */
    boolean existsByProductId(Integer id);
}
