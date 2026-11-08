package com.ffa.tournaments.identity.infrastructure.persistence.mapper;

import org.springframework.stereotype.Component;

import com.ffa.tournaments.identity.domain.model.Account;
import com.ffa.tournaments.identity.domain.valueobject.AccountId;
import com.ffa.tournaments.identity.domain.valueobject.Email;
import com.ffa.tournaments.identity.infrastructure.persistence.entity.AccountJpaEntity;

@Component
public class AccountMapper {

    public AccountJpaEntity toEntity(Account account) {
        return new AccountJpaEntity(
                account.getId().value(),
                account.getEmail().value(),
                account.getPasswordHash(),
                account.isEmailVerified(),
                account.getCreatedAt()
        );
    }

    public Account toDomain(AccountJpaEntity entity) {
        return Account.restore(
                AccountId.from(entity.getId()),
                new Email(entity.getEmail()),
                entity.getPasswordHash(),
                entity.isEmailVerified(),
                entity.getCreatedAt()
        );
    }
}