package com.retireeasy.transaction_service.repository;

import com.retireeasy.transaction_service.entity.Txn;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TxnRepository extends JpaRepository<Txn, Long> {
    List<Txn> findByAccountId(Long accountId);
}