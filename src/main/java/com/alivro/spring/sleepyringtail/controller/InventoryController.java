package com.alivro.spring.sleepyringtail.controller;

import com.alivro.spring.sleepyringtail.handler.ResponseHandler;
import com.alivro.spring.sleepyringtail.model.Inventory;
import com.alivro.spring.sleepyringtail.model.inventory.request.InventoryGenericRequestDto;
import com.alivro.spring.sleepyringtail.model.inventory.response.InventoryGenericResponseDto;
import com.alivro.spring.sleepyringtail.service.IInventoryService;
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
@RequestMapping("/api/v1/inventory")
@CrossOrigin(origins = "http://localhost:4200")
public class InventoryController {
    private final IInventoryService inventoryService;
    private final Logger logger = LoggerFactory.getLogger(InventoryController.class);

    /**
     * Constructor
     *
     * @param inventoryService Inventory service
     */
    @Autowired
    public InventoryController(IInventoryService inventoryService) {
        this.inventoryService = inventoryService;
    }

    /**
     * Endpoint para buscar el stock de todos los productos
     *
     * @return Información del stock de todos los productos
     */
    @GetMapping("/getAll")
    public ResponseEntity<CustomResponse<InventoryGenericResponseDto, CustomPageMetadata>> getAllInventory(
            @PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable) {
        CustomPaginationData<InventoryGenericResponseDto, Inventory> inventoryData = inventoryService.findAll(pageable);

        logger.info("Stock de productos encontrados.");

        return ResponseHandler.sendResponse(
                HttpStatus.OK, "Found product stocks!", inventoryData.getData(), inventoryData.getMetadata()
        );
    }

    /**
     * Endpoint para buscar el stock de un producto por su ID
     *
     * @param id Identificador único del producto en el inventario
     * @return Información del stock del producto buscado
     */
    @GetMapping("/get/{id}")
    public ResponseEntity<CustomResponse<InventoryGenericResponseDto, Void>> getInventory(@PathVariable("id") Integer id) {
        InventoryGenericResponseDto foundProductStock = inventoryService.findById(id);

        logger.info("Stock del producto encontrado. ID: {}", id);

        return ResponseHandler.sendResponse(
                HttpStatus.OK, "Found product stock!", foundProductStock
        );
    }

    /**
     * Endpoint para guardar la información del stock de un producto
     *
     * @param request Información del stock del producto a guardar
     * @return Información del stock del producto guardado
     */
    @PostMapping("/save")
    public ResponseEntity<CustomResponse<InventoryGenericResponseDto, Void>> saveInventory(
            @Valid @RequestBody InventoryGenericRequestDto request) {
        InventoryGenericResponseDto savedProductStock = inventoryService.save(request);

        logger.info("Stock del producto guardado. ID: {}", savedProductStock.getId());

        return ResponseHandler.sendResponse(
                HttpStatus.CREATED, "Saved inventory stock!", savedProductStock
        );
    }

    /**
     * Endpoint para actualizar la información del stock de un producto
     *
     * @param id      Identificador único del producto en el inventario
     * @param request Información del stock del producto a actualizar
     * @return Información del stock del producto actualizado
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<CustomResponse<InventoryGenericResponseDto, Void>> updateInventory(
            @PathVariable("id") Integer id, @Valid @RequestBody InventoryGenericRequestDto request) {
        InventoryGenericResponseDto updatedProductStock = inventoryService.update(id, request);

        logger.info("Stock del producto actualizado. ID: {}", id);

        return ResponseHandler.sendResponse(
                HttpStatus.OK, "Updated stock product!", updatedProductStock
        );
    }

    /**
     * Endpoint para eliminar del inventario un producto por su ID
     *
     * @param id Identificador único del producto en el inventario
     * @return Estatus 200
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<CustomResponse<Void, Void>> deleteInventory(@PathVariable("id") Integer id) {
        inventoryService.deleteById(id);

        logger.info("Stock del producto eliminado. ID: {}", id);

        return ResponseHandler.sendResponse(
                HttpStatus.OK, "Deleted product stock!"
        );
    }
}
