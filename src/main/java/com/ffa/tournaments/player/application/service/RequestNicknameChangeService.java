package com.ffa.tournaments.player.application.service;

import com.ffa.tournaments.identity.domain.valueobject.AccountId;
import com.ffa.tournaments.player.application.exception.NicknameAlreadyTakenException;
import com.ffa.tournaments.player.application.exception.NicknameUnchangedException;
import com.ffa.tournaments.player.application.exception.PendingNicknameChangeRequestExistsException;
import com.ffa.tournaments.player.application.exception.PlayerProfileNotFoundException;
import com.ffa.tournaments.player.application.result.NicknameChangeRequestResult;
import com.ffa.tournaments.player.domain.model.NicknameChangeRequest;
import com.ffa.tournaments.player.domain.model.PlayerProfile;
import com.ffa.tournaments.player.domain.repository.NicknameChangeRequestRepository;
import com.ffa.tournaments.player.domain.repository.PlayerProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RequestNicknameChangeService {

    private static final int MAX_REASON_LENGTH = 1000;

    private final PlayerProfileRepository playerProfileRepository;
    private final NicknameChangeRequestRepository nicknameChangeRequestRepository;

    public RequestNicknameChangeService(
            PlayerProfileRepository playerProfileRepository,
            NicknameChangeRequestRepository nicknameChangeRequestRepository
    ) {
        this.playerProfileRepository = playerProfileRepository;
        this.nicknameChangeRequestRepository = nicknameChangeRequestRepository;
    }

    @Transactional
    public NicknameChangeRequestResult request(
            AccountId accountId,
            String newNickname,
            String reason
    ) {
        PlayerProfile profile = playerProfileRepository.findByAccountId(accountId)
                .orElseThrow(PlayerProfileNotFoundException::new);

        String normalizedNickname = NicknameValidator.normalize(newNickname);
        String normalizedReason = normalizeReason(reason);

        if (profile.getNickname().equalsIgnoreCase(normalizedNickname)) {
            throw new NicknameUnchangedException();
        }

        if (playerProfileRepository.existsByNicknameIgnoreCase(normalizedNickname)) {
            throw new NicknameAlreadyTakenException();
        }

        if (nicknameChangeRequestRepository.existsPendingByPlayerProfileId(profile.getId())) {
            throw new PendingNicknameChangeRequestExistsException();
        }

        NicknameChangeRequest request = NicknameChangeRequest.create(
                profile.getId(),
                profile.getNickname(),
                normalizedNickname,
                normalizedReason
        );

        return NicknameChangeRequestResult.from(
                nicknameChangeRequestRepository.save(request)
        );
    }

    private String normalizeReason(String reason) {
        String normalized = reason == null ? "" : reason.trim();

        if (normalized.isBlank()) {
            throw new IllegalArgumentException("Reason cannot be blank");
        }

        if (normalized.length() > MAX_REASON_LENGTH) {
            throw new IllegalArgumentException(
                    "Reason cannot exceed " + MAX_REASON_LENGTH + " characters"
            );
        }

        return normalized;
    }
}
