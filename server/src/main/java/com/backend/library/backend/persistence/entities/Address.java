package com.backend.library.backend.persistence.entities;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Builder
@Table(name = "addresses")
public class Address { 

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Address road cannot be blank")
    @Column(name="address_road")
    private String road;

    @Column(name="postal_code")
    private int postalCode; 

    @Column(name="address_num")
    private int num;

    @NotBlank(message = "Address city cannot be blank")
    @Column(name="address_city")
    private String city;

    @NotBlank(message = "Address country cannot be blank")
    @Column(name="address_country")
    private String country;
}
