package com.example.blockchainwallet.domain;

public record Transaction(
        String sender,
        String receiver,
        double amount,
        String signature
) {
    // Método para gerar os dados da transação (usado na assinatura)
    public String getData() {
        return sender + receiver + amount;
    }
}