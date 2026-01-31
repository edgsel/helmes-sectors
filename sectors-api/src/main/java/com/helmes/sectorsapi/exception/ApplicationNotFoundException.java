package com.helmes.sectorsapi.exception;

import static com.helmes.sectorsapi.exception.ErrorCode.APPLICATION_NOT_FOUND;

public class ApplicationNotFoundException extends EntityNotFoundException {

    public ApplicationNotFoundException(String description) {
        super(description, APPLICATION_NOT_FOUND);
    }
}
