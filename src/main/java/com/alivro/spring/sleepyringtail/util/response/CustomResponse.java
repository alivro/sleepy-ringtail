package com.alivro.spring.sleepyringtail.util.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomResponse<T, S> {
    // Código de estado HTTP
    private int status;

    // Mensaje
    private String message;

    // Lista de objetos
    private List<T> data;

    // Metadatos
    private S metadata;
}
