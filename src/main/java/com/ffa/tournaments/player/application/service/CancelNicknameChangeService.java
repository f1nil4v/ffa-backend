package com.ffa.tournaments.player.application.service;

import com.ffa.tournaments.identity.domain.valueobject.AccountId;
import com.ffa.tournaments.player.application.exception.NicknameChangeRequestNotFoundException;
import com.ffa.tournaments.player.application.exception.NicknameChangeRequestNotOwnedException;
import com.ffa.tournaments.player.application.exception.NicknameChangeRequestNotPendingException;
import com.ffa.tournaments.player.application.exception.PlayerProfileNotFoundException;
import com.ffa.tournaments.player.domain.model.NicknameChangeRequest;
import com.ffa.tournaments.player.domain.model.NicknameChangeRequestStatus;
import com.ffa.tournaments.player.domain.model.PlayerProfile;
import com.ffa.tournaments.player.domain.repository.NicknameChangeRequestRepository;
import com.ffa.tournaments.player.domain.repository.PlayerProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class CancelNicknameChangeService {

    private final PlayerProfileRepository playerProfileRepository;
    private final NicknameChangeRequestRepository nicknameChangeRequestRepository;

    public CancelNicknameChangeService(
            PlayerProfileRepository playerProfileRepository,
            NicknameChangeRequestRepository nicknameChangeRequestRepository
    ) {
        this.playerProfileRepository = playerProfileRepository;
        this.nicknameChangeRequestRepository = nicknameChangeRequestRepository;
    }

    @Transactional
    public void cancel(AccountId accountId, UUID requestId) {
        PlayerProfile profile = playerProfileRepository.findByAccountId(accountId)
                .orElseThrow(PlayerProfileNotFoundException::new);

        NicknameChangeRequest request = nicknameChangeRequestRepository.findById(requestId)
                .orElseThrow(NicknameChangeRequestNotFoundException::new);

        if (!request.getPlayerProfileId().equals(profile.getId())) {
            throw new NicknameChangeRequestNotOwnedException();
        }

        if (request.getStatus() != NicknameChangeRequestStatus.PENDING) {
            throw new NicknameChangeRequestNotPendingException();
        }

        request.cancel();

        nicknameChangeRequestRepository.save(request);
    }
}
