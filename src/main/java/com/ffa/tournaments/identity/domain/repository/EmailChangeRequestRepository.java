package com.ffa.tournaments.identity.domain.repository;

import com.ffa.tournaments.identity.domain.model.EmailChangeRequest;
import com.ffa.tournaments.identity.domain.valueobject.AccountId;

import java.util.Optional;

public interface EmailChangeRequestRepository {

    EmailChangeRequest save(EmailChangeRequest request);

    Optional<EmailChangeRequest> findByTokenHash(String tokenHash);

    Optional<EmailChangeRequest> findPendingByAccountId(AccountId accountId);
}
