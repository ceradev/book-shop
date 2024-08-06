package com.backend.library.backend.persistence.entities;

import java.util.Date;
import java.util.List;
import java.util.Set;

import com.backend.library.backend.persistence.entities.enums.BookStatus;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "books")
public class Book {

    @Id
    private String isbn;

    private String cover; // ¿Guardar imagen directamente en base de datos o guardar ruta?

    @NotBlank(message = "Book title cannot be blank")
    private String title;

    private String synopsis;

    @NotBlank(message = "Book edition cannot be blank")
    private String edition;

    @Builder.Default
    @NotNull(message = "Book status cannot be null")
    @Enumerated(EnumType.STRING)
    private BookStatus status = BookStatus.PENDING;

    @NotNull(message = "Book price cannot be null")
    private Double price;

    @NotNull(message = "cannot be null")
    private String editorial;

    @Builder.Default
    private Date publishDate = null;

    @NotNull(message = "Stock cannot be null")
    private Integer stock;

    private Double reviewMean;

    private Integer nReviews;

    // @NotNull(message = "SellerId cannot be null")
    private String sellerId;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(name = "books_genres", joinColumns = @JoinColumn(name = "book_id"), inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private Set<Genre> genres;

    @Builder.Default
    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Sale> sales = null;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "author_id")
    private Author author;

    @OneToMany(mappedBy = "book", fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Set<Review> reviews;

    @OneToMany(mappedBy = "book")
    private List<CartBook> cartBook;

    @OneToMany(mappedBy = "book")
    private List<OrderItem> orderItems;

}
