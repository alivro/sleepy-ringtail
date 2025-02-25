package com.alivro.spring.sleepyringtail.service.impl;

import com.alivro.spring.sleepyringtail.constants.MessageConstants;
import com.alivro.spring.sleepyringtail.dao.SubcategoryDao;
import com.alivro.spring.sleepyringtail.exception.DataAlreadyExistsException;
import com.alivro.spring.sleepyringtail.exception.DataNotFoundException;
import com.alivro.spring.sleepyringtail.model.Category;
import com.alivro.spring.sleepyringtail.model.Product;
import com.alivro.spring.sleepyringtail.model.Subcategory;
import com.alivro.spring.sleepyringtail.model.subcategory.request.SubcategoryGenericRequestDto;
import com.alivro.spring.sleepyringtail.model.subcategory.response.SubcategoryGenericResponseDto;
import com.alivro.spring.sleepyringtail.model.subcategory.response.SubcategoryGetResponseDto;
import com.alivro.spring.sleepyringtail.model.util.request.CategoryRequestDto;
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
public class ISubcategoryServiceImplTest {
    @Mock
    private SubcategoryDao subcategoryDao;

    @InjectMocks
    private ISubcategoryServiceImpl subcategoryService;

    // Buscar todas las subcategorías
    private static Subcategory aguaNatural;
    private static Subcategory chocolate;
    private static Subcategory heladoLeche;
    private static Subcategory papasFritas;

    // Guardar una nueva subcategoría
    private static SubcategoryGenericRequestDto gomitasSaveRequest;
    private static Subcategory gomitasToSave;
    private static Subcategory gomitasSaved;

    // Actualizar una subcategoría existente
    private static SubcategoryGenericRequestDto gomitasUpdateRequest;
    private static Subcategory gomitasToUpdate;
    private static Subcategory gomitasUpdated;

    @BeforeAll
    public static void setup() {
        ModelMapper modelMapper = new ModelMapper();

        // Buscar todas las subcategorías
        Category bebidas = Category.builder()
                .id(1)
                .name("Bebidas")
                .build();

        Category botanas = Category.builder()
                .id(2)
                .name("Botanas")
                .build();

        Category dulces = Category.builder()
                .id(3)
                .name("Dulces")
                .build();

        Category helados = Category.builder()
                .id(4)
                .name("Helados")
                .build();

        Product estrellaMarina = Product.builder()
                .id(1)
                .name("Estrella Marina")
                .build();

        aguaNatural = Subcategory.builder()
                .id(1)
                .name("Agua natural")
                .category(bebidas)
                .products(Collections.singletonList(estrellaMarina))
                .build();

        chocolate = Subcategory.builder()
                .id(2)
                .name("Chocolate")
                .category(dulces)
                .products(new ArrayList<>())
                .build();

        heladoLeche = Subcategory.builder()
                .id(3)
                .name("Helado base leche")
                .category(helados)
                .products(new ArrayList<>())
                .build();

        papasFritas = Subcategory.builder()
                .id(4)
                .name("Papas fritas")
                .category(botanas)
                .products(new ArrayList<>())
                .build();

        // Guardar una nueva subcategoría
        CategoryRequestDto botanasRequest = CategoryRequestDto.builder()
                .id(2)
                .name("Botanas")
                .build();

        gomitasSaveRequest = SubcategoryGenericRequestDto.builder()
                .name("Gomitas")
                .category(botanasRequest)
                .build();

        gomitasToSave = modelMapper.map(gomitasSaveRequest, Subcategory.class);

        gomitasSaved = gomitasToSave.toBuilder().id(5).build();

        // Actualizar una subcategoría existente
        CategoryRequestDto dulcesRequest = CategoryRequestDto.builder()
                .id(3)
                .name("Dulces")
                .build();

        gomitasUpdateRequest = SubcategoryGenericRequestDto.builder()
                .name("Caramelos de goma")
                .category(dulcesRequest)
                .build();

        gomitasToUpdate = modelMapper.map(gomitasUpdateRequest, Subcategory.class);

        gomitasUpdated = gomitasToUpdate.toBuilder().id(5).build();
    }

