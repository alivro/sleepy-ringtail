package com.alivro.spring.sleepyringtail.service.impl;

import com.alivro.spring.sleepyringtail.constants.MessageConstants;
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
    private static final String MESSAGE_FORMAT = "{} {}: {}";
    private final Logger logger = LoggerFactory.getLogger(ICategoryServiceImpl.class);
    private final ModelMapper modelMapper = new ModelMapper();
    private final CategoryDao categoryDao;

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
        logger.info(MessageConstants.FIND_ALL_CATEGORIES);

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
        logger.info(MESSAGE_FORMAT, MessageConstants.FIND_ALL_CATEGORIES, MessageConstants.NAME, word);

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
        logger.info(MESSAGE_FORMAT, MessageConstants.FIND_CATEGORY, MessageConstants.ID, id);

        Optional<Category> foundCategory = categoryDao.findById(id);

        if (foundCategory.isEmpty()) {
            logger.info(MessageConstants.CATEGORY_NOT_FOUND);

            throw new DataNotFoundException(MessageConstants.CATEGORY_NOT_FOUND);
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

        logger.info(MESSAGE_FORMAT, MessageConstants.FIND_CATEGORY, MessageConstants.NAME, name);

        // Verifica si ya existe una categoría con el mismo nombre
        if (categoryDao.existsByName(name)) {
            logger.info(MessageConstants.CATEGORY_ALREADY_EXISTS);
            logger.info(MessageConstants.CATEGORY_NOT_SAVED);

            throw new DataAlreadyExistsException(MessageConstants.CATEGORY_ALREADY_EXISTS);
        }

        logger.info(MessageConstants.CATEGORY_NOT_FOUND);
        logger.info(MessageConstants.SAVE_CATEGORY);

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
        logger.info(MESSAGE_FORMAT, MessageConstants.FIND_CATEGORY, MessageConstants.ID, id);

        Optional<Category> foundCategory = categoryDao.findById(id);

        // Verifica si existe una subcategoría con el ID dado
        if (foundCategory.isEmpty()) {
            logger.info(MessageConstants.CATEGORY_NOT_FOUND);
            logger.info(MessageConstants.CATEGORY_NOT_UPDATED);

            throw new DataNotFoundException(MessageConstants.CATEGORY_NOT_FOUND);
        }

        // Información de la categoría a actualizar
        Category categoryToUpdate = foundCategory.get();
        categoryToUpdate.setName(request.getName());
        categoryToUpdate.setDescription(request.getDescription());

        logger.info(MessageConstants.CATEGORY_EXISTS);
        logger.info(MessageConstants.UPDATE_CATEGORY);

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
        logger.info("{} ID: {}", MessageConstants.DELETE_CATEGORY, id);

        categoryDao.deleteById(id);
    }
}
