package com.ffa.tournaments.team.infrastructure.persistence.mapper;

import com.ffa.tournaments.player.domain.valueobject.PlayerProfileId;
import com.ffa.tournaments.team.domain.model.TeamApplication;
import com.ffa.tournaments.team.domain.model.TeamApplicationStatus;
import com.ffa.tournaments.team.infrastructure.persistence.entity.TeamApplicationJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class TeamApplicationMapper {

    public TeamApplicationJpaEntity toEntity(TeamApplication application) {
        return new TeamApplicationJpaEntity(
                application.getId(),
                application.getTeamId(),
                application.getPlayerProfileId().value(),
                application.getMessage(),
                application.getStatus().name(),
                application.getReviewedBy() == null
                        ? null
                        : application.getReviewedBy().value(),
                application.getCreatedAt(),
                application.getReviewedAt()
        );
    }

    public TeamApplication toDomain(TeamApplicationJpaEntity entity) {
        return TeamApplication.restore(
                entity.getId(),
                entity.getTeamId(),
                PlayerProfileId.from(entity.getPlayerProfileId()),
                entity.getMessage(),
                TeamApplicationStatus.valueOf(entity.getStatus()),
                entity.getReviewedBy() == null
                        ? null
                        : PlayerProfileId.from(entity.getReviewedBy()),
                entity.getReviewedAt(),
                entity.getCreatedAt()
        );
    }
}
