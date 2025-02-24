package com.alivro.spring.sleepyringtail.controller;

import com.alivro.spring.sleepyringtail.exception.DataAlreadyExistsException;
import com.alivro.spring.sleepyringtail.exception.DataNotFoundException;
import com.alivro.spring.sleepyringtail.model.Subcategory;
import com.alivro.spring.sleepyringtail.model.subcategory.request.SubcategoryGenericRequestDto;
import com.alivro.spring.sleepyringtail.model.subcategory.response.SubcategoryGenericResponseDto;
import com.alivro.spring.sleepyringtail.model.subcategory.response.SubcategoryGetResponseDto;
import com.alivro.spring.sleepyringtail.model.util.request.CategoryRequestDto;
import com.alivro.spring.sleepyringtail.model.util.response.CategoryResponseDto;
import com.alivro.spring.sleepyringtail.model.util.response.ProductResponseDto;
import com.alivro.spring.sleepyringtail.service.ISubcategoryService;
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
import java.util.Collections;
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
public class SubcategoryControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    private ISubcategoryService subcategoryService;

    @Autowired
    private ObjectMapper objectMapper;

    // URL
    private static final String url = "/api/v1/subcategory";

    // Buscar todas las subcategorías
    private static SubcategoryGenericResponseDto aguaNaturalGetAllResponse;
    private static SubcategoryGenericResponseDto chocolateGetAllResponse;
    private static SubcategoryGenericResponseDto heladoLecheGetAllResponse;
    private static SubcategoryGenericResponseDto papasFritasGetAllResponse;

    // Buscar por ID
    private static SubcategoryGetResponseDto aguaNaturalGetResponse;

    // Guardar una nueva subcategoría
    private static SubcategoryGenericRequestDto gomitasSaveRequest;
    private static SubcategoryGenericResponseDto gomitasSavedResponse;

    // Actualizar una subcategoría existente
    private static SubcategoryGenericRequestDto gomitasUpdateRequest;
    private static SubcategoryGenericResponseDto gomitasUpdatedResponse;

    @BeforeAll
    public static void setup() {
        // Buscar todas las categorías
        CategoryResponseDto bebidas = CategoryResponseDto.builder()
                .id(1)
                .name("Bebidas")
                .build();

        CategoryResponseDto botanas = CategoryResponseDto.builder()
                .id(2)
                .name("Botanas")
                .build();

        CategoryResponseDto dulces = CategoryResponseDto.builder()
                .id(3)
                .name("Dulces")
                .build();

        CategoryResponseDto helados = CategoryResponseDto.builder()
                .id(4)
                .name("Helados")
                .build();

        aguaNaturalGetAllResponse = SubcategoryGenericResponseDto.builder()
                .id(1)
                .name("Agua natural")
                .category(bebidas)
                .build();

        chocolateGetAllResponse = SubcategoryGenericResponseDto.builder()
                .id(2)
                .name("Chocolate")
                .category(dulces)
                .build();

        heladoLecheGetAllResponse = SubcategoryGenericResponseDto.builder()
                .id(3)
                .name("Helado base leche")
                .category(helados)
                .build();

        papasFritasGetAllResponse = SubcategoryGenericResponseDto.builder()
                .id(4)
                .name("Papas fritas")
                .category(botanas)
                .build();

        // Buscar por ID
        ProductResponseDto estrellaMarina = ProductResponseDto.builder()
                .id(1)
                .name("Estrella Marina")
                .build();

        aguaNaturalGetResponse = SubcategoryGetResponseDto.builder()
                .id(1)
                .name("Agua natural")
                .category(bebidas)
                .products(Collections.singletonList(estrellaMarina))
                .build();

        // Guardar una nueva categoría
        CategoryRequestDto botanasRequest = CategoryRequestDto.builder()
                .id(2)
                .name("Botanas")
                .build();

        gomitasSaveRequest = SubcategoryGenericRequestDto.builder()
                .name("Gomitas")
                .category(botanasRequest)
                .build();

        gomitasSavedResponse = mapRequestDtoToResponseDto(5, gomitasSaveRequest);

        // Actualizar una categoría existente
        CategoryRequestDto dulcesRequest = CategoryRequestDto.builder()
                .id(3)
                .name("Dulces")
                .build();

        gomitasUpdateRequest = SubcategoryGenericRequestDto.builder()
                .name("Caramelos de goma")
                .category(dulcesRequest)
                .build();

        gomitasUpdatedResponse = mapRequestDtoToResponseDto(5, gomitasUpdateRequest);
    }

    @Test
    public void getAll_OrderByNameAsc_ExistingSubcategories_Return_IsOk() throws Exception {
        //Given
        int pageNumber = 0;
        int pageSize = 5;
        String sortBy = "name";
        Pageable pageable = PageRequest.ofSize(pageSize)
                .withPage(pageNumber)
                .withSort(Sort.by(sortBy).ascending());

        List<SubcategoryGenericResponseDto> foundSubcategories = new ArrayList<>();
        foundSubcategories.add(aguaNaturalGetAllResponse);
        foundSubcategories.add(chocolateGetAllResponse);
        foundSubcategories.add(heladoLecheGetAllResponse);
        foundSubcategories.add(papasFritasGetAllResponse);

        CustomPageMetadata metadata = CustomPageMetadata.builder()
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .numberOfElements(foundSubcategories.size())
                .totalPages((int) Math.ceil((double) foundSubcategories.size() / pageSize))
                .totalElements(foundSubcategories.size())
                .build();

        given(subcategoryService.findAll(pageable)).willReturn(
                CustomPaginationData.<SubcategoryGenericResponseDto, Subcategory>builder()
                        .data(foundSubcategories)
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
                        CoreMatchers.is("Found subcategories!")));

        response.andExpect(MockMvcResultMatchers.jsonPath("$.data", hasSize(4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name",
                        CoreMatchers.is("Agua natural")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].name",
                        CoreMatchers.is("Chocolate")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[2].name",
                        CoreMatchers.is("Helado base leche")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[3].name",
                        CoreMatchers.is("Papas fritas")));

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
    public void getAll_NoExistingSubcategories_Return_IsOk() throws Exception {
        //Given
        int pageNumber = 0;
        int pageSize = 5;
        String sortBy = "id";
        Pageable pageable = PageRequest.ofSize(pageSize)
                .withPage(pageNumber)
                .withSort(Sort.by(sortBy).ascending());

        List<SubcategoryGenericResponseDto> foundSubcategories = new ArrayList<>();

        CustomPageMetadata metadata = CustomPageMetadata.builder()
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .numberOfElements(0)
                .totalPages(0)
                .totalElements(0)
                .build();

        given(subcategoryService.findAll(pageable)).willReturn(
                CustomPaginationData.<SubcategoryGenericResponseDto, Subcategory>builder()
                        .data(foundSubcategories)
                        .metadata(metadata)
                        .build()
        );

        // When
        ResultActions response = mockMvc.perform(get(url + "/getAll"));

        // Then
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Found subcategories!")));

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
    public void getAllByName_OrderByNameAsc_ExistingSubcategories_Return_IsOk() throws Exception {
        //Given
        String word = "la";
        int pageNumber = 0;
        int pageSize = 5;
        String sortBy = "name";
        Pageable pageable = PageRequest.ofSize(pageSize)
                .withPage(pageNumber)
                .withSort(Sort.by(sortBy).ascending());

        List<SubcategoryGenericResponseDto> foundSubcategories = new ArrayList<>();
        foundSubcategories.add(chocolateGetAllResponse);
        foundSubcategories.add(heladoLecheGetAllResponse);

        CustomPageMetadata metadata = CustomPageMetadata.builder()
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .numberOfElements(foundSubcategories.size())
                .totalPages((int) Math.ceil((double) foundSubcategories.size() / pageSize))
                .totalElements(foundSubcategories.size())
                .build();

        given(subcategoryService.findAllByName(word, pageable)).willReturn(
                CustomPaginationData.<SubcategoryGenericResponseDto, Subcategory>builder()
                        .data(foundSubcategories)
                        .metadata(metadata)
                        .build()
        );

        // When
        ResultActions response = mockMvc.perform(get(url + "/getAllByName/{word}", "la")
                .param("page", "0")
                .param("size", "5")
                .param("sort", "name,asc")
        );

        // Then
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Found subcategories!")));

        response.andExpect(MockMvcResultMatchers.jsonPath("$.data", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name",
                        CoreMatchers.is("Chocolate")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].name",
                        CoreMatchers.is("Helado base leche")));

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
    public void getAllByName_NoExistingSubcategories_Return_IsOk() throws Exception {
        //Given
        String word = "lu";
        int pageNumber = 0;
        int pageSize = 5;
        String sortBy = "id";
        Pageable pageable = PageRequest.ofSize(pageSize)
                .withPage(pageNumber)
                .withSort(Sort.by(sortBy).ascending());

        List<SubcategoryGenericResponseDto> foundSubcategories = new ArrayList<>();

        CustomPageMetadata metadata = CustomPageMetadata.builder()
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .numberOfElements(0)
                .totalPages(0)
                .totalElements(0)
                .build();

        given(subcategoryService.findAllByName(word, pageable)).willReturn(
                CustomPaginationData.<SubcategoryGenericResponseDto, Subcategory>builder()
                        .data(foundSubcategories)
                        .metadata(metadata)
                        .build()
        );

        // When
        ResultActions response = mockMvc.perform(get(url + "/getAllByName/{word}", "lu"));

        // Then
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Found subcategories!")));

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
    public void get_ExistingSubcategory_Return_IsOk() throws Exception {
        //Given
        Integer subcategoryId = 1;

        given(subcategoryService.findById(subcategoryId)).willReturn(aguaNaturalGetResponse);

        // When
        ResultActions response = mockMvc.perform(get(url + "/get/{id}", 1));

        // Then
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Found subcategory!")));

        response.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id")
                        .value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name",
                        CoreMatchers.is("Agua natural")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].description",
                        CoreMatchers.nullValue()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].category.name",
                        CoreMatchers.is("Bebidas")))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].products[0].name",
                        CoreMatchers.is("Estrella Marina")));
    }

    @Test
    public void get_NonExistingSubcategory_Return_IsNotFound() throws Exception {
        //Given
        Integer subcategoryId = 100;

        given(subcategoryService.findById(subcategoryId))
                .willThrow(new DataNotFoundException("Subcategory not found!"));

        // When
        ResultActions response = mockMvc.perform(get(url + "/get/{id}", 100));

        // Then
        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error",
                        CoreMatchers.is("Subcategory not found!")));
    }

    @Test
    public void get_StringId_Return_IsInternalServerError() throws Exception {
        // When
        ResultActions response = mockMvc.perform(get(url + "/get/{id}", "one"));

        // Then
        response.andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }

    @Test
    public void save_NonExistingSubcategory_Return_IsCreated() throws Exception {
        // Given
        given(subcategoryService.save(gomitasSaveRequest))
                .willReturn(gomitasSavedResponse);

        // When
        ResultActions response = mockMvc.perform(post(url + "/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gomitasSaveRequest)));

        // Then
        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Saved subcategory!")));

        response.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name",
                CoreMatchers.is("Gomitas")));
    }

    @Test
    public void save_ExistingSubcategory_Return_Conflict() throws Exception {
        // Given
        given(subcategoryService.save(any(SubcategoryGenericRequestDto.class)))
                .willThrow(new DataAlreadyExistsException("Subcategory already exists!"));

        // When
        ResultActions response = mockMvc.perform(post(url + "/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gomitasSaveRequest)));

        // Then
        response.andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error",
                        CoreMatchers.is("Subcategory already exists!")));
    }

    @Test
    public void update_ExistingSubcategory_Return_IsOk() throws Exception {
        // Given
        Integer subcategoryId = 5;

        given(subcategoryService.update(subcategoryId, gomitasUpdateRequest))
                .willReturn(gomitasUpdatedResponse);

        // When
        ResultActions response = mockMvc.perform(put(url + "/update/{id}", 5)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gomitasUpdateRequest)));

        // Then
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Updated subcategory!")));

        response.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name",
                CoreMatchers.is("Caramelos de goma")));
    }

    @Test
    public void update_NonExistingSubcategory_Return_IsNotFound() throws Exception {
        // Given
        given(subcategoryService.update(anyInt(), any(SubcategoryGenericRequestDto.class)))
                .willThrow(new DataNotFoundException("Subcategory does not exist!"));

        // When
        ResultActions response = mockMvc.perform(put(url + "/update/{id}", 100)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(gomitasUpdateRequest)));

        // Then
        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error",
                        CoreMatchers.is("Subcategory does not exist!")));
    }

    @Test
    public void delete_Subcategory_Return_IsOk() throws Exception {
        // Given
        willDoNothing().given(subcategoryService).deleteById(anyInt());

        // When
        ResultActions response = mockMvc.perform(delete(url + "/delete/{id}", 10));

        // Then
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Deleted subcategory!")));
    }

    private static SubcategoryGenericResponseDto mapRequestDtoToResponseDto(
            Integer id, SubcategoryGenericRequestDto request) {
        CategoryResponseDto categoryResponse = CategoryResponseDto.builder()
                .id(request.getCategory().getId())
                .name(request.getCategory().getName())
                .build();

        return SubcategoryGenericResponseDto.builder()
                .id(id)
                .name(request.getName())
                .description(request.getDescription())
                .category(categoryResponse)
                .build();
    }
}
