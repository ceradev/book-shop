package com.backend.library.backend.mappers.interfaces;


import java.util.Set;
import java.util.stream.Collectors;

import com.backend.library.backend.dto.GenreDTO;
import com.backend.library.backend.persistence.entities.Genre;

public interface GenreMapper {
    static GenreDTO toDTO(Genre genre){
            return GenreDTO.builder()
                        .id(genre.getId())
                        .name(genre.getName())
                        .build(); 
    }
    static Set<GenreDTO> toDTO(Set<Genre> genres){
        return genres.stream().map(GenreMapper::toDTO).collect(Collectors.toSet());
    }
    Genre toGenre(GenreDTO genreDTO);
    Set<Genre> toGenre(Set<GenreDTO> genresDTOs);
}
