package com.alivro.spring.sleepyringtail.controller;

import com.alivro.spring.sleepyringtail.constants.MessageConstants;
import com.alivro.spring.sleepyringtail.handler.ResponseHandler;
import com.alivro.spring.sleepyringtail.model.Category;
import com.alivro.spring.sleepyringtail.model.category.request.CategoryGenericRequestDto;
import com.alivro.spring.sleepyringtail.model.category.response.CategoryGenericResponseDto;
import com.alivro.spring.sleepyringtail.model.category.response.CategoryGetResponseDto;
import com.alivro.spring.sleepyringtail.service.ICategoryService;
import com.alivro.spring.sleepyringtail.util.pagination.CustomPageMetadata;
import com.alivro.spring.sleepyringtail.util.pagination.CustomPaginationData;
import com.alivro.spring.sleepyringtail.util.response.CustomResponse;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/category")
@CrossOrigin(origins = "http://localhost:4200")
public class CategoryController {
    private final Logger logger = LoggerFactory.getLogger(CategoryController.class);
    private final ICategoryService categoryService;

    /**
     * Constructor
     *
     * @param categoryService Category service
     */
    @Autowired
    public CategoryController(ICategoryService categoryService) {
        this.categoryService = categoryService;
    }

    /**
     * Endpoint para buscar todas las categorías
     *
     * @return Información de todas las categorías
     */
    @GetMapping("/getAll")
    public ResponseEntity<CustomResponse<CategoryGenericResponseDto, CustomPageMetadata>> getAllCategories(
            @PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable) {
        CustomPaginationData<CategoryGenericResponseDto, Category> categoriesData =
                categoryService.findAll(pageable);

        logger.info(MessageConstants.FOUND_CATEGORIES);

        return ResponseHandler.sendResponse(
                HttpStatus.OK, MessageConstants.FOUND_CATEGORIES, categoriesData.getData(), categoriesData.getMetadata()
        );
    }

    /**
     * Endpoint para buscar todas las categorías que contengan una palabra dada
     *
     * @param word     Palabra a buscar en el nombre de la categoría
     * @param pageable Información de paginación
     * @return Información de todas las categorías que cumplen con el criterio de búsqueda
     */
    @GetMapping("/getAllByName/{word}")
    public ResponseEntity<CustomResponse<CategoryGenericResponseDto, CustomPageMetadata>> getAllCategoriesByName(
            @PathVariable("word") String word,
            @PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable) {
        CustomPaginationData<CategoryGenericResponseDto, Category> categoriesData =
                categoryService.findAllByName(word, pageable);

        logger.info(MessageConstants.FOUND_CATEGORIES);

        return ResponseHandler.sendResponse(
                HttpStatus.OK, MessageConstants.FOUND_CATEGORIES, categoriesData.getData(), categoriesData.getMetadata()
        );
    }

    /**
     * Endpoint para buscar una categoría por su ID
     *
     * @param id Identificador único de la categoría
     * @return Información de la categoría buscada
     */
    @GetMapping("/get/{id}")
    public ResponseEntity<CustomResponse<CategoryGetResponseDto, Void>> getCategory(@PathVariable("id") Integer id) {
        CategoryGetResponseDto foundCategory = categoryService.findById(id);

        logger.info(MessageConstants.FOUND_CATEGORY);

        return ResponseHandler.sendResponse(
                HttpStatus.OK, MessageConstants.FOUND_CATEGORY, foundCategory
        );
    }

    /**
     * Endpoint para guardar una nueva categoría
     *
     * @param request Información de la categoría a guardar
     * @return Información de la categoría guardada
     */
    @PostMapping("/save")
    public ResponseEntity<CustomResponse<CategoryGenericResponseDto, Void>> saveCategory(
            @Valid @RequestBody CategoryGenericRequestDto request) {
        CategoryGenericResponseDto savedCategory = categoryService.save(request);

        logger.info(MessageConstants.SAVED_CATEGORY);

        return ResponseHandler.sendResponse(
                HttpStatus.CREATED, MessageConstants.SAVED_CATEGORY, savedCategory
        );
    }

    /**
     * Endpoint para actualizar la información de una categoría
     *
     * @param id      Identificador único de la categoría
     * @param request Información de la categoría a actualizar
     * @return Información de la categoría actualizada
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<CustomResponse<CategoryGenericResponseDto, Void>> updateCategory(
            @PathVariable("id") Integer id, @Valid @RequestBody CategoryGenericRequestDto request) {
        CategoryGenericResponseDto updatedCategory = categoryService.update(id, request);

        logger.info(MessageConstants.UPDATED_CATEGORY);

        return ResponseHandler.sendResponse(
                HttpStatus.OK, MessageConstants.UPDATED_CATEGORY, updatedCategory
        );
    }

    /**
     * Endpoint para eliminar una categoría por su ID
     *
     * @param id El identificador único de la categoría
     * @return Estatus 200
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<CustomResponse<Void, Void>> deleteCategory(@PathVariable("id") Integer id) {
        categoryService.deleteById(id);

        logger.info(MessageConstants.DELETED_CATEGORY);

        return ResponseHandler.sendResponse(
                HttpStatus.OK, MessageConstants.DELETED_CATEGORY
        );
    }
}
