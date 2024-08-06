package com.backend.library.backend.services.interfaces;

import com.backend.library.backend.persistence.entities.Wallet;

public interface IWalletService {

    Wallet getWallet();

    Wallet addAmountToWallet(double amount, String sellerId);

    void createWallet(String userId);
}
