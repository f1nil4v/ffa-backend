package com.ffa.tournaments.team.application.service;

import com.ffa.tournaments.identity.domain.valueobject.AccountId;
import com.ffa.tournaments.player.application.service.PlayerProfileProvider;
import com.ffa.tournaments.player.domain.model.PlayerProfile;
import com.ffa.tournaments.team.application.exception.NotTeamCaptainException;
import com.ffa.tournaments.team.domain.model.Team;
import org.springframework.stereotype.Service;

@Service
public class TeamAccessService {

    private final PlayerProfileProvider playerProfileProvider;

    public TeamAccessService(PlayerProfileProvider playerProfileProvider) {
        this.playerProfileProvider = playerProfileProvider;
    }

    public PlayerProfile requireCaptain(Team team, AccountId accountId) {
        PlayerProfile profile = playerProfileProvider.getByAccountId(accountId);

        if (!team.isCaptain(profile.getId())) {
            throw new NotTeamCaptainException();
        }

        return profile;
    }
}
