package com.backend.library.backend.services.implementations;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;
import com.backend.library.backend.dto.books.BookDetailsDTO;
import com.backend.library.backend.dto.books.BookListDTO;
import com.backend.library.backend.dto.books.BookSearchCriteriaDTO;
import com.backend.library.backend.dto.books.UpdateBookRequest;
import com.backend.library.backend.handlers.exceptions.ConflictException;
import com.backend.library.backend.handlers.exceptions.NotFoundException;
import com.backend.library.backend.mappers.implementations.BookMapperImpl;
import com.backend.library.backend.mappers.interfaces.BookMapper;
import com.backend.library.backend.persistence.entities.Author;
import com.backend.library.backend.persistence.entities.Book;
import com.backend.library.backend.persistence.entities.CartBook;
import com.backend.library.backend.persistence.entities.FavoriteBook;
import com.backend.library.backend.persistence.entities.Review;
import com.backend.library.backend.persistence.entities.Sale;
import com.backend.library.backend.persistence.entities.enums.BookStatus;
import com.backend.library.backend.persistence.repositories.AuthorRepository;
import com.backend.library.backend.persistence.repositories.BookRepository;
import com.backend.library.backend.persistence.repositories.CartBookRepository;
import com.backend.library.backend.persistence.repositories.FavoriteBookRepository;
import com.backend.library.backend.persistence.repositories.ReviewRepository;
import com.backend.library.backend.persistence.repositories.SalesRepository;
import com.backend.library.backend.persistence.repositories.specifications.BookSpecs;
import com.backend.library.backend.services.interfaces.IBookservice;
import com.backend.library.backend.utils.KeycloakProvider;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import com.backend.library.backend.dto.books.BookSimpDataDTO;

@Slf4j
@Service
public class BookServiceImpl implements IBookservice {

	private static final String BOOK_NOT_FOUND = "Not found the book with ID:";
	private BookMapperImpl bookMapper;
	private BookRepository bookRepository;
	private AuthorRepository authorRepository;
	private FavoriteBookRepository favoriteBookRepository;
	private ReviewRepository reviewRepository;
	private CartBookRepository cartBookRepository;
	private SalesRepository saleRepository;

	public BookServiceImpl(BookRepository bookRepository, BookMapperImpl bookMapper,
			AuthorRepository authorRepository, FavoriteBookRepository favoriteBookRepository,
			ReviewRepository reviewRepository, CartBookRepository cartBookRepository, SalesRepository saleRepository) {
		this.bookRepository = bookRepository;
		this.bookMapper = bookMapper;
		this.authorRepository = authorRepository;
		this.favoriteBookRepository = favoriteBookRepository;
		this.reviewRepository = reviewRepository;
		this.cartBookRepository = cartBookRepository;
		this.saleRepository = saleRepository;
	}

	/**
	 * Get all books
	 * 
	 * @return A ResponseEntity containing a list of published books
	 */
	@Override
	public ResponseEntity<?> getAllBooks() {
		try {
			// Find all books in the database
			List<Book> books = bookRepository.findAll().stream()
					// Filter books that are not published
					.filter(book -> book.getStatus().equals(BookStatus.PUBLISHED))
					// Collect the filtered books into a list
					.collect(Collectors.toList());

			return ResponseEntity.status(HttpStatus.OK)
					// Return the list of published books as a JSON response
					.body(BookMapper.toListDTO(books));
		} catch (InternalServerError e) {
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					// Return an empty string as a response if an internal server error occurs
					.body("");
		}
	}

	/**
	 * Get all books of a seller
	 * 
	 * @param status   The status of the books to be returned
	 * @param request  The HTTP request
	 * @param pageable The pagination parameters
	 * @return A ResponseEntity containing a Page of BookListDTO objects
	 */
	@Override
	public ResponseEntity<?> getBooksBySeller(BookStatus status, HttpServletRequest request, Pageable pageable) {
		try {

			// Get the seller id from the token
			String sellerId = KeycloakProvider.getUserIdFromToken(request);

			// Find all books of the seller with the given status
			Page<BookListDTO> result = bookRepository.findAllBySellerIdAndStatus(status, sellerId, pageable)
					.map(BookMapper::toListDTO);

			return ResponseEntity.status(HttpStatus.OK)
					// Return the list of books as a JSON response
					.body(result);
		} catch (Exception e) {
			log.error(BOOK_NOT_FOUND, e);
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					// Return an error message if an internal server error occurs
					.body(e.getMessage());
		}
	}

