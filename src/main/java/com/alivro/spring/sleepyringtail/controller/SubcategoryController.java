package com.alivro.spring.sleepyringtail.controller;

import com.alivro.spring.sleepyringtail.constants.MessageConstants;
import com.alivro.spring.sleepyringtail.handler.ResponseHandler;
import com.alivro.spring.sleepyringtail.model.Subcategory;
import com.alivro.spring.sleepyringtail.model.subcategory.request.SubcategoryGenericRequestDto;
import com.alivro.spring.sleepyringtail.model.subcategory.response.SubcategoryGenericResponseDto;
import com.alivro.spring.sleepyringtail.model.subcategory.response.SubcategoryGetResponseDto;
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
    private final Logger logger = LoggerFactory.getLogger(SubcategoryController.class);
    private final ISubcategoryService subcategoryService;

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
    public ResponseEntity<CustomResponse<SubcategoryGenericResponseDto, CustomPageMetadata>> getAllSubcategories(
            @PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable) {
        CustomPaginationData<SubcategoryGenericResponseDto, Subcategory> subcategoriesData =
                subcategoryService.findAll(pageable);

        logger.info(MessageConstants.FOUND_SUBCATEGORIES);

        return ResponseHandler.sendResponse(
                HttpStatus.OK, MessageConstants.FOUND_SUBCATEGORIES, subcategoriesData.getData(), subcategoriesData.getMetadata()
        );
    }

    /**
     * Endpoint para buscar todas las subcategorías que contengan una palabra dada
     *
     * @param word     Palabra a buscar en el nombre de la subcategoría
     * @param pageable Información de paginación
     * @return Información de todas las subcategorías que cumplen con el criterio de búsqueda
     */
    @GetMapping("/getAllByName/{word}")
    public ResponseEntity<CustomResponse<SubcategoryGenericResponseDto, CustomPageMetadata>> getAllSubcategoriesByName(
            @PathVariable("word") String word,
            @PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable) {
        CustomPaginationData<SubcategoryGenericResponseDto, Subcategory> subcategoriesData =
                subcategoryService.findAllByName(word, pageable);

        logger.info(MessageConstants.FOUND_SUBCATEGORIES);

        return ResponseHandler.sendResponse(
                HttpStatus.OK, MessageConstants.FOUND_SUBCATEGORIES, subcategoriesData.getData(), subcategoriesData.getMetadata()
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

        logger.info(MessageConstants.FOUND_SUBCATEGORY);

        return ResponseHandler.sendResponse(
                HttpStatus.OK, MessageConstants.FOUND_SUBCATEGORY, foundSubcategory
        );
    }

    /**
     * Endpoint para guardar una nueva subcategoría
     *
     * @param request Información de la subcategoría a guardar
     * @return Información de la subcategoría guardada
     */
    @PostMapping("/save")
    public ResponseEntity<CustomResponse<SubcategoryGenericResponseDto, Void>> saveSubcategory(
            @Valid @RequestBody SubcategoryGenericRequestDto request) {
        SubcategoryGenericResponseDto savedSubcategory = subcategoryService.save(request);

        logger.info(MessageConstants.SAVED_SUBCATEGORY);

        return ResponseHandler.sendResponse(
                HttpStatus.CREATED, MessageConstants.SAVED_SUBCATEGORY, savedSubcategory
        );
    }

    /**
     * Endpoint para actualizar la información de una subcategoría
     *
     * @param id      Identificador único de la subcategoría
     * @param request Información de la subcategoría a actualizar
     * @return Información de la subcategoría actualizada
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<CustomResponse<SubcategoryGenericResponseDto, Void>> updateSubcategory(
            @PathVariable("id") Integer id, @Valid @RequestBody SubcategoryGenericRequestDto request) {
        SubcategoryGenericResponseDto updatedSubcategory = subcategoryService.update(id, request);

        logger.info(MessageConstants.UPDATED_SUBCATEGORY);

        return ResponseHandler.sendResponse(
                HttpStatus.OK, MessageConstants.UPDATED_SUBCATEGORY, updatedSubcategory
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

        logger.info(MessageConstants.DELETED_SUBCATEGORY);

        return ResponseHandler.sendResponse(
                HttpStatus.OK, MessageConstants.DELETED_SUBCATEGORY
        );
    }
}
