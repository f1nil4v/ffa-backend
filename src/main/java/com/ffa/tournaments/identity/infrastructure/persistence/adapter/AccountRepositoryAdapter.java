package com.ffa.tournaments.identity.infrastructure.persistence.adapter;

import com.ffa.tournaments.identity.domain.model.Account;
import com.ffa.tournaments.identity.domain.repository.AccountRepository;
import com.ffa.tournaments.identity.domain.valueobject.AccountId;
import com.ffa.tournaments.identity.domain.valueobject.Email;
import com.ffa.tournaments.identity.infrastructure.persistence.entity.AccountJpaEntity;
import com.ffa.tournaments.identity.infrastructure.persistence.mapper.AccountMapper;
import com.ffa.tournaments.identity.infrastructure.persistence.repository.SpringDataAccountRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class AccountRepositoryAdapter implements AccountRepository {

    private final SpringDataAccountRepository jpaRepository;
    private final AccountMapper mapper;

    public AccountRepositoryAdapter(SpringDataAccountRepository jpaRepository, AccountMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Account save(Account account) {
        AccountJpaEntity entity = mapper.toEntity(account);
        AccountJpaEntity savedEntity = jpaRepository.save(entity);
        return mapper.toDomain(savedEntity);
    }

    @Override
    public Optional<Account> findById(AccountId id) {
        return jpaRepository.findById(id.value()).map(mapper::toDomain);
    }

    @Override
    public Optional<Account> findByEmail(Email email) {
        return jpaRepository.findByEmail(email.value()).map(mapper::toDomain);
    }

    @Override
    public boolean existsByEmail(Email email) {
        return jpaRepository.existsByEmail(email.value());
    }
}