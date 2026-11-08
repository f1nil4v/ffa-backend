package com.ffa.tournaments.player.domain.repository;

import com.ffa.tournaments.identity.domain.valueobject.AccountId;
import com.ffa.tournaments.player.domain.model.PlayerProfile;
import com.ffa.tournaments.player.domain.valueobject.PlayerProfileId;

import java.util.Optional;

public interface PlayerProfileRepository {

    PlayerProfile save(PlayerProfile profile);

    Optional<PlayerProfile> findById(PlayerProfileId id);

    Optional<PlayerProfile> findByAccountId(AccountId accountId);

    boolean existsByNicknameIgnoreCase(String nickname);

    boolean existsBySteamId(String steamId);
}