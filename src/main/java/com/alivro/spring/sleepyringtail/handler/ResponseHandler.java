package com.alivro.spring.sleepyringtail.handler;

import com.alivro.spring.sleepyringtail.util.response.CustomErrorResponse;
import com.alivro.spring.sleepyringtail.util.response.CustomResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.List;

public class ResponseHandler {
    private ResponseHandler() {
    }

    /**
     * Método para enviar una respuesta
     *
     * @param status   Código de estado HTTP
     * @param message  Mensaje
     * @param data     Lista de objetos
     * @param metadata Metadatos
     * @return Respuesta HTTP
     */
    public static <T, S> ResponseEntity<CustomResponse<T, S>> sendResponse(
            HttpStatus status, String message, List<T> data, S metadata) {
        CustomResponse<T, S> response = CustomResponse.<T, S>builder()
                .status(status.value())
                .message(message)
                .data(data)
                .metadata(metadata)
                .build();

        return new ResponseEntity<>(response, status);
    }

    /**
     * Método para enviar una respuesta
     *
     * @param status  Código de estado HTTP
     * @param message Mensaje
     * @param data    Objeto
     * @return Respuesta HTTP
     */
    public static <T, S> ResponseEntity<CustomResponse<T, S>> sendResponse(
            HttpStatus status, String message, T data) {
        return sendResponse(status, message, Collections.singletonList(data), null);
    }

    /**
     * Método para enviar una respuesta
     *
     * @param status  Código de estado HTTP
     * @param message Mensaje
     * @return Respuesta HTTP
     */
    public static <T, S> ResponseEntity<CustomResponse<T, S>> sendResponse(
            HttpStatus status, String message) {
        return sendResponse(status, message, null, null);
    }

    /**
     * Método para enviar una respuesta de error
     *
     * @param status   Código de estado HTTP
     * @param errors   Mensajes de error
     * @param path     URL de la solicitud
     * @param metadata Metadatos
     * @return Respuesta HTTP
     */
    public static <S> ResponseEntity<CustomErrorResponse<S>> sendErrorResponse(
            HttpStatus status, List<String> errors, String path, S metadata) {
        CustomErrorResponse<S> response = CustomErrorResponse.<S>builder()
                .status(status.value())
                .errors(errors)
                .path(path)
                .timestamp(new Timestamp(System.currentTimeMillis()))
                .metadata(metadata)
                .build();

        return new ResponseEntity<>(response, status);
    }

    /**
     * Método para enviar una respuesta de error
     *
     * @param status Código de estado HTTP
     * @param error  Mensaje de error
     * @param path   URL de la solicitud
     * @return Respuesta HTTP
     */
    public static <S> ResponseEntity<CustomErrorResponse<S>> sendErrorResponse(
            HttpStatus status, String error, String path) {
        return sendErrorResponse(status, Collections.singletonList(error), path, null);
    }

    /**
     * Método para enviar una respuesta de error
     *
     * @param status Código de estado HTTP
     * @param errors Mensajes de error
     * @param path   URL de la solicitud
     * @return Respuesta HTTP
     */
    public static <S> ResponseEntity<CustomErrorResponse<S>> sendErrorResponse(
            HttpStatus status, List<String> errors, String path) {
        return sendErrorResponse(status, errors, path, null);
    }
}