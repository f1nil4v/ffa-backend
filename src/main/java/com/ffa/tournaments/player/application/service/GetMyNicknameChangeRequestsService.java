package com.ffa.tournaments.player.application.service;

import com.ffa.tournaments.identity.domain.valueobject.AccountId;
import com.ffa.tournaments.player.application.exception.PlayerProfileNotFoundException;
import com.ffa.tournaments.player.application.result.NicknameChangeRequestResult;
import com.ffa.tournaments.player.domain.model.PlayerProfile;
import com.ffa.tournaments.player.domain.repository.NicknameChangeRequestRepository;
import com.ffa.tournaments.player.domain.repository.PlayerProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GetMyNicknameChangeRequestsService {

    private final PlayerProfileRepository playerProfileRepository;
    private final NicknameChangeRequestRepository nicknameChangeRequestRepository;

    public GetMyNicknameChangeRequestsService(
            PlayerProfileRepository playerProfileRepository,
            NicknameChangeRequestRepository nicknameChangeRequestRepository
    ) {
        this.playerProfileRepository = playerProfileRepository;
        this.nicknameChangeRequestRepository = nicknameChangeRequestRepository;
    }

    @Transactional(readOnly = true)
    public List<NicknameChangeRequestResult> list(AccountId accountId) {
        PlayerProfile profile = playerProfileRepository.findByAccountId(accountId)
                .orElseThrow(PlayerProfileNotFoundException::new);

        return nicknameChangeRequestRepository.findAllByPlayerProfileId(profile.getId())
                .stream()
                .map(NicknameChangeRequestResult::from)
                .toList();
    }
}
