package com.alivro.spring.sleepyringtail.service.impl;

import com.alivro.spring.sleepyringtail.dao.CategoryDao;
import com.alivro.spring.sleepyringtail.exception.DataAlreadyExistsException;
import com.alivro.spring.sleepyringtail.exception.DataNotFoundException;
import com.alivro.spring.sleepyringtail.model.Category;
import com.alivro.spring.sleepyringtail.model.category.request.CategoryGenericRequestDto;
import com.alivro.spring.sleepyringtail.model.category.response.CategoryGenericResponseDto;
import com.alivro.spring.sleepyringtail.model.category.response.CategoryGetResponseDto;
import com.alivro.spring.sleepyringtail.service.ICategoryService;
import com.alivro.spring.sleepyringtail.util.pagination.CustomPaginationData;
import org.modelmapper.ModelMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ICategoryServiceImpl implements ICategoryService {
    private final CategoryDao categoryDao;
    private final ModelMapper modelMapper = new ModelMapper();
    private final Logger logger = LoggerFactory.getLogger(ICategoryServiceImpl.class);

    /**
     * Constructor
     *
     * @param categoryDao Category Dao
     */
    @Autowired
    public ICategoryServiceImpl(CategoryDao categoryDao) {
        this.categoryDao = categoryDao;
    }

    /**
     * Método para buscar todas las categorías
     *
     * @param pageable Información de paginación
     * @return Información de todas las categorías
     */
    @Override
    public CustomPaginationData<CategoryGenericResponseDto, Category> findAll(Pageable pageable) {
        logger.info("Busca todas las categorías.");

        Page<Category> categoriesPage = categoryDao.findAll(pageable);

        // Información de las categorías
        List<CategoryGenericResponseDto> foundCategories = categoriesPage.stream()
                .map(c -> modelMapper.map(c, CategoryGenericResponseDto.class))
                .toList();

        return new CustomPaginationData<>(foundCategories, categoriesPage);
    }

    /**
     * Método para buscar las categorías que contengan una palabra dada
     *
     * @param word     Palabra a buscar en el nombre de la categoría
     * @param pageable Información de paginación
     * @return Información de las categorías que cumplen con el criterio de búsqueda
     */
    @Override
    public CustomPaginationData<CategoryGenericResponseDto, Category> findAllByName(String word, Pageable pageable) {
        logger.info("Busca todas las categorías.");

        Page<Category> categoriesPage = categoryDao.findByNameContainingIgnoreCase(word, pageable);

        // Información de las categorías
        List<CategoryGenericResponseDto> foundCategories = categoriesPage.stream()
                .map(c -> modelMapper.map(c, CategoryGenericResponseDto.class))
                .toList();

        return new CustomPaginationData<>(foundCategories, categoriesPage);
    }

    /**
     * Método para buscar una categoría por su ID
     *
     * @param id Identificador único de la categoría
     * @return Información de la categoría buscada
     */
    @Override
    public CategoryGetResponseDto findById(Integer id) {
        logger.info("Busca categoría. ID: {}", id);

        Optional<Category> foundCategory = categoryDao.findById(id);

        if (foundCategory.isEmpty()) {
            logger.info("Categoría no encontrada. ID: {}", id);

            throw new DataNotFoundException("Category not found!");
        }

        return modelMapper.map(foundCategory.get(), CategoryGetResponseDto.class);
    }

    /**
     * Método para guardar una nueva categoría
     *
     * @param request Información de la categoría a guardar
     * @return Información de la categoría guardada
     */
    @Override
    public CategoryGenericResponseDto save(CategoryGenericRequestDto request) {
        String name = request.getName();

        logger.info("Busca categoría. Nombre: {}", name);

        // Verifica si ya existe una categoría con el mismo nombre
        if (categoryDao.existsByName(name)) {
            logger.info("Categoría existente. Nombre: {}", name);
            logger.info("Categoría no guardada. Nombre: {}", name);

            throw new DataAlreadyExistsException("Category already exists!");
        }

        logger.info("Categoría no existente. Nombre: {}", name);
        logger.info("Guarda categoría. Nombre: {}", name);

        // Guarda la información de la nueva categoría
        Category savedCategory = categoryDao.save(
                modelMapper.map(request, Category.class)
        );

        return modelMapper.map(savedCategory, CategoryGenericResponseDto.class);
    }

    /**
     * Método para actualizar la información de una categoría
     *
     * @param id      Identificador único de la categoría
     * @param request Información de la categoría a actualizar
     * @return Información de la categoría actualizada
     */
    @Override
    public CategoryGenericResponseDto update(Integer id, CategoryGenericRequestDto request) {
        logger.info("Busca categoría. ID: {}", id);

        Optional<Category> foundCategory = categoryDao.findById(id);

        // Verifica si existe una categoría con ese id
        if (foundCategory.isEmpty()) {
            logger.info("Categoría no existente. ID: {}", id);
            logger.info("Categoría no actualizada. ID: {}", id);

            throw new DataNotFoundException("Category does not exist!");
        }

        // Información de la categoría a actualizar
        Category categoryToUpdate = foundCategory.get();
        categoryToUpdate.setName(request.getName());
        categoryToUpdate.setDescription(request.getDescription());

        logger.info("Actualiza categoría. ID: {}", id);

        // Actualiza la información de la categoría
        Category updatedCategory = categoryDao.save(categoryToUpdate);

        return modelMapper.map(updatedCategory, CategoryGenericResponseDto.class);
    }

    /**
     * Método para eliminar una categoría por su ID
     *
     * @param id Identificador único de la categoría
     */
    @Override
    public void deleteById(Integer id) {
        logger.info("Elimina categoría. ID: {}", id);

        categoryDao.deleteById(id);
    }
}
