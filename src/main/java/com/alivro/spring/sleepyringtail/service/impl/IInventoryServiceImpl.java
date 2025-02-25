package com.alivro.spring.sleepyringtail.service.impl;

import com.alivro.spring.sleepyringtail.constants.MessageConstants;
import com.alivro.spring.sleepyringtail.dao.InventoryDao;
import com.alivro.spring.sleepyringtail.exception.DataAlreadyExistsException;
import com.alivro.spring.sleepyringtail.exception.DataNotFoundException;
import com.alivro.spring.sleepyringtail.model.Inventory;
import com.alivro.spring.sleepyringtail.model.Product;
import com.alivro.spring.sleepyringtail.model.inventory.request.InventoryGenericRequestDto;
import com.alivro.spring.sleepyringtail.model.inventory.response.InventoryGenericResponseDto;
import com.alivro.spring.sleepyringtail.service.IInventoryService;
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
public class IInventoryServiceImpl implements IInventoryService {
    private static final String MESSAGE_FORMAT = "{} {}: {}";
    private final Logger logger = LoggerFactory.getLogger(IInventoryServiceImpl.class);
    private final ModelMapper modelMapper = new ModelMapper();
    private final InventoryDao inventoryDao;

    /**
     * Constructor
     *
     * @param inventoryDao Inventory Dao
     */
    @Autowired
    public IInventoryServiceImpl(InventoryDao inventoryDao) {
        this.inventoryDao = inventoryDao;
    }

    /**
     * Método para buscar el inventario de todos los productos
     *
     * @param pageable Información de paginación
     * @return Información del inventario de todos los productos
     */
    @Override
    public CustomPaginationData<InventoryGenericResponseDto, Inventory> findAll(Pageable pageable) {
        logger.info(MessageConstants.FIND_ALL_INVENTORY);

        Page<Inventory> inventoryPage = inventoryDao.findAll(pageable);

        // Información del inventario de todos los productos
        List<InventoryGenericResponseDto> foundInventory = inventoryPage.stream()
                .map(inventoryProduct -> modelMapper.map(inventoryProduct, InventoryGenericResponseDto.class))
                .toList();

        return new CustomPaginationData<>(foundInventory, inventoryPage);
    }

    /**
     * Método para buscar el inventario de un producto por su ID
     *
     * @param id Identificador único del producto en el inventario
     * @return Información del inventario del producto buscado
     */
    @Override
    public InventoryGenericResponseDto findById(Integer id) {
        logger.info(MESSAGE_FORMAT, MessageConstants.FIND_INVENTORY, MessageConstants.ID, id);

        Optional<Inventory> foundInventory = inventoryDao.findById(id);

        if (foundInventory.isEmpty()) {
            logger.info(MessageConstants.INVENTORY_NOT_FOUND);

            throw new DataNotFoundException(MessageConstants.INVENTORY_NOT_FOUND);
        }

        return modelMapper.map(foundInventory.get(), InventoryGenericResponseDto.class);
    }

    /**
     * Método para guardar la información del inventario de un producto
     *
     * @param request Información del inventario del producto a guardar
     * @return Información del inventario del producto guardado
     */
    @Override
    public InventoryGenericResponseDto save(InventoryGenericRequestDto request) {
        Integer id = request.getProduct().getId();

        logger.info(MESSAGE_FORMAT, MessageConstants.FIND_INVENTORY, MessageConstants.ID, id);

        // Verifica si ya existe en el inventario un producto con el mismo ID
        if (inventoryDao.existsByProductId(id)) {
            logger.info(MessageConstants.INVENTORY_ALREADY_EXISTS);
            logger.info(MessageConstants.INVENTORY_NOT_SAVED);

            throw new DataAlreadyExistsException(MessageConstants.INVENTORY_ALREADY_EXISTS);
        }

        logger.info(MessageConstants.INVENTORY_NOT_FOUND);
        logger.info(MessageConstants.SAVE_INVENTORY);

        // Guarda la información del stock del producto
        Inventory savedInventory = inventoryDao.save(
                modelMapper.map(request, Inventory.class)
        );

        return modelMapper.map(savedInventory, InventoryGenericResponseDto.class);
    }

    /**
     * Método para actualizar la información del inventario de un producto
     *
     * @param id      Identificador único del producto en el inventario
     * @param request Información del inventario del producto a actualizar
     * @return Información del inventario del producto actualizado
     */
    @Override
    public InventoryGenericResponseDto update(Integer id, InventoryGenericRequestDto request) {
        logger.info(MESSAGE_FORMAT, MessageConstants.FIND_INVENTORY, MessageConstants.ID, id);

        Optional<Inventory> foundProductStock = inventoryDao.findById(id);

        // Verifica si existe en el inventario un producto con el ID dado
        if (foundProductStock.isEmpty()) {
            logger.info(MessageConstants.INVENTORY_NOT_FOUND);
            logger.info(MessageConstants.INVENTORY_NOT_UPDATED);

            throw new DataNotFoundException(MessageConstants.INVENTORY_NOT_FOUND);
        }

        // Información del inventario del producto a actualizar
        Inventory inventoryToUpdate = foundProductStock.get();
        Product product = modelMapper.map(request.getProduct(), Product.class);

        inventoryToUpdate.setQuantityAvailable(request.getQuantityAvailable());
        inventoryToUpdate.setMinimumStock(request.getMinimumStock());
        inventoryToUpdate.setMaximumStock(request.getMaximumStock());
        inventoryToUpdate.setProduct(product);


        logger.info(MessageConstants.INVENTORY_EXISTS);
        logger.info(MessageConstants.UPDATE_INVENTORY);

        // Actualiza la información del inventario del producto
        Inventory updatedInventoryProduct = inventoryDao.save(inventoryToUpdate);

        return modelMapper.map(updatedInventoryProduct, InventoryGenericResponseDto.class);
    }

    /**
     * Método para eliminar del inventario un producto por su ID
     *
     * @param id Identificador único del producto en el inventario
     */
    @Override
    public void deleteById(Integer id) {
        logger.info(MESSAGE_FORMAT, MessageConstants.DELETE_INVENTORY, MessageConstants.ID, id);

        inventoryDao.deleteById(id);
    }
}
