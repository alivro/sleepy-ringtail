package com.alivro.spring.sleepyringtail.service.impl;

import com.alivro.spring.sleepyringtail.dao.SubcategoryDao;
import com.alivro.spring.sleepyringtail.exception.DataAlreadyExistsException;
import com.alivro.spring.sleepyringtail.exception.DataNotFoundException;
import com.alivro.spring.sleepyringtail.model.Category;
import com.alivro.spring.sleepyringtail.model.Subcategory;
import com.alivro.spring.sleepyringtail.model.subcategory.request.CategoryOfSubcategoryRequestDto;
import com.alivro.spring.sleepyringtail.model.subcategory.request.SubcategorySaveRequestDto;
import com.alivro.spring.sleepyringtail.model.subcategory.response.SubcategoryGetResponseDto;
import com.alivro.spring.sleepyringtail.model.subcategory.response.SubcategorySaveResponseDto;
import com.alivro.spring.sleepyringtail.service.ISubcategoryService;
import com.alivro.spring.sleepyringtail.util.pagination.CustomPaginationData;
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
    private final SubcategoryDao subcategoryDao;
    private final Logger logger = LoggerFactory.getLogger(ISubcategoryServiceImpl.class);

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
    public CustomPaginationData<SubcategoryGetResponseDto, Subcategory> findAll(Pageable pageable) {
        logger.info("Busca todas las subcategorías.");

        Page<Subcategory> subcategoriesPage = subcategoryDao.findAll(pageable);

        // Información de las subcategorías
        List<SubcategoryGetResponseDto> foundSubcategories = subcategoriesPage.stream()
                .map(SubcategoryGetResponseDto::mapEntityToResponseDto)
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
    public CustomPaginationData<SubcategoryGetResponseDto, Subcategory> findAllByName(String word, Pageable pageable) {
        logger.info("Busca todas las subcategorías.");

        Page<Subcategory> subcategoriesPage = subcategoryDao.findByNameContainingIgnoreCase(word, pageable);

        // Información de las categorías
        List<SubcategoryGetResponseDto> foundSubcategories = subcategoriesPage.stream()
                .map(SubcategoryGetResponseDto::mapEntityToResponseDto)
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
        logger.info("Busca subcategoría. ID: {}", id);

        Optional<Subcategory> foundSubcategory = subcategoryDao.findById(id);

        if (foundSubcategory.isEmpty()) {
            logger.info("Subcategoría no encontrada. ID: {}", id);

            throw new DataNotFoundException("Subcategory not found!");
        }

        return SubcategoryGetResponseDto.mapEntityToResponseDto(foundSubcategory.get());
    }

    /**
     * Método para guardar una nueva subcategoría
     *
     * @param request Información de la subcategoría a guardar
     * @return Información de la subcategoría guardada
     */
    @Override
    public SubcategorySaveResponseDto save(SubcategorySaveRequestDto request) {
        String name = request.getName();

        logger.info("Busca subcategoría. Nombre: {}", name);

        // Verifica si ya existe una subcategoría con el mismo nombre
        if (subcategoryDao.existsByName(name)) {
            logger.info("Subcategoría existente. Nombre: {}", name);
            logger.info("Subcategoría no guardada. Nombre: {}", name);

            throw new DataAlreadyExistsException("Subcategory already exists!");
        }

        logger.info("Subcategoría no existente. Nombre: {}", name);
        logger.info("Guarda subcategoría. Nombre: {}", name);

        // Guarda la información de la nueva subcategoría
        Subcategory savedSubcategory = subcategoryDao.save(
                SubcategorySaveRequestDto.mapRequestDtoToEntity(request)
        );

        return SubcategorySaveResponseDto.mapEntityToResponseDto(savedSubcategory);
    }

    /**
     * Método para actualizar la información de una subcategoría
     *
     * @param id      Identificador único de la subcategoría
     * @param request Información de la subcategoría a actualizar
     * @return Información de la subcategoría actualizada
     */
    @Override
    public SubcategorySaveResponseDto update(Integer id, SubcategorySaveRequestDto request) {
        logger.info("Busca subcategoría. ID: {}", id);

        Optional<Subcategory> foundSubcategory = subcategoryDao.findById(id);

        // Verifica si existe una subcategoría con ese id
        if (foundSubcategory.isEmpty()) {
            logger.info("Subcategoría no existente. ID: {}", id);
            logger.info("Subcategoría no actualizada. ID: {}", id);

            throw new DataNotFoundException("Subcategory does not exist!");
        }

        // Información de la subcategoría a actualizar
        Subcategory categoryToUpdate = foundSubcategory.get();
        Category category = CategoryOfSubcategoryRequestDto
                .mapRequestDtoToEntity(request.getCategory());
        categoryToUpdate.setName(request.getName());
        categoryToUpdate.setDescription(request.getDescription());
        categoryToUpdate.setCategory(category);

        logger.info("Actualiza subcategoría. ID: {}", id);

        // Actualiza la información de la subcategoría
        Subcategory updatedSubcategory = subcategoryDao.save(categoryToUpdate);

        return SubcategorySaveResponseDto.mapEntityToResponseDto(updatedSubcategory);
    }

    /**
     * Método para eliminar una subcategoría por su ID
     *
     * @param id Identificador único de la subcategoría
     */
    @Override
    public void deleteById(Integer id) {
        logger.info("Elimina subcategoría. ID: {}", id);

        subcategoryDao.deleteById(id);
    }
}
