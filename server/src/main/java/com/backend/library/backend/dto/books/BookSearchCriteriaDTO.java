package com.backend.library.backend.dto.books;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class BookSearchCriteriaDTO {
    String title;
    String isbn;
    String authorFirstName;
    String authorLastName;
    String editorial;
    String synopsis;
    String edition;
    Double minPrice;
    Double maxPrice;
    LocalDateTime publishedDate;
    List<String> genres;
    TempPageRequest pageable;
}
