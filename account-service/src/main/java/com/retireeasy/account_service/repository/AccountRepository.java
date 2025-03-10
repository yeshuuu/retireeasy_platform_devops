package com.retireeasy.account_service.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.retireeasy.account_service.entity.Account;

public interface AccountRepository extends JpaRepository<Account, Long> {
}