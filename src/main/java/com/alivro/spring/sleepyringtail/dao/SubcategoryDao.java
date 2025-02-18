package com.alivro.spring.sleepyringtail.dao;

import com.alivro.spring.sleepyringtail.model.Subcategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubcategoryDao extends JpaRepository<Subcategory, Integer> {
    /**
     * Método para buscar la existencia de una subcategoría por su nombre
     *
     * @param name Nombre único de la subcategoría
     * @return true si existe, en caso contrario, false
     */
    boolean existsByName(String name);
}
