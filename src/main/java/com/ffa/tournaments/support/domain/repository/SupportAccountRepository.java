package com.ffa.tournaments.support.domain.repository;

import com.ffa.tournaments.identity.domain.valueobject.Email;
import com.ffa.tournaments.support.domain.model.SupportAccount;
import com.ffa.tournaments.support.domain.valueobject.SupportAccountId;

import java.util.Optional;

public interface SupportAccountRepository {

    SupportAccount save(SupportAccount account);

    Optional<SupportAccount> findById(SupportAccountId id);

    Optional<SupportAccount> findByEmail(Email email);

    boolean existsByEmail(Email email);
}
