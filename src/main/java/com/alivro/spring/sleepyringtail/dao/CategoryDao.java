package com.alivro.spring.sleepyringtail.dao;

import com.alivro.spring.sleepyringtail.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryDao extends JpaRepository<Category, Integer> {
    /**
     * Método para buscar la existencia de una categoría por su nombre
     *
     * @param name Nombre único de la categoría
     * @return true si existe, en caso contrario, false
     */
    boolean existsByName(String name);
}
