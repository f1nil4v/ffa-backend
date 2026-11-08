package com.ffa.tournaments.team.infrastructure.persistence.adapter;

import com.ffa.tournaments.player.domain.valueobject.PlayerProfileId;
import com.ffa.tournaments.team.domain.model.TeamMember;
import com.ffa.tournaments.team.domain.repository.TeamMemberRepository;
import com.ffa.tournaments.team.infrastructure.persistence.entity.TeamMemberId;
import com.ffa.tournaments.team.infrastructure.persistence.mapper.TeamMemberMapper;
import com.ffa.tournaments.team.infrastructure.persistence.repository.SpringDataTeamMemberRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class TeamMemberRepositoryAdapter implements TeamMemberRepository {

    private final SpringDataTeamMemberRepository jpaRepository;
    private final TeamMemberMapper mapper;

    public TeamMemberRepositoryAdapter(
            SpringDataTeamMemberRepository jpaRepository,
            TeamMemberMapper mapper
    ) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public TeamMember save(TeamMember member) {
        return mapper.toDomain(
                jpaRepository.saveAndFlush(mapper.toEntity(member))
        );
    }

    @Override
    public Optional<TeamMember> findByTeamIdAndPlayerProfileId(
            UUID teamId,
            PlayerProfileId playerProfileId
    ) {
        return jpaRepository
                .findByTeamIdAndPlayerProfileId(teamId, playerProfileId.value())
                .map(mapper::toDomain);
    }

    @Override
    public Optional<TeamMember> findByPlayerProfileId(PlayerProfileId playerProfileId) {
        return jpaRepository.findByPlayerProfileId(playerProfileId.value())
                .map(mapper::toDomain);
    }

    @Override
    public List<TeamMember> findAllByTeamId(UUID teamId) {
        return jpaRepository.findAllByTeamId(teamId)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public long countByTeamId(UUID teamId) {
        return jpaRepository.countByTeamId(teamId);
    }

    @Override
    public boolean existsByPlayerProfileId(PlayerProfileId playerProfileId) {
        return jpaRepository.existsByPlayerProfileId(playerProfileId.value());
    }

    @Override
    public void delete(TeamMember member) {
        jpaRepository.deleteById(
                new TeamMemberId(
                        member.getTeamId(),
                        member.getPlayerProfileId().value()
                )
        );
    }

    @Override
    public void deleteAllByTeamId(UUID teamId) {
        jpaRepository.deleteAllByTeamId(teamId);
    }
}
