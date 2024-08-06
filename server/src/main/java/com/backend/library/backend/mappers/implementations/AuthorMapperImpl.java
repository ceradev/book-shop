package com.backend.library.backend.mappers.implementations;

import org.springframework.stereotype.Service;

import com.backend.library.backend.dto.authors.AuthorDetailsDTO;
import com.backend.library.backend.dto.authors.RequestAuthorDTO;
import com.backend.library.backend.mappers.interfaces.AuthorMapper;
import com.backend.library.backend.persistence.entities.Author;

@Service
public class AuthorMapperImpl implements AuthorMapper {

    @Override
    public AuthorDetailsDTO toDetailDTO(Author author) {
        return AuthorDetailsDTO.builder()
                .authorId(author.getId())
                .firstName(author.getFirstName())
                .lastName(author.getLastName())
                .biography(author.getBiography())
                .portrait(author.getPortrait())
                .build();
    }

    @Override
    public Author toAuthor(AuthorDetailsDTO authorDTO) {
        return Author.builder()
                .id(authorDTO.authorId())
                .firstName(authorDTO.firstName())
                .lastName(authorDTO.lastName())
                .biography(authorDTO.biography())
                .portrait(authorDTO.portrait())
                .build();
    }

    @Override
    public Author authorRequestDTOtoAuthor(RequestAuthorDTO authorDTO) {
        return Author.builder()
                .firstName(authorDTO.getFirstName())
                .lastName(authorDTO.getFirstName())
                .biography(authorDTO.getBiography())
                .portrait(authorDTO.getPortrait())
                .build();
    }

}
