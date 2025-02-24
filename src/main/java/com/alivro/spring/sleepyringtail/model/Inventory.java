package com.alivro.spring.sleepyringtail.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "inventory")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder(toBuilder = true)
public class Inventory {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "inventory_id")
    private Integer id;

    @Column(name = "quantity_available", nullable = false, unique = false)
    private Short quantityAvailable;

    @Column(name = "minimum_stock", nullable = false, unique = false)
    private Short minimumStock;

    @Column(name = "maximum_stock", nullable = false, unique = false)
    private Short maximumStock;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false, unique = true)
    private Product product;
}
