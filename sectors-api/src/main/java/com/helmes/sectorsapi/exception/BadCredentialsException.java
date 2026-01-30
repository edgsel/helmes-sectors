package com.helmes.sectorsapi.exception;

import lombok.Getter;

@Getter
public class BadCredentialsException extends RuntimeException {

    private final String code;

    public BadCredentialsException(String description, String code) {
        super(description);
        this.code = code;
    }
}