    @Test
    public void findAll_OrderByNameAsc_ExistingSubcategories_Return_ListOfSubcategories() {
        // Given
        int pageNumber = 0;
        int pageSize = 5;
        String sortBy = "name";

        List<Subcategory> subcategories = new ArrayList<>();
        subcategories.add(aguaNatural);
        subcategories.add(chocolate);
        subcategories.add(heladoLeche);
        subcategories.add(papasFritas);

        Pageable pageable = PageRequest.ofSize(pageSize)
                .withPage(pageNumber)
                .withSort(Sort.by(sortBy).ascending());

        given(subcategoryDao.findAll(pageable)).willReturn(
                new PageImpl<>(subcategories, pageable, subcategories.size())
        );

        // When
        CustomPaginationData<SubcategoryGenericResponseDto, Subcategory> subcategoriesData = subcategoryService.findAll(
                PageRequest.of(0, 5, Sort.by("name").ascending())
        );

        // Then
        List<SubcategoryGenericResponseDto> data = subcategoriesData.getData();
        CustomPageMetadata meta = subcategoriesData.getMetadata();

        assertThat(data.size()).isEqualTo(4);
        assertThat(data.get(0).getName()).isEqualTo("Agua natural");
        assertThat(data.get(1).getName()).isEqualTo("Chocolate");
        assertThat(data.get(2).getName()).isEqualTo("Helado base leche");
        assertThat(data.get(3).getName()).isEqualTo("Papas fritas");

        assertThat(meta.getPageNumber()).isZero();
        assertThat(meta.getPageSize()).isEqualTo(5);
        assertThat(meta.getNumberOfElements()).isEqualTo(4);
        assertThat(meta.getTotalElements()).isEqualTo(4);
        assertThat(meta.getTotalPages()).isEqualTo(1);
    }

    @Test
    public void findAll_NonExistingSubcategories_Return_EmptyListOfSubcategories() {
        int pageNumber = 0;
        int pageSize = 5;
        String sortBy = "id";

        List<Subcategory> subcategories = new ArrayList<>();

        Pageable pageable = PageRequest.ofSize(pageSize)
                .withPage(pageNumber)
                .withSort(Sort.by(sortBy).ascending());

        given(subcategoryDao.findAll(pageable)).willReturn(
                new PageImpl<>(subcategories, pageable, 0)
        );

        // When
        CustomPaginationData<SubcategoryGenericResponseDto, Subcategory> subcategoriesData = subcategoryService.findAll(
                PageRequest.of(0, 5, Sort.by("id").ascending())
        );

        // Then
        List<SubcategoryGenericResponseDto> data = subcategoriesData.getData();
        CustomPageMetadata meta = subcategoriesData.getMetadata();

        assertThat(data.size()).isEqualTo(0);

        assertThat(meta.getPageNumber()).isEqualTo(0);
        assertThat(meta.getPageSize()).isEqualTo(5);
        assertThat(meta.getNumberOfElements()).isEqualTo(0);
        assertThat(meta.getTotalElements()).isEqualTo(0);
        assertThat(meta.getTotalPages()).isEqualTo(0);
    }

    @Test
    public void findAllByName_OrderByNameAsc_ExistingSubcategories_Return_ListOfSubcategories() {
        // Given
        String word = "la";
        int pageNumber = 0;
        int pageSize = 5;
        String sortBy = "name";

        List<Subcategory> subcategories = new ArrayList<>();
        subcategories.add(chocolate);
        subcategories.add(heladoLeche);

        Pageable pageable = PageRequest.ofSize(pageSize)
                .withPage(pageNumber)
                .withSort(Sort.by(sortBy).ascending());

        given(subcategoryDao.findByNameContainingIgnoreCase(word, pageable)).willReturn(
                new PageImpl<>(subcategories, pageable, subcategories.size())
        );

        // When
        CustomPaginationData<SubcategoryGenericResponseDto, Subcategory> subcategoriesData = subcategoryService.findAllByName(
                "la",
                PageRequest.of(0, 5, Sort.by("name").ascending())
        );

        // Then
        List<SubcategoryGenericResponseDto> data = subcategoriesData.getData();
        CustomPageMetadata meta = subcategoriesData.getMetadata();

        assertThat(data.size()).isEqualTo(2);
        assertThat(data.get(0).getName()).isEqualTo("Chocolate");
        assertThat(data.get(1).getName()).isEqualTo("Helado base leche");

        assertThat(meta.getPageNumber()).isZero();
        assertThat(meta.getPageSize()).isEqualTo(5);
        assertThat(meta.getNumberOfElements()).isEqualTo(2);
        assertThat(meta.getTotalElements()).isEqualTo(2);
        assertThat(meta.getTotalPages()).isEqualTo(1);
    }

    @Test
    public void findAllByName_NonExistingSubcategories_Return_EmptyListOfSubcategories() {
        String word = "lu";
        int pageNumber = 0;
        int pageSize = 5;
        String sortBy = "id";

        List<Subcategory> subcategories = new ArrayList<>();

        Pageable pageable = PageRequest.ofSize(pageSize)
                .withPage(pageNumber)
                .withSort(Sort.by(sortBy).ascending());

        given(subcategoryDao.findByNameContainingIgnoreCase(word, pageable)).willReturn(
                new PageImpl<>(subcategories, pageable, 0)
        );

        // When
        CustomPaginationData<SubcategoryGenericResponseDto, Subcategory> subcategoriesData = subcategoryService.findAllByName(
                "lu",
                PageRequest.of(0, 5, Sort.by("id").ascending())
        );

        // Then
        List<SubcategoryGenericResponseDto> data = subcategoriesData.getData();
        CustomPageMetadata meta = subcategoriesData.getMetadata();

        assertThat(data.size()).isEqualTo(0);

        assertThat(meta.getPageNumber()).isEqualTo(0);
        assertThat(meta.getPageSize()).isEqualTo(5);
        assertThat(meta.getNumberOfElements()).isEqualTo(0);
        assertThat(meta.getTotalElements()).isEqualTo(0);
        assertThat(meta.getTotalPages()).isEqualTo(0);
    }