	/**
	 * Find a book by its ISBN
	 * 
	 * @param bookISBN The ISBN of the book to find
	 * @return The book object found, or a NotFoundException with a message
	 *         if the book is not found
	 */
	public Book getBook(String bookISBN) {
		return bookRepository.findById(bookISBN)
				.orElseThrow(() -> new NotFoundException(
						String.format("Not found any book with ISBN %s", bookISBN)));
	}

	/**
	 * Find a book by its ISBN
	 * 
	 * @param bookISBN The ISBN of the book to find
	 * @return A ResponseEntity containing a BookDetailsDTO object, or a
	 *         NotFoundException
	 *         with a message if the book is not found
	 */
	@Override
	public ResponseEntity<?> getBookByISBN(String bookISBN) {

		Book book = bookRepository.findById(bookISBN)
				.orElseThrow(() -> new NotFoundException("Not found any book with ISBN " + bookISBN));

		try {
			/**
			 * Return the book as a JSON response
			 */
			return ResponseEntity.status(HttpStatus.OK)
					.body(bookMapper.toDetailsDTO(book));

		} catch (InternalServerError e) {
			/**
			 * Return an error message if an internal server error occurs
			 */
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("The book could not be retrieved.");
		}
	}

	/**
	 * Search books based on the given parameters
	 * 
	 * @param params   The search criteria
	 * @param pageable The pagination parameters
	 * @param status   The status of the books to be returned
	 * @return A ResponseEntity containing a Page of BookListDTO objects
	 */
	@Override
	public ResponseEntity<?> getBooks(BookSearchCriteriaDTO params, Pageable pageable, String status) {
		/**
		 * Create a Specification object to filter the books based on the given
		 * parameters. The Specification object contains the conditions to be
		 * applied to the query. If the parameters are null, the query will
		 * return all books
		 */
		Specification<Book> spec = Specification.where(null);
		if (params != null) {
			if (params.getIsbn() != null && !params.getIsbn().isEmpty()) {
				/**
				 * Add a condition to the query to filter books by title
				 */
				spec = spec.and(BookSpecs.hasIsbn(params.getIsbn()));
			}
			if (params.getTitle() != null && !params.getTitle().isEmpty()) {
				/**
				 * Add a condition to the query to filter books by title
				 */
				spec = spec.and(BookSpecs.hasTitle(params.getTitle()));
			}
			if (params.getAuthorFirstName() != null && !params.getAuthorFirstName().isEmpty()) {
				/**
				 * Add a condition to the query to filter books by author first
				 * name
				 */
				spec = spec.and(BookSpecs.hasAuthorFirstName(params.getAuthorFirstName()));
			}
			if (params.getAuthorLastName() != null && !params.getAuthorLastName().isEmpty()) {
				/**
				 * Add a condition to the query to filter books by author last
				 * name
				 */
				spec = spec.and(BookSpecs.hasAuthorLastName(params.getAuthorLastName()));
			}
			if (params.getEditorial() != null && !params.getEditorial().isEmpty()) {
				/**
				 * Add a condition to the query to filter books by editorial
				 */
				spec = spec.and(BookSpecs.hasEditorial(params.getEditorial()));
			}
			if (params.getSynopsis() != null && !params.getSynopsis().isEmpty()) {
				/**
				 * Add a condition to the query to filter books by synopsis
				 */
				spec = spec.and(BookSpecs.hasSynopsis(params.getSynopsis()));
			}
			if (params.getEdition() != null && !params.getEdition().isEmpty()) {
				/**
				 * Add a condition to the query to filter books by edition
				 */
				spec = spec.and(BookSpecs.hasEdition(params.getEdition()));
			}
			if (params.getMinPrice() != null || params.getMaxPrice() != null) {
				/**
				 * Add a condition to the query to filter books by price range
				 */
				spec = spec.and(BookSpecs.hasPriceInRange(params.getMinPrice(), params.getMaxPrice()));
			}
			if (params.getPublishedDate() != null) {
				/**
				 * Add a condition to the query to filter books by published date
				 */
				spec = spec.and(BookSpecs.hasPublishedDate(params.getPublishedDate()));
			}
			if (params.getGenres() != null && !params.getGenres().isEmpty()) {
				/**
				 * Add a condition to the query to filter books by genres
				 */
				// for (String genre : params.getGenres()) {
				// Optional<Genre> genreOptional = genreRepository.findByName(genre);
				// if(genreOptional!=null && genreOptional.isPresent()){

				// }

				// }

				spec = spec.and(BookSpecs.hasGenres(params.getGenres()));
			}
		}

		/**
		 * Add a condition to the query to filter books by status
		 */
		spec = spec.and((root, query, criteriaBuilder) -> criteriaBuilder.equal(root.get("status"), status));

		/**
		 * Execute the query and return the results as a Page of BookListDTO
		 * objects
		 */
		Page<BookListDTO> result = bookRepository.findAll(spec, pageable)
				.map(BookMapper::toListDTO);
		log.info("Found {} books with the filters provided.", result.getTotalElements());

		return ResponseEntity.status(HttpStatus.OK).body(result);

	}

