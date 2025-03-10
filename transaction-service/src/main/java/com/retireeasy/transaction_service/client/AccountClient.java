package com.retireeasy.transaction_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "account-service", url = "${account.service.url}")
public interface AccountClient {

    @PutMapping("/accounts/{id}/balance")
    void updateBalance(@PathVariable("id") Long id, @RequestParam("amount") Double amount);
}