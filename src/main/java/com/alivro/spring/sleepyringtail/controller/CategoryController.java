package com.alivro.spring.sleepyringtail.controller;

import com.alivro.spring.sleepyringtail.handler.ResponseHandler;
import com.alivro.spring.sleepyringtail.model.Category;
import com.alivro.spring.sleepyringtail.model.category.request.CategorySaveRequestDto;
import com.alivro.spring.sleepyringtail.model.category.response.CategoryGetResponseDto;
import com.alivro.spring.sleepyringtail.model.category.response.CategorySaveResponseDto;
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
    private final ICategoryService categoryService;
    private final Logger logger = LoggerFactory.getLogger(CategoryController.class);

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
     * @return Información de todos las categorías
     */
    @GetMapping("/getAll")
    public ResponseEntity<CustomResponse<CategoryGetResponseDto, CustomPageMetadata>> getAllCategories(
            @PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable) {
        CustomPaginationData<CategoryGetResponseDto, Category> categoriesData = categoryService.findAll(pageable);

        logger.info("Categorías encontradas.");

        return ResponseHandler.sendResponse(
                HttpStatus.OK, "Found categories!", categoriesData.getData(), categoriesData.getMetadata()
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

        logger.info("Categoría encontrada. ID: {}", id);

        return ResponseHandler.sendResponse(
                HttpStatus.OK, "Found category!", foundCategory
        );
    }

    /**
     * Endpoint para guardar una nueva categoría
     *
     * @param request Información de la categoría a guardar
     * @return Información de la categoría guardada
     */
    @PostMapping("/save")
    public ResponseEntity<CustomResponse<CategorySaveResponseDto, Void>> saveCategory(
            @Valid @RequestBody CategorySaveRequestDto request) {
        CategorySaveResponseDto savedCategory = categoryService.save(request);

        logger.info("Categoría guardada. ID: {}", savedCategory.getId());

        return ResponseHandler.sendResponse(
                HttpStatus.CREATED, "Saved category!", savedCategory
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
    public ResponseEntity<CustomResponse<CategorySaveResponseDto, Void>> updateCategory(
            @PathVariable("id") Integer id, @Valid @RequestBody CategorySaveRequestDto request) {
        CategorySaveResponseDto updatedCategory = categoryService.update(id, request);

        logger.info("Categoría actualizada. ID: {}", id);

        return ResponseHandler.sendResponse(
                HttpStatus.OK, "Updated category!", updatedCategory
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

        logger.info("Categoría eliminada. ID: {}", id);

        return ResponseHandler.sendResponse(
                HttpStatus.OK, "Deleted category!"
        );
    }
}