    @Test
    public void findById_ExistingSubcategory_Return_FoundSubcategory() {
        // Given
        Integer subcategoryId = 1;

        given(subcategoryDao.findById(subcategoryId)).willReturn(Optional.of(aguaNatural));

        // When
        SubcategoryGetResponseDto foundSubcategory = subcategoryService.findById(1);

        // Then
        assertThat(foundSubcategory).isNotNull();
        assertThat(foundSubcategory.getId()).isEqualTo(1);
        assertThat(foundSubcategory.getName()).isEqualTo("Agua natural");
        assertThat(foundSubcategory.getDescription()).isEqualTo(null);
        assertThat(foundSubcategory.getCategory().getName()).isEqualTo("Bebidas");
        assertThat(foundSubcategory.getProducts().get(0).getName()).isEqualTo("Estrella Marina");
    }

    @Test
    public void findById_NonExistingSubcategory_Throw_DataNotFoundException() {
        // Given
        given(subcategoryDao.findById(anyInt())).willReturn(Optional.empty());

        // When
        Throwable thrown = assertThrows(DataNotFoundException.class,
                () -> subcategoryService.findById(100));

        // Then
        MatcherAssert.assertThat(thrown.getMessage(), is(MessageConstants.SUBCATEGORY_NOT_FOUND));
    }

    @Test
    public void save_NonExistingSubcategory_Return_SavedSubcategory() {
        // Given
        given(subcategoryDao.existsByName(gomitasSaveRequest.getName())).willReturn(false);
        given(subcategoryDao.save(gomitasToSave)).willReturn(gomitasSaved);

        // When
        SubcategoryGenericResponseDto savedSubcategory = subcategoryService.save(gomitasSaveRequest);

        // Then
        assertThat(savedSubcategory).isNotNull();
        assertThat(savedSubcategory.getName()).isEqualTo("Gomitas");
        assertThat(savedSubcategory.getDescription()).isEqualTo(null);
        assertThat(savedSubcategory.getCategory().getName()).isEqualTo("Botanas");
    }

    @Test
    public void save_ExistingSubcategory_Throw_DataAlreadyExistsException() {
        // Given
        given(subcategoryDao.existsByName(anyString())).willReturn(true);

        // When
        Throwable thrown = assertThrows(DataAlreadyExistsException.class,
                () -> subcategoryService.save(gomitasSaveRequest));

        // Then
        MatcherAssert.assertThat(thrown.getMessage(), is(MessageConstants.SUBCATEGORY_ALREADY_EXISTS));
    }

    @Test
    public void update_ExistingSubcategory_Return_UpdatedSubcategory() {
        // Given
        Integer subcategoryId = 5;

        given(subcategoryDao.findById(subcategoryId)).willReturn(Optional.ofNullable(gomitasToUpdate));
        given(subcategoryDao.save(gomitasToUpdate)).willReturn(gomitasUpdated);

        // When
        SubcategoryGenericResponseDto updatedSubcategory = subcategoryService.update(5, gomitasUpdateRequest);

        // Then
        assertThat(updatedSubcategory).isNotNull();
        assertThat(updatedSubcategory.getId()).isEqualTo(5);
        assertThat(updatedSubcategory.getName()).isEqualTo("Caramelos de goma");
        assertThat(updatedSubcategory.getDescription()).isEqualTo(null);
        assertThat(updatedSubcategory.getCategory().getName()).isEqualTo("Dulces");
    }

    @Test
    public void update_NonExistingSubcategory_Throw_DataNotFoundException() {
        // Given
        given(subcategoryDao.findById(anyInt())).willReturn(Optional.empty());

        // When
        Throwable thrown = assertThrows(DataNotFoundException.class,
                () -> subcategoryService.update(100, gomitasUpdateRequest));

        // Then
        MatcherAssert.assertThat(thrown.getMessage(), is(MessageConstants.SUBCATEGORY_NOT_FOUND));
    }

    @Test
    public void deleteById_Category_NoReturn() {
        // Given
        willDoNothing().given(subcategoryDao).deleteById(anyInt());

        // When
        subcategoryService.deleteById(10);

        // Then
        verify(subcategoryDao, times(1)).deleteById(10);
    }
}
