package com.backend.library.backend.persistence.entities;

import java.util.Set;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Entity
@Builder
@Table(name = "authors")
public class Author {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "author_id")
    private Long id;

    @NotBlank(message = "Author firstName cannot be blank")
    @Column(name = "first_name")
    private String firstName;

    @NotBlank(message = "Author lastName cannot be blank")
    @Column(name = "last_name")
    private String lastName;

    private String biography;

    private String portrait;

    @OneToMany(mappedBy = "author")
    private Set<Book> writedBooks;

}
