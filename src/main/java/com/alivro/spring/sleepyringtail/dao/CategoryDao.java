package com.alivro.spring.sleepyringtail.dao;

import com.alivro.spring.sleepyringtail.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryDao extends JpaRepository<Category, Integer> {
    /**
     * Método para buscar todas las categorías que contengan una palabra dada
     *
     * @param word     Palabra a buscar en el nombre de la categoría
     * @param pageable Información de paginación
     * @return Lista de categorías que cumplen con el criterio de búsqueda
     */
    Page<Category> findByNameContainingIgnoreCase(String word, Pageable pageable);

    /**
     * Método para buscar la existencia de una categoría por su nombre
     *
     * @param name Nombre único de la categoría
     * @return true si existe, en caso contrario, false
     */
    boolean existsByName(String name);
}
