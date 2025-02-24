package com.alivro.spring.sleepyringtail.util.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class CustomErrorResponse<S> {
    // Código de estado HTTP
    private int status;

    // Mensaje de error
    private List<String> errors;

    // URL de la solicitud
    private String path;

    // Marca de tiempo
    private Timestamp timestamp;

    // Metadatos
    private S metadata;
}
