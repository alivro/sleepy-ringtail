package com.alivro.spring.sleepyringtail.service.impl;

import com.alivro.spring.sleepyringtail.dao.ProductDao;
import com.alivro.spring.sleepyringtail.exception.DataAlreadyExistsException;
import com.alivro.spring.sleepyringtail.exception.DataNotFoundException;
import com.alivro.spring.sleepyringtail.model.Product;
import com.alivro.spring.sleepyringtail.model.Subcategory;
import com.alivro.spring.sleepyringtail.model.product.request.ProductGenericRequestDto;
import com.alivro.spring.sleepyringtail.model.product.response.ProductGenericResponseDto;
import com.alivro.spring.sleepyringtail.model.product.response.ProductGetResponseDto;
import com.alivro.spring.sleepyringtail.service.IProductService;
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
public class IProductServiceImpl implements IProductService {
    private final ProductDao productDao;
    private final ModelMapper modelMapper = new ModelMapper();
    private final Logger logger = LoggerFactory.getLogger(IProductServiceImpl.class);

    /**
     * Constructor
     *
     * @param productDao Product Dao
     */
    @Autowired
    public IProductServiceImpl(ProductDao productDao) {
        this.productDao = productDao;
    }

    /**
     * Método para buscar todos los productos
     *
     * @return Información de todos los productos
     */
    @Override
    public CustomPaginationData<ProductGenericResponseDto, Product> findAll(Pageable pageable) {
        logger.info("Busca todos los productos.");

        Page<Product> productsPage = productDao.findAll(pageable);

        // Información de los productos
        List<ProductGenericResponseDto> foundProducts = productsPage.stream()
                .map(product -> modelMapper.map(product, ProductGenericResponseDto.class))
                .toList();

        return new CustomPaginationData<>(foundProducts, productsPage);
    }

    /**
     * Método para buscar los productos que contengan una palabra dada en su nombre
     *
     * @param word     Palabra a buscar en el nombre del producto
     * @param pageable Información de paginación
     * @return Información de los productos que cumplen con el criterio de búsqueda
     */
    @Override
    public CustomPaginationData<ProductGenericResponseDto, Product> findAllByName(String word, Pageable pageable) {
        logger.info("Busca todos los productos. Nombre: {} ", word);

        Page<Product> productPage = productDao.findByNameContainingIgnoreCase(word, pageable);

        // Información de las categorías
        List<ProductGenericResponseDto> foundProducts = productPage.stream()
                .map(product -> modelMapper.map(product, ProductGenericResponseDto.class))
                .toList();

        return new CustomPaginationData<>(foundProducts, productPage);
    }

    /**
     * Método para buscar los productos que contengan una palabra dada en su descripción
     *
     * @param word     Palabra a buscar en la descripción del producto
     * @param pageable Información de paginación
     * @return Información de los productos que cumplen con el criterio de búsqueda
     */
    @Override
    public CustomPaginationData<ProductGenericResponseDto, Product> findAllByDescription(String word, Pageable pageable) {
        logger.info("Busca todos los productos. Descripción: {} ", word);

        Page<Product> productPage = productDao.findByDescriptionContainingIgnoreCase(word, pageable);

        // Información de las categorías
        List<ProductGenericResponseDto> foundProducts = productPage.stream()
                .map(product -> modelMapper.map(product, ProductGenericResponseDto.class))
                .toList();

        return new CustomPaginationData<>(foundProducts, productPage);
    }

    /**
     * Método para buscar un producto por su ID
     *
     * @param id Identificador único del producto
     * @return Información del producto buscado
     */
    @Override
    public ProductGetResponseDto findById(Integer id) {
        logger.info("Busca producto. ID: {}", id);

        Optional<Product> foundProduct = productDao.findById(id);

        if (foundProduct.isEmpty()) {
            logger.info("Producto no encontrada. ID: {}", id);

            throw new DataNotFoundException("Product not found!");
        }

        return modelMapper.map(foundProduct.get(), ProductGetResponseDto.class);
    }

    /**
     * Método para guardar un nuevo producto
     *
     * @param request Información del producto a guardar
     * @return Información del producto guardado
     */
    @Override
    public ProductGenericResponseDto save(ProductGenericRequestDto request) {
        String barcode = request.getBarcode();

        logger.info("Busca producto. Código de barras: {}", barcode);

        // Verifica si ya existe un producto con el mismo código de barras
        if (productDao.existsByBarcode(barcode)) {
            logger.info("Producto existente. Código de barras: {}", barcode);
            logger.info("Producto no guardado. Código de barras: {}", barcode);

            throw new DataAlreadyExistsException("Product already exists!");
        }

        logger.info("Producto no existente. Código de barras: {}", barcode);
        logger.info("Guarda producto. Código de barras: {}", barcode);

        // Guarda la información del nuevo producto
        Product savedProduct = productDao.save(
                modelMapper.map(request, Product.class)
        );

        return modelMapper.map(savedProduct, ProductGenericResponseDto.class);
    }

    /**
     * Método para actualizar la información de un producto
     *
     * @param id      Identificador único del producto
     * @param request Información del producto a actualizar
     * @return Información del producto actualizado
     */
    @Override
    public ProductGenericResponseDto update(Integer id, ProductGenericRequestDto request) {
        logger.info("Busca producto. ID: {}", id);

        Optional<Product> foundProduct = productDao.findById(id);

        // Verifica si existe un producto con ese id
        if (foundProduct.isEmpty()) {
            logger.info("Producto no existente. ID: {}", id);
            logger.info("Producto no actualizado. ID: {}", id);

            throw new DataNotFoundException("Product does not exist!");
        }

        // Información del producto a actualizar
        Product productToUpdate = foundProduct.get();
        Subcategory subcategory = modelMapper.map(request.getSubcategory(), Subcategory.class);

        productToUpdate.setName(request.getName());
        productToUpdate.setDescription(request.getDescription());
        productToUpdate.setSize(request.getSize());
        productToUpdate.setPrice(request.getPrice());
        productToUpdate.setBarcode(request.getBarcode());
        productToUpdate.setSubcategory(subcategory);

        logger.info("Actualiza producto. ID: {}", id);

        // Actualiza la información del producto
        Product updatedProduct = productDao.save(productToUpdate);

        return modelMapper.map(updatedProduct, ProductGenericResponseDto.class);
    }

    /**
     * Método para eliminar un producto por su ID
     *
     * @param id Identificador único del producto
     */
    @Override
    public void deleteById(Integer id) {
        logger.info("Elimina producto. ID: {}", id);

        productDao.deleteById(id);
    }
}
