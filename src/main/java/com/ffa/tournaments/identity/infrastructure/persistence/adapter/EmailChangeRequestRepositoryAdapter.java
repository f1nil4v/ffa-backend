package com.ffa.tournaments.identity.infrastructure.persistence.adapter;

import com.ffa.tournaments.identity.domain.model.EmailChangeRequest;
import com.ffa.tournaments.identity.domain.model.EmailChangeRequestStatus;
import com.ffa.tournaments.identity.domain.repository.EmailChangeRequestRepository;
import com.ffa.tournaments.identity.domain.valueobject.AccountId;
import com.ffa.tournaments.identity.infrastructure.persistence.mapper.EmailChangeRequestMapper;
import com.ffa.tournaments.identity.infrastructure.persistence.repository.SpringDataEmailChangeRequestRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class EmailChangeRequestRepositoryAdapter implements EmailChangeRequestRepository {

    private final SpringDataEmailChangeRequestRepository jpaRepository;
    private final EmailChangeRequestMapper mapper;

    public EmailChangeRequestRepositoryAdapter(
            SpringDataEmailChangeRequestRepository jpaRepository,
            EmailChangeRequestMapper mapper
    ) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public EmailChangeRequest save(EmailChangeRequest request) {
        return mapper.toDomain(
                jpaRepository.saveAndFlush(mapper.toEntity(request))
        );
    }

    @Override
    public Optional<EmailChangeRequest> findByTokenHash(String tokenHash) {
        return jpaRepository.findByTokenHash(tokenHash).map(mapper::toDomain);
    }

    @Override
    public Optional<EmailChangeRequest> findPendingByAccountId(AccountId accountId) {
        return jpaRepository
                .findByAccountIdAndStatus(
                        accountId.value(),
                        EmailChangeRequestStatus.PENDING.name()
                )
                .map(mapper::toDomain);
    }
}
