package com.backend.library.backend.handlers.exceptions;

public class FieldInvalidException extends BadRequestException {
    public FieldInvalidException(String msg) {
        super(msg);
    }

}
