package com.backend.library.backend.services.implementations;

import org.springframework.stereotype.Service;

import com.backend.library.backend.dto.users.UserDTO;
import com.backend.library.backend.handlers.exceptions.NotFoundException;
import com.backend.library.backend.handlers.exceptions.UnexpectedException;
import com.backend.library.backend.persistence.entities.Wallet;
import com.backend.library.backend.persistence.repositories.WalletRepository;
import com.backend.library.backend.services.interfaces.IWalletService;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
@RequiredArgsConstructor
public class WalletServiceImpl implements IWalletService {
    private final UserServiceImpl userService;
    private final WalletRepository walletRepository;

    @Override
    public Wallet getWallet() {
        try {
            UserDTO userDTO = userService.getUserDTO();
            Wallet wallet = walletRepository.findBySalerId(userDTO.id()).orElseThrow(
                    () -> {
                        log.error("ERROR: No wallet found for user " + userDTO.surname());
                        throw new NotFoundException("No wallet found for user " + userDTO.surname());
                    });
            return wallet;
        } catch (Exception e) {
            log.error("ERROR: Unexpected ", e);
            throw new UnexpectedException("ERROR: Unexpected error " + e.getMessage());
        }

    }

    @Override
    public Wallet addAmountToWallet(double amount, String sellerId) {
        Wallet existingWallet = walletRepository.findBySalerId(sellerId).orElseThrow(
                () -> {
                    log.error("ERROR: No wallet found for user " + sellerId);
                    throw new NotFoundException("No wallet found for user " + sellerId);
                });
        try {
            existingWallet.setAmount(existingWallet.getAmount() + amount);
            walletRepository.save(existingWallet);
            return existingWallet;
        } catch (Exception e) {
            log.error("ERROR: Unexpected erro adding amout to wallet ", e);
            throw new UnexpectedException("ERROR: Unexpected error adding amout to wallet" + e.getMessage());
        }

    }

    @Override
    public void createWallet(String userId) {
        try {
            Wallet wallet = Wallet.builder()
                    .salerId(userId)
                    .amount(0.0)
                    .build();
            walletRepository.save(wallet);
        } catch (Exception e) {
            log.error("ERROR: Unexpected error creating a wallet ", e);
            throw new UnexpectedException("ERROR: Unexpected error creating a wallet" + e.getMessage());
        }

    }

}
