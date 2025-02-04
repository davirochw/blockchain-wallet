package com.example.blockchainwallet.service;

import com.example.blockchainwallet.domain.BlockEntity;
import com.example.blockchainwallet.domain.Transaction;
import com.example.blockchainwallet.repository.BlockRepository;
import com.example.blockchainwallet.util.CryptoUtil;
import org.springframework.stereotype.Service;
import java.util.ArrayList;
import java.util.List;

@Service
public class BlockchainService {

    private final BlockRepository blockRepository;
    private final BalanceService balanceService;

    private List<BlockEntity> blockchain = new ArrayList<>();
    private List<Transaction> pendingTransactions = new ArrayList<>();
    private final int difficulty = 4;
    private static final double MINER_REWARD = 10.0;

    public BlockchainService(BlockRepository blockRepository, BalanceService balanceService) {
        this.blockRepository = blockRepository;
        this.balanceService = balanceService;
        createGenesisBlock();
    }

    private void createGenesisBlock() {
        List<Transaction> genesisTransactions = new ArrayList<>();
        BlockEntity genesis = new BlockEntity(
                0,
                "0",
                System.currentTimeMillis(),
                genesisTransactions,
                0,
                CryptoUtil.sha256("genesis-block")
        );
        blockchain.add(genesis);
        blockRepository.save(genesis);
    }

    public BlockEntity mineBlock(String minerAddress) {
        BlockEntity lastBlock = blockchain.get(blockchain.size() - 1);

        // Adiciona recompensa ao minerador
        addMinerReward(minerAddress);

        int nonce = 0;
        String hash;
        String target = new String(new char[difficulty]).replace('\0', '0');

        do {
            nonce++;
            hash = calculateBlockHash(lastBlock, nonce);
        } while (!hash.startsWith(target));

        BlockEntity newBlock = new BlockEntity(
                lastBlock.getIndex() + 1,
                lastBlock.getHash(),
                System.currentTimeMillis(),
                new ArrayList<>(pendingTransactions),
                nonce,
                hash
        );

        blockchain.add(newBlock);
        blockRepository.save(newBlock);
        balanceService.updateBalances(pendingTransactions);
        pendingTransactions.clear();

        return newBlock;
    }

    private String calculateBlockHash(BlockEntity previousBlock, int nonce) {
        return CryptoUtil.sha256(
                previousBlock.getIndex() +
                        previousBlock.getPreviousHash() +
                        previousBlock.getTimestamp() +
                        pendingTransactions.toString() +
                        nonce
        );
    }

    private void addMinerReward(String minerAddress) {
        Transaction reward = new Transaction(
                "system",
                minerAddress,
                MINER_REWARD,
                "miner-reward-signature"
        );
        pendingTransactions.add(reward);
    }

    public void addTransaction(Transaction transaction) {
        if (!transaction.sender().equals("system")) {
            double balance = balanceService.getBalance(transaction.sender());
            if (balance < transaction.amount()) {
                throw new IllegalArgumentException("Saldo insuficiente");
            }
        }
        pendingTransactions.add(transaction);
    }

    public boolean isChainValid() {
        for (int i = 1; i < blockchain.size(); i++) {
            BlockEntity currentBlock = blockchain.get(i);
            BlockEntity previousBlock = blockchain.get(i - 1);

            String calculatedHash = calculateBlockHash(previousBlock, currentBlock.getNonce());
            if (!currentBlock.getHash().equals(calculatedHash)) {
                return false;
            }

            if (!currentBlock.getPreviousHash().equals(previousBlock.getHash())) {
                return false;
            }

            if (!currentBlock.getHash().substring(0, difficulty).equals(
                    new String(new char[difficulty]).replace('\0', '0'))) {
                return false;
            }
        }
        return true;
    }

    public List<BlockEntity> getBlockchain() {
        return new ArrayList<>(blockchain);
    }
}