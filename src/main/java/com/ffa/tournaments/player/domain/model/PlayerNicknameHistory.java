package com.ffa.tournaments.player.domain.model;

import com.ffa.tournaments.player.domain.valueobject.PlayerProfileId;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class PlayerNicknameHistory {

    private final UUID id;
    private final PlayerProfileId playerProfileId;
    private final String nickname;
    private final Instant validFrom;
    private Instant validTo;

    private PlayerNicknameHistory(
            UUID id,
            PlayerProfileId playerProfileId,
            String nickname,
            Instant validFrom,
            Instant validTo
    ) {
        this.id = Objects.requireNonNull(id);
        this.playerProfileId = Objects.requireNonNull(playerProfileId);
        this.nickname = Objects.requireNonNull(nickname);
        this.validFrom = Objects.requireNonNull(validFrom);
        this.validTo = validTo;

        if (nickname.isBlank()) {
            throw new IllegalArgumentException("Nickname cannot be blank");
        }

        if (validTo != null && !validTo.isAfter(validFrom)) {
            throw new IllegalArgumentException("Invalid nickname history period");
        }
    }

    public static PlayerNicknameHistory createInitial(
            PlayerProfileId playerProfileId,
            String nickname,
            Instant validFrom
    ) {
        return new PlayerNicknameHistory(
                UUID.randomUUID(),
                playerProfileId,
                nickname,
                validFrom,
                null
        );
    }

    public static PlayerNicknameHistory restore(
            UUID id,
            PlayerProfileId playerProfileId,
            String nickname,
            Instant validFrom,
            Instant validTo
    ) {
        return new PlayerNicknameHistory(
                id,
                playerProfileId,
                nickname,
                validFrom,
                validTo
        );
    }

    public void close(Instant validTo) {
        Objects.requireNonNull(validTo);

        if (this.validTo != null) {
            throw new IllegalStateException("Nickname history is already closed");
        }

        if (!validTo.isAfter(validFrom)) {
            throw new IllegalArgumentException("Invalid nickname history period");
        }

        this.validTo = validTo;
    }

    public UUID getId() {
        return id;
    }

    public PlayerProfileId getPlayerProfileId() {
        return playerProfileId;
    }

    public String getNickname() {
        return nickname;
    }

    public Instant getValidFrom() {
        return validFrom;
    }

    public Instant getValidTo() {
        return validTo;
    }

    public boolean isCurrent() {
        return validTo == null;
    }
}