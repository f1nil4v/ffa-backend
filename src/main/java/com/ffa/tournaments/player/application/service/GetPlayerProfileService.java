package com.ffa.tournaments.player.application.service;

import com.ffa.tournaments.identity.domain.valueobject.AccountId;
import com.ffa.tournaments.player.domain.repository.PlayerProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class GetPlayerProfileService {

    private final PlayerProfileRepository playerProfileRepository;

    public GetPlayerProfileService(PlayerProfileRepository playerProfileRepository) {
        this.playerProfileRepository = playerProfileRepository;
    }

    @Transactional(readOnly = true)
    public PlayerProfileResult get(AccountId accountId) {
        return playerProfileRepository.findByAccountId(accountId)
                .map(profile -> new PlayerProfileResult(
                        true,
                        profile.getId().value(),
                        profile.getNickname(),
                        profile.getAvatarUrl(),
                        profile.getSteamId()
                ))
                .orElseGet(() -> new PlayerProfileResult(
                        false,
                        null,
                        null,
                        null,
                        null
                ));
    }

    public record PlayerProfileResult(
            boolean profileExists,
            java.util.UUID id,
            String nickname,
            String avatarUrl,
            String steamId
    ) {}
}