package com.backend.library.backend.mappers.implementations;

import java.util.HashSet;
import java.util.Set;

import org.springframework.stereotype.Service;

import com.backend.library.backend.dto.SaleDTO;
import com.backend.library.backend.mappers.interfaces.BookMapper;
import com.backend.library.backend.mappers.interfaces.IUserAddressMapper;
import com.backend.library.backend.mappers.interfaces.SaleMapper;
import com.backend.library.backend.mappers.interfaces.UserMapper;
import com.backend.library.backend.persistence.entities.Sale;

@Service
public class SaleMapperImpl implements SaleMapper {

    BookMapperImpl bookMapper;

    UserMapperImpl userMapper;

    IUserAddressMapper userAddressMapper;

    public SaleMapperImpl(BookMapperImpl bookMapper, UserMapperImpl userMapper, IUserAddressMapper userAddressMapper) {
        this.bookMapper = bookMapper;
        this.userMapper = userMapper;
        this.userAddressMapper = userAddressMapper;
    }

    public SaleDTO toDTO(Sale sale) {
        SaleDTO saleDTO = new SaleDTO();
        saleDTO.setQuantity(sale.getQuantity());
        saleDTO.setBook(BookMapper.toListDTO(sale.getBook()));
        saleDTO.setClient(UserMapper.fromKeycloak(sale.getClientId()));
        saleDTO.setSaleDate(sale.getSaleDate());
        return saleDTO;
    }

    public Set<SaleDTO> toDTO(Set<Sale> sales) {
        Set<SaleDTO> saleDTOs = new HashSet<>();
        for (Sale sale : sales) {
            SaleDTO saleDTO = toDTO(sale);
            saleDTOs.add(saleDTO);
        }
        return saleDTOs;
    }

}
