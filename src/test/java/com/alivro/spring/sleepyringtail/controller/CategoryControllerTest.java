package com.alivro.spring.sleepyringtail.controller;

import com.alivro.spring.sleepyringtail.exception.DataAlreadyExistsException;
import com.alivro.spring.sleepyringtail.exception.DataNotFoundException;
import com.alivro.spring.sleepyringtail.model.Category;
import com.alivro.spring.sleepyringtail.model.category.request.CategorySaveRequestDto;
import com.alivro.spring.sleepyringtail.model.category.response.CategoryGetResponseDto;
import com.alivro.spring.sleepyringtail.model.category.response.CategorySaveResponseDto;
import com.alivro.spring.sleepyringtail.model.category.response.SubcategoryOfCategoryResponseDto;
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
public class CategoryControllerTest {
    @Autowired
    MockMvc mockMvc;

    @MockBean
    private ICategoryService categoryService;

    @Autowired
    private ObjectMapper objectMapper;

    // URL
    private static final String url = "/api/v1/category";

    // Buscar todas las categorías
    private static CategoryGetResponseDto categoryResponseBebidas;
    private static CategoryGetResponseDto categoryResponseHelados;
    private static CategoryGetResponseDto categoryResponseBotanas;
    private static CategoryGetResponseDto categoryResponseDulces;

    // Guardar una nueva categoría
    private static CategorySaveRequestDto categorySaveRequestVinos;
    private static CategorySaveResponseDto categorySavedResponseVinos;

    // Actualizar una categoría existente
    private static CategorySaveRequestDto categoryUpdateRequestVinos;
    private static CategorySaveResponseDto categoryUpdatedResponseVinos;

    @BeforeAll
    public static void setup() {
        // Buscar todas las categorías
        SubcategoryOfCategoryResponseDto papasFritas = SubcategoryOfCategoryResponseDto.builder()
                .id(1)
                .name("Papas fritas")
                .build();

        SubcategoryOfCategoryResponseDto aguaMineral = SubcategoryOfCategoryResponseDto.builder()
                .id(2)
                .name("Agua mineral")
                .build();

        SubcategoryOfCategoryResponseDto heladoLeche = SubcategoryOfCategoryResponseDto.builder()
                .id(3)
                .name("Helado base leche")
                .build();

        SubcategoryOfCategoryResponseDto chocolate = SubcategoryOfCategoryResponseDto.builder()
                .id(4)
                .name("Chocolate")
                .build();

        categoryResponseBebidas = CategoryGetResponseDto.builder()
                .id(1)
                .name("Bebidas")
                .subcategories(Collections.singletonList(aguaMineral))
                .build();

        categoryResponseHelados = CategoryGetResponseDto.builder()
                .id(2)
                .name("Helados")
                .subcategories(Collections.singletonList(heladoLeche))
                .build();

        categoryResponseBotanas = CategoryGetResponseDto.builder()
                .id(3)
                .name("Botanas")
                .subcategories(Collections.singletonList(papasFritas))
                .build();

        categoryResponseDulces = CategoryGetResponseDto.builder()
                .id(4)
                .name("Dulces")
                .subcategories(Collections.singletonList(chocolate))
                .build();

        // Guardar una nueva categoría
        categorySaveRequestVinos = CategorySaveRequestDto.builder()
                .name("Vinos")
                .build();

        categorySavedResponseVinos = mapRequestDtoToResponseDto(5, categorySaveRequestVinos);

        // Actualizar una categoría existente
        categoryUpdateRequestVinos = CategorySaveRequestDto.builder()
                .name("Vinos y Licores")
                .build();

        categoryUpdatedResponseVinos = mapRequestDtoToResponseDto(5, categoryUpdateRequestVinos);
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
        foundCategories.add(categoryResponseBebidas);
        foundCategories.add(categoryResponseBotanas);
        foundCategories.add(categoryResponseDulces);
        foundCategories.add(categoryResponseHelados);

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
        ResultActions response = mockMvc.perform(get(url + "/getAll")
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
                        CoreMatchers.is(categoryResponseBebidas.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].name",
                        CoreMatchers.is(categoryResponseBotanas.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[2].name",
                        CoreMatchers.is(categoryResponseDulces.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[3].name",
                        CoreMatchers.is(categoryResponseHelados.getName())));

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
        ResultActions response = mockMvc.perform(get(url + "/getAll"));

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

        given(categoryService.findById(categoryId)).willReturn(categoryResponseBebidas);

        // When
        ResultActions response = mockMvc.perform(get(url + "/get/{id}", categoryId));

        // Then
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Found category!")));

        response.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id")
                        .value(categoryResponseBebidas.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name",
                        CoreMatchers.is(categoryResponseBebidas.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].subcategories[0].name",
                        CoreMatchers.is(categoryResponseBebidas.getSubcategories().get(0).getName())));
    }

    @Test
    public void findById_NonExistingCategory_Return_IsNotFound() throws Exception {
        //Given
        Integer categoryId = 100;

        given(categoryService.findById(categoryId))
                .willThrow(new DataNotFoundException("Category not found!"));

        // When
        ResultActions response = mockMvc.perform(get(url + "/get/{id}", categoryId));

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
        ResultActions response = mockMvc.perform(get(url + "/get/{id}", categoryId));

        // Then
        response.andExpect(MockMvcResultMatchers.status().isInternalServerError());
    }

    @Test
    public void save_NonExistingCategory_Return_IsCreated() throws Exception {
        // Given
        given(categoryService.save(categorySaveRequestVinos))
                .willReturn(categorySavedResponseVinos);

        // When
        ResultActions response = mockMvc.perform(post(url + "/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categorySaveRequestVinos)));

        // Then
        response.andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Saved category!")));

        response.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name",
                CoreMatchers.is(categorySavedResponseVinos.getName())));
    }

    @Test
    public void save_ExistingCategory_Return_Conflict() throws Exception {
        // Given
        given(categoryService.save(any(CategorySaveRequestDto.class)))
                .willThrow(new DataAlreadyExistsException("Category already exists!"));

        // When
        ResultActions response = mockMvc.perform(post(url + "/save")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categorySaveRequestVinos)));

        // Then
        response.andExpect(MockMvcResultMatchers.status().isConflict())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error",
                        CoreMatchers.is("Category already exists!")));
    }

    @Test
    public void update_ExistingCategory_Return_IsOk() throws Exception {
        // Given
        Integer categoryId = 5;

        given(categoryService.update(categoryId, categoryUpdateRequestVinos))
                .willReturn(categoryUpdatedResponseVinos);

        // When
        ResultActions response = mockMvc.perform(put(url + "/update/{id}", categoryId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryUpdateRequestVinos)));

        // Then
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Updated category!")));

        response.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name",
                CoreMatchers.is(categoryUpdatedResponseVinos.getName())));
    }

    @Test
    public void update_NonExistingCategory_Return_IsNotFound() throws Exception {
        // Given
        Integer categoryId = 10;

        given(categoryService.update(anyInt(), any(CategorySaveRequestDto.class)))
                .willThrow(new DataNotFoundException("Category does not exist!"));

        // When
        ResultActions response = mockMvc.perform(put(url + "/update/{id}", categoryId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(categoryUpdateRequestVinos)));

        // Then
        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error",
                        CoreMatchers.is("Category does not exist!")));
    }

    @Test
    public void deleteById_Category_Return_IsOk() throws Exception {
        // Given
        Integer categoryId = 10;

        willDoNothing().given(categoryService).deleteById(anyInt());

        // When
        ResultActions response = mockMvc.perform(delete(url + "/delete/{id}", categoryId));

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
