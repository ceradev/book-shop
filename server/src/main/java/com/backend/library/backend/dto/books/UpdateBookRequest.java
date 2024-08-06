package com.backend.library.backend.dto.books;

import java.util.Set;

import com.backend.library.backend.persistence.entities.Genre;
import com.backend.library.backend.persistence.entities.enums.BookStatus;

import lombok.Builder;

@Builder
public record UpdateBookRequest(

    String cover,

    String title,

    Long authorId,

    Long editorialId,    

    String synopsis,

    String edition,

    Double price,

    BookStatus status,
    
    Set<Genre> genres
    
) {

}
