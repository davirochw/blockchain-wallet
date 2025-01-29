package com.example.blockchainwallet.domain;

import com.example.blockchainwallet.util.CryptoUtil;

import java.util.List;

public record Block(
        int index,
        String previousHash,
        long timestamp,
        List<Transaction> transactions,
        int nonce,
        String hash
) {
    // Método para calcular o hash do bloco
    public String calculateHash() {
        String data = index + previousHash + timestamp + transactions.toString() + nonce;
        return CryptoUtil.sha256(data);
    }
}