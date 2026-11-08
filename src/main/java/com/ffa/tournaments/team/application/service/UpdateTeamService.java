package com.ffa.tournaments.team.application.service;

import com.ffa.tournaments.identity.domain.valueobject.AccountId;
import com.ffa.tournaments.team.application.exception.TeamNameAlreadyTakenException;
import com.ffa.tournaments.team.application.exception.TeamNotFoundException;
import com.ffa.tournaments.team.application.exception.TeamTagAlreadyTakenException;
import com.ffa.tournaments.team.application.result.TeamResult;
import com.ffa.tournaments.team.domain.model.Team;
import com.ffa.tournaments.team.domain.repository.TeamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class UpdateTeamService {

    private final TeamRepository teamRepository;
    private final TeamAccessService teamAccessService;
    private final TeamQueryService teamQueryService;

    public UpdateTeamService(
            TeamRepository teamRepository,
            TeamAccessService teamAccessService,
            TeamQueryService teamQueryService
    ) {
        this.teamRepository = teamRepository;
        this.teamAccessService = teamAccessService;
        this.teamQueryService = teamQueryService;
    }

    @Transactional
    public TeamResult update(
            AccountId accountId,
            UUID teamId,
            String name,
            String tag,
            String description
    ) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(TeamNotFoundException::new);

        teamAccessService.requireCaptain(team, accountId);

        String normalizedName = name == null ? "" : name.trim();
        String normalizedTag = tag == null ? "" : tag.trim();

        if (!team.getName().equalsIgnoreCase(normalizedName)
                && teamRepository.existsByNameIgnoreCase(normalizedName)) {
            throw new TeamNameAlreadyTakenException();
        }

        if (!team.getTag().equalsIgnoreCase(normalizedTag)
                && teamRepository.existsByTagIgnoreCase(normalizedTag)) {
            throw new TeamTagAlreadyTakenException();
        }

        team.update(normalizedName, normalizedTag, description);

        teamRepository.save(team);

        return teamQueryService.get(teamId);
    }
}
