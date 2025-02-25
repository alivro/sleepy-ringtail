package com.alivro.spring.sleepyringtail.service.impl;

import com.alivro.spring.sleepyringtail.constants.MessageConstants;
import com.alivro.spring.sleepyringtail.dao.InventoryDao;
import com.alivro.spring.sleepyringtail.exception.DataAlreadyExistsException;
import com.alivro.spring.sleepyringtail.exception.DataNotFoundException;
import com.alivro.spring.sleepyringtail.model.Inventory;
import com.alivro.spring.sleepyringtail.model.Product;
import com.alivro.spring.sleepyringtail.model.inventory.request.InventoryGenericRequestDto;
import com.alivro.spring.sleepyringtail.model.inventory.response.InventoryGenericResponseDto;
import com.alivro.spring.sleepyringtail.model.util.request.ProductRequestDto;
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
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.CoreMatchers.is;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
public class IInventoryServiceImplTest {
    @Mock
    private InventoryDao inventoryDao;

    @InjectMocks
    private IInventoryServiceImpl inventoryService;

    // Buscar el inventario de todos los productos
    private static Inventory ardillasSaladas;
    private static Inventory estrellaMarina;
    private static Inventory osoAlmendrado;
    private static Inventory vacaNapolitana;

    // Guardar el inventario de un nuevo producto
    private static InventoryGenericRequestDto vacaChocolateSaveRequest;
    private static Inventory vacaChocolateToSave;
    private static Inventory vacaChocolateSaved;

    // Actualizar el inventario de un producto existente
    private static InventoryGenericRequestDto vacaChocolateUpdateRequest;
    private static Inventory vacaChocolateToUpdate;
    private static Inventory vacaChocolateUpdated;

    @BeforeAll
    public static void setup() {
        ModelMapper modelMapper = new ModelMapper();

        // Buscar el inventario de todos los productos
        Product ardillasSaladasProduct = Product.builder()
                .id(1)
                .name("Ardillas Saladas")
                .build();

        Product estrellaMarinaProduct = Product.builder()
                .id(2)
                .name("Estrella Marina")
                .build();

        Product osoAlmendradoProduct = Product.builder()
                .id(3)
                .name("Oso Almendrado")
                .build();

        Product vacaNapolitanaProduct = Product.builder()
                .id(4)
                .name("Vaca Napolitana")
                .build();

        ardillasSaladas = Inventory.builder()
                .id(1)
                .quantityAvailable((short) 322)
                .minimumStock((short) 250)
                .maximumStock((short) 625)
                .product(ardillasSaladasProduct)
                .build();

        estrellaMarina = Inventory.builder()
                .id(2)
                .quantityAvailable((short) 202)
                .minimumStock((short) 120)
                .maximumStock((short) 300)
                .product(estrellaMarinaProduct)
                .build();

        osoAlmendrado = Inventory.builder()
                .id(3)
                .quantityAvailable((short) 271)
                .minimumStock((short) 230)
                .maximumStock((short) 575)
                .product(osoAlmendradoProduct)
                .build();

        vacaNapolitana = Inventory.builder()
                .id(4)
                .quantityAvailable((short) 113)
                .minimumStock((short) 70)
                .maximumStock((short) 175)
                .product(vacaNapolitanaProduct)
                .build();

        // Guardar el inventario de un nuevo producto
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

        vacaChocolateToSave = modelMapper.map(vacaChocolateSaveRequest, Inventory.class);

        vacaChocolateSaved = vacaChocolateToSave.toBuilder().id(5).build();

        // Actualizar el inventario de un producto existente
        vacaChocolateUpdateRequest = InventoryGenericRequestDto.builder()
                .quantityAvailable((short) 150)
                .minimumStock((short) 100)
                .maximumStock((short) 250)
                .product(vacaChocolateProductRequest)
                .build();

        vacaChocolateToUpdate = modelMapper.map(vacaChocolateUpdateRequest, Inventory.class);

        vacaChocolateUpdated = vacaChocolateToUpdate.toBuilder().id(5).build();
    }

    @Test
    public void findAll_OrderByQuantityAvailableDesc_ExistingInventory_Return_ListOfInventory() {
        // Given
        int pageNumber = 0;
        int pageSize = 5;
        String sortBy = "quantityAvailable";

        List<Inventory> inventoryStocks = new ArrayList<>();
        inventoryStocks.add(ardillasSaladas);
        inventoryStocks.add(osoAlmendrado);
        inventoryStocks.add(estrellaMarina);
        inventoryStocks.add(vacaNapolitana);

        Pageable pageable = PageRequest.ofSize(pageSize)
                .withPage(pageNumber)
                .withSort(Sort.by(sortBy).descending());

        given(inventoryDao.findAll(pageable)).willReturn(
                new PageImpl<>(inventoryStocks, pageable, inventoryStocks.size())
        );

        // When
        CustomPaginationData<InventoryGenericResponseDto, Inventory> inventoryData = inventoryService.findAll(
                PageRequest.of(0, 5, Sort.by("quantityAvailable").descending())
        );

        // Then
        List<InventoryGenericResponseDto> data = inventoryData.getData();
        CustomPageMetadata meta = inventoryData.getMetadata();

        assertThat(data.size()).isEqualTo(4);
        assertThat(data.get(0).getQuantityAvailable()).isEqualTo((short) 322);
        assertThat(data.get(1).getQuantityAvailable()).isEqualTo((short) 271);
        assertThat(data.get(2).getQuantityAvailable()).isEqualTo((short) 202);
        assertThat(data.get(3).getQuantityAvailable()).isEqualTo((short) 113);

        assertThat(meta.getPageNumber()).isZero();
        assertThat(meta.getPageSize()).isEqualTo(5);
        assertThat(meta.getNumberOfElements()).isEqualTo(4);
        assertThat(meta.getTotalElements()).isEqualTo(4);
        assertThat(meta.getTotalPages()).isEqualTo(1);
    }

