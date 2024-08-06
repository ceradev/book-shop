package com.backend.library.backend.dto.addresses;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record CreateRequestAddress(

        @NotBlank(message = "Address road cannot be blank") String road,

        @NotNull(message = "Address postalCode cannot be null") Integer postalCode,

        @NotNull(message = "Address num cannot be null") Integer num,

        @NotBlank(message = "Address city cannot be blank") String city,

        @NotBlank(message = "Address country cannot be blank") String country) {

}
