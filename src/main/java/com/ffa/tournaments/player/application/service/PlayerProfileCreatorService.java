package com.ffa.tournaments.player.application.service;

import com.ffa.tournaments.identity.domain.valueobject.AccountId;
import com.ffa.tournaments.player.application.exception.NicknameAlreadyTakenException;
import com.ffa.tournaments.player.application.exception.PlayerProfileAlreadyExistsException;
import com.ffa.tournaments.player.application.port.PlayerProfileCreator;
import com.ffa.tournaments.player.domain.model.PlayerNicknameHistory;
import com.ffa.tournaments.player.domain.model.PlayerProfile;
import com.ffa.tournaments.player.domain.repository.PlayerNicknameHistoryRepository;
import com.ffa.tournaments.player.domain.repository.PlayerProfileRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PlayerProfileCreatorService implements PlayerProfileCreator {

    private final PlayerProfileRepository playerProfileRepository;
    private final PlayerNicknameHistoryRepository nicknameHistoryRepository;

    public PlayerProfileCreatorService(
            PlayerProfileRepository playerProfileRepository,
            PlayerNicknameHistoryRepository nicknameHistoryRepository
    ) {
        this.playerProfileRepository = playerProfileRepository;
        this.nicknameHistoryRepository = nicknameHistoryRepository;
    }

    @Override
    @Transactional
    public void create(AccountId accountId, String nickname) {

        String normalizedNickname = NicknameValidator.normalize(nickname);

        if (playerProfileRepository.findByAccountId(accountId).isPresent()) {
            throw new PlayerProfileAlreadyExistsException();
        }

        if (playerProfileRepository.existsByNicknameIgnoreCase(normalizedNickname)) {
            throw new NicknameAlreadyTakenException();
        }

        PlayerProfile profile = PlayerProfile.create(accountId, normalizedNickname);

        playerProfileRepository.save(profile);

        PlayerNicknameHistory history = PlayerNicknameHistory.createInitial(
                profile.getId(),
                profile.getNickname(),
                profile.getCreatedAt()
        );

        nicknameHistoryRepository.save(history);
    }
}