package com.example.blockchainwallet.repository;

import com.example.blockchainwallet.domain.BlockEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BlockRepository extends JpaRepository<BlockEntity, Long> {
}