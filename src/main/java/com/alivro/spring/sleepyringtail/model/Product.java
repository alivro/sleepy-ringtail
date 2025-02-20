package com.alivro.spring.sleepyringtail.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "products")
@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder(toBuilder = true)
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id")
    private Integer id;

    @Column(name = "name", length = 50, nullable = false, unique = false)
    private String name;

    @Column(name = "description", length = 150, nullable = true, unique = false)
    private String description;

    @Column(name = "size", length = 10, nullable = false, unique = false)
    private String size;

    @Column(name = "price", precision = 8, scale = 2, nullable = false, unique = false)
    private BigDecimal price;

    @Column(name = "barcode", length = 13, nullable = false, unique = true)
    private String barcode;
}
