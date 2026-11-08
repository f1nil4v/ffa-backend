package com.ffa.tournaments.identity.application.port;

import com.ffa.tournaments.identity.domain.valueobject.AccountId;

public interface TokenIssuer {
    String generateToken(AccountId accountId);
}