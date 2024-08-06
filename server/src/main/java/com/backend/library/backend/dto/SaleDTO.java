package com.backend.library.backend.dto;

import java.util.Date;

import com.backend.library.backend.dto.books.BookListDTO;
import com.backend.library.backend.dto.users.UserDTO;

import lombok.Data;

@Data
public class SaleDTO {

    private BookListDTO book;
    private UserDTO client;
    // private UserDTO saler;
    private Date saleDate;
    private Integer quantity;

}