package com.helmes.sectorsapi.exception;

import static com.helmes.sectorsapi.exception.ErrorCode.SECTORS_NOT_FOUND;

public class SectorsNotFoundException extends EntityNotFoundException {

    public SectorsNotFoundException(String description) {
        super(description, SECTORS_NOT_FOUND.name());
    }
}
