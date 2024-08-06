package com.backend.library.backend.services.implementations;

import java.util.Set;

import org.springframework.stereotype.Service;

import com.backend.library.backend.dto.SaleDTO;
import com.backend.library.backend.dto.users.UserDTO;
import com.backend.library.backend.handlers.exceptions.UnexpectedException;
import com.backend.library.backend.mappers.interfaces.SaleMapper;
import com.backend.library.backend.persistence.repositories.SalesRepository;
import com.backend.library.backend.services.interfaces.ISaleService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class SaleServiceImpl implements ISaleService {
    private final UserServiceImpl userService;
    private final SalesRepository salesRepository;
    private final SaleMapper saleMapper;

    @Override
    public Set<SaleDTO> getAllSales() {
        try {
            UserDTO userDTO = userService.getUserDTO();
            return saleMapper.toDTO(salesRepository.findAllBySellerIdOrClientId(userDTO.id()));
        } catch (Exception e) {
            log.error("ERROR: Unexpected error occurred while retrieving sales for user ", e);
            throw new UnexpectedException(
                    "ERROR: Unexpected error occurred while retrieving sales for user ");
        }

    }

    @Override
    public void deleteSale() {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteSale'");
    }

}
