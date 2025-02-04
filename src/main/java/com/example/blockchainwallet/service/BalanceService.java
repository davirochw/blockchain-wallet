package com.example.blockchainwallet.service;

import com.example.blockchainwallet.domain.Transaction;
import org.springframework.stereotype.Service;
import java.util.*;

@Service
public class BalanceService {
    private final Map<String, Double> balances = new HashMap<>(); // Chave: endereço (publicKey)

    public void updateBalances(List<Transaction> transactions) {
        for (Transaction transaction : transactions) {
            // Deduz do remetente
            balances.put(transaction.sender(),
                    balances.getOrDefault(transaction.sender(), 0.0) - transaction.amount());

            // Adiciona ao destinatário
            balances.put(transaction.receiver(),
                    balances.getOrDefault(transaction.receiver(), 0.0) + transaction.amount());
        }
    }

    public double getBalance(String address) {
        return balances.getOrDefault(address, 0.0);
    }
}