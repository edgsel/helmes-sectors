package com.helmes.sectorsapi.config;

import com.helmes.sectorsapi.dto.ErrorDTO;
import com.helmes.sectorsapi.exception.EntityNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

import static com.helmes.sectorsapi.exception.ErrorCode.INTERNAL_ERROR;
import static com.helmes.sectorsapi.exception.ErrorCode.VALIDATION_ERROR;

@Slf4j
@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleEntityNotFoundException(EntityNotFoundException ex) {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .contentType(MediaType.APPLICATION_JSON)
            .body(buildErrorDTO(ex.getMessage(), ex.getCode()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO> handleValidationException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .collect(Collectors.joining(", "));

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .contentType(MediaType.APPLICATION_JSON)
            .body(buildErrorDTO(errorMessage, VALIDATION_ERROR.name()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorDTO> handleUnexpectedException(Exception ex) {
        log.error("Unexpected exception", ex);

        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .contentType(MediaType.APPLICATION_JSON)
            .body(buildErrorDTO("Internal Server Error", INTERNAL_ERROR.name()));
    }

    private static ErrorDTO buildErrorDTO(String description, String code) {
        return ErrorDTO.builder()
            .description(description)
            .code(code)
            .build();
    }
}
