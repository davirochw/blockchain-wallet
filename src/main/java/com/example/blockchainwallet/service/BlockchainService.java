package com.example.blockchainwallet.service;

import com.example.blockchainwallet.domain.Block;
import com.example.blockchainwallet.domain.Transaction;
import com.example.blockchainwallet.util.CryptoUtil;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class BlockchainService {
    private List<Block> blockchain = new ArrayList<>();
    private List<Transaction> pendingTransactions = new ArrayList<>();
    private int difficulty = 4; // Dificuldade do Proof of Work (ex: hash deve começar com "0000")

    // Construtor: cria o bloco genesis
    public BlockchainService() {
        blockchain.add(createGenesisBlock());
    }

    private Block createGenesisBlock() {
        return new Block(0, "0", System.currentTimeMillis(), new ArrayList<>(), 0, "genesis-hash");
    }

    // Minera um novo bloco (Proof of Work)
    public Block mineBlock() {
        Block lastBlock = blockchain.get(blockchain.size() - 1);
        int nonce = 0;
        String hash;
        String target = new String(new char[difficulty]).replace('\0', '0');

        // loop do PoW:
        do {
            nonce++;
            hash = CryptoUtil.sha256(
                    lastBlock.index() +
                            lastBlock.previousHash() +
                            lastBlock.timestamp() +
                            pendingTransactions.toString() +
                            nonce
            );
        } while (!hash.substring(0, difficulty).equals(target));

        Block newBlock = new Block(
                lastBlock.index() + 1,
                lastBlock.hash(),
                System.currentTimeMillis(),
                pendingTransactions,
                nonce,
                hash
        );

        blockchain.add(newBlock);
        pendingTransactions = new ArrayList<>(); // Limpa transações pendentes
        return newBlock;
    }

    // Valida a integridade do blockchain
    public boolean isChainValid() {
        // Verifica o bloco genesis
        Block genesis = blockchain.get(0);
        if (!genesis.hash().equals("genesis-hash") || genesis.index() != 0) {
            return false;
        }

        // Verifica os demais blocos
        for (int i = 1; i < blockchain.size(); i++) {
            Block currentBlock = blockchain.get(i);
            Block previousBlock = blockchain.get(i - 1);

            // Valida o hash do bloco atual
            String calculatedHash = currentBlock.calculateHash();
            if (!currentBlock.hash().equals(calculatedHash)) {
                return false;
            }

            // Valida a referência ao bloco anterior
            if (!currentBlock.previousHash().equals(previousBlock.hash())) {
                return false;
            }

            // Valida o Proof of Work (hash começa com zeros)
            String target = new String(new char[difficulty]).replace('\0', '0');
            if (!currentBlock.hash().substring(0, difficulty).equals(target)) {
                return false;
            }
        }
        return true;
    }

    // Adiciona uma transação à lista pendente
    public void addTransaction(Transaction transaction) {
        pendingTransactions.add(transaction);
    }

    // Getters
    public List<Block> getBlockchain() {
        return blockchain;
    }
}