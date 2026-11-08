package com.ffa.tournaments.team.application.service;

import com.ffa.tournaments.identity.domain.valueobject.AccountId;
import com.ffa.tournaments.player.application.service.PlayerProfileProvider;
import com.ffa.tournaments.player.domain.model.PlayerProfile;
import com.ffa.tournaments.team.application.exception.PlayerAlreadyInTeamException;
import com.ffa.tournaments.team.application.exception.TeamApplicationAlreadyExistsException;
import com.ffa.tournaments.team.application.exception.TeamApplicationNotFoundException;
import com.ffa.tournaments.team.application.exception.TeamApplicationNotOwnedException;
import com.ffa.tournaments.team.application.exception.TeamApplicationNotPendingException;
import com.ffa.tournaments.team.application.exception.TeamNotFoundException;
import com.ffa.tournaments.team.application.exception.TeamRosterFullException;
import com.ffa.tournaments.team.application.result.TeamApplicationResult;
import com.ffa.tournaments.team.domain.model.Team;
import com.ffa.tournaments.team.domain.model.TeamApplication;
import com.ffa.tournaments.team.domain.model.TeamApplicationStatus;
import com.ffa.tournaments.team.domain.model.TeamMember;
import com.ffa.tournaments.team.domain.repository.TeamApplicationRepository;
import com.ffa.tournaments.team.domain.repository.TeamMemberRepository;
import com.ffa.tournaments.team.domain.repository.TeamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class TeamApplicationService {

    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final TeamApplicationRepository teamApplicationRepository;
    private final TeamAccessService teamAccessService;
    private final PlayerProfileProvider playerProfileProvider;

    public TeamApplicationService(
            TeamRepository teamRepository,
            TeamMemberRepository teamMemberRepository,
            TeamApplicationRepository teamApplicationRepository,
            TeamAccessService teamAccessService,
            PlayerProfileProvider playerProfileProvider
    ) {
        this.teamRepository = teamRepository;
        this.teamMemberRepository = teamMemberRepository;
        this.teamApplicationRepository = teamApplicationRepository;
        this.teamAccessService = teamAccessService;
        this.playerProfileProvider = playerProfileProvider;
    }

    @Transactional
    public TeamApplicationResult apply(
            AccountId accountId,
            UUID teamId,
            String message
    ) {
        PlayerProfile profile = playerProfileProvider.getByAccountId(accountId);

        teamRepository.findById(teamId)
                .orElseThrow(TeamNotFoundException::new);

        if (teamMemberRepository.existsByPlayerProfileId(profile.getId())) {
            throw new PlayerAlreadyInTeamException();
        }

        if (teamMemberRepository.countByTeamId(teamId) >= Team.MAX_ROSTER_SIZE) {
            throw new TeamRosterFullException();
        }

        if (teamApplicationRepository.existsPending(teamId, profile.getId())) {
            throw new TeamApplicationAlreadyExistsException();
        }

        TeamApplication application = TeamApplication.create(
                teamId,
                profile.getId(),
                message
        );

        return TeamApplicationResult.from(
                teamApplicationRepository.save(application)
        );
    }

    @Transactional(readOnly = true)
    public List<TeamApplicationResult> list(
            AccountId accountId,
            UUID teamId,
            TeamApplicationStatus status
    ) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(TeamNotFoundException::new);

        teamAccessService.requireCaptain(team, accountId);

        return teamApplicationRepository.findAllByTeamIdAndStatus(teamId, status)
                .stream()
                .map(TeamApplicationResult::from)
                .toList();
    }

    @Transactional
    public TeamApplicationResult approve(
            AccountId accountId,
            UUID teamId,
            UUID applicationId
    ) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(TeamNotFoundException::new);

        PlayerProfile captain = teamAccessService.requireCaptain(team, accountId);

        TeamApplication application = findApplication(teamId, applicationId);

        if (teamMemberRepository.countByTeamId(teamId) >= Team.MAX_ROSTER_SIZE) {
            throw new TeamRosterFullException();
        }

        if (teamMemberRepository.existsByPlayerProfileId(application.getPlayerProfileId())) {
            throw new PlayerAlreadyInTeamException();
        }

        TeamMember member = TeamMember.createMember(
                teamId,
                application.getPlayerProfileId()
        );

        teamMemberRepository.save(member);

        application.approve(captain.getId(), Instant.now());

        return TeamApplicationResult.from(
                teamApplicationRepository.save(application)
        );
    }

    @Transactional
    public TeamApplicationResult reject(
            AccountId accountId,
            UUID teamId,
            UUID applicationId
    ) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(TeamNotFoundException::new);

        PlayerProfile captain = teamAccessService.requireCaptain(team, accountId);

        TeamApplication application = findApplication(teamId, applicationId);

        application.reject(captain.getId(), Instant.now());

        return TeamApplicationResult.from(
                teamApplicationRepository.save(application)
        );
    }

    @Transactional
    public void withdraw(AccountId accountId, UUID teamId, UUID applicationId) {
        PlayerProfile profile = playerProfileProvider.getByAccountId(accountId);

        TeamApplication application = findApplication(teamId, applicationId);

        if (!application.getPlayerProfileId().equals(profile.getId())) {
            throw new TeamApplicationNotOwnedException();
        }

        if (application.getStatus() != TeamApplicationStatus.PENDING) {
            throw new TeamApplicationNotPendingException();
        }

        application.withdraw();

        teamApplicationRepository.save(application);
    }

    private TeamApplication findApplication(UUID teamId, UUID applicationId) {
        TeamApplication application = teamApplicationRepository.findById(applicationId)
                .orElseThrow(TeamApplicationNotFoundException::new);

        if (!application.getTeamId().equals(teamId)) {
            throw new TeamApplicationNotFoundException();
        }

        if (application.getStatus() != TeamApplicationStatus.PENDING) {
            throw new TeamApplicationNotPendingException();
        }

        return application;
    }
}
