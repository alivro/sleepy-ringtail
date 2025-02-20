package com.alivro.spring.sleepyringtail.dao;

import com.alivro.spring.sleepyringtail.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductDao extends JpaRepository<Product, Integer> {
    /**
     * Método para buscar todos los productos que contengan una palabra dada en su nombre
     *
     * @param word     Palabra a buscar en el nombre del producto
     * @param pageable Información de paginación
     * @return Lista de productos que cumplen con el criterio de búsqueda
     */
    Page<Product> findByNameContainingIgnoreCase(String word, Pageable pageable);

    /**
     * Método para buscar todos los productos que contengan una palabra dada en su descripción
     *
     * @param word     Palabra a buscar en la descripción del producto
     * @param pageable Información de paginación
     * @return Lista de productos que cumplen con el criterio de búsqueda
     */
    Page<Product> findByDescriptionContainingIgnoreCase(String word, Pageable pageable);

    /**
     * Método para buscar la existencia de un producto por su código de barras
     *
     * @param barcode Código de barras único del producto
     * @return true si existe, en caso contrario, false
     */
    boolean existsByBarcode(String barcode);
}
