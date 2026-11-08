package com.ffa.tournaments.tournament.infrastructure.persistence.mapper;

import com.ffa.tournaments.player.domain.valueobject.PlayerProfileId;
import com.ffa.tournaments.support.domain.valueobject.SupportAccountId;
import com.ffa.tournaments.tournament.domain.model.TournamentRegistration;
import com.ffa.tournaments.tournament.domain.model.TournamentRegistrationStatus;
import com.ffa.tournaments.tournament.infrastructure.persistence.entity.TournamentRegistrationJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class TournamentRegistrationMapper {

    public TournamentRegistrationJpaEntity toEntity(TournamentRegistration registration) {
        return new TournamentRegistrationJpaEntity(
                registration.getId(),
                registration.getTournamentId(),
                registration.getTeamId(),
                registration.getRegisteredBy().value(),
                registration.getStatus().name(),
                registration.getRosterSnapshot(),
                registration.getSeed(),
                registration.getCreatedAt(),
                registration.getReviewedBy() == null
                        ? null
                        : registration.getReviewedBy().value(),
                registration.getReviewedAt()
        );
    }

    public TournamentRegistration toDomain(TournamentRegistrationJpaEntity entity) {
        return TournamentRegistration.restore(
                entity.getId(),
                entity.getTournamentId(),
                entity.getTeamId(),
                PlayerProfileId.from(entity.getRegisteredBy()),
                entity.getRosterSnapshot(),
                TournamentRegistrationStatus.valueOf(entity.getStatus()),
                entity.getSeed(),
                entity.getReviewedBy() == null
                        ? null
                        : SupportAccountId.from(entity.getReviewedBy()),
                entity.getReviewedAt(),
                entity.getCreatedAt()
        );
    }
}
