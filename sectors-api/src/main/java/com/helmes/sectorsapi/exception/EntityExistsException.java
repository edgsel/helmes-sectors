package com.helmes.sectorsapi.exception;

import lombok.Getter;

@Getter
public class EntityExistsException extends RuntimeException {

    private final String code;

    public EntityExistsException(String description, String code) {
        super(description);
        this.code = code;
    }
}