	/**
	 * Find books based on a query string
	 * 
	 * @param q        The query string to search for books
	 * @param status   The status of the books to be returned
	 * @param pageable The pagination parameters
	 * @return A ResponseEntity containing a Page of BookListDTO objects
	 */
	@Override
	public ResponseEntity<?> getBooksByQuery(String q, BookStatus status, Pageable pageable) {
		if (q == null || q.isEmpty()) {
			/**
			 * Return an error message if the query parameter is null or empty
			 */
			return ResponseEntity.status(HttpStatus.BAD_REQUEST)
					.body("The query parameter is null or empty");
		}

		/**
		 * Find all books in the database that match the given query string and status
		 * and return them as a Page of BookListDTO objects
		 */
		Page<BookListDTO> result = bookRepository.findAllByQueryString(q, status, pageable)
				.map(BookMapper::toListDTO);

		return ResponseEntity.status(HttpStatus.OK).body(result);

	}

	/**
	 * Update a book based on the given ISBN and UpdateBookRequest
	 * 
	 * @param updatedBook The book data to update
	 * @param isbn        The ISBN of the book to update
	 * @return A ResponseEntity containing a BookDetailsDTO object, or a
	 *         NotFoundException
	 *         with a message if the book is not found
	 */
	@Override
	public ResponseEntity<BookDetailsDTO> updateBook(UpdateBookRequest updatedBook, String isbn) {
		try {
			// Search the book by its ISBN
			Book existingBook = bookRepository.findById(isbn)
					.orElseThrow(() -> new NotFoundException(BOOK_NOT_FOUND + isbn));

			// Check if the book has been published
			if (existingBook.getStatus().equals(BookStatus.PUBLISHED)) {
				throw new ResponseStatusException(HttpStatus.CONFLICT, "Published books cannot be updated");
			}

			// Update the book fields with the given data
			if (updatedBook.cover() != null) {
				existingBook.setCover(updatedBook.cover());
			}
			if (updatedBook.title() != null) {
				existingBook.setTitle(updatedBook.title());
			}
			if (updatedBook.authorId() != null) {
				Author author = authorRepository.findById(updatedBook.authorId()).orElseThrow(
						() -> new NotFoundException("Not found the author with ID: " +
								updatedBook.authorId()));
				existingBook.setAuthor(author);
			}
			if (updatedBook.synopsis() != null) {
				existingBook.setSynopsis(updatedBook.synopsis());
			}
			if (updatedBook.edition() != null) {
				existingBook.setEdition(updatedBook.edition());
			}
			if (updatedBook.price() != null) {
				existingBook.setPrice(updatedBook.price());
			}
			if (updatedBook.genres() != null) {
				existingBook.setGenres(updatedBook.genres());
			}
			// Automatically set the status to PENDING
			existingBook.setStatus(BookStatus.PENDING);
			// Save the updated book in the database
			Book bookUpdated = bookRepository.save(existingBook);

			// Return the updated book as a JSON response
			return ResponseEntity.status(HttpStatus.OK)
					.body(bookMapper.toDetailsDTO(bookUpdated));

		} catch (Exception e) {
			log.error("Error updating book: {}", e.getMessage());
			throw e;
		}
	}

