package com.backend.library.backend.services.interfaces;

import java.util.Set;

import com.backend.library.backend.dto.SaleDTO;

public interface ISaleService {

    Set<SaleDTO> getAllSales();

    void deleteSale();

}
