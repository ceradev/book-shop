package com.backend.library.backend.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.backend.library.backend.services.interfaces.IWalletService;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/wallet")
public class WalletController {

    public final IWalletService walletService;

    @GetMapping()
    public ResponseEntity<?> getWalletForUser() {
        return ResponseEntity.ok(walletService.getWallet());
    }

}