	/**
	 * Change the status of a book
	 * 
	 * @param status The new status of the book
	 * @param isbn   The ISBN of the book to update
	 * @return A ResponseEntity containing a BookDetailsDTO object, or a
	 *         NotFoundException with a message if the book is not found
	 */
	@Override
	public ResponseEntity<BookDetailsDTO> changeBookStatus(String status, String isbn) {
		/**
		 * Find the book by its ISBN
		 */
		try {
			Book existingBook = bookRepository.findById(isbn)
					.orElseThrow(() -> new NotFoundException(BOOK_NOT_FOUND + isbn));

			/**
			 * If the book is already published, throw a ConflictException
			 * because published books cannot change their status
			 */
			if (existingBook.getStatus().equals(BookStatus.PUBLISHED)) {
				throw new ConflictException(
						"Published books cannot change the status, delete the book if there are something wrong");
			}
			// Change the status of the book to the status sent
			existingBook.setStatus(BookStatus.valueOf(status));

			// Save the updated book in the database
			Book bookUpdated = bookRepository.save(existingBook);

			// Return the updated book as a JSON response
			return ResponseEntity.status(HttpStatus.OK)
					.body(bookMapper.toDetailsDTO(bookUpdated));

		} catch (Exception e) {
			// Log the exception if it occurs
			log.error("Error updating book: {}", e.getMessage());
			// Throw the exception if it occurs
			throw e;
		}
	}

	/**
	 * Delete a book based on its ISBN
	 * 
	 * @param isbn The ISBN of the book to delete
	 * @return A ResponseEntity with an OK status and a string message if the book
	 *         is
	 *         deleted successfully, or an Internal Server Error with an empty body
	 *         if an
	 *         error occurs
	 */
	@Override
	@Transactional
	public ResponseEntity<?> deleteBook(String isbn) {
		Book existingBook = bookRepository.findById(isbn)
				.orElseThrow(() -> new NotFoundException(
						String.format("Not found the book with ISBN %s", isbn)));

		try {

			favoriteBookRepository.deleteAllByBookIsbn(isbn);
			reviewRepository.deleteAllByBookIsbn(isbn);
			cartBookRepository.deleteAllByBookIsbn(isbn);
			saleRepository.deleteAllByBookIsbn(isbn);

			Optional<FavoriteBook> existingBooks = favoriteBookRepository.findAllByBookIsbn(isbn);
			Optional<CartBook> existingCartBooks = cartBookRepository.findAllByBookIsbn(isbn);
			Optional<Review> existingReviews = reviewRepository.findAllByBookIsbn(isbn);
			Optional<Sale> existingSales = saleRepository.findAllByBookIsbn(isbn);

			if (existingBooks.isEmpty() || existingCartBooks.isEmpty() || existingReviews.isEmpty() || existingSales.isEmpty()) {

				// Delete the book from the database
				bookRepository.delete(existingBook);
				// Return a success message
				return ResponseEntity.status(HttpStatus.OK)
						.body("\"Book deleted successfully\"");
			} else {
				// Return an error message if the book is not deleted
				return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
						.body("\"An error occurred while deleting the book\"");
			}
		} catch (Exception e) {
			// Return an error message if an internal server error occurs
			log.error("Error deleting book: {}", e.getMessage());
			return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
					.body("An error occurred while deleting the book");
		}
	}

