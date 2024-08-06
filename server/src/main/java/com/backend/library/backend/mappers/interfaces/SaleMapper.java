package com.backend.library.backend.mappers.interfaces;

import java.util.Set;

import com.backend.library.backend.dto.SaleDTO;
import com.backend.library.backend.persistence.entities.Sale;

public interface SaleMapper {

    SaleDTO toDTO(Sale sale);
    Set<SaleDTO> toDTO(Set<Sale> sales);
}
