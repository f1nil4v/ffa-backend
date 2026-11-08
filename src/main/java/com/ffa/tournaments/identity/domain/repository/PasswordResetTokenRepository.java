package com.ffa.tournaments.identity.domain.repository;

import com.ffa.tournaments.identity.domain.model.PasswordResetToken;
import com.ffa.tournaments.identity.domain.valueobject.AccountId;

import java.util.List;
import java.util.Optional;

public interface PasswordResetTokenRepository {

    PasswordResetToken save(PasswordResetToken token);

    Optional<PasswordResetToken> findByTokenHash(String tokenHash);

    List<PasswordResetToken> findAllUnusedByAccountId(AccountId accountId);
}