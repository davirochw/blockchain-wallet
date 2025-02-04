package com.example.blockchainwallet.domain;

import jakarta.persistence.*;
import java.util.List;

@Entity
public class BlockEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private int index;
    private String previousHash;
    private long timestamp;
    @OneToMany(cascade = CascadeType.ALL)
    private List<Transaction> transactions;
    private int nonce;
    private String hash;

    // Construtor padrão (obrigatório para JPA)
    public BlockEntity() {}

    // Construtor com parâmetros
    public BlockEntity(int index, String previousHash, long timestamp, List<Transaction> transactions, int nonce, String hash) {
        this.index = index;
        this.previousHash = previousHash;
        this.timestamp = timestamp;
        this.transactions = transactions;
        this.nonce = nonce;
        this.hash = hash;
    }

    // Getters e Setters (ou use Lombok @Data)
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public int getIndex() {
        return index;
    }

    public void setIndex(int index) {
        this.index = index;
    }

    public String getPreviousHash() {
        return previousHash;
    }

    public void setPreviousHash(String previousHash) {
        this.previousHash = previousHash;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }

    public List<Transaction> getTransactions() {
        return transactions;
    }

    public void setTransactions(List<Transaction> transactions) {
        this.transactions = transactions;
    }

    public int getNonce() {
        return nonce;
    }

    public void setNonce(int nonce) {
        this.nonce = nonce;
    }

    public String getHash() {
        return hash;
    }

    public void setHash(String hash) {
        this.hash = hash;
    }
}