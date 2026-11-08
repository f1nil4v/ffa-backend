package com.ffa.tournaments.team.infrastructure.persistence.mapper;

import com.ffa.tournaments.player.domain.valueobject.PlayerProfileId;
import com.ffa.tournaments.team.domain.model.Team;
import com.ffa.tournaments.team.infrastructure.persistence.entity.TeamJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class TeamMapper {

    public TeamJpaEntity toEntity(Team team) {
        return new TeamJpaEntity(
                team.getId(),
                team.getName(),
                team.getTag(),
                team.getCaptainProfileId().value(),
                team.getDescription(),
                team.getCreatedAt()
        );
    }

    public Team toDomain(TeamJpaEntity entity) {
        return Team.restore(
                entity.getId(),
                entity.getName(),
                entity.getTag(),
                PlayerProfileId.from(entity.getCaptainProfileId()),
                entity.getDescription(),
                entity.getCreatedAt()
        );
    }
}
