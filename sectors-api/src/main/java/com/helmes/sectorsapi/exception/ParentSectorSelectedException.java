package com.helmes.sectorsapi.exception;

import lombok.Getter;

import static com.helmes.sectorsapi.exception.ErrorCode.PARENT_SECTOR_SELECTED;

@Getter
public class ParentSectorSelectedException extends RuntimeException {

    private final String code;

    public ParentSectorSelectedException(String description) {
        super(description);
        this.code = PARENT_SECTOR_SELECTED.name();
    }
}
