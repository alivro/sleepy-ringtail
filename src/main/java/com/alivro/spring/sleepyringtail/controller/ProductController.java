package com.alivro.spring.sleepyringtail.controller;

import com.alivro.spring.sleepyringtail.handler.ResponseHandler;
import com.alivro.spring.sleepyringtail.model.Product;
import com.alivro.spring.sleepyringtail.model.product.request.ProductGenericRequestDto;
import com.alivro.spring.sleepyringtail.model.product.response.ProductGenericResponseDto;
import com.alivro.spring.sleepyringtail.model.product.response.ProductGetResponseDto;
import com.alivro.spring.sleepyringtail.service.IProductService;
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
@RequestMapping("/api/v1/product")
@CrossOrigin(origins = "http://localhost:4200")
public class ProductController {
    private final IProductService productService;
    private final Logger logger = LoggerFactory.getLogger(ProductController.class);

    /**
     * Constructor
     *
     * @param productService Product service
     */
    @Autowired
    public ProductController(IProductService productService) {
        this.productService = productService;
    }

    /**
     * Endpoint para buscar todos los productos
     *
     * @return Información de todos los productos
     */
    @GetMapping("/getAll")
    public ResponseEntity<CustomResponse<ProductGenericResponseDto, CustomPageMetadata>> getAllProducts(
            @PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable) {
        CustomPaginationData<ProductGenericResponseDto, Product> productsData = productService.findAll(pageable);

        logger.info("Productos encontrados.");

        return ResponseHandler.sendResponse(
                HttpStatus.OK, "Found products!", productsData.getData(), productsData.getMetadata()
        );
    }

    /**
     * Endpoint para buscar todos los productos que contengan una palabra dada en su nombre
     *
     * @param word     Palabra a buscar en el nombre del producto
     * @param pageable Información de paginación
     * @return Información de todos los productos que cumplen con el criterio de búsqueda
     */
    @GetMapping("/getAllByName/{word}")
    public ResponseEntity<CustomResponse<ProductGenericResponseDto, CustomPageMetadata>> getAllProductsByName(
            @PathVariable("word") String word,
            @PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable) {
        CustomPaginationData<ProductGenericResponseDto, Product> productsData =
                productService.findAllByName(word, pageable);

        logger.info("Productos encontrados.");

        return ResponseHandler.sendResponse(
                HttpStatus.OK, "Found products!", productsData.getData(), productsData.getMetadata()
        );
    }

    /**
     * Endpoint para buscar todos los productos que contengan una palabra dada en su descripción
     *
     * @param word     Palabra a buscar en la descripción del producto
     * @param pageable Información de paginación
     * @return Información de todos los productos que cumplen con el criterio de búsqueda
     */
    @GetMapping("/getAllByDescription/{word}")
    public ResponseEntity<CustomResponse<ProductGenericResponseDto, CustomPageMetadata>> getAllProductsByDescription(
            @PathVariable("word") String word,
            @PageableDefault(page = 0, size = 5, sort = "id", direction = Sort.Direction.ASC)
            Pageable pageable) {
        CustomPaginationData<ProductGenericResponseDto, Product> productsData =
                productService.findAllByDescription(word, pageable);

        logger.info("Productos encontrados.");

        return ResponseHandler.sendResponse(
                HttpStatus.OK, "Found products!", productsData.getData(), productsData.getMetadata()
        );
    }

    /**
     * Endpoint para buscar un producto por su ID
     *
     * @param id Identificador único del producto
     * @return Información del producto buscado
     */
    @GetMapping("/get/{id}")
    public ResponseEntity<CustomResponse<ProductGetResponseDto, Void>> getProduct(@PathVariable("id") Integer id) {
        ProductGetResponseDto foundProduct = productService.findById(id);

        logger.info("Producto encontrado. ID: {}", id);

        return ResponseHandler.sendResponse(
                HttpStatus.OK, "Found product!", foundProduct
        );
    }

    /**
     * Endpoint para guardar una nueva producto
     *
     * @param request Información del producto a guardar
     * @return Información del producto guardado
     */
    @PostMapping("/save")
    public ResponseEntity<CustomResponse<ProductGenericResponseDto, Void>> saveProduct(
            @Valid @RequestBody ProductGenericRequestDto request) {
        ProductGenericResponseDto savedProduct = productService.save(request);

        logger.info("Producto guardado. ID: {}", savedProduct.getId());

        return ResponseHandler.sendResponse(
                HttpStatus.CREATED, "Saved product!", savedProduct
        );
    }

    /**
     * Endpoint para actualizar la información de un producto
     *
     * @param id      Identificador único del producto
     * @param request Información deñ producto a actualizar
     * @return Información del producto actualizado
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<CustomResponse<ProductGenericResponseDto, Void>> updateProduct(
            @PathVariable("id") Integer id, @Valid @RequestBody ProductGenericRequestDto request) {
        ProductGenericResponseDto updatedProduct = productService.update(id, request);

        logger.info("Producto actualizado. ID: {}", id);

        return ResponseHandler.sendResponse(
                HttpStatus.OK, "Updated product!", updatedProduct
        );
    }

    /**
     * Endpoint para eliminar un producto por su ID
     *
     * @param id El identificador único del producto
     * @return Estatus 200
     */
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<CustomResponse<Void, Void>> deleteProduct(@PathVariable("id") Integer id) {
        productService.deleteById(id);

        logger.info("Producto eliminado. ID: {}", id);

        return ResponseHandler.sendResponse(
                HttpStatus.OK, "Deleted product!"
        );
    }
}
