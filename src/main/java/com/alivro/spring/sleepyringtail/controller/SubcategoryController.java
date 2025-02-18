package com.alivro.spring.sleepyringtail.controller;

import com.alivro.spring.sleepyringtail.handler.ResponseHandler;
import com.alivro.spring.sleepyringtail.model.Subcategory;
import com.alivro.spring.sleepyringtail.model.subcategory.request.SubcategorySaveRequestDto;
import com.alivro.spring.sleepyringtail.model.subcategory.response.SubcategoryGetResponseDto;
import com.alivro.spring.sleepyringtail.model.subcategory.response.SubcategorySaveResponseDto;
import com.alivro.spring.sleepyringtail.service.ISubcategoryService;
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
@RequestMapping("/api/v1/subcategory")
@CrossOrigin(origins = "http://localhost:4200")
public class SubcategoryController {
    private final ISubcategoryService subcategoryService;
    private final Logger logger = LoggerFactory.getLogger(SubcategoryController.class);

    /**
     * Constructor
     *
     * @param subcategoryService Subcategory service
     */
    @Autowired
    public SubcategoryController(ISubcategoryService subcategoryService) {
        this.subcategoryService = subcategoryService;
    }

    /**
     * Endpoint para buscar todas las subcategorías
     *
     * @return Información de todos las subcategorías
     */
    @GetMapping("/getAll")
    public ResponseEntity<CustomResponse<SubcategoryGetResponseDto, CustomPageMetadata>> getAllSubcategories(
            @PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable) {
        CustomPaginationData<SubcategoryGetResponseDto, Subcategory> subcategoriesData = subcategoryService.findAll(pageable);

        logger.info("Subcategorías encontradas.");

        return ResponseHandler.sendResponse(
                HttpStatus.OK, "Found subcategories!", subcategoriesData.getData(), subcategoriesData.getMetadata()
        );
    }

    /**
     * Endpoint para buscar una subcategoría por su ID
     *
     * @param id Identificador único de la subcategoría
     * @return Información de la subcategoría buscada
     */
    @GetMapping("/get/{id}")
    public ResponseEntity<CustomResponse<SubcategoryGetResponseDto, Void>> getSubcategory(@PathVariable("id") Integer id) {
        SubcategoryGetResponseDto foundSubcategory = subcategoryService.findById(id);

        logger.info("Subcategoría encontrada. ID: {}", id);

        return ResponseHandler.sendResponse(
                HttpStatus.OK, "Found subcategory!", foundSubcategory
        );
    }

    /**
     * Endpoint para guardar una nueva subcategoría
     *
     * @param subcategory Información de la subcategoría a guardar
     * @return Información de la subcategoría guardada
     */
    @PostMapping("/save")
    public ResponseEntity<CustomResponse<SubcategorySaveResponseDto, Void>> saveCategory(
            @Valid @RequestBody SubcategorySaveRequestDto subcategory) {
        SubcategorySaveResponseDto savedSubcategory = subcategoryService.save(subcategory);

        logger.info("Subcategoría guardada. ID: {}", savedSubcategory.getId());

        return ResponseHandler.sendResponse(
                HttpStatus.CREATED, "Saved subcategory!", savedSubcategory
        );
    }

    /**
     * Endpoint para actualizar la información de una subcategoría
     *
     * @param id          Identificador único de la subcategoría
     * @param subcategory Información de la subcategoría a actualizar
     * @return Información de la subcategoría actualizada
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<CustomResponse<SubcategorySaveResponseDto, Void>> updateSubcategory(
            @PathVariable("id") Integer id, @Valid @RequestBody SubcategorySaveRequestDto subcategory) {
        SubcategorySaveResponseDto updatedSubcategory = subcategoryService.update(id, subcategory);

        logger.info("Subcategoría actualizada. ID: {}", id);

        return ResponseHandler.sendResponse(
                HttpStatus.OK, "Updated subcategory!", updatedSubcategory
        );
    }

    /**
     * Endpoint para eliminar una subcategoría por su ID
     *
     * @param id El identificador único de la subcategoría
     * @return Estatus 200
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<CustomResponse<Void, Void>> deleteSubcategory(@PathVariable("id") Integer id) {
        subcategoryService.deleteById(id);

        logger.info("Subcategoría eliminada. ID: {}", id);

        return ResponseHandler.sendResponse(
                HttpStatus.OK, "Deleted subcategory!"
        );
    }
}