    @Test
    public void findAll_NonExistingInventory_Return_EmptyListOfInventory() {
        int pageNumber = 0;
        int pageSize = 5;
        String sortBy = "id";

        List<Inventory> inventoryStocks = new ArrayList<>();

        Pageable pageable = PageRequest.ofSize(pageSize)
                .withPage(pageNumber)
                .withSort(Sort.by(sortBy).ascending());

        given(inventoryDao.findAll(pageable)).willReturn(
                new PageImpl<>(inventoryStocks, pageable, 0)
        );

        // When
        CustomPaginationData<InventoryGenericResponseDto, Inventory> inventoryData = inventoryService.findAll(
                PageRequest.of(0, 5, Sort.by("id").ascending())
        );

        // Then
        List<InventoryGenericResponseDto> data = inventoryData.getData();
        CustomPageMetadata meta = inventoryData.getMetadata();

        assertThat(data.size()).isEqualTo(0);

        assertThat(meta.getPageNumber()).isEqualTo(0);
        assertThat(meta.getPageSize()).isEqualTo(5);
        assertThat(meta.getNumberOfElements()).isEqualTo(0);
        assertThat(meta.getTotalElements()).isEqualTo(0);
        assertThat(meta.getTotalPages()).isEqualTo(0);
    }

    @Test
    public void findById_ExistingInventory_Return_FoundInventory() {
        // Given
        Integer inventoryId = 1;

        given(inventoryDao.findById(inventoryId)).willReturn(Optional.of(ardillasSaladas));

        // When
        InventoryGenericResponseDto foundInventory = inventoryService.findById(1);

        // Then
        assertThat(foundInventory).isNotNull();
        assertThat(foundInventory.getId()).isEqualTo(1);
        assertThat(foundInventory.getQuantityAvailable()).isEqualTo((short) 322);
        assertThat(foundInventory.getMinimumStock()).isEqualTo((short) 250);
        assertThat(foundInventory.getMaximumStock()).isEqualTo((short) 625);
        assertThat(foundInventory.getProduct().getName()).isEqualTo("Ardillas Saladas");
    }

    @Test
    public void findById_NonExistingInventory_Throw_DataNotFoundException() {
        // Given
        given(inventoryDao.findById(anyInt())).willReturn(Optional.empty());

        // When
        Throwable thrown = assertThrows(DataNotFoundException.class,
                () -> inventoryService.findById(100));

        // Then
        MatcherAssert.assertThat(thrown.getMessage(), is(MessageConstants.INVENTORY_NOT_FOUND));
    }

    @Test
    public void save_NonExistingInventory_Return_SavedInventory() {
        // Given
        given(inventoryDao.existsByProductId(vacaChocolateSaveRequest.getProduct().getId())).willReturn(false);
        given(inventoryDao.save(vacaChocolateToSave)).willReturn(vacaChocolateSaved);

        // When
        InventoryGenericResponseDto savedInventory = inventoryService.save(vacaChocolateSaveRequest);

        // Then
        assertThat(savedInventory).isNotNull();
        assertThat(savedInventory.getQuantityAvailable()).isEqualTo((short) 55);
        assertThat(savedInventory.getMinimumStock()).isEqualTo((short) 10);
        assertThat(savedInventory.getMaximumStock()).isEqualTo((short) 150);
        assertThat(savedInventory.getProduct().getName()).isEqualTo("Vaca de Chocolate");
    }

    @Test
    public void save_ExistingInventory_Throw_DataAlreadyExistsException() {
        // Given
        given(inventoryDao.existsByProductId(anyInt())).willReturn(true);

        // When
        Throwable thrown = assertThrows(DataAlreadyExistsException.class,
                () -> inventoryService.save(vacaChocolateSaveRequest));

        // Then
        MatcherAssert.assertThat(thrown.getMessage(), is(MessageConstants.INVENTORY_ALREADY_EXISTS));
    }

    @Test
    public void update_ExistingInventory_Return_UpdatedInventory() {
        // Given
        Integer productId = 5;

        given(inventoryDao.findById(productId)).willReturn(Optional.ofNullable(vacaChocolateToUpdate));
        given(inventoryDao.save(vacaChocolateToUpdate)).willReturn(vacaChocolateUpdated);

        // When
        InventoryGenericResponseDto updatedInventory = inventoryService.update(5, vacaChocolateUpdateRequest);

        // Then
        assertThat(updatedInventory).isNotNull();
        assertThat(updatedInventory.getId()).isEqualTo(5);
        assertThat(updatedInventory.getQuantityAvailable()).isEqualTo((short) 150);
        assertThat(updatedInventory.getMinimumStock()).isEqualTo((short) 100);
        assertThat(updatedInventory.getMaximumStock()).isEqualTo((short) 250);
        assertThat(updatedInventory.getProduct().getName()).isEqualTo("Vaca de Chocolate");
    }

    @Test
    public void update_NonExistingInventory_Throw_DataNotFoundException() {
        // Given
        given(inventoryDao.findById(anyInt())).willReturn(Optional.empty());

        // When
        Throwable thrown = assertThrows(DataNotFoundException.class,
                () -> inventoryService.update(100, vacaChocolateUpdateRequest));

        // Then
        MatcherAssert.assertThat(thrown.getMessage(), is(MessageConstants.INVENTORY_NOT_FOUND));
    }

    @Test
    public void deleteById_Category_NoReturn() {
        // Given
        willDoNothing().given(inventoryDao).deleteById(anyInt());

        // When
        inventoryService.deleteById(10);

        // Then
        verify(inventoryDao, times(1)).deleteById(10);
    }
}
