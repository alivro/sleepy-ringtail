package com.alivro.spring.sleepyringtail.service.impl;

import com.alivro.spring.sleepyringtail.dao.ProductDao;
import com.alivro.spring.sleepyringtail.exception.DataAlreadyExistsException;
import com.alivro.spring.sleepyringtail.exception.DataNotFoundException;
import com.alivro.spring.sleepyringtail.model.Product;
import com.alivro.spring.sleepyringtail.model.product.request.ProductSaveRequestDto;
import com.alivro.spring.sleepyringtail.model.product.response.ProductResponseDto;
import com.alivro.spring.sleepyringtail.util.pagination.CustomPageMetadata;
import com.alivro.spring.sleepyringtail.util.pagination.CustomPaginationData;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class IProductServiceImplTest {
    @Mock
    private ProductDao productDao;

    @InjectMocks
    private IProductServiceImpl productService;

    // Buscar todos los productos
    private static Product ardillasSaladas;
    private static Product estrellaMarina;
    private static Product osoAlmendrado;
    private static Product vacaNapolitana;

    // Guardar un nuevo producto
    private static ProductSaveRequestDto productSaveRequestVacaChocolate;
    private static Product productToSaveVacaChocolate;
    private static Product productSavedVacaChocolate;

    // Actualizar un producto existente
    private static ProductSaveRequestDto productUpdateRequestVacaChocolate;
    private static Product productToUpdateVacaChocolate;
    private static Product productUpdatedVacaChocolate;

    @BeforeAll
    public static void setup() {
        // Buscar todos los productos
        ardillasSaladas = Product.builder()
                .id(1)
                .name("Ardillas Saladas")
                .description("Cacahuates salados")
                .size("60 g")
                .price(BigDecimal.valueOf(21.00))
                .barcode("7501030459941")
                .build();

        estrellaMarina = Product.builder()
                .id(2)
                .name("Estrella Marina")
                .description("Agua embotellada")
                .size("1 L")
                .price(BigDecimal.valueOf(14.00))
                .barcode("7501086801046")
                .build();

        osoAlmendrado = Product.builder()
                .id(3)
                .name("Oso Almendrado")
                .description("Barra de chocolate con leche y almendras")
                .size("40 g")
                .price(BigDecimal.valueOf(36.00))
                .barcode("7501024544295")
                .build();

        vacaNapolitana = Product.builder()
                .id(4)
                .name("Vaca Napolitana")
                .description("Helado sabor napolitano")
                .size("1 L")
                .price(BigDecimal.valueOf(45.00))
                .barcode("7501130902194")
                .build();

        // Guardar un nuevo producto
        productSaveRequestVacaChocolate = ProductSaveRequestDto.builder()
                .name("Vaca de chocolate")
                .description("Helado")
                .size("500 ml")
                .price(BigDecimal.valueOf(42.00))
                .barcode("7506306417854")
                .build();

        productToSaveVacaChocolate = ProductSaveRequestDto.mapRequestDtoToEntity(productSaveRequestVacaChocolate);

        productSavedVacaChocolate = ProductSaveRequestDto.mapRequestDtoToEntity(5, productSaveRequestVacaChocolate);

        // Actualizar un producto existente
        productUpdateRequestVacaChocolate = ProductSaveRequestDto.builder()
                .name("Vaca de Chocolate")
                .description("Helado sabor chocolate")
                .size("1 L")
                .price(BigDecimal.valueOf(52.00))
                .barcode("7501130902095")
                .build();

        productToUpdateVacaChocolate = ProductSaveRequestDto.mapRequestDtoToEntity(productUpdateRequestVacaChocolate);

        productUpdatedVacaChocolate = ProductSaveRequestDto.mapRequestDtoToEntity(5, productUpdateRequestVacaChocolate);
    }

    @Test
    public void findAll_OrderByNameAsc_ExistingProducts_Return_ListOfProducts() {
        // Given
        int pageNumber = 0;
        int pageSize = 5;
        String sortBy = "name";

        List<Product> products = new ArrayList<>();
        products.add(ardillasSaladas);
        products.add(estrellaMarina);
        products.add(osoAlmendrado);
        products.add(vacaNapolitana);

        Pageable pageable = PageRequest.ofSize(pageSize)
                .withPage(pageNumber)
                .withSort(Sort.by(sortBy).ascending());

        given(productDao.findAll(pageable)).willReturn(
                new PageImpl<>(products, pageable, products.size())
        );

        // When
        CustomPaginationData<ProductResponseDto, Product> productsData = productService.findAll(
                PageRequest.of(0, 5, Sort.by("name").ascending())
        );

        // Then
        List<ProductResponseDto> data = productsData.getData();
        CustomPageMetadata meta = productsData.getMetadata();

        assertThat(data.size()).isEqualTo(4);
        assertThat(data.get(0).getName()).isEqualTo("Ardillas Saladas");
        assertThat(data.get(1).getName()).isEqualTo("Estrella Marina");
        assertThat(data.get(2).getName()).isEqualTo("Oso Almendrado");
        assertThat(data.get(3).getName()).isEqualTo("Vaca Napolitana");

        assertThat(meta.getPageNumber()).isZero();
        assertThat(meta.getPageSize()).isEqualTo(5);
        assertThat(meta.getNumberOfElements()).isEqualTo(4);
        assertThat(meta.getTotalElements()).isEqualTo(4);
        assertThat(meta.getTotalPages()).isEqualTo(1);
    }

    @Test
    public void findAll_NonExistingProducts_Return_EmptyListOfProducts() {
        int pageNumber = 0;
        int pageSize = 5;
        String sortBy = "id";

        List<Product> products = new ArrayList<>();

        Pageable pageable = PageRequest.ofSize(pageSize)
                .withPage(pageNumber)
                .withSort(Sort.by(sortBy).ascending());

        given(productDao.findAll(pageable)).willReturn(
                new PageImpl<>(products, pageable, 0)
        );

        // When
        CustomPaginationData<ProductResponseDto, Product> productsData = productService.findAll(
                PageRequest.of(0, 5, Sort.by("id").ascending())
        );

        // Then
        List<ProductResponseDto> data = productsData.getData();
        CustomPageMetadata meta = productsData.getMetadata();

        assertThat(data.size()).isEqualTo(0);

        assertThat(meta.getPageNumber()).isEqualTo(0);
        assertThat(meta.getPageSize()).isEqualTo(5);
        assertThat(meta.getNumberOfElements()).isEqualTo(0);
        assertThat(meta.getTotalElements()).isEqualTo(0);
        assertThat(meta.getTotalPages()).isEqualTo(0);
    }

    @Test
    public void findAllByName_OrderByNameAsc_ExistingProducts_Return_ListOfProducts() {
        // Given
        String word = "na";
        int pageNumber = 0;
        int pageSize = 5;
        String sortBy = "name";

        List<Product> products = new ArrayList<>();
        products.add(estrellaMarina);
        products.add(vacaNapolitana);

        Pageable pageable = PageRequest.ofSize(pageSize)
                .withPage(pageNumber)
                .withSort(Sort.by(sortBy).ascending());

        given(productDao.findByNameContainingIgnoreCase(word, pageable)).willReturn(
                new PageImpl<>(products, pageable, products.size())
        );

        // When
        CustomPaginationData<ProductResponseDto, Product> productsData = productService.findAllByName(
                "na",
                PageRequest.of(0, 5, Sort.by("name").ascending())
        );

        // Then
        List<ProductResponseDto> data = productsData.getData();
        CustomPageMetadata meta = productsData.getMetadata();

        assertThat(data.size()).isEqualTo(2);
        assertThat(data.get(0).getName()).isEqualTo("Estrella Marina");
        assertThat(data.get(1).getName()).isEqualTo("Vaca Napolitana");

        assertThat(meta.getPageNumber()).isZero();
        assertThat(meta.getPageSize()).isEqualTo(5);
        assertThat(meta.getNumberOfElements()).isEqualTo(2);
        assertThat(meta.getTotalElements()).isEqualTo(2);
        assertThat(meta.getTotalPages()).isEqualTo(1);
    }

    @Test
    public void findAllByName_NonExistingProducts_Return_EmptyListOfProducts() {
        String word = "nu";
        int pageNumber = 0;
        int pageSize = 5;
        String sortBy = "id";

        List<Product> products = new ArrayList<>();

        Pageable pageable = PageRequest.ofSize(pageSize)
                .withPage(pageNumber)
                .withSort(Sort.by(sortBy).ascending());

        given(productDao.findByNameContainingIgnoreCase(word, pageable)).willReturn(
                new PageImpl<>(products, pageable, 0)
        );

        // When
        CustomPaginationData<ProductResponseDto, Product> productsData = productService.findAllByName(
                "nu",
                PageRequest.of(0, 5, Sort.by("id").ascending())
        );

        // Then
        List<ProductResponseDto> data = productsData.getData();
        CustomPageMetadata meta = productsData.getMetadata();

        assertThat(data.size()).isEqualTo(0);

        assertThat(meta.getPageNumber()).isEqualTo(0);
        assertThat(meta.getPageSize()).isEqualTo(5);
        assertThat(meta.getNumberOfElements()).isEqualTo(0);
        assertThat(meta.getTotalElements()).isEqualTo(0);
        assertThat(meta.getTotalPages()).isEqualTo(0);
    }

    @Test
    public void findAllByDescription_OrderByNameAsc_ExistingProducts_Return_ListOfProducts() {
        // Given
        String word = "ua";
        int pageNumber = 0;
        int pageSize = 5;
        String sortBy = "name";

        List<Product> products = new ArrayList<>();
        products.add(ardillasSaladas);
        products.add(estrellaMarina);

        Pageable pageable = PageRequest.ofSize(pageSize)
                .withPage(pageNumber)
                .withSort(Sort.by(sortBy).ascending());

        given(productDao.findByDescriptionContainingIgnoreCase(word, pageable)).willReturn(
                new PageImpl<>(products, pageable, products.size())
        );

        // When
        CustomPaginationData<ProductResponseDto, Product> productsData = productService.findAllByDescription(
                "ua",
                PageRequest.of(0, 5, Sort.by("name").ascending())
        );

        // Then
        List<ProductResponseDto> data = productsData.getData();
        CustomPageMetadata meta = productsData.getMetadata();

        assertThat(data.size()).isEqualTo(2);
        assertThat(data.get(0).getName()).isEqualTo("Ardillas Saladas");
        assertThat(data.get(0).getDescription()).isEqualTo("Cacahuates salados");
        assertThat(data.get(1).getName()).isEqualTo("Estrella Marina");
        assertThat(data.get(1).getDescription()).isEqualTo("Agua embotellada");

        assertThat(meta.getPageNumber()).isZero();
        assertThat(meta.getPageSize()).isEqualTo(5);
        assertThat(meta.getNumberOfElements()).isEqualTo(2);
        assertThat(meta.getTotalElements()).isEqualTo(2);
        assertThat(meta.getTotalPages()).isEqualTo(1);
    }

    @Test
    public void findAllByDescription_NonExistingProducts_Return_EmptyListOfProducts() {
        String word = "au";
        int pageNumber = 0;
        int pageSize = 5;
        String sortBy = "id";

        List<Product> products = new ArrayList<>();

        Pageable pageable = PageRequest.ofSize(pageSize)
                .withPage(pageNumber)
                .withSort(Sort.by(sortBy).ascending());

        given(productDao.findByDescriptionContainingIgnoreCase(word, pageable)).willReturn(
                new PageImpl<>(products, pageable, 0)
        );

        // When
        CustomPaginationData<ProductResponseDto, Product> productsData = productService.findAllByDescription(
                "au",
                PageRequest.of(0, 5, Sort.by("id").ascending())
        );

        // Then
        List<ProductResponseDto> data = productsData.getData();
        CustomPageMetadata meta = productsData.getMetadata();

        assertThat(data.size()).isEqualTo(0);

        assertThat(meta.getPageNumber()).isEqualTo(0);
        assertThat(meta.getPageSize()).isEqualTo(5);
        assertThat(meta.getNumberOfElements()).isEqualTo(0);
        assertThat(meta.getTotalElements()).isEqualTo(0);
        assertThat(meta.getTotalPages()).isEqualTo(0);
    }

    @Test
    public void findById_ExistingProduct_Return_FoundProduct() {
        // Given
        Integer productId = 1;

        given(productDao.findById(productId)).willReturn(Optional.of(ardillasSaladas));

        // When
        ProductResponseDto foundProduct = productService.findById(1);

        // Then
        assertThat(foundProduct).isNotNull();
        assertThat(foundProduct.getId()).isEqualTo(1);
        assertThat(foundProduct.getName()).isEqualTo("Ardillas Saladas");
        assertThat(foundProduct.getPrice()).isEqualTo(BigDecimal.valueOf(21.00));
        assertThat(foundProduct.getBarcode()).isEqualTo("7501030459941");
    }

    @Test
    public void findById_NonExistingProduct_Throw_DataNotFoundException() {
        // Given
        given(productDao.findById(anyInt())).willReturn(Optional.empty());

        // When
        Throwable thrown = assertThrows(DataNotFoundException.class,
                () -> productService.findById(100));

        // Then
        MatcherAssert.assertThat(thrown.getMessage(), is("Product not found!"));
    }

    @Test
    public void save_NonExistingProduct_Return_SavedProduct() {
        // Given
        given(productDao.existsByBarcode(productSaveRequestVacaChocolate.getBarcode())).willReturn(false);
        given(productDao.save(productToSaveVacaChocolate)).willReturn(productSavedVacaChocolate);

        // When
        ProductResponseDto savedProduct = productService.save(productSaveRequestVacaChocolate);

        // Then
        assertThat(savedProduct).isNotNull();
        assertThat(savedProduct.getName()).isEqualTo("Vaca de chocolate");
        assertThat(savedProduct.getDescription()).isEqualTo("Helado");
        assertThat(savedProduct.getSize()).isEqualTo("500 ml");
        assertThat(savedProduct.getPrice()).isEqualTo(BigDecimal.valueOf(42.00));
        assertThat(savedProduct.getBarcode()).isEqualTo("7506306417854");
    }

    @Test
    public void save_ExistingProduct_Throw_DataAlreadyExistsException() {
        // Given
        given(productDao.existsByBarcode(anyString())).willReturn(true);

        // When
        Throwable thrown = assertThrows(DataAlreadyExistsException.class,
                () -> productService.save(productSaveRequestVacaChocolate));

        // Then
        MatcherAssert.assertThat(thrown.getMessage(), is("Product already exists!"));
    }

    @Test
    public void update_ExistingProduct_Return_UpdatedProduct() {
        // Given
        Integer productId = 5;

        given(productDao.findById(productId)).willReturn(Optional.ofNullable(productToUpdateVacaChocolate));
        given(productDao.save(productToUpdateVacaChocolate)).willReturn(productUpdatedVacaChocolate);

        // When
        ProductResponseDto updatedProduct = productService.update(5, productUpdateRequestVacaChocolate);

        // Then
        assertThat(updatedProduct).isNotNull();
        assertThat(updatedProduct.getId()).isEqualTo(5);
        assertThat(updatedProduct.getName()).isEqualTo("Vaca de Chocolate");
        assertThat(updatedProduct.getDescription()).isEqualTo("Helado sabor chocolate");
        assertThat(updatedProduct.getSize()).isEqualTo("1 L");
        assertThat(updatedProduct.getPrice()).isEqualTo(BigDecimal.valueOf(52.00));
        assertThat(updatedProduct.getBarcode()).isEqualTo("7501130902095");
    }

    @Test
    public void update_NonExistingProduct_Throw_DataNotFoundException() {
        // Given
        given(productDao.findById(anyInt())).willReturn(Optional.empty());

        // When
        Throwable thrown = assertThrows(DataNotFoundException.class,
                () -> productService.update(100, productUpdateRequestVacaChocolate));

        // Then
        MatcherAssert.assertThat(thrown.getMessage(), is("Product does not exist!"));
    }

    @Test
    public void deleteById_Category_NoReturn() {
        // Given
        willDoNothing().given(productDao).deleteById(anyInt());

        // When
        productService.deleteById(10);

        // Then
        verify(productDao, times(1)).deleteById(10);
    }
}
