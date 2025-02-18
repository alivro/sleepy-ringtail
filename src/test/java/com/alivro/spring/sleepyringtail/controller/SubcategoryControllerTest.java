package com.alivro.spring.sleepyringtail.controller;

import com.alivro.spring.sleepyringtail.exception.DataAlreadyExistsException;
import com.alivro.spring.sleepyringtail.exception.DataNotFoundException;
import com.alivro.spring.sleepyringtail.model.Subcategory;
import com.alivro.spring.sleepyringtail.model.subcategory.request.CategoryOfSubcategoryRequestDto;
import com.alivro.spring.sleepyringtail.model.subcategory.request.SubcategorySaveRequestDto;
import com.alivro.spring.sleepyringtail.model.subcategory.response.CategoryOfSubcategoryResponseDto;
import com.alivro.spring.sleepyringtail.model.subcategory.response.SubcategoryResponseDto;
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
    private static SubcategoryResponseDto subcategoryResponsePapasFritas;
    private static SubcategoryResponseDto subcategoryResponseAguaMineral;
    private static SubcategoryResponseDto subcategoryResponseHeladoLeche;
    private static SubcategoryResponseDto subcategoryResponseChocolate;

    // Guardar una nueva subcategoría
    private static SubcategorySaveRequestDto subcategorySaveRequestGomitas;
    private static SubcategoryResponseDto subcategorySavedResponseGomitas;

    // Actualizar una subcategoría existente
    private static SubcategorySaveRequestDto subcategoryUpdateRequestGomitas;
    private static SubcategoryResponseDto subcategoryUpdatedResponseGomitas;

    @BeforeAll
    public static void setup() {
        // Buscar todas las categorías
        CategoryOfSubcategoryResponseDto bebidas = CategoryOfSubcategoryResponseDto.builder()
                .id(1)
                .name("Bebidas")
                .build();

        CategoryOfSubcategoryResponseDto helados = CategoryOfSubcategoryResponseDto.builder()
                .id(2)
                .name("Helados")
                .build();

        CategoryOfSubcategoryResponseDto botanas = CategoryOfSubcategoryResponseDto.builder()
                .id(3)
                .name("Botanas")
                .build();

        CategoryOfSubcategoryResponseDto dulces = CategoryOfSubcategoryResponseDto.builder()
                .id(4)
                .name("Dulces")
                .build();

        subcategoryResponsePapasFritas = SubcategoryResponseDto.builder()
                .id(1)
                .name("Papas fritas")
                .category(botanas)
                .build();

        subcategoryResponseAguaMineral = SubcategoryResponseDto.builder()
                .id(2)
                .name("Agua mineral")
                .category(bebidas)
                .build();

        subcategoryResponseHeladoLeche = SubcategoryResponseDto.builder()
                .id(3)
                .name("Helado base leche")
                .category(helados)
                .build();

        subcategoryResponseChocolate = SubcategoryResponseDto.builder()
                .id(4)
                .name("Chocolate")
                .category(dulces)
                .build();

        // Guardar una nueva categoría
        CategoryOfSubcategoryRequestDto categoryOfSubcategoryRequestDulces = CategoryOfSubcategoryRequestDto.builder()
                .id(4)
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
    public void findAllByNameAsc_ExistingSubcategories_Return_IsOk() throws Exception {
        //Given
        int pageNumber = 0;
        int pageSize = 5;
        String sortBy = "name";
        Pageable pageable = PageRequest.ofSize(pageSize)
                .withPage(pageNumber)
                .withSort(Sort.by(sortBy).ascending());

        List<SubcategoryResponseDto> foundSubcategories = new ArrayList<>();
        foundSubcategories.add(subcategoryResponseAguaMineral);
        foundSubcategories.add(subcategoryResponseChocolate);
        foundSubcategories.add(subcategoryResponseHeladoLeche);
        foundSubcategories.add(subcategoryResponsePapasFritas);

        CustomPageMetadata metadata = CustomPageMetadata.builder()
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .numberOfElements(foundSubcategories.size())
                .totalPages((int) Math.ceil((double) foundSubcategories.size() / pageSize))
                .totalElements(foundSubcategories.size())
                .build();

        given(subcategoryService.findAll(pageable)).willReturn(
                CustomPaginationData.<SubcategoryResponseDto, Subcategory>builder()
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
                        CoreMatchers.is(subcategoryResponseAguaMineral.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[1].name",
                        CoreMatchers.is(subcategoryResponseChocolate.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[2].name",
                        CoreMatchers.is(subcategoryResponseHeladoLeche.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[3].name",
                        CoreMatchers.is(subcategoryResponsePapasFritas.getName())));

        response.andExpect(MockMvcResultMatchers.jsonPath("$.metadata.pageNumber",
                        CoreMatchers.is(pageNumber)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.metadata.pageSize",
                        CoreMatchers.is(pageSize)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.metadata.numberOfElements",
                        CoreMatchers.is(foundSubcategories.size())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.metadata.totalPages",
                        CoreMatchers.is(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.metadata.totalElements",
                        CoreMatchers.is(foundSubcategories.size())));
    }

    @Test
    public void findAll_NoExistingSubcategories_Return_IsOk() throws Exception {
        //Given
        int pageNumber = 0;
        int pageSize = 5;
        String sortBy = "id";
        Pageable pageable = PageRequest.ofSize(pageSize)
                .withPage(pageNumber)
                .withSort(Sort.by(sortBy).ascending());

        List<SubcategoryResponseDto> foundSubcategories = new ArrayList<>();

        CustomPageMetadata metadata = CustomPageMetadata.builder()
                .pageNumber(pageNumber)
                .pageSize(pageSize)
                .numberOfElements(0)
                .totalPages(0)
                .totalElements(0)
                .build();

        given(subcategoryService.findAll(pageable)).willReturn(
                CustomPaginationData.<SubcategoryResponseDto, Subcategory>builder()
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
                        CoreMatchers.is(pageNumber)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.metadata.pageSize",
                        CoreMatchers.is(pageSize)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.metadata.numberOfElements",
                        CoreMatchers.is(foundSubcategories.size())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.metadata.totalPages",
                        CoreMatchers.is(0)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.metadata.totalElements",
                        CoreMatchers.is(foundSubcategories.size())));
    }

    @Test
    public void findById_ExistingSubcategory_Return_IsOk() throws Exception {
        //Given
        Integer subcategoryId = 1;

        given(subcategoryService.findById(subcategoryId)).willReturn(subcategoryResponsePapasFritas);

        // When
        ResultActions response = mockMvc.perform(get(url + "/get/{id}", subcategoryId));

        // Then
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Found subcategory!")));

        response.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].id")
                        .value(subcategoryResponsePapasFritas.getId()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name",
                        CoreMatchers.is(subcategoryResponsePapasFritas.getName())))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data[0].category.name",
                        CoreMatchers.is(subcategoryResponsePapasFritas.getCategory().getName())));
    }

    @Test
    public void findById_NonExistingSubcategory_Return_IsNotFound() throws Exception {
        //Given
        Integer subcategoryId = 100;

        given(subcategoryService.findById(subcategoryId))
                .willThrow(new DataNotFoundException("Subcategory not found!"));

        // When
        ResultActions response = mockMvc.perform(get(url + "/get/{id}", subcategoryId));

        // Then
        response.andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.error",
                        CoreMatchers.is("Subcategory not found!")));
    }

    @Test
    public void findById_StringId_Return_IsInternalServerError() throws Exception {
        //Given
        String subcategoryId = "one";

        // When
        ResultActions response = mockMvc.perform(get(url + "/get/{id}", subcategoryId));

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
                CoreMatchers.is(subcategorySavedResponseGomitas.getName())));
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
        ResultActions response = mockMvc.perform(put(url + "/update/{id}", subcategoryId)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(subcategoryUpdateRequestGomitas)));

        // Then
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Updated subcategory!")));

        response.andExpect(MockMvcResultMatchers.jsonPath("$.data[0].name",
                CoreMatchers.is(subcategoryUpdatedResponseGomitas.getName())));
    }

    @Test
    public void update_NonExistingSubcategory_Return_IsNotFound() throws Exception {
        // Given
        Integer subcategoryId = 100;

        given(subcategoryService.update(anyInt(), any(SubcategorySaveRequestDto.class)))
                .willThrow(new DataNotFoundException("Subcategory does not exist!"));

        // When
        ResultActions response = mockMvc.perform(put(url + "/update/{id}", subcategoryId)
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
        Integer subcategoryId = 10;

        willDoNothing().given(subcategoryService).deleteById(anyInt());

        // When
        ResultActions response = mockMvc.perform(delete(url + "/delete/{id}", subcategoryId));

        // Then
        response.andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.message",
                        CoreMatchers.is("Deleted subcategory!")));
    }

    private static SubcategoryResponseDto mapRequestDtoToResponseDto(Integer id, SubcategorySaveRequestDto request) {
        CategoryOfSubcategoryResponseDto categoryOfSubcategory = CategoryOfSubcategoryResponseDto.builder()
                .id(request.getCategory().getId())
                .name(request.getCategory().getName())
                .build();

        return SubcategoryResponseDto.builder()
                .id(id)
                .name(request.getName())
                .description(request.getDescription())
                .category(categoryOfSubcategory)
                .build();
    }
}
