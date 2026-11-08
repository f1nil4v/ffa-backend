package com.ffa.tournaments.support.infrastructure.persistence.mapper;

import com.ffa.tournaments.identity.domain.valueobject.Email;
import com.ffa.tournaments.support.domain.model.SupportAccount;
import com.ffa.tournaments.support.domain.valueobject.SupportAccountId;
import com.ffa.tournaments.support.infrastructure.persistence.entity.SupportAccountJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class SupportAccountMapper {

    public SupportAccountJpaEntity toEntity(SupportAccount account) {
        return new SupportAccountJpaEntity(
                account.getId().value(),
                account.getEmail().value(),
                account.getPasswordHash(),
                account.getDisplayName(),
                account.isActive(),
                account.getCreatedAt()
        );
    }

    public SupportAccount toDomain(SupportAccountJpaEntity entity) {
        return SupportAccount.restore(
                SupportAccountId.from(entity.getId()),
                new Email(entity.getEmail()),
                entity.getPasswordHash(),
                entity.getDisplayName(),
                entity.isActive(),
                entity.getCreatedAt()
        );
    }
}
