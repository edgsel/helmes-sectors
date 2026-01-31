package com.helmes.sectorsapi.config;

import com.helmes.sectorsapi.dto.response.ErrorResponseDTO;
import com.helmes.sectorsapi.exception.BadCredentialsException;
import com.helmes.sectorsapi.exception.EntityNotFoundException;
import com.helmes.sectorsapi.exception.ErrorCode;
import com.helmes.sectorsapi.exception.ParentSectorSelectedException;
import com.helmes.sectorsapi.exception.UserExistsException;
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
    public ResponseEntity<ErrorResponseDTO> handleEntityNotFoundException(EntityNotFoundException ex) {
        return ResponseEntity
            .status(HttpStatus.NOT_FOUND)
            .contentType(MediaType.APPLICATION_JSON)
            .body(buildErrorResponseDTO(ex.getMessage(), ex.getCode()));
    }

    @ExceptionHandler(UserExistsException.class)
    public ResponseEntity<ErrorResponseDTO> handleEntityExistsException(UserExistsException ex) {
        return ResponseEntity
            .status(HttpStatus.CONFLICT)
            .contentType(MediaType.APPLICATION_JSON)
            .body(buildErrorResponseDTO(ex.getMessage(), ex.getCode()));
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponseDTO> handleValidationException(MethodArgumentNotValidException ex) {
        String errorMessage = ex.getBindingResult()
            .getFieldErrors()
            .stream()
            .map(error -> error.getField() + ": " + error.getDefaultMessage())
            .collect(Collectors.joining(", "));

        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .contentType(MediaType.APPLICATION_JSON)
            .body(buildErrorResponseDTO(errorMessage, VALIDATION_ERROR));
    }

    @ExceptionHandler(ParentSectorSelectedException.class)
    public ResponseEntity<ErrorResponseDTO> handleParentSectorSelectedException(ParentSectorSelectedException ex) {
        return ResponseEntity
            .status(HttpStatus.BAD_REQUEST)
            .contentType(MediaType.APPLICATION_JSON)
            .body(buildErrorResponseDTO(ex.getMessage(), ex.getCode()));
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ErrorResponseDTO> handleBadCredentialsException(BadCredentialsException ex) {
        return ResponseEntity
            .status(HttpStatus.UNAUTHORIZED)
            .contentType(MediaType.APPLICATION_JSON)
            .body(buildErrorResponseDTO(ex.getMessage(), ex.getCode()));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponseDTO> handleUnexpectedException(Exception ex) {
        log.error("Unexpected exception", ex);

        return ResponseEntity
            .status(HttpStatus.INTERNAL_SERVER_ERROR)
            .contentType(MediaType.APPLICATION_JSON)
            .body(buildErrorResponseDTO("Internal Server Error", INTERNAL_ERROR));
    }

    private static ErrorResponseDTO buildErrorResponseDTO(String description, ErrorCode code) {
        return ErrorResponseDTO.builder()
            .description(description)
            .code(code)
            .build();
    }
}
