package com.helmes.sectorsapi.exception;

import lombok.Getter;

import java.io.Serial;

@Getter
public class BadCredentialsException extends RuntimeException {

    @Serial
    private static final long serialVersionUID = 1L;

    private final String code;

    public BadCredentialsException(String description, String code) {
        super(description);
        this.code = code;
    }
}
