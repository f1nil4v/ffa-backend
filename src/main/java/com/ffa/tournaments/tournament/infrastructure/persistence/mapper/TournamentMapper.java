package com.ffa.tournaments.tournament.infrastructure.persistence.mapper;

import com.ffa.tournaments.support.domain.valueobject.SupportAccountId;
import com.ffa.tournaments.tournament.domain.model.Tournament;
import com.ffa.tournaments.tournament.domain.model.TournamentFormat;
import com.ffa.tournaments.tournament.domain.model.TournamentStatus;
import com.ffa.tournaments.tournament.infrastructure.persistence.entity.TournamentJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class TournamentMapper {

    public TournamentJpaEntity toEntity(Tournament tournament) {
        return new TournamentJpaEntity(
                tournament.getId(),
                tournament.getName(),
                tournament.getDescription(),
                tournament.getFormat().name(),
                tournament.getStatus().name(),
                tournament.getMaxTeams(),
                tournament.getRegistrationOpensAt(),
                tournament.getRegistrationClosesAt(),
                tournament.getStartsAt(),
                tournament.getCreatedBy().value(),
                tournament.getCreatedAt(),
                tournament.getUpdatedAt()
        );
    }

    public Tournament toDomain(TournamentJpaEntity entity) {
        return Tournament.restore(
                entity.getId(),
                entity.getName(),
                entity.getDescription(),
                TournamentFormat.valueOf(entity.getFormat()),
                TournamentStatus.valueOf(entity.getStatus()),
                entity.getMaxTeams(),
                entity.getRegistrationOpensAt(),
                entity.getRegistrationClosesAt(),
                entity.getStartsAt(),
                SupportAccountId.from(entity.getCreatedBy()),
                entity.getCreatedAt(),
                entity.getUpdatedAt()
        );
    }
}
