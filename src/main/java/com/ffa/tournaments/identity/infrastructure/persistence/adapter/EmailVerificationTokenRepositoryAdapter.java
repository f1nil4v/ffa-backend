package com.ffa.tournaments.identity.infrastructure.persistence.adapter;

import com.ffa.tournaments.identity.domain.model.EmailVerificationToken;
import com.ffa.tournaments.identity.domain.repository.EmailVerificationTokenRepository;
import com.ffa.tournaments.identity.domain.valueobject.AccountId;
import com.ffa.tournaments.identity.infrastructure.persistence.mapper.EmailVerificationTokenMapper;
import com.ffa.tournaments.identity.infrastructure.persistence.repository.SpringDataEmailVerificationTokenRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class EmailVerificationTokenRepositoryAdapter implements EmailVerificationTokenRepository {

    private final SpringDataEmailVerificationTokenRepository jpaRepository;
    private final EmailVerificationTokenMapper mapper;

    public EmailVerificationTokenRepositoryAdapter(SpringDataEmailVerificationTokenRepository jpaRepository, EmailVerificationTokenMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public EmailVerificationToken save(EmailVerificationToken token) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(token)));
    }

    @Override
    public Optional<EmailVerificationToken> findByTokenHash(String tokenHash) {
        return jpaRepository.findByTokenHashAndUsedAtIsNull(tokenHash).map(mapper::toDomain);
    }

    @Override
    public List<EmailVerificationToken> findAllUnusedByAccountId(AccountId accountId) {
        return jpaRepository.findAllByAccountIdAndUsedAtIsNull(accountId.value()).stream().map(mapper::toDomain).toList();
    }
}