package com.ffa.tournaments.identity.infrastructure.persistence.mapper;

import com.ffa.tournaments.identity.domain.model.EmailChangeRequest;
import com.ffa.tournaments.identity.domain.model.EmailChangeRequestStatus;
import com.ffa.tournaments.identity.domain.valueobject.AccountId;
import com.ffa.tournaments.identity.domain.valueobject.Email;
import com.ffa.tournaments.identity.infrastructure.persistence.entity.EmailChangeRequestJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class EmailChangeRequestMapper {

    public EmailChangeRequestJpaEntity toEntity(EmailChangeRequest request) {
        return new EmailChangeRequestJpaEntity(
                request.getId(),
                request.getAccountId().value(),
                request.getCurrentEmail().value(),
                request.getNewEmail().value(),
                request.getOldTokenHash(),
                request.getNewTokenHash(),
                request.getOldConfirmedAt(),
                request.getNewConfirmedAt(),
                request.getStatus().name(),
                request.getCreatedAt(),
                request.getExpiresAt()
        );
    }

    public EmailChangeRequest toDomain(EmailChangeRequestJpaEntity entity) {
        return EmailChangeRequest.restore(
                entity.getId(),
                AccountId.from(entity.getAccountId()),
                new Email(entity.getCurrentEmail()),
                new Email(entity.getNewEmail()),
                entity.getOldTokenHash(),
                entity.getNewTokenHash(),
                entity.getOldConfirmedAt(),
                entity.getNewConfirmedAt(),
                EmailChangeRequestStatus.valueOf(entity.getStatus()),
                entity.getCreatedAt(),
                entity.getExpiresAt()
        );
    }
}
