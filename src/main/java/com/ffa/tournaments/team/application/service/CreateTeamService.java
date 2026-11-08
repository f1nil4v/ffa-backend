package com.ffa.tournaments.team.application.service;

import com.ffa.tournaments.identity.domain.valueobject.AccountId;
import com.ffa.tournaments.player.application.service.PlayerProfileProvider;
import com.ffa.tournaments.player.domain.model.PlayerProfile;
import com.ffa.tournaments.team.application.exception.PlayerAlreadyInTeamException;
import com.ffa.tournaments.team.application.exception.TeamNameAlreadyTakenException;
import com.ffa.tournaments.team.application.exception.TeamTagAlreadyTakenException;
import com.ffa.tournaments.team.application.result.TeamResult;
import com.ffa.tournaments.team.domain.model.Team;
import com.ffa.tournaments.team.domain.model.TeamMember;
import com.ffa.tournaments.team.domain.repository.TeamMemberRepository;
import com.ffa.tournaments.team.domain.repository.TeamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class CreateTeamService {

    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final PlayerProfileProvider playerProfileProvider;
    private final TeamQueryService teamQueryService;

    public CreateTeamService(
            TeamRepository teamRepository,
            TeamMemberRepository teamMemberRepository,
            PlayerProfileProvider playerProfileProvider,
            TeamQueryService teamQueryService
    ) {
        this.teamRepository = teamRepository;
        this.teamMemberRepository = teamMemberRepository;
        this.playerProfileProvider = playerProfileProvider;
        this.teamQueryService = teamQueryService;
    }

    @Transactional
    public TeamResult create(
            AccountId accountId,
            String name,
            String tag,
            String description
    ) {
        PlayerProfile profile = playerProfileProvider.getByAccountId(accountId);

        if (teamMemberRepository.existsByPlayerProfileId(profile.getId())) {
            throw new PlayerAlreadyInTeamException();
        }

        String normalizedName = name == null ? "" : name.trim();
        String normalizedTag = tag == null ? "" : tag.trim();

        if (teamRepository.existsByNameIgnoreCase(normalizedName)) {
            throw new TeamNameAlreadyTakenException();
        }

        if (teamRepository.existsByTagIgnoreCase(normalizedTag)) {
            throw new TeamTagAlreadyTakenException();
        }

        Team team = Team.create(
                normalizedName,
                normalizedTag,
                profile.getId(),
                description
        );

        Team savedTeam = teamRepository.save(team);

        teamMemberRepository.save(
                TeamMember.createCaptain(savedTeam.getId(), profile.getId())
        );

        return teamQueryService.get(savedTeam.getId());
    }
}
