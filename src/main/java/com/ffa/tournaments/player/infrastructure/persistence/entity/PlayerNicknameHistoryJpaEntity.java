package com.ffa.tournaments.player.infrastructure.persistence.entity;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "player_nickname_history")
public class PlayerNicknameHistoryJpaEntity {

    @Id
    private UUID id;

    @Column(name = "player_profile_id", nullable = false)
    private UUID playerProfileId;

    @Column(nullable = false, length = 32)
    private String nickname;

    @Column(name = "valid_from", nullable = false)
    private Instant validFrom;

    @Column(name = "valid_to")
    private Instant validTo;

    protected PlayerNicknameHistoryJpaEntity() {
    }

    public PlayerNicknameHistoryJpaEntity(
            UUID id,
            UUID playerProfileId,
            String nickname,
            Instant validFrom,
            Instant validTo
    ) {
        this.id = id;
        this.playerProfileId = playerProfileId;
        this.nickname = nickname;
        this.validFrom = validFrom;
        this.validTo = validTo;
    }

    public UUID getId() {
        return id;
    }

    public UUID getPlayerProfileId() {
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

    public void setValidTo(Instant validTo) {
        this.validTo = validTo;
    }
}