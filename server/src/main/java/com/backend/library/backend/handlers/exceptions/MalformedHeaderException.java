package com.backend.library.backend.handlers.exceptions;

public class MalformedHeaderException extends BadRequestException {
    public MalformedHeaderException(String msg) {
        super(msg);
    }
}