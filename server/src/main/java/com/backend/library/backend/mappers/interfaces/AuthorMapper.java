package com.backend.library.backend.mappers.interfaces;

import com.backend.library.backend.dto.authors.AuthorDetailsDTO;
import com.backend.library.backend.dto.authors.AuthorListDTO;
import com.backend.library.backend.dto.authors.RequestAuthorDTO;
import com.backend.library.backend.persistence.entities.Author;

public interface AuthorMapper {
    public AuthorDetailsDTO toDetailDTO(Author author);

    public Author toAuthor(AuthorDetailsDTO authorDTO);

    public Author authorRequestDTOtoAuthor(RequestAuthorDTO authorDTO);

    public static AuthorListDTO toList(Author author) {
        return AuthorListDTO.builder()
                .authorId(author.getId())
                .firstName(author.getFirstName())
                .lastName(author.getLastName())
                .build();
    }
}
