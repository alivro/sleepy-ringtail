package com.alivro.spring.sleepyringtail.util.pagination;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomPaginationData<T, S> {
    // Lista de objetos
    private List<T> data;

    // Metadatos de la paginación
    private CustomPageMetadata metadata;

    public CustomPaginationData(List<T> data, Page<S> metadata) {
        this.data = data;
        this.metadata = CustomPageMetadata.builder()
                .pageNumber(metadata.getNumber())
                .pageSize(metadata.getSize())
                .numberOfElements(metadata.getNumberOfElements())
                .totalPages(metadata.getTotalPages())
                .totalElements(metadata.getTotalElements())
                .build();
    }
}
