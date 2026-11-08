package com.ffa.tournaments.player.infrastructure.persistence.adapter;

import com.ffa.tournaments.identity.domain.valueobject.AccountId;
import com.ffa.tournaments.player.domain.model.PlayerProfile;
import com.ffa.tournaments.player.domain.repository.PlayerProfileRepository;
import com.ffa.tournaments.player.domain.valueobject.PlayerProfileId;
import com.ffa.tournaments.player.application.exception.SteamAccountAlreadyLinkedException;
import com.ffa.tournaments.player.infrastructure.persistence.entity.PlayerProfileJpaEntity;
import com.ffa.tournaments.player.infrastructure.persistence.mapper.PlayerProfileMapper;
import com.ffa.tournaments.player.infrastructure.persistence.repository.SpringDataPlayerProfileRepository;
import org.springframework.stereotype.Repository;

import com.ffa.tournaments.player.infrastructure.persistence.exception.DuplicateNicknameException;
import org.springframework.dao.DataIntegrityViolationException;
import org.postgresql.util.PSQLException;

import java.util.Optional;

@Repository
public class PlayerProfileRepositoryAdapter implements PlayerProfileRepository {

    private final SpringDataPlayerProfileRepository jpaRepository;
    private final PlayerProfileMapper mapper;

    public PlayerProfileRepositoryAdapter(SpringDataPlayerProfileRepository jpaRepository, PlayerProfileMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    private boolean isUniqueViolation(Throwable exception, String constraintName) {
        Throwable cause = exception;

        while (cause != null) {
            if (cause instanceof PSQLException postgresException) {
                return "23505".equals(postgresException.getSQLState())
                        && postgresException.getServerErrorMessage() != null
                        && constraintName.equals(
                        postgresException.getServerErrorMessage().getConstraint()
                );
            }

            cause = cause.getCause();
        }

        return false;
    }

    @Override
    public PlayerProfile save(PlayerProfile profile) {
        PlayerProfileJpaEntity entity = mapper.toEntity(profile);

        try {
            PlayerProfileJpaEntity savedEntity = jpaRepository.saveAndFlush(entity);
            return mapper.toDomain(savedEntity);
        } catch (DataIntegrityViolationException exception) {
            if (isUniqueViolation(exception, "idx_player_profiles_nickname_lower")) {
                throw new DuplicateNicknameException(exception);
            }

            if (isUniqueViolation(exception, "ux_player_profiles_steam_id")) {
                throw new SteamAccountAlreadyLinkedException();
            }

            throw exception;
        }
    }

    @Override
    public Optional<PlayerProfile> findByAccountId(AccountId accountId) {
        return jpaRepository.findByAccountId(accountId.value()).map(mapper::toDomain);
    }

    @Override
    public Optional<PlayerProfile> findById(PlayerProfileId id) {
        return jpaRepository.findById(id.value()).map(mapper::toDomain);
    }

    @Override
    public boolean existsByNicknameIgnoreCase(String nickname) {
        return jpaRepository.existsByNicknameIgnoreCase(nickname);
    }

    @Override
    public boolean existsBySteamId(String steamId) {
        return jpaRepository.existsBySteamId(steamId);
    }
}