package com.ffa.tournaments.player.infrastructure.persistence.adapter;

import com.ffa.tournaments.player.domain.model.NicknameChangeRequest;
import com.ffa.tournaments.player.domain.model.NicknameChangeRequestStatus;
import com.ffa.tournaments.player.domain.repository.NicknameChangeRequestRepository;
import com.ffa.tournaments.player.domain.valueobject.PlayerProfileId;
import com.ffa.tournaments.player.infrastructure.persistence.mapper.NicknameChangeRequestMapper;
import com.ffa.tournaments.player.infrastructure.persistence.repository.SpringDataNicknameChangeRequestRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class NicknameChangeRequestRepositoryAdapter
        implements NicknameChangeRequestRepository {

    private final SpringDataNicknameChangeRequestRepository repository;
    private final NicknameChangeRequestMapper mapper;

    public NicknameChangeRequestRepositoryAdapter(
            SpringDataNicknameChangeRequestRepository repository,
            NicknameChangeRequestMapper mapper
    ) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public NicknameChangeRequest save(NicknameChangeRequest request) {
        return mapper.toDomain(
                repository.saveAndFlush(mapper.toEntity(request))
        );
    }

    @Override
    public Optional<NicknameChangeRequest> findById(UUID id) {
        return repository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<NicknameChangeRequest> findAllByStatus(
            NicknameChangeRequestStatus status
    ) {
        return repository.findAllByStatus(status.name())
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<NicknameChangeRequest> findAllByPlayerProfileId(
            PlayerProfileId playerProfileId
    ) {
        return repository
                .findAllByPlayerProfileIdOrderByCreatedAtDesc(playerProfileId.value())
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsPendingByPlayerProfileId(
            PlayerProfileId playerProfileId
    ) {
        return repository.existsByPlayerProfileIdAndStatus(
                playerProfileId.value(),
                NicknameChangeRequestStatus.PENDING.name()
        );
    }
}