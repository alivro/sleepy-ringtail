package com.alivro.spring.sleepyringtail.controller;

import com.alivro.spring.sleepyringtail.exception.DataAlreadyExistsException;
import com.alivro.spring.sleepyringtail.exception.DataNotFoundException;
import com.alivro.spring.sleepyringtail.model.Category;
import com.alivro.spring.sleepyringtail.model.category.request.CategorySaveRequestDto;
import com.alivro.spring.sleepyringtail.model.category.response.CategoryGetResponseDto;
import com.alivro.spring.sleepyringtail.model.category.response.CategorySaveResponseDto;
import com.alivro.spring.sleepyringtail.service.ICategoryService;
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
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext
public class CategoryControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    private ICategoryService categoryService;

    @Autowired
    private ObjectMapper objectMapper;

    // Buscar todas las categorías
    private static CategoryGetResponseDto categoryResponseDrinks;
    private static CategoryGetResponseDto categoryResponseIceCreams;
    private static CategoryGetResponseDto categoryResponseSnacks;
    private static CategoryGetResponseDto categoryResponseSweets;

    // Guardar una nueva categoría
    private static CategorySaveRequestDto categorySaveRequestWines;
    private static CategorySaveResponseDto categorySavedResponseWines;

    // Actualizar una categoría existente
    private static CategorySaveRequestDto categoryUpdateRequestWines;
    private static CategorySaveResponseDto categoryUpdatedResponseWines;

    @BeforeAll
    public static void setup() {
        // Buscar todas las categorías
        categoryResponseDrinks = CategoryGetResponseDto.builder()
                .id(1)
                .name("Bebidas")
                .build();

        categoryResponseIceCreams = CategoryGetResponseDto.builder()
                .id(2)
                .name("Helados")
                .build();

        categoryResponseSnacks = CategoryGetResponseDto.builder()
                .id(3)
                .name("Botanas")
                .build();

        categoryResponseSweets = CategoryGetResponseDto.builder()
                .id(4)
                .name("Dulcería")
                .build();

        // Guardar una nueva categoría
        categorySaveRequestWines = CategorySaveRequestDto.builder()
                .name("Vinos")
                .build();

        categorySavedResponseWines = mapRequestDtoToResponseDto(5, categorySaveRequestWines);

        // Actualizar una categoría existente
        categoryUpdateRequestWines = CategorySaveRequestDto.builder()
                .name("Vinos y Licores")
                .build();

        categoryUpdatedResponseWines = mapRequestDtoToResponseDto(5, categoryUpdateRequestWines);
    }

    @Test
    public void findAllByNameAsc_ExistingCategories_Return_IsOk() throws Exception {
        //Given
        int pageNumber = 0;
        int pageSize = 5;
        String sortBy = "name";
        Pageable pageable = PageRequest.ofSize(pageSize)
                .withPage(pageNumber)
                .withSort(Sort.by(sortBy).ascending());

        List<CategoryGetResponseDto> foundCategories = new ArrayList<>();
        foundCategories.add(categoryResponseDrinks);
        foundCategories.add(categoryResponseSnacks);
        foundCategories.add(categoryResponseSweets);
        foundCategories.add(categoryResponseIceCreams);

        CustomPageMetadata metadata = CustomPageMetadata.builder()
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .numberOfElements(foundCategories.size())
                .totalPages((int) Math.ceil((double) foundCategories.size() / pageSize))
                .totalElements(foundCategories.size())
                .build();

        given(categoryService.findAll(pageable)).willReturn(
                CustomPaginationData.<CategoryGetResponseDto, Category>builder()
                        .data(foundCategories)
                        .metadata(metadata)
                        .build()
        );

        // When
        ResultActions response = mockMvc.perform(get("/api/v1/category/getAll")
                .param("page", "0")
                .param("size", "5")
                .param("sort", "name,asc")
        );

        // Then
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Found categories!")));

        response.andExpect(MockMvcResultMatchers.jsonPath("$.data", hasSize(4)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name",
                        CoreMatchers.is(categoryResponseDrinks.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].name",
                        CoreMatchers.is(categoryResponseSnacks.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[2].name",
                        CoreMatchers.is(categoryResponseSweets.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[3].name",
                        CoreMatchers.is(categoryResponseIceCreams.getName())));

        response.andExpect(MockMvcResultMatchers.jsonPath("$.metadata.pageNumber",
                        CoreMatchers.is(pageNumber)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.metadata.pageSize",
                        CoreMatchers.is(pageSize)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.metadata.numberOfElements",
                        CoreMatchers.is(foundCategories.size())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.metadata.totalPages",
                        CoreMatchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.metadata.totalElements",
                        CoreMatchers.is(foundCategories.size())));
    }

    @Test
    public void findAll_NoExistingCategories_Return_IsOk() throws Exception {
        //Given
        int pageNumber = 0;
        int pageSize = 5;
        String sortBy = "id";
        Pageable pageable = PageRequest.ofSize(pageSize)
                .withPage(pageNumber)
                .withSort(Sort.by(sortBy).ascending());

        List<CategoryGetResponseDto> foundCategories = new ArrayList<>();

        CustomPageMetadata metadata = CustomPageMetadata.builder()
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .numberOfElements(0)
                .totalPages(0)
                .totalElements(0)
                .build();

        given(categoryService.findAll(pageable)).willReturn(
                CustomPaginationData.<CategoryGetResponseDto, Category>builder()
                        .data(foundCategories)
                        .metadata(metadata)
                        .build()
        );

        // When
        ResultActions response = mockMvc.perform(get("/api/v1/category/getAll"));

        // Then
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Found categories!")));

        response.andExpect(MockMvcResultMatchers.jsonPath("$.data", hasSize(0)));

        response.andExpect(MockMvcResultMatchers.jsonPath("$.metadata.pageNumber",
                        CoreMatchers.is(pageNumber)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.metadata.pageSize",
                        CoreMatchers.is(pageSize)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.metadata.numberOfElements",
                        CoreMatchers.is(foundCategories.size())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.metadata.totalPages",
                        CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.metadata.totalElements",
                        CoreMatchers.is(foundCategories.size())));
    }

    @Test
    public void findById_ExistingCategory_Return_IsOk() throws Exception {
        //Given
        Integer categoryId = 1;

        given(categoryService.findById(categoryId)).willReturn(categoryResponseDrinks);

        // When
        ResultActions response = mockMvc.perform(get("/api/v1/category/get/{id}", categoryId));

        // Then
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Found category!")));

        response.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id")
                        .value(categoryResponseDrinks.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name",
                        CoreMatchers.is(categoryResponseDrinks.getName())));
    }

    @Test
    public void findById_NonExistingCategory_Return_IsNotFound() throws Exception {
        //Given
        Integer categoryId = 10;

        given(categoryService.findById(categoryId))
                .willThrow(new DataNotFoundException("Category not found!"));

        // When
        ResultActions response = mockMvc.perform(get("/api/v1/category/get/{id}", categoryId));

        // Then
        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error",
                        CoreMatchers.is("Category not found!")));
    }

    @Test
    public void findById_StringId_Return_IsInternalServerError() throws Exception {
        //Given
        String categoryId = "one";

        // When
        ResultActions response = mockMvc.perform(get("/api/v1/category/get/{id}", categoryId));

        // Then
        response.andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }

    @Test
    public void save_NonExistingCategory_Return_IsCreated() throws Exception {
        // Given
        given(categoryService.save(categorySaveRequestWines))
                .willReturn(categorySavedResponseWines);

        // When
        ResultActions response = mockMvc.perform(post("/api/v1/category/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categorySaveRequestWines)));

        // Then
        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Saved category!")));

        response.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name",
                        CoreMatchers.is(categorySavedResponseWines.getName())));
    }

    @Test
    public void save_ExistingCategory_Return_Conflict() throws Exception {
        // Given
        given(categoryService.save(any(CategorySaveRequestDto.class)))
                .willThrow(new DataAlreadyExistsException("Category already exists!"));

        // When
        ResultActions response = mockMvc.perform(post("/api/v1/category/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categorySaveRequestWines)));

        // Then
        response.andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error",
                        CoreMatchers.is("Category already exists!")));
    }

    @Test
    public void update_ExistingCategory_Return_IsOk() throws Exception {
        // Given
        Integer categoryId = 5;

        given(categoryService.update(categoryId, categoryUpdateRequestWines))
                .willReturn(categoryUpdatedResponseWines);

        // When
        ResultActions response = mockMvc.perform(put("/api/v1/category/update/{id}", categoryId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryUpdateRequestWines)));

        // Then
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Updated category!")));

        response.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name",
                        CoreMatchers.is(categoryUpdatedResponseWines.getName())));
    }

    @Test
    public void update_NonExistingCategory_Return_IsNotFound() throws Exception {
        // Given
        Integer categoryId = 10;

        given(categoryService.update(anyInt(), any(CategorySaveRequestDto.class)))
                .willThrow(new DataNotFoundException("Category does not exist!"));

        // When
        ResultActions response = mockMvc.perform(put("/api/v1/category/update/{id}", categoryId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryUpdateRequestWines)));

        // Then
        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error",
                        CoreMatchers.is("Category does not exist!")));
    }

    @Test
    public void deleteById_Category_Return_IsOk() throws Exception {
        // Given
        Integer categoryId = 1;

        willDoNothing().given(categoryService).deleteById(anyInt());

        // When
        ResultActions response = mockMvc.perform(delete("/api/v1/category/delete/{id}", categoryId));

        // Then
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Deleted category!")));
    }

    private static CategorySaveResponseDto mapRequestDtoToResponseDto(Integer id, CategorySaveRequestDto request) {
        return CategorySaveResponseDto.builder()
                .id(id)
                .name(request.getName())
                .description(request.getDescription())
                .build();
    }
}
