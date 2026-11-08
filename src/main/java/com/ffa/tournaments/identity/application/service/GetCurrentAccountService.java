package com.ffa.tournaments.identity.application.service;

import com.ffa.tournaments.identity.application.result.CurrentAccountResult;
import com.ffa.tournaments.identity.domain.repository.AccountRepository;
import com.ffa.tournaments.identity.domain.valueobject.AccountId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class GetCurrentAccountService {
    private final AccountRepository accountRepository;

    public GetCurrentAccountService(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Transactional(readOnly = true)
    public CurrentAccountResult get(UUID accountId) {
        var account = accountRepository.findById(AccountId.from(accountId))
                .orElseThrow(() -> new IllegalStateException("Authenticated account not found"));

        return new CurrentAccountResult(account.getId().value(), account.getEmail().value());
    }
}