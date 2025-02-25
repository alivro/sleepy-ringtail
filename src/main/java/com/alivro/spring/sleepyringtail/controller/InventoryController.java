package com.alivro.spring.sleepyringtail.controller;

import com.alivro.spring.sleepyringtail.constants.MessageConstants;
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
    private final Logger logger = LoggerFactory.getLogger(InventoryController.class);
    private final IInventoryService inventoryService;

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
     * Endpoint para buscar el inventario de todos los productos
     *
     * @return Información del inventario de todos los productos
     */
    @GetMapping("/getAll")
    public ResponseEntity<CustomResponse<InventoryGenericResponseDto, CustomPageMetadata>> getAllInventory(
            @PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable) {
        CustomPaginationData<InventoryGenericResponseDto, Inventory> inventoryData = inventoryService.findAll(pageable);

        logger.info(MessageConstants.FOUND_ALL_INVENTORY);

        return ResponseHandler.sendResponse(
                HttpStatus.OK, MessageConstants.FOUND_ALL_INVENTORY, inventoryData.getData(), inventoryData.getMetadata()
        );
    }

    /**
     * Endpoint para buscar el inventario de un producto por su ID
     *
     * @param id Identificador único del producto en el inventario
     * @return Información del inventario del producto buscado
     */
    @GetMapping("/get/{id}")
    public ResponseEntity<CustomResponse<InventoryGenericResponseDto, Void>> getInventory(@PathVariable("id") Integer id) {
        InventoryGenericResponseDto foundInventory = inventoryService.findById(id);

        logger.info(MessageConstants.FOUND_INVENTORY);

        return ResponseHandler.sendResponse(
                HttpStatus.OK, MessageConstants.FOUND_INVENTORY, foundInventory
        );
    }

    /**
     * Endpoint para guardar la información del inventario de un producto
     *
     * @param request Información del inventario del producto a guardar
     * @return Información del inventario del producto guardado
     */
    @PostMapping("/save")
    public ResponseEntity<CustomResponse<InventoryGenericResponseDto, Void>> saveInventory(
            @Valid @RequestBody InventoryGenericRequestDto request) {
        InventoryGenericResponseDto savedInventory = inventoryService.save(request);

        logger.info(MessageConstants.SAVED_INVENTORY);

        return ResponseHandler.sendResponse(
                HttpStatus.CREATED, MessageConstants.SAVED_INVENTORY, savedInventory
        );
    }

    /**
     * Endpoint para actualizar la información del inventario de un producto
     *
     * @param id      Identificador único del producto en el inventario
     * @param request Información del inventario del producto a actualizar
     * @return Información del inventario del producto actualizado
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<CustomResponse<InventoryGenericResponseDto, Void>> updateInventory(
            @PathVariable("id") Integer id, @Valid @RequestBody InventoryGenericRequestDto request) {
        InventoryGenericResponseDto updatedInventory = inventoryService.update(id, request);

        logger.info(MessageConstants.UPDATED_INVENTORY);

        return ResponseHandler.sendResponse(
                HttpStatus.OK, MessageConstants.UPDATED_INVENTORY, updatedInventory
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

        logger.info(MessageConstants.DELETED_INVENTORY);

        return ResponseHandler.sendResponse(
                HttpStatus.OK, MessageConstants.DELETED_INVENTORY
        );
    }
}
