package com.alivro.spring.sleepyringtail.service.impl;

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
    private final InventoryDao inventoryDao;
    private final ModelMapper modelMapper = new ModelMapper();
    private final Logger logger = LoggerFactory.getLogger(IInventoryServiceImpl.class);

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
     * Método para buscar el stock de todos los productos
     *
     * @param pageable Información de paginación
     * @return Información del stock de todos los productos
     */
    @Override
    public CustomPaginationData<InventoryGenericResponseDto, Inventory> findAll(Pageable pageable) {
        logger.info("Busca el stock de todos los productos.");

        Page<Inventory> productStockPage = inventoryDao.findAll(pageable);

        // Información del stock de los productos
        List<InventoryGenericResponseDto> foundProductStocks = productStockPage.stream()
                .map(inventoryProduct -> modelMapper.map(inventoryProduct, InventoryGenericResponseDto.class))
                .toList();

        return new CustomPaginationData<>(foundProductStocks, productStockPage);
    }

    /**
     * Método para buscar el stock de un producto por su ID
     *
     * @param id Identificador único del producto en el inventario
     * @return Información del stock del producto buscado
     */
    @Override
    public InventoryGenericResponseDto findById(Integer id) {
        logger.info("Busca el stock del producto. ID: {}", id);

        Optional<Inventory> foundProductStock = inventoryDao.findById(id);

        if (foundProductStock.isEmpty()) {
            logger.info("Stock del producto no encontrado. ID: {}", id);

            throw new DataNotFoundException("Product stock not found!");
        }

        return modelMapper.map(foundProductStock.get(), InventoryGenericResponseDto.class);
    }

    /**
     * Método para guardar la información del stock de un producto
     *
     * @param request Información del stock del producto a guardar
     * @return Información del stock del producto guardado
     */
    @Override
    public InventoryGenericResponseDto save(InventoryGenericRequestDto request) {
        Integer id = request.getProduct().getId();

        logger.info("Busca existencia del producto en el inventario. ID: {}", id);

        // Verifica si ya existe en el inventario un producto con el mismo ID
        if (inventoryDao.existsByProductId(id)) {
            logger.info("Stock del producto existente. ID: {}", id);
            logger.info("Stock del producto no guardado. ID: {}", id);

            throw new DataAlreadyExistsException("Existing product stock!");
        }

        logger.info("Stock del producto no existente. ID: {}", id);
        logger.info("Guarda stock del producto. ID: {}", id);

        // Guarda la información del stock del producto
        Inventory savedProductStock = inventoryDao.save(
                modelMapper.map(request, Inventory.class)
        );

        return modelMapper.map(savedProductStock, InventoryGenericResponseDto.class);
    }

    /**
     * Método para actualizar la información del stock de un producto
     *
     * @param id      Identificador único del producto en el inventario
     * @param request Información del stock del producto a actualizar
     * @return Información del stock del producto actualizado
     */
    @Override
    public InventoryGenericResponseDto update(Integer id, InventoryGenericRequestDto request) {
        logger.info("Busca el producto en el inventario. ID: {}", id);

        Optional<Inventory> foundProductStock = inventoryDao.findById(id);

        // Verifica si existe en el inventario un producto con ese id
        if (foundProductStock.isEmpty()) {
            logger.info("Stock del producto no existente. ID: {}", id);
            logger.info("Stock del producto no actualizado. ID: {}", id);

            throw new DataNotFoundException("Product stock does not exist!");
        }

        // Información del stock del producto a actualizar
        Inventory productStockToUpdate = foundProductStock.get();
        Product product = modelMapper.map(request.getProduct(), Product.class);

        productStockToUpdate.setQuantityAvailable(request.getQuantityAvailable());
        productStockToUpdate.setMinimumStock(request.getMinimumStock());
        productStockToUpdate.setMaximumStock(request.getMaximumStock());
        productStockToUpdate.setProduct(product);

        logger.info("Actualiza stock del producto. ID: {}", id);

        // Actualiza la información del stock del producto
        Inventory updatedInventoryProduct = inventoryDao.save(productStockToUpdate);

        return modelMapper.map(updatedInventoryProduct, InventoryGenericResponseDto.class);
    }

    /**
     * Método para eliminar del inventario un producto por su ID
     *
     * @param id Identificador único del producto en el inventario
     */
    @Override
    public void deleteById(Integer id) {
        logger.info("Elimina stock del producto. ID: {}", id);

        inventoryDao.deleteById(id);
    }
}
