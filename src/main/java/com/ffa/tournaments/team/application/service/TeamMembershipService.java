package com.ffa.tournaments.team.application.service;

import com.ffa.tournaments.identity.domain.valueobject.AccountId;
import com.ffa.tournaments.player.application.service.PlayerProfileProvider;
import com.ffa.tournaments.player.domain.model.PlayerProfile;
import com.ffa.tournaments.player.domain.valueobject.PlayerProfileId;
import com.ffa.tournaments.team.application.exception.CannotKickCaptainException;
import com.ffa.tournaments.team.application.exception.CaptainCannotLeaveTeamException;
import com.ffa.tournaments.team.application.exception.InvalidCaptainTransferException;
import com.ffa.tournaments.team.application.exception.TeamMemberNotFoundException;
import com.ffa.tournaments.team.application.exception.TeamNotFoundException;
import com.ffa.tournaments.team.domain.model.Team;
import com.ffa.tournaments.team.domain.model.TeamMember;
import com.ffa.tournaments.team.domain.repository.TeamMemberRepository;
import com.ffa.tournaments.team.domain.repository.TeamRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class TeamMembershipService {

    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final TeamAccessService teamAccessService;
    private final PlayerProfileProvider playerProfileProvider;

    public TeamMembershipService(
            TeamRepository teamRepository,
            TeamMemberRepository teamMemberRepository,
            TeamAccessService teamAccessService,
            PlayerProfileProvider playerProfileProvider
    ) {
        this.teamRepository = teamRepository;
        this.teamMemberRepository = teamMemberRepository;
        this.teamAccessService = teamAccessService;
        this.playerProfileProvider = playerProfileProvider;
    }

    @Transactional
    public void leave(AccountId accountId, UUID teamId) {
        PlayerProfile profile = playerProfileProvider.getByAccountId(accountId);

        teamRepository.findById(teamId)
                .orElseThrow(TeamNotFoundException::new);

        TeamMember member = teamMemberRepository
                .findByTeamIdAndPlayerProfileId(teamId, profile.getId())
                .orElseThrow(TeamMemberNotFoundException::new);

        if (member.isCaptain()) {
            throw new CaptainCannotLeaveTeamException();
        }

        teamMemberRepository.delete(member);
    }

    @Transactional
    public void kick(AccountId accountId, UUID teamId, UUID playerProfileId) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(TeamNotFoundException::new);

        PlayerProfile captain = teamAccessService.requireCaptain(team, accountId);
        PlayerProfileId targetId = PlayerProfileId.from(playerProfileId);

        TeamMember target = teamMemberRepository
                .findByTeamIdAndPlayerProfileId(teamId, targetId)
                .orElseThrow(TeamMemberNotFoundException::new);

        if (target.getPlayerProfileId().equals(captain.getId())) {
            throw new CannotKickCaptainException();
        }

        teamMemberRepository.delete(target);
    }

    @Transactional
    public void transferCaptain(
            AccountId accountId,
            UUID teamId,
            UUID newCaptainProfileId
    ) {
        Team team = teamRepository.findById(teamId)
                .orElseThrow(TeamNotFoundException::new);

        PlayerProfile captain = teamAccessService.requireCaptain(team, accountId);
        PlayerProfileId targetId = PlayerProfileId.from(newCaptainProfileId);

        if (targetId.equals(captain.getId())) {
            throw new InvalidCaptainTransferException();
        }

        TeamMember currentCaptain = teamMemberRepository
                .findByTeamIdAndPlayerProfileId(teamId, captain.getId())
                .orElseThrow(TeamMemberNotFoundException::new);

        TeamMember newCaptain = teamMemberRepository
                .findByTeamIdAndPlayerProfileId(teamId, targetId)
                .orElseThrow(InvalidCaptainTransferException::new);

        currentCaptain.demoteToMember();
        teamMemberRepository.save(currentCaptain);

        newCaptain.promoteToCaptain();
        teamMemberRepository.save(newCaptain);

        team.transferCaptain(targetId);
        teamRepository.save(team);
    }
}
