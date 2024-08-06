package com.backend.library.backend.dto.books;

public record TempPageRequest (
    int pageNumber,
    int pageSize,
    String sorts
){   

}