	/**
	 * Save a book in the database
	 * 
	 * @param bookJson A JSON string containing the book data to save
	 * @param image    The MultipartFile containing the cover image of the book
	 * @param request  The HttpServletRequest object containing information about
	 *                 the HTTP request
	 * @return A ResponseEntity with a String message indicating the result of the
	 *         operation,
	 *         or an Internal Server Error with an empty body if an error occurs
	 */
	@Override
	public ResponseEntity<String> saveBook(String bookJson, MultipartFile image, HttpServletRequest request) {
		// Create a new Book object to store the book data
		Book book = new Book();

		// Get the book data from the JSON string
		BookSimpDataDTO bookData;
		try {
			ObjectMapper objectMapper = new ObjectMapper();
			bookData = objectMapper.readValue(bookJson, BookSimpDataDTO.class);
		} catch (IOException e) {
			return new ResponseEntity<>("Request json error", HttpStatus.BAD_REQUEST);
		}

		// Check if the book with the same ISBN already exists in the database
		Optional<Book> bookDb = bookRepository.findById(bookData.getIsbn());
		if (bookDb.isPresent()) {
			return new ResponseEntity<>("Book exist", HttpStatus.FOUND);
		}

		// Create the directory to store the book cover image, if it doesn't exist
		File directory = new File("C:\\images\\uploads\\books");
		if (!directory.exists()) {
			directory.mkdirs();
		}

		// Save the cover image to the directory
		try {
			if (!image.isEmpty()) {
				// Get the original filename and extension of the image
				String originalFilename = image.getOriginalFilename();
				String ext = "";
				int i = originalFilename.lastIndexOf('.');
				if (i > 0) {
					ext = originalFilename.substring(i);
				}

				// Use the ISBN of the book as the filename
				String isbn = bookData.getIsbn();
				if (isbn == null || isbn.isEmpty()) {
					return new ResponseEntity<>("ISBN not present", HttpStatus.BAD_REQUEST);
				}

				// Create the filename for the image
				File destinationFile = new File(directory, isbn + ext);

				// Save the image to the file
				image.transferTo(destinationFile);

				// Store the URL of the image in the book
				book.setCover("http://localhost:8089/images/" + isbn + ext);
			} else {
				return new ResponseEntity<>("No se ha proporcionado una imagen.", HttpStatus.BAD_REQUEST);
			}
		} catch (IOException e) {
			return new ResponseEntity<>("Error al guardar la imagen.", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		// Convert the JSON book data to a Book object
		book.setIsbn(bookData.getIsbn());
		book.setTitle(bookData.getTitle());

		// Get the author of the book, or create a new one if it doesn't exist
		Optional<Author> author = authorRepository.findByFirstNameAndLastName(bookData.getAuthorFirstName(),
				bookData.getAuthorLastName());
		try {
			if (!author.isEmpty()) {
				book.setAuthor(author.get());
			} else {
				Author newAuthor = new Author();
				newAuthor.setFirstName(bookData.getAuthorFirstName());
				newAuthor.setLastName(bookData.getAuthorLastName());

				authorRepository.save(newAuthor);
				author = authorRepository.findByFirstNameAndLastName(bookData.getAuthorFirstName(),
						bookData.getAuthorLastName());
				if (author.isEmpty()) {
					return new ResponseEntity<>("El autor no existe", HttpStatus.INTERNAL_SERVER_ERROR);
				}
				book.setAuthor(author.get());
			}
		} catch (Exception e) {
			return new ResponseEntity<>("Error con el autor", HttpStatus.INTERNAL_SERVER_ERROR);
		}

		// Set the other book data fields
		book.setEditorial(bookData.getEditorial());
		book.setSynopsis(bookData.getSynopsis());
		book.setEdition(bookData.getEdition());
		book.setPrice(Double.valueOf(bookData.getPrice()));
		book.setPublishDate(bookData.getPublishDate());
		book.setStock(bookData.getStock());
		book.setSellerId(KeycloakProvider.getUserIdFromToken(request));

		// Save the book to the database
		try {
			bookRepository.save(book);
			return new ResponseEntity<>("Se ha añadido el libro con ISBN " + bookData.getIsbn(), HttpStatus.CREATED);
		} catch (Exception e) {
			return new ResponseEntity<>("Error interno " + bookData.getIsbn(), HttpStatus.INTERNAL_SERVER_ERROR);
		}
	}

}