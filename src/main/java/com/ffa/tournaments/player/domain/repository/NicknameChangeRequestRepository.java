package com.ffa.tournaments.player.domain.repository;

import com.ffa.tournaments.player.domain.model.NicknameChangeRequest;
import com.ffa.tournaments.player.domain.model.NicknameChangeRequestStatus;
import com.ffa.tournaments.player.domain.valueobject.PlayerProfileId;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface NicknameChangeRequestRepository {

    NicknameChangeRequest save(NicknameChangeRequest request);

    Optional<NicknameChangeRequest> findById(UUID id);

    List<NicknameChangeRequest> findAllByStatus(
            NicknameChangeRequestStatus status
    );

    List<NicknameChangeRequest> findAllByPlayerProfileId(
            PlayerProfileId playerProfileId
    );

    boolean existsPendingByPlayerProfileId(
            PlayerProfileId playerProfileId
    );
}