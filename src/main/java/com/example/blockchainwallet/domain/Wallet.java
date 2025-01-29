package com.example.blockchainwallet.domain;

import com.example.blockchainwallet.util.CryptoUtil;

import java.security.*;

public class Wallet {
    private PrivateKey privateKey;
    private PublicKey publicKey;

    public Wallet() {
        generateKeyPair();
    }

    // Gera par de chaves RSA
    private void generateKeyPair() {
        try {
            KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
            keyGen.initialize(2048);
            KeyPair pair = keyGen.generateKeyPair();
            this.privateKey = pair.getPrivate();
            this.publicKey = pair.getPublic();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Falha ao gerar chaves", e);
        }
    }

    // Assina uma transação
    public String sign(String data) {
        try {
            Signature signature = Signature.getInstance("SHA256withRSA");
            signature.initSign(privateKey);
            signature.update(data.getBytes());
            return CryptoUtil.bytesToHex(signature.sign());
        } catch (Exception e) {
            throw new RuntimeException("Falha ao assinar transação", e);
        }
    }

    // Getters
    public PublicKey getPublicKey() {
        return publicKey;
    }
}