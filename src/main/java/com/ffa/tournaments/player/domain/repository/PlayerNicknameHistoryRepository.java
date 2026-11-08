package com.ffa.tournaments.player.domain.repository;

import com.ffa.tournaments.player.domain.model.PlayerNicknameHistory;
import com.ffa.tournaments.player.domain.valueobject.PlayerProfileId;

import java.util.List;
import java.util.Optional;

public interface PlayerNicknameHistoryRepository {

    PlayerNicknameHistory save(PlayerNicknameHistory history);

    List<PlayerNicknameHistory> findAllByPlayerProfileId(PlayerProfileId playerProfileId);

    Optional<PlayerNicknameHistory> findCurrentByPlayerProfileId(PlayerProfileId playerProfileId);

}