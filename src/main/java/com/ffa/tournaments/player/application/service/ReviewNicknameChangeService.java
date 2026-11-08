package com.ffa.tournaments.player.application.service;

import com.ffa.tournaments.player.application.exception.NicknameAlreadyTakenException;
import com.ffa.tournaments.player.application.exception.NicknameChangeRequestNotFoundException;
import com.ffa.tournaments.player.application.exception.NicknameChangeRequestNotPendingException;
import com.ffa.tournaments.player.application.result.NicknameChangeRequestResult;
import com.ffa.tournaments.player.domain.model.NicknameChangeRequest;
import com.ffa.tournaments.player.domain.model.NicknameChangeRequestStatus;
import com.ffa.tournaments.player.domain.model.PlayerNicknameHistory;
import com.ffa.tournaments.player.domain.model.PlayerProfile;
import com.ffa.tournaments.player.domain.repository.NicknameChangeRequestRepository;
import com.ffa.tournaments.player.domain.repository.PlayerNicknameHistoryRepository;
import com.ffa.tournaments.player.domain.repository.PlayerProfileRepository;
import com.ffa.tournaments.support.domain.valueobject.SupportAccountId;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class ReviewNicknameChangeService {

    private final NicknameChangeRequestRepository nicknameChangeRequestRepository;
    private final PlayerProfileRepository playerProfileRepository;
    private final PlayerNicknameHistoryRepository nicknameHistoryRepository;

    public ReviewNicknameChangeService(
            NicknameChangeRequestRepository nicknameChangeRequestRepository,
            PlayerProfileRepository playerProfileRepository,
            PlayerNicknameHistoryRepository nicknameHistoryRepository
    ) {
        this.nicknameChangeRequestRepository = nicknameChangeRequestRepository;
        this.playerProfileRepository = playerProfileRepository;
        this.nicknameHistoryRepository = nicknameHistoryRepository;
    }

    @Transactional(readOnly = true)
    public List<NicknameChangeRequestResult> list(NicknameChangeRequestStatus status) {
        return nicknameChangeRequestRepository.findAllByStatus(status)
                .stream()
                .map(NicknameChangeRequestResult::from)
                .toList();
    }

    @Transactional
    public NicknameChangeRequestResult approve(
            UUID requestId,
            SupportAccountId reviewerId
    ) {
        NicknameChangeRequest request = findPendingRequest(requestId);

        PlayerProfile profile = playerProfileRepository.findById(request.getPlayerProfileId())
                .orElseThrow(IllegalStateException::new);

        if (playerProfileRepository.existsByNicknameIgnoreCase(request.getNewNickname())) {
            throw new NicknameAlreadyTakenException();
        }

        Instant now = Instant.now();

        profile.changeNickname(request.getNewNickname());
        playerProfileRepository.save(profile);

        PlayerNicknameHistory currentHistory = nicknameHistoryRepository
                .findCurrentByPlayerProfileId(profile.getId())
                .orElseThrow(IllegalStateException::new);

        currentHistory.close(now);
        nicknameHistoryRepository.save(currentHistory);

        nicknameHistoryRepository.save(
                PlayerNicknameHistory.createInitial(
                        profile.getId(),
                        request.getNewNickname(),
                        now
                )
        );

        request.approve(reviewerId, now);

        return NicknameChangeRequestResult.from(
                nicknameChangeRequestRepository.save(request)
        );
    }

    @Transactional
    public NicknameChangeRequestResult reject(
            UUID requestId,
            SupportAccountId reviewerId
    ) {
        NicknameChangeRequest request = findPendingRequest(requestId);

        request.reject(reviewerId, Instant.now());

        return NicknameChangeRequestResult.from(
                nicknameChangeRequestRepository.save(request)
        );
    }

    private NicknameChangeRequest findPendingRequest(UUID requestId) {
        NicknameChangeRequest request = nicknameChangeRequestRepository.findById(requestId)
                .orElseThrow(NicknameChangeRequestNotFoundException::new);

        if (request.getStatus() != NicknameChangeRequestStatus.PENDING) {
            throw new NicknameChangeRequestNotPendingException();
        }

        return request;
    }
}
