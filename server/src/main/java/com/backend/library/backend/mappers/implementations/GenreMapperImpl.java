package com.backend.library.backend.mappers.implementations;

import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.backend.library.backend.dto.GenreDTO;
import com.backend.library.backend.mappers.interfaces.GenreMapper;
import com.backend.library.backend.persistence.entities.Genre;

@Service
public class GenreMapperImpl implements GenreMapper {

    @Override
    public Genre toGenre(GenreDTO genreDTO) {
        return Genre.builder()
                    .id(genreDTO.id())
                    .name(genreDTO.name())
                    .build(); 
        }

    @Override
    public Set<Genre> toGenre(Set<GenreDTO> genresDTOs) {
        return genresDTOs.stream()
            .map(this::toGenre).collect(Collectors.toSet());
    }

}
