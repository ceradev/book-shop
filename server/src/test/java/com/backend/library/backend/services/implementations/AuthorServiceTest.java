package com.backend.library.backend.services.implementations;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.boot.test.context.SpringBootTest;

import com.backend.library.backend.dto.authors.AuthorDetailsDTO;
import com.backend.library.backend.dto.authors.RequestAuthorDTO;
import com.backend.library.backend.mappers.implementations.AuthorMapperImpl;
import com.backend.library.backend.persistence.entities.Author;
import com.backend.library.backend.persistence.repositories.AuthorRepository;

@SpringBootTest
class AuthorServiceTest {

	@Mock
	AuthorRepository authorRepository;
	@Mock
	AuthorMapperImpl mapper;

	@InjectMocks
	AuthorServiceImpl authorService;

	@BeforeEach
	void setUp() {

		when(mapper.authorRequestDTOtoAuthor(Mockito.any(RequestAuthorDTO.class)))
				.thenReturn(new Author());

		when(mapper.toDetailDTO(Mockito.any(Author.class)))
				.thenReturn(AuthorDetailsDTO.builder().build());
	}

	@Test
	@DisplayName("Test to add a new author")
	void authorService_add_returnsRequestAuthorDTO() {
		Author author = Author.builder()
				.id(1L)
				.biography("some biografe text")
				.firstName("khallifa")
				.lastName("boulbayem")
				.build();
		RequestAuthorDTO requestAuthorDTO = RequestAuthorDTO.builder()
				.biography("some biografe text")
				.firstName("khallifa")
				.lastName("boulbayem")
				.build();
		when(authorRepository.save(Mockito.any(Author.class))).thenReturn(author);
		RequestAuthorDTO newAuthor = authorService.add(requestAuthorDTO);
		Assertions.assertThat(newAuthor).isNotNull();
	}

	@Test
	@DisplayName("Test get author by ID")
	void authorService_getAuthorById_returnsAuthorDetailsDTO() {
		Author author = Author.builder()
				.id(1L)
				.biography("some biografe text")
				.firstName("khallifa")
				.lastName("boulbayem")
				.build();
		when(authorRepository.findById(1L)).thenReturn(Optional.ofNullable(author));
		AuthorDetailsDTO findedAuthor = authorService.getAuthor(1L);
		Assertions.assertThat(findedAuthor).isNotNull();
	}

	@Test
	@DisplayName("Test to update a specific author")
	void authorService_update_returnsRequestAuthorDTO() {
		Author author = Author.builder()
				.id(1L)
				.biography("existing biography")
				.firstName("Existing")
				.lastName("Author")
				.build();

		RequestAuthorDTO updatedAuthorDTO = RequestAuthorDTO.builder()
				.biography("updated biography")
				.firstName("Updated")
				.lastName("Author")
				.build();

		when(authorRepository.findById(1L)).thenReturn(Optional.ofNullable(author));
		when(authorRepository.save(Mockito.any(Author.class))).thenReturn(author);

		RequestAuthorDTO updatedAuthor = authorService.update(updatedAuthorDTO, author.getId());

		Assertions.assertThat(updatedAuthor).isNotNull();

		Assertions.assertThat(updatedAuthor.getBiography()).isEqualTo(updatedAuthorDTO.getBiography());
		Assertions.assertThat(updatedAuthor.getFirstName()).isEqualTo(updatedAuthorDTO.getFirstName());
		Assertions.assertThat(updatedAuthor.getLastName()).isEqualTo(updatedAuthorDTO.getLastName());
	}

	@Test
	@DisplayName("Test to delete a specific author")
	void authorService_delete_returnsNothing() {
		Author existingAuthor = Author.builder()
				.id(1L)
				.biography("existing biography")
				.firstName("Khalifa")
				.lastName("Boulbayem")
				.build();
		when(authorRepository.findById(existingAuthor.getId())).thenReturn(Optional.ofNullable(existingAuthor));

		assertAll(() -> authorService.delete(existingAuthor.getId()));
	}

	@Test
	@DisplayName("Test to render all authors")
	void authorService_getAll_returnsListOfAuthorDetailsDTO() {
		// AuthorDetailsDTO authorResponse = Mockito.mock(AuthorDetailsDTO.class);
		List<Author> authors = Mockito.mock(List.class);

		when(authorRepository.findAll()).thenReturn(authors);
		List<AuthorDetailsDTO> saveAuthorDetailsDTO = authorService.getAll();

		Assertions.assertThat(saveAuthorDetailsDTO).isNotNull();
	}

}
