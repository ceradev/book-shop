package com.backend.library.backend.services.implementations;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException.InternalServerError;

import com.backend.library.backend.dto.RequestUserIdBookIsbn;
import com.backend.library.backend.dto.review.CreateReviewDTO;
import com.backend.library.backend.dto.review.ReviewBookDTO;
import com.backend.library.backend.dto.review.ReviewDTO;
import com.backend.library.backend.handlers.exceptions.ConflictException;
import com.backend.library.backend.handlers.exceptions.NotFoundException;
import com.backend.library.backend.mappers.interfaces.ReviewMapper;
import com.backend.library.backend.persistence.entities.Book;
import com.backend.library.backend.persistence.entities.Review;
import com.backend.library.backend.persistence.entities.Sale;
import com.backend.library.backend.persistence.repositories.BookRepository;
import com.backend.library.backend.persistence.repositories.ReviewRepository;
import com.backend.library.backend.persistence.repositories.SalesRepository;
import com.backend.library.backend.services.interfaces.IReviewService;
import com.backend.library.backend.utils.KeycloakProvider;

import jakarta.servlet.http.HttpServletRequest;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class ReviewServiceImpl implements IReviewService {
    private ReviewRepository reviewRepository;
    private BookServiceImpl bookServiceImpl;
    private SalesRepository salesRepository;
    private BookRepository bookRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository, BookServiceImpl bookServiceImpl,
            SalesRepository salesRepository, BookRepository bookRepository) {
        this.reviewRepository = reviewRepository;
        this.bookServiceImpl = bookServiceImpl;
        this.salesRepository = salesRepository;
        this.bookRepository = bookRepository;
    }

    @Override
    public ResponseEntity<List<ReviewDTO>> getReviewsByBook(String bookIsbn) {
        try {
            List<ReviewDTO> reviews = reviewRepository.findByBookIsbn(bookIsbn).stream().map(ReviewMapper::toDTO)
                    .collect(java.util.stream.Collectors.toList());

            return ResponseEntity.status(HttpStatus.OK).body(reviews);

        } catch (InternalServerError e) {
            log.error("Unexpected Error getting reviews from a book: ", e.getMessage());
            throw new IllegalArgumentException("Unexpected Error getting reviews from a book: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<ReviewDTO> addReview(CreateReviewDTO createReviewDTO, HttpServletRequest request) {

        try {
            String userID = KeycloakProvider.getUserIdFromToken(request);
            Optional<Review> schordingerReview = reviewRepository.findByBookIsbnAndUserId(createReviewDTO.bookIsbn(),
                    userID);
            if (schordingerReview.isPresent()) {
                throw new ConflictException("Review already exists");
            }

            Boolean isPurchased = false;
            Optional<Sale> sale = salesRepository.findByClientIdAndBookIsbn(userID, createReviewDTO.bookIsbn());
            if (sale.isPresent()) {
                isPurchased = true;
            }

            Book book = bookServiceImpl.getBook(createReviewDTO.bookIsbn());
            Review review = reviewRepository.save(
                    Review.builder()
                            .book(book)
                            .userId(userID)
                            .rating(createReviewDTO.rating())
                            .comment(createReviewDTO.comment())
                            .isPurchased(isPurchased)
                            .build());

            //calculo la media
            calculateMean(review);

            return ResponseEntity.status(HttpStatus.OK).body(ReviewMapper.toDTO(review));
        } catch (InternalServerError e) {
            log.error("Unexpected Error adding review: ", e.getMessage());
            throw new IllegalArgumentException("Unexpected Error adding review: " + e.getMessage());
        }

    }

    @Override
        public ResponseEntity<ReviewDTO> deleteReview(String isbn, HttpServletRequest request) {
            
        try {
            RequestUserIdBookIsbn requestReview=new RequestUserIdBookIsbn(KeycloakProvider.getUserIdFromToken(request), isbn);
            Review review = reviewRepository.findByBookIsbnAndUserId(requestReview.bookIsbn(), requestReview.userId()).orElseThrow(() -> new NotFoundException("Review not found"));
            reviewRepository.delete(review);
            
            //calculo la media
            calculateMean(review);

            return ResponseEntity.status(HttpStatus.OK).body(ReviewMapper.toDTO(review));
		} catch (InternalServerError e) {
			log.error("Unexpected Error deleting review: ", e.getMessage());
            throw new IllegalArgumentException("Unexpected Error deleting review: " + e.getMessage());
        }
    }

    @Override
    public ResponseEntity<ReviewDTO> modReview(CreateReviewDTO createReviewDTO, HttpServletRequest request) {
        try {
            String userId = KeycloakProvider.getUserIdFromToken(request);
            Optional<Review> review = reviewRepository.findByBookIsbnAndUserId(createReviewDTO.bookIsbn(), userId);

            if (review.isPresent()) {
                review.get().setRating(createReviewDTO.rating());
                review.get().setComment(createReviewDTO.comment());
            }

            reviewRepository.save(review.get());
            
            //calculo la media
            calculateMean(review.get());

            return ResponseEntity.status(HttpStatus.OK).body(ReviewMapper.toDTO(review.get()));
        } catch (Exception e) {
            throw new IllegalArgumentException("Unexpected Error adding review: " + e.getMessage());

        }
    }


    //método para el admin, borra cualquier review
    @Override
    public ResponseEntity<ReviewDTO> deleteReviewById(Long id) {
        try{
            Optional<Review> review = reviewRepository.findById(id);
            
            double mean=0;

            if(review.get()!=null){

                //borro la review
                reviewRepository.deleteById(review.get().getId());
                
                //calculo la media
                calculateMean(review.get());
            }

            return ResponseEntity.status(HttpStatus.OK).body(ReviewMapper.toDTO(review.get()));

        }catch(Exception e){
            log.error("Unexpected Error deleting review: ", e.getMessage());
            throw new IllegalArgumentException("Unexpected Error deleting review: " + e.getMessage());
        }
    }

    
    @Override
    public ResponseEntity<List<ReviewBookDTO>> getReviewsByUser(HttpServletRequest request) {
        try{
            String userId=KeycloakProvider.getUserIdFromToken(request);
            Optional<List<Review>> reviews = reviewRepository.findByUserId(userId);

            if(reviews.get()!=null){
                Set<Review> conjunto = new HashSet<>(reviews.get());
                return ResponseEntity.status(HttpStatus.OK).body(ReviewMapper.toDTOBook(conjunto));
            }
            
            //esto hay que arreglarlo
            return null;
            

        }catch(Exception e){
            log.error("Unexpected Error deleting review: ", e.getMessage());
            throw new IllegalArgumentException("Unexpected Error deleting review: " + e.getMessage());
        }
        
    }


    //obtiene todas las reviews que se han hecho a los libros del vendedor que hace la solicitud
    @Override
    public ResponseEntity<List<ReviewBookDTO>> getReviewBySeller(HttpServletRequest request) {

        try{
            String userId=KeycloakProvider.getUserIdFromToken(request);

            List<Review> reviews = reviewRepository.findReviewsSellerBooks(userId);
            
            Set<Review> conjunto = new HashSet<>(reviews);
            return ResponseEntity.status(HttpStatus.OK).body(ReviewMapper.toDTOBook(conjunto));
        }catch(Exception e){
            log.error("Unexpected Error getting reviews: ", e.getMessage());
            throw new IllegalArgumentException("Unexpected Error getting reviews: " + e.getMessage());

        }
        

        
        
        
    }



    //METODOS COMPARTIDOS

   
    /*INPUT: la review el la que hay cambios */
    private void calculateMean(Review review){

        double mean = 0;
        List<Review> reviews;

        //obtengo el libro de la review
        Optional<Book> book = bookRepository.findById(review.getBook().getIsbn());
        
        //obtengo todas las reviews con el isbn del libro
        reviews=reviewRepository.findByBookIsbn(book.get().getIsbn());

        //si existen reviews hago el sumatorio
        if(!reviews.isEmpty()){ 
            for (Review reviewElement: reviews) {
                mean=mean+reviewElement.getRating();
            }   
        }

        //si el tamaño de la lista es mayor que cero calculo la media 
        if(reviews.size()>0){
            mean=mean/reviews.size();
        }else{ //si no la media es cero
            mean=0;
        }

        //guardo datos en el libro        
        book.get().setNReviews(reviews.size());
        book.get().setReviewMean(mean);
        bookRepository.save(book.get());

    }


    

    

    

}

    

