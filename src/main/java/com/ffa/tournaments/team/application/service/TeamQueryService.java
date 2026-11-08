package com.ffa.tournaments.team.application.service;

import com.ffa.tournaments.identity.domain.valueobject.AccountId;
import com.ffa.tournaments.player.application.service.PlayerProfileProvider;
import com.ffa.tournaments.player.domain.model.PlayerProfile;
import com.ffa.tournaments.player.domain.repository.PlayerProfileRepository;
import com.ffa.tournaments.team.application.exception.PlayerNotInTeamException;
import com.ffa.tournaments.team.application.exception.TeamNotFoundException;
import com.ffa.tournaments.team.application.result.TeamMemberResult;
import com.ffa.tournaments.team.application.result.TeamResult;
import com.ffa.tournaments.team.application.result.TeamSummaryResult;
import com.ffa.tournaments.team.domain.model.Team;
import com.ffa.tournaments.team.domain.model.TeamMember;
import com.ffa.tournaments.team.domain.repository.TeamMemberRepository;
import com.ffa.tournaments.team.domain.repository.TeamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class TeamQueryService {

    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final PlayerProfileRepository playerProfileRepository;
    private final PlayerProfileProvider playerProfileProvider;

    public TeamQueryService(
            TeamRepository teamRepository,
            TeamMemberRepository teamMemberRepository,
            PlayerProfileRepository playerProfileRepository,
            PlayerProfileProvider playerProfileProvider
    ) {
        this.teamRepository = teamRepository;
        this.teamMemberRepository = teamMemberRepository;
        this.playerProfileRepository = playerProfileRepository;
        this.playerProfileProvider = playerProfileProvider;
    }

    @Transactional(readOnly = true)
    public TeamResult get(UUID teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(TeamNotFoundException::new);

        return toResult(team);
    }

    @Transactional(readOnly = true)
    public TeamResult getByAccount(AccountId accountId) {
        PlayerProfile profile = playerProfileProvider.getByAccountId(accountId);

        TeamMember member = teamMemberRepository.findByPlayerProfileId(profile.getId())
                .orElseThrow(PlayerNotInTeamException::new);

        return get(member.getTeamId());
    }

    @Transactional(readOnly = true)
    public List<TeamSummaryResult> list() {
        return teamRepository.findAll()
                .stream()
                .map(team -> new TeamSummaryResult(
                        team.getId(),
                        team.getName(),
                        team.getTag(),
                        team.getCaptainProfileId().value(),
                        team.getDescription(),
                        team.getCreatedAt(),
                        teamMemberRepository.countByTeamId(team.getId())
                ))
                .toList();
    }

    private TeamResult toResult(Team team) {
        List<TeamMemberResult> members = teamMemberRepository.findAllByTeamId(team.getId())
                .stream()
                .map(this::toMemberResult)
                .toList();

        return new TeamResult(
                team.getId(),
                team.getName(),
                team.getTag(),
                team.getCaptainProfileId().value(),
                team.getDescription(),
                team.getCreatedAt(),
                members
        );
    }

    private TeamMemberResult toMemberResult(TeamMember member) {
        PlayerProfile profile = playerProfileRepository.findById(member.getPlayerProfileId())
                .orElse(null);

        return new TeamMemberResult(
                member.getPlayerProfileId().value(),
                profile == null ? null : profile.getNickname(),
                profile == null ? null : profile.getSteamId(),
                member.getRole().name(),
                member.getJoinedAt()
        );
    }
}
