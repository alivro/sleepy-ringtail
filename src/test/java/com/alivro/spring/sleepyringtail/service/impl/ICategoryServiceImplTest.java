package com.alivro.spring.sleepyringtail.service.impl;

import com.alivro.spring.sleepyringtail.dao.CategoryDao;
import com.alivro.spring.sleepyringtail.exception.DataAlreadyExistsException;
import com.alivro.spring.sleepyringtail.exception.DataNotFoundException;
import com.alivro.spring.sleepyringtail.model.Category;
import com.alivro.spring.sleepyringtail.model.Subcategory;
import com.alivro.spring.sleepyringtail.model.category.request.CategoryGenericRequestDto;
import com.alivro.spring.sleepyringtail.model.category.response.CategoryGenericResponseDto;
import com.alivro.spring.sleepyringtail.model.category.response.CategoryGetResponseDto;
import com.alivro.spring.sleepyringtail.util.pagination.CustomPageMetadata;
import com.alivro.spring.sleepyringtail.util.pagination.CustomPaginationData;
import org.hamcrest.MatcherAssert;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Collections;
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
    private static Category bebidas;
    private static Category botanas;
    private static Category dulces;
    private static Category helados;

    // Guardar una nueva categoría
    private static CategoryGenericRequestDto vinosSaveRequest;
    private static Category vinosToSave;
    private static Category vinosSaved;

    // Actualizar una categoría existente
    private static CategoryGenericRequestDto vinosUpdateRequest;
    private static Category vinosToUpdate;
    private static Category vinosUpdated;

    @BeforeAll
    public static void setup() {
        ModelMapper modelMapper = new ModelMapper();

        // Buscar todas las categorías
        Subcategory aguaNatural = Subcategory.builder()
                .id(1)
                .name("Agua natural")
                .build();

        Subcategory chocolate = Subcategory.builder()
                .id(2)
                .name("Chocolate")
                .build();

        Subcategory heladoLeche = Subcategory.builder()
                .id(3)
                .name("Helado base leche")
                .build();

        Subcategory papasFritas = Subcategory.builder()
                .id(4)
                .name("Papas fritas")
                .build();

        bebidas = Category.builder()
                .id(1)
                .name("Bebidas")
                .subcategories(Collections.singletonList(aguaNatural))
                .build();

        botanas = Category.builder()
                .id(2)
                .name("Botanas")
                .subcategories(Collections.singletonList(papasFritas))
                .build();

        dulces = Category.builder()
                .id(3)
                .name("Dulces")
                .subcategories(Collections.singletonList(chocolate))
                .build();

        helados = Category.builder()
                .id(4)
                .name("Helados")
                .subcategories(Collections.singletonList(heladoLeche))
                .build();

        // Guardar una nueva categoría
        vinosSaveRequest = CategoryGenericRequestDto.builder()
                .name("Vinos")
                .build();

        vinosToSave = modelMapper.map(vinosSaveRequest, Category.class);

        vinosSaved = vinosToSave.toBuilder().id(5).build();

        // Actualizar una categoría existente
        vinosUpdateRequest = CategoryGenericRequestDto.builder()
                .name("Vinos y Licores")
                .build();

        vinosToUpdate = modelMapper.map(vinosUpdateRequest, Category.class);

        vinosUpdated = vinosToUpdate.toBuilder().id(5).build();
    }

    @Test
    public void findAll_OrderByNameAsc_ExistingCategories_Return_ListOfCategories() {
        // Given
        int pageNumber = 0;
        int pageSize = 5;
        String sortBy = "name";

        List<Category> categories = new ArrayList<>();
        categories.add(bebidas);
        categories.add(botanas);
        categories.add(dulces);
        categories.add(helados);

        Pageable pageable = PageRequest.ofSize(pageSize)
                .withPage(pageNumber)
                .withSort(Sort.by(sortBy).ascending());

        given(categoryDao.findAll(pageable)).willReturn(
                new PageImpl<>(categories, pageable, categories.size())
        );

        // When
        CustomPaginationData<CategoryGenericResponseDto, Category> categoriesData = categoryService.findAll(
                PageRequest.of(0, 5, Sort.by("name").ascending())
        );

        // Then
        List<CategoryGenericResponseDto> data = categoriesData.getData();
        CustomPageMetadata meta = categoriesData.getMetadata();

        assertThat(data.size()).isEqualTo(4);
        assertThat(data.get(0).getName()).isEqualTo("Bebidas");
        assertThat(data.get(1).getName()).isEqualTo("Botanas");
        assertThat(data.get(2).getName()).isEqualTo("Dulces");
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
        CustomPaginationData<CategoryGenericResponseDto, Category> categoriesData = categoryService.findAll(
                PageRequest.of(0, 5, Sort.by("id").ascending())
        );

        // Then
        List<CategoryGenericResponseDto> data = categoriesData.getData();
        CustomPageMetadata meta = categoriesData.getMetadata();

        assertThat(data.size()).isEqualTo(0);

        assertThat(meta.getPageNumber()).isEqualTo(0);
        assertThat(meta.getPageSize()).isEqualTo(5);
        assertThat(meta.getNumberOfElements()).isEqualTo(0);
        assertThat(meta.getTotalElements()).isEqualTo(0);
        assertThat(meta.getTotalPages()).isEqualTo(0);
    }

    @Test
    public void findAllByName_OrderByNameAsc_ExistingCategories_Return_ListOfCategories() {
        // Given
        String word = "as";
        int pageNumber = 0;
        int pageSize = 5;
        String sortBy = "name";

        List<Category> categories = new ArrayList<>();
        categories.add(bebidas);
        categories.add(botanas);

        Pageable pageable = PageRequest.ofSize(pageSize)
                .withPage(pageNumber)
                .withSort(Sort.by(sortBy).ascending());

        given(categoryDao.findByNameContainingIgnoreCase(word, pageable)).willReturn(
                new PageImpl<>(categories, pageable, categories.size())
        );

        // When
        CustomPaginationData<CategoryGenericResponseDto, Category> categoriesData = categoryService.findAllByName(
                "as",
                PageRequest.of(0, 5, Sort.by("name").ascending())
        );

        // Then
        List<CategoryGenericResponseDto> data = categoriesData.getData();
        CustomPageMetadata meta = categoriesData.getMetadata();

        assertThat(data.size()).isEqualTo(2);
        assertThat(data.get(0).getName()).isEqualTo("Bebidas");
        assertThat(data.get(1).getName()).isEqualTo("Botanas");

        assertThat(meta.getPageNumber()).isZero();
        assertThat(meta.getPageSize()).isEqualTo(5);
        assertThat(meta.getNumberOfElements()).isEqualTo(2);
        assertThat(meta.getTotalElements()).isEqualTo(2);
        assertThat(meta.getTotalPages()).isEqualTo(1);
    }

    @Test
    public void findAllByName_NonExistingCategories_Return_EmptyListOfCategories() {
        String word = "us";
        int pageNumber = 0;
        int pageSize = 5;
        String sortBy = "id";

        List<Category> categories = new ArrayList<>();

        Pageable pageable = PageRequest.ofSize(pageSize)
                .withPage(pageNumber)
                .withSort(Sort.by(sortBy).ascending());

        given(categoryDao.findByNameContainingIgnoreCase(word, pageable)).willReturn(
                new PageImpl<>(categories, pageable, 0)
        );

        // When
        CustomPaginationData<CategoryGenericResponseDto, Category> categoriesData = categoryService.findAllByName(
                "us",
                PageRequest.of(0, 5, Sort.by("id").ascending())
        );

        // Then
        List<CategoryGenericResponseDto> data = categoriesData.getData();
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

        given(categoryDao.findById(categoryId)).willReturn(Optional.of(bebidas));

        // When
        CategoryGetResponseDto foundCategory = categoryService.findById(1);

        // Then
        assertThat(foundCategory).isNotNull();
        assertThat(foundCategory.getId()).isEqualTo(1);
        assertThat(foundCategory.getName()).isEqualTo("Bebidas");
        assertThat(foundCategory.getDescription()).isEqualTo(null);
        assertThat(foundCategory.getSubcategories().get(0).getName()).isEqualTo("Agua natural");
    }

    @Test
    public void findById_NonExistingCategory_Throw_DataNotFoundException() {
        // Given
        given(categoryDao.findById(anyInt())).willReturn(Optional.empty());

        // When
        Throwable thrown = assertThrows(DataNotFoundException.class,
                () -> categoryService.findById(100));

        // Then
        MatcherAssert.assertThat(thrown.getMessage(), is("Category not found!"));
    }

    @Test
    public void save_NonExistingCategory_Return_SavedCategory() {
        // Given
        given(categoryDao.existsByName(vinosSaveRequest.getName())).willReturn(false);
        given(categoryDao.save(vinosToSave)).willReturn(vinosSaved);

        // When
        CategoryGenericResponseDto savedCategory = categoryService.save(vinosSaveRequest);

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
                () -> categoryService.save(vinosSaveRequest));

        // Then
        MatcherAssert.assertThat(thrown.getMessage(), is("Category already exists!"));
    }

    @Test
    public void update_ExistingCategory_Return_UpdatedCategory() {
        // Given
        Integer categoryId = 5;

        given(categoryDao.findById(categoryId)).willReturn(Optional.ofNullable(vinosToUpdate));
        given(categoryDao.save(vinosToUpdate)).willReturn(vinosUpdated);

        // When
        CategoryGenericResponseDto updatedCategory = categoryService.update(5, vinosUpdateRequest);

        // Then
        assertThat(updatedCategory).isNotNull();
        assertThat(updatedCategory.getId()).isEqualTo(5);
        assertThat(updatedCategory.getName()).isEqualTo("Vinos y Licores");
        assertThat(updatedCategory.getDescription()).isEqualTo(null);
    }

    @Test
    public void update_NonExistingCategory_Throw_DataNotFoundException() {
        // Given
        given(categoryDao.findById(anyInt())).willReturn(Optional.empty());

        // When
        Throwable thrown = assertThrows(DataNotFoundException.class,
                () -> categoryService.update(100, vinosUpdateRequest));

        // Then
        MatcherAssert.assertThat(thrown.getMessage(), is("Category does not exist!"));
    }

    @Test
    public void deleteById_Category_NoReturn() {
        // Given
        willDoNothing().given(categoryDao).deleteById(anyInt());

        // When
        categoryService.deleteById(10);

        // Then
        verify(categoryDao, times(1)).deleteById(10);
    }
}
