package com.ffa.tournaments.player.application.service;

import com.ffa.tournaments.identity.domain.valueobject.AccountId;
import com.ffa.tournaments.player.application.exception.PlayerProfileNotFoundException;
import com.ffa.tournaments.player.domain.model.PlayerProfile;
import com.ffa.tournaments.player.domain.repository.PlayerProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PlayerProfileProvider {

    private final PlayerProfileRepository playerProfileRepository;

    public PlayerProfileProvider(PlayerProfileRepository playerProfileRepository) {
        this.playerProfileRepository = playerProfileRepository;
    }

    @Transactional(readOnly = true)
    public PlayerProfile getByAccountId(AccountId accountId) {
        return playerProfileRepository.findByAccountId(accountId)
                .orElseThrow(PlayerProfileNotFoundException::new);
    }
}
