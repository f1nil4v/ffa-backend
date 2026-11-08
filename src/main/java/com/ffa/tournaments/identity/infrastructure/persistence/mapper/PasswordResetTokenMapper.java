package com.ffa.tournaments.identity.infrastructure.persistence.mapper;

import com.ffa.tournaments.identity.domain.model.PasswordResetToken;
import com.ffa.tournaments.identity.domain.valueobject.AccountId;
import com.ffa.tournaments.identity.infrastructure.persistence.entity.PasswordResetTokenJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class PasswordResetTokenMapper {

    public PasswordResetTokenJpaEntity toEntity(PasswordResetToken token) {
        return new PasswordResetTokenJpaEntity(
                token.getId(),
                token.getAccountId().value(),
                token.getTokenHash(),
                token.getExpiresAt(),
                token.getUsedAt(),
                token.getCreatedAt()
        );
    }

    public PasswordResetToken toDomain(PasswordResetTokenJpaEntity entity) {
        return PasswordResetToken.restore(
                entity.getId(),
                AccountId.from(entity.getAccountId()),
                entity.getTokenHash(),
                entity.getExpiresAt(),
                entity.getUsedAt(),
                entity.getCreatedAt()
        );
    }
}