package com.alivro.spring.sleepyringtail.dao;

import com.alivro.spring.sleepyringtail.model.Subcategory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubcategoryDao extends JpaRepository<Subcategory, Integer> {
    /**
     * Método para buscar todas las subcategorías que contengan una palabra dada
     *
     * @param word     Palabra a buscar en el nombre de la subcategoría
     * @param pageable Información de paginación
     * @return Lista de subcategorías que cumplen con el criterio de búsqueda
     */
    Page<Subcategory> findByNameContainingIgnoreCase(String word, Pageable pageable);

    /**
     * Método para buscar la existencia de una subcategoría por su nombre
     *
     * @param name Nombre único de la subcategoría
     * @return true si existe, en caso contrario, false
     */
    boolean existsByName(String name);
}
