package com.backend.library.backend.services.implementations;

import java.util.List;
import java.util.stream.Collectors;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.backend.library.backend.dto.authors.AuthorDetailsDTO;
import com.backend.library.backend.dto.authors.RequestAuthorDTO;
import com.backend.library.backend.handlers.exceptions.BadRequestException;
import com.backend.library.backend.handlers.exceptions.NotFoundException;
import com.backend.library.backend.mappers.implementations.AuthorMapperImpl;
import com.backend.library.backend.persistence.entities.Author;
import com.backend.library.backend.persistence.repositories.AuthorRepository;
import com.backend.library.backend.services.interfaces.IAuthorService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class AuthorServiceImpl implements IAuthorService {

    private AuthorRepository authorRepository;
    private AuthorMapperImpl authorMapper;

    public AuthorServiceImpl(AuthorRepository authorRepository, AuthorMapperImpl authorMapper) {
        this.authorRepository = authorRepository;
        this.authorMapper = authorMapper;
    }

    /**
     * Get all authors in the database.
     *
     * @return a list of authors as {@link AuthorDetailsDTO}
     */
    @Override
    public List<AuthorDetailsDTO> getAll() {
        return authorRepository.findAll().stream()
                .map(authorMapper::toDetailDTO)
                .collect(Collectors.toList());
    }


    /**
     * Get an author by its id.
     *
     * @param id the id of the author
     * @return the author with the given id
     * @throws BadRequestException if the id is null
     * @throws NotFoundException if the author is not found
     */
    @Override
    public AuthorDetailsDTO getAuthor(Long id) {
        if (id == null) {
            log.error("The Id cannot be null");
            throw new BadRequestException("The ID cannot be null");
        }
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Not Author found with id " + id));
        return authorMapper.toDetailDTO(author);
    }

    /**
     * Add a new author to the database.
     *
     * @param author the new author to be added
     * @return the new author
     * @throws IllegalArgumentException if any error occurs while adding the new author
     */
    @Override
    @Transactional
    public RequestAuthorDTO add(RequestAuthorDTO author) {

        try {
            // Map the RequestAuthorDTO to an Author entity and save it in the database
            Author newAuthor = authorMapper.authorRequestDTOtoAuthor(author);
            authorRepository.save(newAuthor);
            // Return the new author as a RequestAuthorDTO
            return author;

        } catch (Exception e) {
            // If an error occurs while adding the new author, log the error and
            // throw an IllegalArgumentException
            log.error("Error creating new author: ", e.getMessage());
            throw new IllegalArgumentException("Error creating new author: " + e.getMessage());
        }

    }


    /**
     * Update an existing author in the database.
     *
     * @param requestAuthorDTO the updated author data
     * @param id the id of the author to be updated
     * @return the updated author
     * @throws BadRequestException if the id is null
     * @throws IllegalArgumentException if any error occurs while updating the author
     */
    @Override
    @Transactional
    public RequestAuthorDTO update(RequestAuthorDTO requestAuthorDTO, Long id) {
        if (id == null) {
            log.error("The Id cannot be null");
            throw new BadRequestException("The ID cannot be null");
        }

        // Get the existing author to update
        AuthorDetailsDTO existingAuthor = this.getAuthor(id);

        // Create a new AuthorDetailsDTO with the updated data
        AuthorDetailsDTO authorUpdated = AuthorDetailsDTO.builder()
                .authorId(existingAuthor.authorId())
                .biography(requestAuthorDTO.getPortrait() != null ? requestAuthorDTO.getPortrait()
                        : existingAuthor.biography())
                .firstName(requestAuthorDTO.getFirstName() != null ? requestAuthorDTO.getFirstName()
                        : existingAuthor.firstName())
                .lastName(requestAuthorDTO.getLastName() != null ? requestAuthorDTO.getLastName()
                        : existingAuthor.lastName())
                .biography(requestAuthorDTO.getBiography() != null ? requestAuthorDTO.getBiography()
                        : existingAuthor.biography())
                .build();

        try {
            // Save the updated author in the database
            authorRepository.save(authorMapper.toAuthor(authorUpdated));
            return requestAuthorDTO;

        } catch (Exception e) {
            // If any error occurs while updating the author, log the error and
            // throw an IllegalArgumentException
            log.error("Unexpected Error updating Author: ", e.getMessage());
            throw new IllegalArgumentException("Unexpected Error updating Author: " + e.getMessage());
        }

    }

    /**
     * Deletes an author by its id
     * 
     * @param id the id of the author to be deleted
     * @throws BadRequestException if the id is null
     * @throws IllegalArgumentException if any error occurs while deleting the author
     */
    @Override
    public void delete(Long id) {
        if (id == null) {
            log.error("The Id cannot be null");
            throw new BadRequestException("The ID cannot be null");
        }
        try {
            authorRepository.deleteById(id);
        } catch (Exception e) {
            // If any error occurs while deleting the author, log the error and
            // throw an IllegalArgumentException
            log.error("Unexpected Error deleting the Author: ", e.getMessage());
            throw new IllegalArgumentException("Unexpected Error deleting the Author: " + e.getMessage());
        }
    }



    

}
