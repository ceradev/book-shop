package com.backend.library.backend.services.interfaces;

import java.util.Set;
import org.springframework.http.ResponseEntity;
import com.backend.library.backend.dto.GenreDTO;
public interface IGenreService {

    public ResponseEntity<Set<GenreDTO>> getAllGenres();

    public ResponseEntity<GenreDTO> saveGenre(GenreDTO genre);
}
