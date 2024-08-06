package com.backend.library.backend.dto.books;

import java.util.Date;
import java.util.List;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@Getter
@Setter

public class BookSimpDataDTO {

    //atributos recibidos
    private String isbn;
    private String title;
    private String authorFirstName;
    private String authorLastName;
    private int authorId;
    private String editorial;
    private List<Integer> genre;
    private String synopsis;
    private String edition;
    private String price;
    private Date publishDate;
    private int stock;
    private String seller;

}
