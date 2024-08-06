package com.backend.library.backend.dto.books;

import java.util.Date;

import lombok.*;

@Builder
public record bookSimpDTO(
        String isbn,
        String title,
        String author,
        String authorId,
        String editorial,
        int genre,
        String synopsis,
        int edition,
        boolean isPublished,
        String price,
        Date publishDate) {

}
