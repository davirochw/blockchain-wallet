package com.example.blockchainwallet.controller;

import com.example.blockchainwallet.domain.BlockEntity;
import com.example.blockchainwallet.domain.Transaction;
import com.example.blockchainwallet.domain.Wallet;
import com.example.blockchainwallet.service.BlockchainService;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/blockchain")
public class BlockchainController {
    private final BlockchainService blockchainService;

    public BlockchainController(BlockchainService blockchainService) {
        this.blockchainService = blockchainService;
    }

    // Cria uma nova carteira
    @PostMapping("/wallet")
    public Wallet createWallet() {
        return new Wallet();
    }

    // Envia uma transação
    @PostMapping("/transactions")
    public Transaction sendTransaction(
            @RequestParam String sender,
            @RequestParam String receiver,
            @RequestParam double amount,
            @RequestParam String signature
    ) {
        Transaction transaction = new Transaction(sender, receiver, amount, signature);
        blockchainService.addTransaction(transaction);
        return transaction;
    }

    // Retorna a cadeia de blocos
    @GetMapping("/blocks")
    public List<BlockEntity> getBlocks() {
        return blockchainService.getBlockchain();
    }

    // Mina um novo bloco
    @PostMapping("/mine/{minerAddress}")
    public BlockEntity mineBlock(@PathVariable String minerAddress) {
        return blockchainService.mineBlock(minerAddress);
    }
}