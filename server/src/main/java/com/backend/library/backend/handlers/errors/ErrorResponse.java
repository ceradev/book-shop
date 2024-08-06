package com.backend.library.backend.handlers.errors;

import org.springframework.http.HttpStatus;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ErrorResponse {
    private Integer code;
    private HttpStatus status;
    private String message;
    private String path;

    public ErrorResponse(HttpStatus status, Exception ex, String path) {
        this.code = status.value();
        this.status = status;
        this.message = ex.getMessage();
        this.path = path;
    }

}
