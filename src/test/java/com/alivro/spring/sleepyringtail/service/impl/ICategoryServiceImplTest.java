package com.alivro.spring.sleepyringtail.service.impl;

import com.alivro.spring.sleepyringtail.dao.CategoryDao;
import com.alivro.spring.sleepyringtail.exception.DataAlreadyExistsException;
import com.alivro.spring.sleepyringtail.exception.DataNotFoundException;
import com.alivro.spring.sleepyringtail.model.Category;
import com.alivro.spring.sleepyringtail.model.category.request.CategorySaveRequestDto;
import com.alivro.spring.sleepyringtail.model.category.response.CategoryGetResponseDto;
import com.alivro.spring.sleepyringtail.model.category.response.CategorySaveResponseDto;
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
public class ICategoryServiceImplTest {
    @Mock
    private CategoryDao categoryDao;

    @InjectMocks
    private ICategoryServiceImpl categoryService;

    // Buscar todas las categorías
    private static Category drinks;
    private static Category iceCreams;
    private static Category snacks;
    private static Category sweets;

    // Guardar una nueva categoría
    private static CategorySaveRequestDto categorySaveRequestWines;
    private static Category categoryToSaveWines;
    private static Category categorySavedWines;

    // Actualizar una categoría existente
    private static CategorySaveRequestDto categoryUpdateRequestWines;
    private static Category categoryToUpdateWines;
    private static Category categoryUpdatedWines;

    @BeforeAll
    public static void setup() {
        // Buscar todas las categorías
        drinks = Category.builder()
                .id(1)
                .name("Bebidas")
                .build();

        iceCreams = Category.builder()
                .id(2)
                .name("Helados")
                .build();

        snacks = Category.builder()
                .id(3)
                .name("Botanas")
                .build();

        sweets = Category.builder()
                .id(4)
                .name("Dulcería")
                .build();

        // Guardar una nueva categoría
        categorySaveRequestWines = CategorySaveRequestDto.builder()
                .name("Vinos")
                .build();

        categoryToSaveWines = CategorySaveRequestDto.mapRequestDtoToEntity(categorySaveRequestWines);

        categorySavedWines = CategorySaveRequestDto.mapRequestDtoToEntity(5, categorySaveRequestWines);

        // Actualizar una categoría existente
        categoryUpdateRequestWines = CategorySaveRequestDto.builder()
                .name("Vinos y Licores")
                .build();

        categoryToUpdateWines = CategorySaveRequestDto.mapRequestDtoToEntity(categoryUpdateRequestWines);

        categoryUpdatedWines = CategorySaveRequestDto.mapRequestDtoToEntity(5, categoryUpdateRequestWines);
    }

    @Test
    public void findAllByNameAsc_ExistingCategories_Return_ListOfCategories() {
        // Given
        int pageNumber = 0;
        int pageSize = 5;
        String sortBy = "name";

        List<Category> categories = new ArrayList<>();
        categories.add(drinks);
        categories.add(snacks);
        categories.add(sweets);
        categories.add(iceCreams);

        Pageable pageable = PageRequest.ofSize(pageSize)
                .withPage(pageNumber)
                .withSort(Sort.by(sortBy).ascending());

        given(categoryDao.findAll(pageable)).willReturn(
                new PageImpl<>(categories, pageable, categories.size())
        );

        // When
        CustomPaginationData<CategoryGetResponseDto, Category> categoriesData = categoryService.findAll(
                PageRequest.of(0, 5, Sort.by("name").ascending())
        );

        // Then
        List<CategoryGetResponseDto> data = categoriesData.getData();
        CustomPageMetadata meta = categoriesData.getMetadata();

        assertThat(data.size()).isEqualTo(4);
        assertThat(data.get(0).getName()).isEqualTo("Bebidas");
        assertThat(data.get(1).getName()).isEqualTo("Botanas");
        assertThat(data.get(2).getName()).isEqualTo("Dulcería");
        assertThat(data.get(3).getName()).isEqualTo("Helados");

        assertThat(meta.getPageNumber()).isZero();
        assertThat(meta.getPageSize()).isEqualTo(5);
        assertThat(meta.getNumberOfElements()).isEqualTo(4);
        assertThat(meta.getTotalElements()).isEqualTo(4);
        assertThat(meta.getTotalPages()).isEqualTo(1);
    }

