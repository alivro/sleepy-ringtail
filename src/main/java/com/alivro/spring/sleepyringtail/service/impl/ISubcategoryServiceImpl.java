package com.alivro.spring.sleepyringtail.service.impl;

import com.alivro.spring.sleepyringtail.constants.MessageConstants;
import com.alivro.spring.sleepyringtail.dao.SubcategoryDao;
import com.alivro.spring.sleepyringtail.exception.DataAlreadyExistsException;
import com.alivro.spring.sleepyringtail.exception.DataNotFoundException;
import com.alivro.spring.sleepyringtail.model.Category;
import com.alivro.spring.sleepyringtail.model.Subcategory;
import com.alivro.spring.sleepyringtail.model.subcategory.request.SubcategoryGenericRequestDto;
import com.alivro.spring.sleepyringtail.model.subcategory.response.SubcategoryGenericResponseDto;
import com.alivro.spring.sleepyringtail.model.subcategory.response.SubcategoryGetResponseDto;
import com.alivro.spring.sleepyringtail.service.ISubcategoryService;
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
public class ISubcategoryServiceImpl implements ISubcategoryService {
    private static final String MESSAGE_FORMAT = "{} {}: {}";
    private final Logger logger = LoggerFactory.getLogger(ISubcategoryServiceImpl.class);
    private final ModelMapper modelMapper = new ModelMapper();
    private final SubcategoryDao subcategoryDao;

    /**
     * Constructor
     *
     * @param subcategoryDao Subcategory Dao
     */
    @Autowired
    public ISubcategoryServiceImpl(SubcategoryDao subcategoryDao) {
        this.subcategoryDao = subcategoryDao;
    }

    /**
     * Método para buscar todas las subcategorías
     *
     * @return Información de todas las subcategorías
     */
    @Override
    public CustomPaginationData<SubcategoryGenericResponseDto, Subcategory> findAll(Pageable pageable) {
        logger.info(MessageConstants.FIND_ALL_SUBCATEGORIES);

        Page<Subcategory> subcategoriesPage = subcategoryDao.findAll(pageable);

        // Información de las subcategorías
        List<SubcategoryGenericResponseDto> foundSubcategories = subcategoriesPage.stream()
                .map(subcategory -> modelMapper.map(subcategory, SubcategoryGenericResponseDto.class))
                .toList();

        return new CustomPaginationData<>(foundSubcategories, subcategoriesPage);
    }

    /**
     * Método para buscar las subcategorías que contengan una palabra dada
     *
     * @param word     Palabra a buscar en el nombre de la subcategoría
     * @param pageable Información de paginación
     * @return Información de las subcategorías que cumplen con el criterio de búsqueda
     */
    @Override
    public CustomPaginationData<SubcategoryGenericResponseDto, Subcategory> findAllByName(String word, Pageable pageable) {
        logger.info(MESSAGE_FORMAT, MessageConstants.FIND_ALL_SUBCATEGORIES, MessageConstants.NAME, word);

        Page<Subcategory> subcategoriesPage = subcategoryDao.findByNameContainingIgnoreCase(word, pageable);

        // Información de las categorías
        List<SubcategoryGenericResponseDto> foundSubcategories = subcategoriesPage.stream()
                .map(subcategory -> modelMapper.map(subcategory, SubcategoryGenericResponseDto.class))
                .toList();

        return new CustomPaginationData<>(foundSubcategories, subcategoriesPage);
    }

    /**
     * Método para buscar una subcategoría por su ID
     *
     * @param id Identificador único de la subcategoría
     * @return Información de la subcategoría buscada
     */
    @Override
    public SubcategoryGetResponseDto findById(Integer id) {
        logger.info(MESSAGE_FORMAT, MessageConstants.FIND_SUBCATEGORY, MessageConstants.ID, id);

        Optional<Subcategory> foundSubcategory = subcategoryDao.findById(id);

        if (foundSubcategory.isEmpty()) {
            logger.info(MessageConstants.SUBCATEGORY_NOT_FOUND);

            throw new DataNotFoundException(MessageConstants.SUBCATEGORY_NOT_FOUND);
        }

        return modelMapper.map(foundSubcategory.get(), SubcategoryGetResponseDto.class);
    }

    /**
     * Método para guardar una nueva subcategoría
     *
     * @param request Información de la subcategoría a guardar
     * @return Información de la subcategoría guardada
     */
    @Override
    public SubcategoryGenericResponseDto save(SubcategoryGenericRequestDto request) {
        String name = request.getName();

        logger.info(MESSAGE_FORMAT, MessageConstants.FIND_SUBCATEGORY, MessageConstants.NAME, name);

        // Verifica si ya existe una subcategoría con el mismo nombre
        if (subcategoryDao.existsByName(name)) {
            logger.info(MessageConstants.SUBCATEGORY_ALREADY_EXISTS);
            logger.info(MessageConstants.SUBCATEGORY_NOT_SAVED);

            throw new DataAlreadyExistsException(MessageConstants.SUBCATEGORY_ALREADY_EXISTS);
        }

        logger.info(MessageConstants.SUBCATEGORY_NOT_FOUND);
        logger.info(MessageConstants.SAVE_SUBCATEGORY);

        // Guarda la información de la nueva subcategoría
        Subcategory savedSubcategory = subcategoryDao.save(
                modelMapper.map(request, Subcategory.class)
        );

        return modelMapper.map(savedSubcategory, SubcategoryGenericResponseDto.class);
    }

    /**
     * Método para actualizar la información de una subcategoría
     *
     * @param id      Identificador único de la subcategoría
     * @param request Información de la subcategoría a actualizar
     * @return Información de la subcategoría actualizada
     */
    @Override
    public SubcategoryGenericResponseDto update(Integer id, SubcategoryGenericRequestDto request) {
        logger.info(MESSAGE_FORMAT, MessageConstants.FIND_SUBCATEGORY, MessageConstants.ID, id);

        Optional<Subcategory> foundSubcategory = subcategoryDao.findById(id);

        // Verifica si existe una subcategoría con el ID dado
        if (foundSubcategory.isEmpty()) {
            logger.info(MessageConstants.SUBCATEGORY_NOT_FOUND);
            logger.info(MessageConstants.SUBCATEGORY_NOT_UPDATED);

            throw new DataNotFoundException(MessageConstants.SUBCATEGORY_NOT_FOUND);
        }

        // Información de la subcategoría a actualizar
        Subcategory categoryToUpdate = foundSubcategory.get();
        Category category = modelMapper.map(request.getCategory(), Category.class);

        categoryToUpdate.setName(request.getName());
        categoryToUpdate.setDescription(request.getDescription());
        categoryToUpdate.setCategory(category);

        logger.info(MessageConstants.SUBCATEGORY_EXISTS);
        logger.info(MessageConstants.UPDATE_SUBCATEGORY);

        // Actualiza la información de la subcategoría
        Subcategory updatedSubcategory = subcategoryDao.save(categoryToUpdate);

        return modelMapper.map(updatedSubcategory, SubcategoryGenericResponseDto.class);
    }

    /**
     * Método para eliminar una subcategoría por su ID
     *
     * @param id Identificador único de la subcategoría
     */
    @Override
    public void deleteById(Integer id) {
        logger.info(MESSAGE_FORMAT, MessageConstants.DELETE_SUBCATEGORY, MessageConstants.ID, id);

        subcategoryDao.deleteById(id);
    }
}
