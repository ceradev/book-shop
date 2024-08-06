package com.backend.library.backend.handlers.exceptions;

public class FieldAlreadyExistException extends ConflictException {
    public FieldAlreadyExistException(String msg) {
        super(msg);
    }
}
