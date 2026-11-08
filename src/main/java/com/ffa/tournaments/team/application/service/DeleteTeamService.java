package com.ffa.tournaments.team.application.service;

import com.ffa.tournaments.identity.domain.valueobject.AccountId;
import com.ffa.tournaments.team.application.exception.TeamNotFoundException;
import com.ffa.tournaments.team.domain.model.Team;
import com.ffa.tournaments.team.domain.repository.TeamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class DeleteTeamService {

    private final TeamRepository teamRepository;
    private final TeamAccessService teamAccessService;

    public DeleteTeamService(
            TeamRepository teamRepository,
            TeamAccessService teamAccessService
    ) {
        this.teamRepository = teamRepository;
        this.teamAccessService = teamAccessService;
    }

    @Transactional
    public void delete(AccountId accountId, UUID teamId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(TeamNotFoundException::new);

        teamAccessService.requireCaptain(team, accountId);

        teamRepository.delete(team);
    }
}
