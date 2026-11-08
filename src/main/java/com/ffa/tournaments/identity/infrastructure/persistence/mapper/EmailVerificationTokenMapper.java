package com.ffa.tournaments.identity.infrastructure.persistence.mapper;

import com.ffa.tournaments.identity.domain.model.EmailVerificationToken;
import com.ffa.tournaments.identity.domain.valueobject.AccountId;
import com.ffa.tournaments.identity.infrastructure.persistence.entity.EmailVerificationTokenJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class EmailVerificationTokenMapper {

    public EmailVerificationTokenJpaEntity toEntity(EmailVerificationToken token) {
        return new EmailVerificationTokenJpaEntity(
                token.getId(),
                token.getAccountId().value(),
                token.getTokenHash(),
                token.getExpiresAt(),
                token.getUsedAt(),
                token.getCreatedAt()
        );
    }

    public EmailVerificationToken toDomain(EmailVerificationTokenJpaEntity entity) {
        return EmailVerificationToken.restore(
                entity.getId(),
                AccountId.from(entity.getAccountId()),
                entity.getTokenHash(),
                entity.getExpiresAt(),
                entity.getUsedAt(),
                entity.getCreatedAt()
        );
    }
}