package com.ffa.tournaments.player.infrastructure.persistence.entity;

import jakarta.persistence.*;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "player_profiles")
public class PlayerProfileJpaEntity {

    @Id
    private UUID id;

    @Column(name = "account_id", nullable = false, unique = true)
    private UUID accountId;

    @Column(nullable = false, length = 32)
    private String nickname;

    @Column(name = "avatar_url")
    private String avatarUrl;

    @Column(name = "steam_id", length = 17)
    private String steamId;

    @Column(name = "steam_linked_at")
    private Instant steamLinkedAt;

    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    protected PlayerProfileJpaEntity() {
    }

    public PlayerProfileJpaEntity(
            UUID id,
            UUID accountId,
            String nickname,
            String avatarUrl,
            String steamId,
            Instant steamLinkedAt,
            Instant createdAt
    ) {
        this.id = id;
        this.accountId = accountId;
        this.nickname = nickname;
        this.avatarUrl = avatarUrl;
        this.steamId = steamId;
        this.steamLinkedAt = steamLinkedAt;
        this.createdAt = createdAt;
    }

    public UUID getId() { return id; }
    public UUID getAccountId() { return accountId; }
    public String getNickname() { return nickname; }
    public String getAvatarUrl() { return avatarUrl; }
    public String getSteamId() { return steamId; }
    public Instant getSteamLinkedAt() { return steamLinkedAt; }
    public Instant getCreatedAt() { return createdAt; }
}
