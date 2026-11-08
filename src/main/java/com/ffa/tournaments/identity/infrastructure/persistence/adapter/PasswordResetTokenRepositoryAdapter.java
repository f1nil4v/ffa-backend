package com.ffa.tournaments.identity.infrastructure.persistence.adapter;

import com.ffa.tournaments.identity.domain.model.PasswordResetToken;
import com.ffa.tournaments.identity.domain.repository.PasswordResetTokenRepository;
import com.ffa.tournaments.identity.domain.valueobject.AccountId;
import com.ffa.tournaments.identity.infrastructure.persistence.mapper.PasswordResetTokenMapper;
import com.ffa.tournaments.identity.infrastructure.persistence.repository.SpringDataPasswordResetTokenRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class PasswordResetTokenRepositoryAdapter implements PasswordResetTokenRepository {

    private final SpringDataPasswordResetTokenRepository jpaRepository;
    private final PasswordResetTokenMapper mapper;

    public PasswordResetTokenRepositoryAdapter(SpringDataPasswordResetTokenRepository jpaRepository, PasswordResetTokenMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public PasswordResetToken save(PasswordResetToken token) {
        return mapper.toDomain(jpaRepository.save(mapper.toEntity(token)));
    }

    @Override
    public Optional<PasswordResetToken> findByTokenHash(String tokenHash) {
        return jpaRepository.findByTokenHashAndUsedAtIsNull(tokenHash).map(mapper::toDomain);
    }

    @Override
    public List<PasswordResetToken> findAllUnusedByAccountId(AccountId accountId) {
        return jpaRepository.findAllByAccountIdAndUsedAtIsNull(accountId.value()).stream().map(mapper::toDomain).toList();
    }
}