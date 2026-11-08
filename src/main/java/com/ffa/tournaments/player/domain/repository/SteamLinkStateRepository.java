package com.ffa.tournaments.player.domain.repository;

import com.ffa.tournaments.player.domain.model.SteamLinkState;

import java.util.Optional;
import java.util.UUID;

public interface SteamLinkStateRepository {

    SteamLinkState save(SteamLinkState state);

    Optional<SteamLinkState> findById(UUID id);

    void deleteById(UUID id);
}
