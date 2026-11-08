package com.ffa.tournaments.team.infrastructure.persistence.mapper;

import com.ffa.tournaments.player.domain.valueobject.PlayerProfileId;
import com.ffa.tournaments.team.domain.model.TeamMember;
import com.ffa.tournaments.team.domain.model.TeamMemberRole;
import com.ffa.tournaments.team.infrastructure.persistence.entity.TeamMemberJpaEntity;
import org.springframework.stereotype.Component;

@Component
public class TeamMemberMapper {

    public TeamMemberJpaEntity toEntity(TeamMember member) {
        return new TeamMemberJpaEntity(
                member.getTeamId(),
                member.getPlayerProfileId().value(),
                member.getRole().name(),
                member.getJoinedAt()
        );
    }

    public TeamMember toDomain(TeamMemberJpaEntity entity) {
        return TeamMember.restore(
                entity.getTeamId(),
                PlayerProfileId.from(entity.getPlayerProfileId()),
                TeamMemberRole.valueOf(entity.getRole()),
                entity.getJoinedAt()
        );
    }
}
