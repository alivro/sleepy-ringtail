package com.alivro.spring.sleepyringtail.controller;

import com.alivro.spring.sleepyringtail.exception.DataAlreadyExistsException;
import com.alivro.spring.sleepyringtail.exception.DataNotFoundException;
import com.alivro.spring.sleepyringtail.model.Subcategory;
import com.alivro.spring.sleepyringtail.model.subcategory.request.CategoryOfSubcategoryRequestDto;
import com.alivro.spring.sleepyringtail.model.subcategory.request.SubcategorySaveRequestDto;
import com.alivro.spring.sleepyringtail.model.subcategory.response.CategoryOfSubcategoryResponseDto;
import com.alivro.spring.sleepyringtail.model.subcategory.response.ProductOfSubcategoryResponseDto;
import com.alivro.spring.sleepyringtail.model.subcategory.response.SubcategoryGetResponseDto;
import com.alivro.spring.sleepyringtail.model.subcategory.response.SubcategorySaveResponseDto;
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

import java.math.BigDecimal;
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
    private static SubcategoryGetResponseDto subcategoryGetResponseAguaNatural;
    private static SubcategoryGetResponseDto subcategoryGetResponseChocolate;
    private static SubcategoryGetResponseDto subcategoryGetResponseHeladoLeche;
    private static SubcategoryGetResponseDto subcategoryGetResponsePapasFritas;

    // Guardar una nueva subcategoría
    private static SubcategorySaveRequestDto subcategorySaveRequestGomitas;
    private static SubcategorySaveResponseDto subcategorySavedResponseGomitas;

    // Actualizar una subcategoría existente
    private static SubcategorySaveRequestDto subcategoryUpdateRequestGomitas;
    private static SubcategorySaveResponseDto subcategoryUpdatedResponseGomitas;

    @BeforeAll
    public static void setup() {
        // Buscar todas las categorías
        CategoryOfSubcategoryResponseDto bebidas = CategoryOfSubcategoryResponseDto.builder()
                .id(1)
                .name("Bebidas")
                .build();

        CategoryOfSubcategoryResponseDto botanas = CategoryOfSubcategoryResponseDto.builder()
                .id(2)
                .name("Botanas")
                .build();

        CategoryOfSubcategoryResponseDto dulces = CategoryOfSubcategoryResponseDto.builder()
                .id(3)
                .name("Dulces")
                .build();

        CategoryOfSubcategoryResponseDto helados = CategoryOfSubcategoryResponseDto.builder()
                .id(4)
                .name("Helados")
                .build();

        ProductOfSubcategoryResponseDto estrellaMarina = ProductOfSubcategoryResponseDto.builder()
                .name("Estrella Marina")
                .size("1 L")
                .price(BigDecimal.valueOf(14.00))
                .build();

        subcategoryGetResponseAguaNatural = SubcategoryGetResponseDto.builder()
                .id(1)
                .name("Agua natural")
                .category(bebidas)
                .products(Collections.singletonList(estrellaMarina))
                .build();

        subcategoryGetResponseChocolate = SubcategoryGetResponseDto.builder()
                .id(2)
                .name("Chocolate")
                .category(dulces)
                .products(new ArrayList<>())
                .build();

        subcategoryGetResponseHeladoLeche = SubcategoryGetResponseDto.builder()
                .id(3)
                .name("Helado base leche")
                .category(helados)
                .products(new ArrayList<>())
                .build();

        subcategoryGetResponsePapasFritas = SubcategoryGetResponseDto.builder()
                .id(4)
                .name("Papas fritas")
                .category(botanas)
                .products(new ArrayList<>())
                .build();

        // Guardar una nueva categoría
        CategoryOfSubcategoryRequestDto categoryOfSubcategoryRequestDulces = CategoryOfSubcategoryRequestDto.builder()
                .id(3)
                .name("Dulces")
                .build();

        subcategorySaveRequestGomitas = SubcategorySaveRequestDto.builder()
                .name("Gomitas")
                .category(categoryOfSubcategoryRequestDulces)
                .build();

        subcategorySavedResponseGomitas = mapRequestDtoToResponseDto(5, subcategorySaveRequestGomitas);

        // Actualizar una categoría existente
        subcategoryUpdateRequestGomitas = SubcategorySaveRequestDto.builder()
                .name("Caramelos de goma")
                .category(categoryOfSubcategoryRequestDulces)
                .build();

        subcategoryUpdatedResponseGomitas = mapRequestDtoToResponseDto(5, subcategoryUpdateRequestGomitas);
    }

    @Test
    public void getAllByNameAsc_ExistingSubcategories_Return_IsOk() throws Exception {
        //Given
        int pageNumber = 0;
        int pageSize = 5;
        String sortBy = "name";
        Pageable pageable = PageRequest.ofSize(pageSize)
                .withPage(pageNumber)
                .withSort(Sort.by(sortBy).ascending());

        List<SubcategoryGetResponseDto> foundSubcategories = new ArrayList<>();
        foundSubcategories.add(subcategoryGetResponseAguaNatural);
        foundSubcategories.add(subcategoryGetResponseChocolate);
        foundSubcategories.add(subcategoryGetResponseHeladoLeche);
        foundSubcategories.add(subcategoryGetResponsePapasFritas);

        CustomPageMetadata metadata = CustomPageMetadata.builder()
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .numberOfElements(foundSubcategories.size())
                .totalPages((int) Math.ceil((double) foundSubcategories.size() / pageSize))
                .totalElements(foundSubcategories.size())
                .build();

        given(subcategoryService.findAll(pageable)).willReturn(
                CustomPaginationData.<SubcategoryGetResponseDto, Subcategory>builder()
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

        List<SubcategoryGetResponseDto> foundSubcategories = new ArrayList<>();

        CustomPageMetadata metadata = CustomPageMetadata.builder()
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .numberOfElements(0)
                .totalPages(0)
                .totalElements(0)
                .build();

        given(subcategoryService.findAll(pageable)).willReturn(
                CustomPaginationData.<SubcategoryGetResponseDto, Subcategory>builder()
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

        List<SubcategoryGetResponseDto> foundSubcategories = new ArrayList<>();
        foundSubcategories.add(subcategoryGetResponseChocolate);
        foundSubcategories.add(subcategoryGetResponseHeladoLeche);

        CustomPageMetadata metadata = CustomPageMetadata.builder()
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .numberOfElements(foundSubcategories.size())
                .totalPages((int) Math.ceil((double) foundSubcategories.size() / pageSize))
                .totalElements(foundSubcategories.size())
                .build();

        given(subcategoryService.findAllByName(word, pageable)).willReturn(
                CustomPaginationData.<SubcategoryGetResponseDto, Subcategory>builder()
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
    public void getAllByName_NoExistingCategories_Return_IsOk() throws Exception {
        //Given
        String word = "lu";
        int pageNumber = 0;
        int pageSize = 5;
        String sortBy = "id";
        Pageable pageable = PageRequest.ofSize(pageSize)
                .withPage(pageNumber)
                .withSort(Sort.by(sortBy).ascending());

        List<SubcategoryGetResponseDto> foundSubcategories = new ArrayList<>();

        CustomPageMetadata metadata = CustomPageMetadata.builder()
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .numberOfElements(0)
                .totalPages(0)
                .totalElements(0)
                .build();

        given(subcategoryService.findAllByName(word, pageable)).willReturn(
                CustomPaginationData.<SubcategoryGetResponseDto, Subcategory>builder()
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
    public void getById_ExistingSubcategory_Return_IsOk() throws Exception {
        //Given
        Integer subcategoryId = 1;

        given(subcategoryService.findById(subcategoryId)).willReturn(subcategoryGetResponseAguaNatural);

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
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].category.name",
                        CoreMatchers.is("Bebidas")));
    }

    @Test
    public void getById_NonExistingSubcategory_Return_IsNotFound() throws Exception {
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
    public void getById_StringId_Return_IsInternalServerError() throws Exception {
        // When
        ResultActions response = mockMvc.perform(get(url + "/get/{id}", "one"));

        // Then
        response.andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }

    @Test
    public void save_NonExistingSubcategory_Return_IsCreated() throws Exception {
        // Given
        given(subcategoryService.save(subcategorySaveRequestGomitas))
                .willReturn(subcategorySavedResponseGomitas);

        // When
        ResultActions response = mockMvc.perform(post(url + "/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(subcategorySaveRequestGomitas)));

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
        given(subcategoryService.save(any(SubcategorySaveRequestDto.class)))
                .willThrow(new DataAlreadyExistsException("Subcategory already exists!"));

        // When
        ResultActions response = mockMvc.perform(post(url + "/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(subcategorySaveRequestGomitas)));

        // Then
        response.andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error",
                        CoreMatchers.is("Subcategory already exists!")));
    }

    @Test
    public void update_ExistingSubcategory_Return_IsOk() throws Exception {
        // Given
        Integer subcategoryId = 5;

        given(subcategoryService.update(subcategoryId, subcategoryUpdateRequestGomitas))
                .willReturn(subcategoryUpdatedResponseGomitas);

        // When
        ResultActions response = mockMvc.perform(put(url + "/update/{id}", 5)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(subcategoryUpdateRequestGomitas)));

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
        given(subcategoryService.update(anyInt(), any(SubcategorySaveRequestDto.class)))
                .willThrow(new DataNotFoundException("Subcategory does not exist!"));

        // When
        ResultActions response = mockMvc.perform(put(url + "/update/{id}", 100)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(subcategoryUpdateRequestGomitas)));

        // Then
        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error",
                        CoreMatchers.is("Subcategory does not exist!")));
    }

    @Test
    public void deleteById_Subcategory_Return_IsOk() throws Exception {
        // Given
        willDoNothing().given(subcategoryService).deleteById(anyInt());

        // When
        ResultActions response = mockMvc.perform(delete(url + "/delete/{id}", 10));

        // Then
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Deleted subcategory!")));
    }

    private static SubcategorySaveResponseDto mapRequestDtoToResponseDto(Integer id, SubcategorySaveRequestDto request) {
        CategoryOfSubcategoryResponseDto categoryOfSubcategory = CategoryOfSubcategoryResponseDto.builder()
                .id(request.getCategory().getId())
                .name(request.getCategory().getName())
                .build();

        return SubcategorySaveResponseDto.builder()
                .id(id)
                .name(request.getName())
                .description(request.getDescription())
                .category(categoryOfSubcategory)
                .build();
    }
}
