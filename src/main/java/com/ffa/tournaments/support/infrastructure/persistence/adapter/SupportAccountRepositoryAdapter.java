package com.ffa.tournaments.support.infrastructure.persistence.adapter;

import com.ffa.tournaments.identity.domain.valueobject.Email;
import com.ffa.tournaments.support.domain.model.SupportAccount;
import com.ffa.tournaments.support.domain.repository.SupportAccountRepository;
import com.ffa.tournaments.support.domain.valueobject.SupportAccountId;
import com.ffa.tournaments.support.infrastructure.persistence.mapper.SupportAccountMapper;
import com.ffa.tournaments.support.infrastructure.persistence.repository.SpringDataSupportAccountRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class SupportAccountRepositoryAdapter implements SupportAccountRepository {

    private final SpringDataSupportAccountRepository jpaRepository;
    private final SupportAccountMapper mapper;

    public SupportAccountRepositoryAdapter(
            SpringDataSupportAccountRepository jpaRepository,
            SupportAccountMapper mapper
    ) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public SupportAccount save(SupportAccount account) {
        return mapper.toDomain(
                jpaRepository.save(mapper.toEntity(account))
        );
    }

    @Override
    public Optional<SupportAccount> findById(SupportAccountId id) {
        return jpaRepository.findById(id.value()).map(mapper::toDomain);
    }

    @Override
    public Optional<SupportAccount> findByEmail(Email email) {
        return jpaRepository.findByEmail(email.value()).map(mapper::toDomain);
    }

    @Override
    public boolean existsByEmail(Email email) {
        return jpaRepository.existsByEmail(email.value());
    }
}
