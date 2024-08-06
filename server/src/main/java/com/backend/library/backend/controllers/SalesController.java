package com.backend.library.backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.library.backend.services.interfaces.ISaleService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/sales")
public class SalesController {

    public final ISaleService saleService;

    @GetMapping()
    public ResponseEntity<?> getSalesForUser() {
        return ResponseEntity.ok(saleService.getAllSales());
    }

}