    @Test
    public void findAll_NonExistingCategories_Return_EmptyListOfCategories() {
        int pageNumber = 0;
        int pageSize = 5;
        String sortBy = "id";

        List<Category> categories = new ArrayList<>();

        Pageable pageable = PageRequest.ofSize(pageSize)
                .withPage(pageNumber)
                .withSort(Sort.by(sortBy).ascending());

        given(categoryDao.findAll(pageable)).willReturn(
                new PageImpl<>(categories, pageable, 0)
        );

        // When
        CustomPaginationData<CategoryGetResponseDto, Category> categoriesData = categoryService.findAll(
                PageRequest.of(0, 5, Sort.by("id").ascending())
        );

        // Then
        List<CategoryGetResponseDto> data = categoriesData.getData();
        CustomPageMetadata meta = categoriesData.getMetadata();

        assertThat(data.size()).isEqualTo(0);

        assertThat(meta.getPageNumber()).isEqualTo(0);
        assertThat(meta.getPageSize()).isEqualTo(5);
        assertThat(meta.getNumberOfElements()).isEqualTo(0);
        assertThat(meta.getTotalElements()).isEqualTo(0);
        assertThat(meta.getTotalPages()).isEqualTo(0);
    }

    @Test
    public void findById_ExistingCategory_Return_FoundCategory() {
        // Given
        Integer categoryId = 1;

        given(categoryDao.findById(categoryId)).willReturn(Optional.of(drinks));

        // When
        CategoryGetResponseDto foundCategory = categoryService.findById(categoryId);

        // Then
        assertThat(foundCategory).isNotNull();
        assertThat(foundCategory.getId()).isEqualTo(1);
        assertThat(foundCategory.getName()).isEqualTo("Bebidas");
        assertThat(foundCategory.getDescription()).isEqualTo(null);
    }

    @Test
    public void findById_NonExistingCategory_Throw_DataNotFoundException() {
        // Given
        Integer categoryId = 10;

        given(categoryDao.findById(anyInt())).willReturn(Optional.empty());

        // When
        Throwable thrown = assertThrows(DataNotFoundException.class,
                () -> categoryService.findById(categoryId));

        // Then
        MatcherAssert.assertThat(thrown.getMessage(), is("Category not found!"));
    }

    @Test
    public void save_NonExistingCategory_Return_SavedCategory() {
        // Given
        given(categoryDao.existsByName(categorySaveRequestWines.getName())).willReturn(false);
        given(categoryDao.save(categoryToSaveWines)).willReturn(categorySavedWines);

        // When
        CategorySaveResponseDto savedCategory = categoryService.save(categorySaveRequestWines);

        // Then
        assertThat(savedCategory).isNotNull();
        assertThat(savedCategory.getName()).isEqualTo("Vinos");
        assertThat(savedCategory.getDescription()).isEqualTo(null);
    }

    @Test
    public void save_ExistingCategory_Throw_DataAlreadyExistsException() {
        // Given
        given(categoryDao.existsByName(anyString())).willReturn(true);

        // When
        Throwable thrown = assertThrows(DataAlreadyExistsException.class,
                () -> categoryService.save(categorySaveRequestWines));

        // Then
        MatcherAssert.assertThat(thrown.getMessage(), is("Category already exists!"));
    }

    @Test
    public void update_ExistingCategory_Return_UpdatedCategory() {
        // Given
        Integer categoryId = 5;

        given(categoryDao.findById(categoryId)).willReturn(Optional.ofNullable(categoryToUpdateWines));
        given(categoryDao.save(categoryToUpdateWines)).willReturn(categoryUpdatedWines);

        // When
        CategorySaveResponseDto updatedCategory = categoryService.update(categoryId, categoryUpdateRequestWines);

        // Then
        assertThat(updatedCategory).isNotNull();
        assertThat(updatedCategory.getId()).isEqualTo(5);
        assertThat(updatedCategory.getName()).isEqualTo("Vinos y Licores");
        assertThat(updatedCategory.getDescription()).isEqualTo(null);
    }

    @Test
    public void update_NonExistingCategory_Throw_DataNotFoundException() {
        // Given
        Integer categoryId = 10;

        given(categoryDao.findById(categoryId)).willReturn(Optional.empty());

        // When
        Throwable thrown = assertThrows(DataNotFoundException.class,
                () -> categoryService.update(categoryId, categoryUpdateRequestWines));

        // Then
        MatcherAssert.assertThat(thrown.getMessage(), is("Category does not exist!"));
    }

    @Test
    public void deleteById_Category_NoReturn() {
        // Given
        Integer categoryId = 1;

        willDoNothing().given(categoryDao).deleteById(anyInt());

        // When
        categoryService.deleteById(categoryId);

        // Then
        verify(categoryDao, times(1)).deleteById(categoryId);
    }
}
