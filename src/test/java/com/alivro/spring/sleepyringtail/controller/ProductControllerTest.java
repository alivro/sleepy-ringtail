package com.alivro.spring.sleepyringtail.controller;

import com.alivro.spring.sleepyringtail.exception.DataAlreadyExistsException;
import com.alivro.spring.sleepyringtail.exception.DataNotFoundException;
import com.alivro.spring.sleepyringtail.model.Product;
import com.alivro.spring.sleepyringtail.model.product.request.ProductSaveRequestDto;
import com.alivro.spring.sleepyringtail.model.product.request.SubcategoryOfProductRequestDto;
import com.alivro.spring.sleepyringtail.model.product.response.ProductResponseDto;
import com.alivro.spring.sleepyringtail.model.product.response.SubcategoryOfProductResponseDto;
import com.alivro.spring.sleepyringtail.service.IProductService;
import com.alivro.spring.sleepyringtail.util.pagination.CustomPageMetadata;
import com.alivro.spring.sleepyringtail.util.pagination.CustomPaginationData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext
public class ProductControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    private IProductService productService;

    @Autowired
    private ObjectMapper objectMapper;

    // URL
    private static final String url = "/api/v1/product";

    // Buscar todos los productos
    private static ProductResponseDto productResponseArdillasSaladas;
    private static ProductResponseDto productResponseEstrellaMarina;
    private static ProductResponseDto productResponseOsoAlmendrado;
    private static ProductResponseDto productResponseVacaNapolitana;

    // Guardar un nuevo producto
    private static ProductSaveRequestDto productSaveRequestVacaChocolate;
    private static ProductResponseDto productSavedResponseVacaChocolate;

    // Actualizar un producto existente
    private static ProductSaveRequestDto productUpdateRequestVacaChocolate;
    private static ProductResponseDto productUpdatedResponseVacaChocolate;

    @BeforeAll
    public static void setup() {
        // Buscar todos los productos
        SubcategoryOfProductResponseDto aguaNatural = SubcategoryOfProductResponseDto.builder()
                .id(1)
                .name("Agua natural")
                .build();

        SubcategoryOfProductResponseDto botanas = SubcategoryOfProductResponseDto.builder()
                .id(2)
                .name("Botanas")
                .build();

        SubcategoryOfProductResponseDto chocolate = SubcategoryOfProductResponseDto.builder()
                .id(3)
                .name("Chocolate")
                .build();

        SubcategoryOfProductResponseDto heladoLeche = SubcategoryOfProductResponseDto.builder()
                .id(4)
                .name("Helado base leche")
                .build();

        productResponseArdillasSaladas = ProductResponseDto.builder()
                .id(1)
                .name("Ardillas Saladas")
                .description("Cacahuates salados")
                .size("60 g")
                .price(BigDecimal.valueOf(21.00))
                .barcode("7501030459941")
                .subcategory(botanas)
                .build();

        productResponseEstrellaMarina = ProductResponseDto.builder()
                .id(2)
                .name("Estrella Marina")
                .description("Agua embotellada")
                .size("1 L")
                .price(BigDecimal.valueOf(14.00))
                .barcode("7501086801046")
                .subcategory(aguaNatural)
                .build();

        productResponseOsoAlmendrado = ProductResponseDto.builder()
                .id(3)
                .name("Oso Almendrado")
                .description("Barra de chocolate con leche y almendras")
                .size("40 g")
                .price(BigDecimal.valueOf(36.00))
                .barcode("7501024544295")
                .subcategory(chocolate)
                .build();

        productResponseVacaNapolitana = ProductResponseDto.builder()
                .id(4)
                .name("Vaca Napolitana")
                .description("Helado sabor napolitano")
                .size("1 L")
                .price(BigDecimal.valueOf(45.00))
                .barcode("7501130902194")
                .subcategory(heladoLeche)
                .build();

        // Guardar un nuevo producto
        SubcategoryOfProductRequestDto subcategoryOfProductRequestChocolate = SubcategoryOfProductRequestDto.builder()
                .id(3)
                .name("Dulces")
                .build();

        productSaveRequestVacaChocolate = ProductSaveRequestDto.builder()
                .name("Vaca de chocolate")
                .description("Helado")
                .size("500 ml")
                .price(BigDecimal.valueOf(42.00))
                .barcode("7506306417854")
                .subcategory(subcategoryOfProductRequestChocolate)
                .build();

        productSavedResponseVacaChocolate = mapRequestDtoToResponseDto(5, productSaveRequestVacaChocolate);

        // Actualizar un producto existente
        productUpdateRequestVacaChocolate = ProductSaveRequestDto.builder()
                .name("Vaca de Chocolate")
                .description("Helado sabor chocolate")
                .size("1 L")
                .price(BigDecimal.valueOf(52.00))
                .barcode("7501130902095")
                .subcategory(subcategoryOfProductRequestChocolate)
                .build();

        productUpdatedResponseVacaChocolate = mapRequestDtoToResponseDto(5, productUpdateRequestVacaChocolate);
    }

    @Test
    public void getAllByNameAsc_ExistingProducts_Return_IsOk() throws Exception {
        //Given
        int pageNumber = 0;
        int pageSize = 5;
        String sortBy = "name";
        Pageable pageable = PageRequest.ofSize(pageSize)
                .withPage(pageNumber)
                .withSort(Sort.by(sortBy).ascending());

        List<ProductResponseDto> foundProducts = new ArrayList<>();
        foundProducts.add(productResponseArdillasSaladas);
        foundProducts.add(productResponseEstrellaMarina);
        foundProducts.add(productResponseOsoAlmendrado);
        foundProducts.add(productResponseVacaNapolitana);

        CustomPageMetadata metadata = CustomPageMetadata.builder()
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .numberOfElements(foundProducts.size())
                .totalPages((int) Math.ceil((double) foundProducts.size() / pageSize))
                .totalElements(foundProducts.size())
                .build();

        given(productService.findAll(pageable)).willReturn(
                CustomPaginationData.<ProductResponseDto, Product>builder()
                        .data(foundProducts)
                        .metadata(metadata)
                        .build()
        );

        // When
        ResultActions response = mockMvc.perform(get(url + "/getAll")
                .param("page", "0")
                .param("size", "5")
                .param("sort", "name,asc")
        );

        // Then
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Found products!")));

        response.andExpect(MockMvcResultMatchers.jsonPath("$.data", hasSize(4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name",
                        CoreMatchers.is("Ardillas Saladas")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].name",
                        CoreMatchers.is("Estrella Marina")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[2].name",
                        CoreMatchers.is("Oso Almendrado")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[3].name",
                        CoreMatchers.is("Vaca Napolitana")));

        response.andExpect(MockMvcResultMatchers.jsonPath("$.metadata.pageNumber",
                        CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.metadata.pageSize",
                        CoreMatchers.is(5)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.metadata.numberOfElements",
                        CoreMatchers.is(4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.metadata.totalPages",
                        CoreMatchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.metadata.totalElements",
                        CoreMatchers.is(4)));
    }

    @Test
    public void getAll_NoExistingProducts_Return_IsOk() throws Exception {
        //Given
        int pageNumber = 0;
        int pageSize = 5;
        String sortBy = "id";
        Pageable pageable = PageRequest.ofSize(pageSize)
                .withPage(pageNumber)
                .withSort(Sort.by(sortBy).ascending());

        List<ProductResponseDto> foundProducts = new ArrayList<>();

        CustomPageMetadata metadata = CustomPageMetadata.builder()
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .numberOfElements(0)
                .totalPages(0)
                .totalElements(0)
                .build();

        given(productService.findAll(pageable)).willReturn(
                CustomPaginationData.<ProductResponseDto, Product>builder()
                        .data(foundProducts)
                        .metadata(metadata)
                        .build()
        );

        // When
        ResultActions response = mockMvc.perform(get(url + "/getAll"));

        // Then
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Found products!")));

        response.andExpect(MockMvcResultMatchers.jsonPath("$.data", hasSize(0)));

        response.andExpect(MockMvcResultMatchers.jsonPath("$.metadata.pageNumber",
                        CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.metadata.pageSize",
                        CoreMatchers.is(5)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.metadata.numberOfElements",
                        CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.metadata.totalPages",
                        CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.metadata.totalElements",
                        CoreMatchers.is(0)));
    }

    @Test
    public void getAllByName_OrderByNameAsc_ExistingProducts_Return_IsOk() throws Exception {
        //Given
        String word = "na";
        int pageNumber = 0;
        int pageSize = 5;
        String sortBy = "name";
        Pageable pageable = PageRequest.ofSize(pageSize)
                .withPage(pageNumber)
                .withSort(Sort.by(sortBy).ascending());

        List<ProductResponseDto> foundProducts = new ArrayList<>();
        foundProducts.add(productResponseEstrellaMarina);
        foundProducts.add(productResponseVacaNapolitana);

        CustomPageMetadata metadata = CustomPageMetadata.builder()
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .numberOfElements(foundProducts.size())
                .totalPages((int) Math.ceil((double) foundProducts.size() / pageSize))
                .totalElements(foundProducts.size())
                .build();

        given(productService.findAllByName(word, pageable)).willReturn(
                CustomPaginationData.<ProductResponseDto, Product>builder()
                        .data(foundProducts)
                        .metadata(metadata)
                        .build()
        );

        // When
        ResultActions response = mockMvc.perform(get(url + "/getAllByName/{word}", "na")
                .param("page", "0")
                .param("size", "5")
                .param("sort", "name,asc")
        );

        // Then
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Found products!")));

        response.andExpect(MockMvcResultMatchers.jsonPath("$.data", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name",
                        CoreMatchers.is("Estrella Marina")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].name",
                        CoreMatchers.is("Vaca Napolitana")));

        response.andExpect(MockMvcResultMatchers.jsonPath("$.metadata.pageNumber",
                        CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.metadata.pageSize",
                        CoreMatchers.is(5)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.metadata.numberOfElements",
                        CoreMatchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.metadata.totalPages",
                        CoreMatchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.metadata.totalElements",
                        CoreMatchers.is(2)));
    }

    @Test
    public void getAllByName_NoExistingProducts_Return_IsOk() throws Exception {
        //Given
        String word = "nu";
        int pageNumber = 0;
        int pageSize = 5;
        String sortBy = "id";
        Pageable pageable = PageRequest.ofSize(pageSize)
                .withPage(pageNumber)
                .withSort(Sort.by(sortBy).ascending());

        List<ProductResponseDto> foundProducts = new ArrayList<>();

        CustomPageMetadata metadata = CustomPageMetadata.builder()
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .numberOfElements(0)
                .totalPages(0)
                .totalElements(0)
                .build();

        given(productService.findAllByName(word, pageable)).willReturn(
                CustomPaginationData.<ProductResponseDto, Product>builder()
                        .data(foundProducts)
                        .metadata(metadata)
                        .build()
        );

        // When
        ResultActions response = mockMvc.perform(get(url + "/getAllByName/{word}", "nu"));

        // Then
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Found products!")));

        response.andExpect(MockMvcResultMatchers.jsonPath("$.data", hasSize(0)));

        response.andExpect(MockMvcResultMatchers.jsonPath("$.metadata.pageNumber",
                        CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.metadata.pageSize",
                        CoreMatchers.is(5)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.metadata.numberOfElements",
                        CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.metadata.totalPages",
                        CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.metadata.totalElements",
                        CoreMatchers.is(0)));
    }

    @Test
    public void getAllByDescription_OrderByNameAsc_ExistingProducts_Return_IsOk() throws Exception {
        //Given
        String word = "ua";
        int pageNumber = 0;
        int pageSize = 5;
        String sortBy = "name";
        Pageable pageable = PageRequest.ofSize(pageSize)
                .withPage(pageNumber)
                .withSort(Sort.by(sortBy).ascending());

        List<ProductResponseDto> foundProducts = new ArrayList<>();
        foundProducts.add(productResponseArdillasSaladas);
        foundProducts.add(productResponseEstrellaMarina);

        CustomPageMetadata metadata = CustomPageMetadata.builder()
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .numberOfElements(foundProducts.size())
                .totalPages((int) Math.ceil((double) foundProducts.size() / pageSize))
                .totalElements(foundProducts.size())
                .build();

        given(productService.findAllByDescription(word, pageable)).willReturn(
                CustomPaginationData.<ProductResponseDto, Product>builder()
                        .data(foundProducts)
                        .metadata(metadata)
                        .build()
        );

        // When
        ResultActions response = mockMvc.perform(get(url + "/getAllByDescription/{word}", "ua")
                .param("page", "0")
                .param("size", "5")
                .param("sort", "name,asc")
        );

        // Then
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Found products!")));

        response.andExpect(MockMvcResultMatchers.jsonPath("$.data", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name",
                        CoreMatchers.is("Ardillas Saladas")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].description",
                        CoreMatchers.is("Cacahuates salados")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].name",
                        CoreMatchers.is("Estrella Marina")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].description",
                        CoreMatchers.is("Agua embotellada")));

        response.andExpect(MockMvcResultMatchers.jsonPath("$.metadata.pageNumber",
                        CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.metadata.pageSize",
                        CoreMatchers.is(5)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.metadata.numberOfElements",
                        CoreMatchers.is(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.metadata.totalPages",
                        CoreMatchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.metadata.totalElements",
                        CoreMatchers.is(2)));
    }

    @Test
    public void getAllByDescription_NoExistingProducts_Return_IsOk() throws Exception {
        //Given
        String word = "au";
        int pageNumber = 0;
        int pageSize = 5;
        String sortBy = "id";
        Pageable pageable = PageRequest.ofSize(pageSize)
                .withPage(pageNumber)
                .withSort(Sort.by(sortBy).ascending());

        List<ProductResponseDto> foundProducts = new ArrayList<>();

        CustomPageMetadata metadata = CustomPageMetadata.builder()
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .numberOfElements(0)
                .totalPages(0)
                .totalElements(0)
                .build();

        given(productService.findAllByDescription(word, pageable)).willReturn(
                CustomPaginationData.<ProductResponseDto, Product>builder()
                        .data(foundProducts)
                        .metadata(metadata)
                        .build()
        );

        // When
        ResultActions response = mockMvc.perform(get(url + "/getAllByDescription/{word}", "au"));

        // Then
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Found products!")));

        response.andExpect(MockMvcResultMatchers.jsonPath("$.data", hasSize(0)));

        response.andExpect(MockMvcResultMatchers.jsonPath("$.metadata.pageNumber",
                        CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.metadata.pageSize",
                        CoreMatchers.is(5)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.metadata.numberOfElements",
                        CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.metadata.totalPages",
                        CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.metadata.totalElements",
                        CoreMatchers.is(0)));
    }

    @Test
    public void getById_ExistingProduct_Return_IsOk() throws Exception {
        //Given
        Integer productId = 1;

        given(productService.findById(productId)).willReturn(productResponseArdillasSaladas);

        // When
        ResultActions response = mockMvc.perform(get(url + "/get/{id}", 1));

        // Then
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Found product!")));

        response.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id")
                        .value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name",
                        CoreMatchers.is("Ardillas Saladas")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].price",
                        CoreMatchers.is(21.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].barcode",
                        CoreMatchers.is("7501030459941")));
    }

    @Test
    public void getById_NonExistingProduct_Return_IsNotFound() throws Exception {
        //Given
        Integer productId = 100;

        given(productService.findById(productId))
                .willThrow(new DataNotFoundException("Product not found!"));

        // When
        ResultActions response = mockMvc.perform(get(url + "/get/{id}", 100));

        // Then
        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error",
                        CoreMatchers.is("Product not found!")));
    }

    @Test
    public void getById_StringId_Return_IsInternalServerError() throws Exception {
        // When
        ResultActions response = mockMvc.perform(get(url + "/get/{id}", "one"));

        // Then
        response.andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }

    @Test
    public void save_NonExistingProduct_Return_IsCreated() throws Exception {
        // Given
        given(productService.save(productSaveRequestVacaChocolate))
                .willReturn(productSavedResponseVacaChocolate);

        // When
        ResultActions response = mockMvc.perform(post(url + "/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productSaveRequestVacaChocolate)));

        // Then
        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Saved product!")));

        response.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name",
                        CoreMatchers.is("Vaca de chocolate")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].description",
                        CoreMatchers.is("Helado")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].size",
                        CoreMatchers.is("500 ml")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].price",
                        CoreMatchers.is(42.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].barcode",
                        CoreMatchers.is("7506306417854")));
    }

    @Test
    public void save_ExistingProduct_Return_Conflict() throws Exception {
        // Given
        given(productService.save(any(ProductSaveRequestDto.class)))
                .willThrow(new DataAlreadyExistsException("Product already exists!"));

        // When
        ResultActions response = mockMvc.perform(post(url + "/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productSaveRequestVacaChocolate)));

        // Then
        response.andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error",
                        CoreMatchers.is("Product already exists!")));
    }

    @Test
    public void update_ExistingProduct_Return_IsOk() throws Exception {
        // Given
        Integer productId = 5;

        given(productService.update(productId, productUpdateRequestVacaChocolate))
                .willReturn(productUpdatedResponseVacaChocolate);

        // When
        ResultActions response = mockMvc.perform(put(url + "/update/{id}", 5)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productUpdateRequestVacaChocolate)));

        // Then
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Updated product!")));

        response.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name",
                        CoreMatchers.is("Vaca de Chocolate")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].description",
                        CoreMatchers.is("Helado sabor chocolate")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].size",
                        CoreMatchers.is("1 L")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].price",
                        CoreMatchers.is(52.0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].barcode",
                        CoreMatchers.is("7501130902095")));
    }

    @Test
    public void update_NonExistingProduct_Return_IsNotFound() throws Exception {
        // Given
        given(productService.update(anyInt(), any(ProductSaveRequestDto.class)))
                .willThrow(new DataNotFoundException("Product does not exist!"));

        // When
        ResultActions response = mockMvc.perform(put(url + "/update/{id}", 100)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(productUpdateRequestVacaChocolate)));

        // Then
        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error",
                        CoreMatchers.is("Product does not exist!")));
    }

    @Test
    public void deleteById_Product_Return_IsOk() throws Exception {
        // Given
        willDoNothing().given(productService).deleteById(anyInt());

        // When
        ResultActions response = mockMvc.perform(delete(url + "/delete/{id}", 10));

        // Then
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Deleted product!")));
    }

    private static ProductResponseDto mapRequestDtoToResponseDto(Integer id, ProductSaveRequestDto request) {
        return ProductResponseDto.builder()
                .id(id)
                .name(request.getName())
                .description(request.getDescription())
                .size(request.getSize())
                .price(request.getPrice())
                .barcode(request.getBarcode())
                .build();
    }
}
