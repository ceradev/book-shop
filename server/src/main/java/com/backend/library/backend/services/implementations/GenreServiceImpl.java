package com.backend.library.backend.services.implementations;

import java.util.Set;
import java.util.stream.Collectors;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;
import com.backend.library.backend.dto.GenreDTO;
import com.backend.library.backend.mappers.implementations.GenreMapperImpl;
import com.backend.library.backend.mappers.interfaces.GenreMapper;
import com.backend.library.backend.persistence.entities.Genre;
import com.backend.library.backend.persistence.repositories.GenreRepository;
import com.backend.library.backend.services.interfaces.IGenreService;

@Service
public class GenreServiceImpl implements IGenreService {

    private GenreRepository genreRepository;
    private GenreMapperImpl genreMapper;

    public GenreServiceImpl(GenreRepository genreRepository, GenreMapperImpl genreMapper) {
        this.genreRepository = genreRepository;
        this.genreMapper = genreMapper;
    }

    /**
     * Get all genres from the database
     * 
     * @return A ResponseEntity containing a set of GenreDTOs representing all
     *         genres
     */
    @Override
    public ResponseEntity<Set<GenreDTO>> getAllGenres() {
        try {

            // Find all genres in the database
            Set<Genre> genres = genreRepository.findAll().stream()
                    // Collect the genres into a set
                    .collect(Collectors.toSet());

            // Return the set of genres as a JSON response
            return ResponseEntity.status(HttpStatus.OK)
                    // Use the GenreMapper to convert the set of genres to a set of GenreDTOs
                    .body(GenreMapper.toDTO(genres));
        } catch (InternalServerError e) {
            // Return a 500 Internal Server Error response if an internal server error
            // occurs
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

    /**
     * Save a new genre in the database
     * 
     * @param newGenre A GenreDTO representing the new genre to be saved
     * @return A ResponseEntity containing the new GenreDTO with the ID of the saved
     *         genre
     *         and a 200 OK status code if the save operation is successful.
     *         If an internal server error occurs, a 500 Internal Server Error
     *         response is returned.
     */
    @Override
    public ResponseEntity<GenreDTO> saveGenre(GenreDTO newGenre) {
        try {
            // Convert the GenreDTO to a Genre entity
            Genre genre = genreMapper.toGenre(newGenre);

            // Save the genre in the database
            genre = genreRepository.save(genre);

            // Convert the saved genre to a GenreDTO and return it in a
            // 200 OK response
            return ResponseEntity.status(HttpStatus.OK)
                    .body(GenreMapper.toDTO(genre));
        } catch (InternalServerError e) {
            // Return a 500 Internal Server Error response if an internal
            // server error occurs
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(null);
        }
    }

}
