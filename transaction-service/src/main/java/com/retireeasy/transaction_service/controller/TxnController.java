package com.retireeasy.transaction_service.controller;

import com.retireeasy.transaction_service.client.AccountClient;
import com.retireeasy.transaction_service.entity.Txn;
import com.retireeasy.transaction_service.repository.TxnRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/transactions")
public class TxnController {

    private final TxnRepository txnRepository;
    private final AccountClient accountClient;

    public TxnController(TxnRepository txnRepository, AccountClient accountClient) {
        this.txnRepository = txnRepository;
        this.accountClient = accountClient;
    }

    @PostMapping
    public ResponseEntity<Txn> createTxn(@RequestBody Txn txn) {
        txn.setTxnDate(LocalDateTime.now());
        Txn saved = txnRepository.save(txn);

        if ("CONTRIBUTION".equalsIgnoreCase(txn.getTxnType())) {
            accountClient.updateBalance(txn.getAccountId(), txn.getAmount());
        }

        return ResponseEntity.ok(saved);
    }

    @GetMapping("/account/{accountId}")
    public ResponseEntity<List<Txn>> getTxns(@PathVariable Long accountId) {
        return ResponseEntity.ok(txnRepository.findByAccountId(accountId));
    }
}