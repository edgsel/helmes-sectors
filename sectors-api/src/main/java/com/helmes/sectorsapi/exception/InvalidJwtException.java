package com.helmes.sectorsapi.exception;

public class InvalidJwtException extends RuntimeException {

    public InvalidJwtException(String description, Throwable cause) {
        super(description, cause);
    }
}
