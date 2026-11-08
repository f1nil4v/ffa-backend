package com.ffa.tournaments.identity.domain.repository;

import com.ffa.tournaments.identity.domain.model.Account;
import com.ffa.tournaments.identity.domain.valueobject.AccountId;
import com.ffa.tournaments.identity.domain.valueobject.Email;

import java.util.Optional;

public interface AccountRepository {

    Account save(Account account);

    Optional<Account> findById(AccountId id);

    Optional<Account> findByEmail(Email email);

    boolean existsByEmail(Email email);
}