package com.helmes.sectorsapi.exception;

import lombok.Getter;

@Getter
public class EntityNotFoundException extends RuntimeException {

    private final String code;

    public EntityNotFoundException(String description, String code) {
        super(description);
        this.code = code;
    }
}
