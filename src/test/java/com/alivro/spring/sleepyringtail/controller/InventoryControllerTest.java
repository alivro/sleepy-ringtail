package com.alivro.spring.sleepyringtail.controller;

import com.alivro.spring.sleepyringtail.exception.DataAlreadyExistsException;
import com.alivro.spring.sleepyringtail.exception.DataNotFoundException;
import com.alivro.spring.sleepyringtail.model.Inventory;
import com.alivro.spring.sleepyringtail.model.inventory.request.InventoryGenericRequestDto;
import com.alivro.spring.sleepyringtail.model.inventory.response.InventoryGenericResponseDto;
import com.alivro.spring.sleepyringtail.model.util.request.ProductRequestDto;
import com.alivro.spring.sleepyringtail.model.util.response.ProductResponseDto;
import com.alivro.spring.sleepyringtail.service.IInventoryService;
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
public class InventoryControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    private IInventoryService inventoryService;

    @Autowired
    private ObjectMapper objectMapper;

    // URL
    private static final String url = "/api/v1/inventory";

    // Buscar el stock de todos los productos
    private static InventoryGenericResponseDto ardillasSaladasResponse;
    private static InventoryGenericResponseDto estrellaMarinaResponse;
    private static InventoryGenericResponseDto osoAlmendradoResponse;
    private static InventoryGenericResponseDto vacaNapolitanaResponse;

    // Guardar el stock de un nuevo producto
    private static InventoryGenericRequestDto vacaChocolateSaveRequest;
    private static InventoryGenericResponseDto vacaChocolateSavedResponse;

    // Guardar el stock de un nuevo producto (información incompleta)
    private static InventoryGenericRequestDto vacaVainillaIncompleteRequest;

    // Actualizar el stock de un producto existente
    private static InventoryGenericRequestDto vacaChocolateUpdateRequest;
    private static InventoryGenericResponseDto vacaChocolateUpdatedResponse;

    @BeforeAll
    public static void setup() {
        // Buscar el stock de todos los productos
        ProductResponseDto ardillasSaladasProduct = ProductResponseDto.builder()
                .id(1)
                .name("Ardillas Saladas")
                .build();

        ProductResponseDto estrellaMarinaProduct = ProductResponseDto.builder()
                .id(2)
                .name("Estrella Marina")
                .build();

        ProductResponseDto osoAlmendradoProduct = ProductResponseDto.builder()
                .id(3)
                .name("Oso Almendrado")
                .build();

        ProductResponseDto vacaNapolitanaProduct = ProductResponseDto.builder()
                .id(4)
                .name("Vaca Napolitana")
                .build();

        ardillasSaladasResponse = InventoryGenericResponseDto.builder()
                .id(1)
                .quantityAvailable((short) 322)
                .minimumStock((short) 250)
                .maximumStock((short) 625)
                .product(ardillasSaladasProduct)
                .build();

        estrellaMarinaResponse = InventoryGenericResponseDto.builder()
                .id(2)
                .quantityAvailable((short) 202)
                .minimumStock((short) 120)
                .maximumStock((short) 300)
                .product(estrellaMarinaProduct)
                .build();

        osoAlmendradoResponse = InventoryGenericResponseDto.builder()
                .id(3)
                .quantityAvailable((short) 271)
                .minimumStock((short) 230)
                .maximumStock((short) 575)
                .product(osoAlmendradoProduct)
                .build();

        vacaNapolitanaResponse = InventoryGenericResponseDto.builder()
                .id(4)
                .quantityAvailable((short) 113)
                .minimumStock((short) 70)
                .maximumStock((short) 175)
                .product(vacaNapolitanaProduct)
                .build();

        // Guardar el stock de un nuevo producto
        ProductRequestDto vacaChocolateProductRequest = ProductRequestDto.builder()
                .id(5)
                .name("Vaca de Chocolate")
                .build();

        vacaChocolateSaveRequest = InventoryGenericRequestDto.builder()
                .quantityAvailable((short) 55)
                .minimumStock((short) 10)
                .maximumStock((short) 150)
                .product(vacaChocolateProductRequest)
                .build();

        vacaChocolateSavedResponse = mapRequestDtoToResponseDto(5, vacaChocolateSaveRequest);

        // Guardar el stock de un nuevo producto (información incompleta)
        vacaVainillaIncompleteRequest = InventoryGenericRequestDto.builder()
                .quantityAvailable((short) 120)
                .minimumStock((short) 80)
                .maximumStock((short) 200)
                .build();

        // Actualizar el stock de un producto existente
        vacaChocolateUpdateRequest = InventoryGenericRequestDto.builder()
                .quantityAvailable((short) 150)
                .minimumStock((short) 100)
                .maximumStock((short) 250)
                .product(vacaChocolateProductRequest)
                .build();

        vacaChocolateUpdatedResponse = mapRequestDtoToResponseDto(5, vacaChocolateUpdateRequest);
    }

    @Test
    public void getAll_OrderByQuantityAvailableDesc_ExistingInventory_Return_IsOk() throws Exception {
        //Given
        int pageNumber = 0;
        int pageSize = 5;
        String sortBy = "quantityAvailable";
        Pageable pageable = PageRequest.ofSize(pageSize)
                .withPage(pageNumber)
                .withSort(Sort.by(sortBy).descending());

        List<InventoryGenericResponseDto> foundInventoryStocks = new ArrayList<>();
        foundInventoryStocks.add(ardillasSaladasResponse);
        foundInventoryStocks.add(osoAlmendradoResponse);
        foundInventoryStocks.add(estrellaMarinaResponse);
        foundInventoryStocks.add(vacaNapolitanaResponse);

        CustomPageMetadata metadata = CustomPageMetadata.builder()
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .numberOfElements(foundInventoryStocks.size())
                .totalPages((int) Math.ceil((double) foundInventoryStocks.size() / pageSize))
                .totalElements(foundInventoryStocks.size())
                .build();

        given(inventoryService.findAll(pageable)).willReturn(
                CustomPaginationData.<InventoryGenericResponseDto, Inventory>builder()
                        .data(foundInventoryStocks)
                        .metadata(metadata)
                        .build()
        );

        // When
        ResultActions response = mockMvc.perform(get(url + "/getAll")
                .param("page", "0")
                .param("size", "5")
                .param("sort", "quantityAvailable,desc")
        );

        // Then
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Found product stocks!")));

        response.andExpect(MockMvcResultMatchers.jsonPath("$.data", hasSize(4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].quantityAvailable",
                        CoreMatchers.is(322)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].quantityAvailable",
                        CoreMatchers.is(271)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[2].quantityAvailable",
                        CoreMatchers.is(202)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[3].quantityAvailable",
                        CoreMatchers.is(113)));

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
    public void getAll_NoExistingInventory_Return_IsOk() throws Exception {
        //Given
        int pageNumber = 0;
        int pageSize = 5;
        String sortBy = "id";
        Pageable pageable = PageRequest.ofSize(pageSize)
                .withPage(pageNumber)
                .withSort(Sort.by(sortBy).ascending());

        List<InventoryGenericResponseDto> foundInventoryStocks = new ArrayList<>();

        CustomPageMetadata metadata = CustomPageMetadata.builder()
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .numberOfElements(0)
                .totalPages(0)
                .totalElements(0)
                .build();

        given(inventoryService.findAll(pageable)).willReturn(
                CustomPaginationData.<InventoryGenericResponseDto, Inventory>builder()
                        .data(foundInventoryStocks)
                        .metadata(metadata)
                        .build()
        );

        // When
        ResultActions response = mockMvc.perform(get(url + "/getAll"));

        // Then
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Found product stocks!")));

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
    public void get_ExistingInventory_Return_IsOk() throws Exception {
        //Given
        Integer inventoryId = 1;

        given(inventoryService.findById(inventoryId)).willReturn(ardillasSaladasResponse);

        // When
        ResultActions response = mockMvc.perform(get(url + "/get/{id}", 1));

        // Then
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Found product stock!")));

        response.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id")
                        .value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].quantityAvailable",
                        CoreMatchers.is(322)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].minimumStock",
                        CoreMatchers.is(250)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].maximumStock",
                        CoreMatchers.is(625)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].product.name",
                        CoreMatchers.is("Ardillas Saladas")));
    }

    @Test
    public void get_NonExistingInventory_Return_IsNotFound() throws Exception {
        //Given
        Integer productId = 100;

        given(inventoryService.findById(productId))
                .willThrow(new DataNotFoundException("Product stock not found!"));

        // When
        ResultActions response = mockMvc.perform(get(url + "/get/{id}", 100));

        // Then
        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]",
                        CoreMatchers.is("Product stock not found!")));
    }

    @Test
    public void get_StringId_Return_IsInternalServerError() throws Exception {
        // When
        ResultActions response = mockMvc.perform(get(url + "/get/{id}", "one"));

        // Then
        response.andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }

    @Test
    public void save_NonExistingInventory_Return_IsCreated() throws Exception {
        // Given
        given(inventoryService.save(vacaChocolateSaveRequest))
                .willReturn(vacaChocolateSavedResponse);

        // When
        ResultActions response = mockMvc.perform(post(url + "/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(vacaChocolateSaveRequest)));

        // Then
        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Saved inventory stock!")));

        response.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].quantityAvailable",
                        CoreMatchers.is(55)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].minimumStock",
                        CoreMatchers.is(10)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].maximumStock",
                        CoreMatchers.is(150)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].product.name",
                        CoreMatchers.is("Vaca de Chocolate")));
    }

    @Test
    public void save_ExistingInventory_Return_IsConflict() throws Exception {
        // Given
        given(inventoryService.save(any(InventoryGenericRequestDto.class)))
                .willThrow(new DataAlreadyExistsException("Existing product stock!"));

        // When
        ResultActions response = mockMvc.perform(post(url + "/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(vacaChocolateSaveRequest)));

        // Then
        response.andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]",
                        CoreMatchers.is("Existing product stock!")));
    }

    @Test
    public void save_IncompleteRequestInventory_Return_IsBadRequest() throws Exception {
        // When
        ResultActions response = mockMvc.perform(post(url + "/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(vacaVainillaIncompleteRequest)));

        // Then
        response.andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]",
                        CoreMatchers.is("product: El campo producto es obligatorio.")));
    }

    @Test
    public void update_ExistingInventory_Return_IsOk() throws Exception {
        // Given
        Integer inventoryId = 5;

        given(inventoryService.update(inventoryId, vacaChocolateUpdateRequest))
                .willReturn(vacaChocolateUpdatedResponse);

        // When
        ResultActions response = mockMvc.perform(put(url + "/update/{id}", 5)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(vacaChocolateUpdateRequest)));

        // Then
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Updated stock product!")));

        response.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id")
                        .value(5))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].quantityAvailable",
                        CoreMatchers.is(150)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].minimumStock",
                        CoreMatchers.is(100)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].maximumStock",
                        CoreMatchers.is(250)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].product.name",
                        CoreMatchers.is("Vaca de Chocolate")));
    }

    @Test
    public void update_NonExistingInventory_Return_IsNotFound() throws Exception {
        // Given
        given(inventoryService.update(anyInt(), any(InventoryGenericRequestDto.class)))
                .willThrow(new DataNotFoundException("Product stock does not exist!"));

        // When
        ResultActions response = mockMvc.perform(put(url + "/update/{id}", 100)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(vacaChocolateUpdateRequest)));

        // Then
        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.errors[0]",
                        CoreMatchers.is("Product stock does not exist!")));
    }

    @Test
    public void delete_Inventory_Return_IsOk() throws Exception {
        // Given
        willDoNothing().given(inventoryService).deleteById(anyInt());

        // When
        ResultActions response = mockMvc.perform(delete(url + "/delete/{id}", 10));

        // Then
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Deleted product stock!")));
    }

    private static InventoryGenericResponseDto mapRequestDtoToResponseDto(Integer id, InventoryGenericRequestDto request) {
        ProductResponseDto product = ProductResponseDto.builder()
                .id(request.getProduct().getId())
                .name(request.getProduct().getName())
                .build();

        return InventoryGenericResponseDto.builder()
                .id(id)
                .quantityAvailable(request.getQuantityAvailable())
                .minimumStock(request.getMinimumStock())
                .maximumStock(request.getMaximumStock())
                .product(product)
                .build();
    }
}
