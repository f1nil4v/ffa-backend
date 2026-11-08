package com.ffa.tournaments.identity.domain.repository;

import com.ffa.tournaments.identity.domain.model.EmailVerificationToken;
import com.ffa.tournaments.identity.domain.valueobject.AccountId;

import java.util.List;
import java.util.Optional;

public interface EmailVerificationTokenRepository {

    EmailVerificationToken save(EmailVerificationToken token);

    Optional<EmailVerificationToken> findByTokenHash(String tokenHash);

    List<EmailVerificationToken> findAllUnusedByAccountId(AccountId accountId);
}