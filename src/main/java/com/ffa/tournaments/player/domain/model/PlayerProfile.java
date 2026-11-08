package com.ffa.tournaments.player.domain.model;

import com.ffa.tournaments.identity.domain.valueobject.AccountId;
import com.ffa.tournaments.player.domain.valueobject.PlayerProfileId;

import java.time.Instant;
import java.util.Objects;

public class PlayerProfile {

    private final PlayerProfileId id;
    private final AccountId accountId;
    private String nickname;
    private String avatarUrl;
    private String steamId;
    private Instant steamLinkedAt;
    private final Instant createdAt;

    private PlayerProfile(
            PlayerProfileId id,
            AccountId accountId,
            String nickname,
            String avatarUrl,
            String steamId,
            Instant steamLinkedAt,
            Instant createdAt
    ) {
        this.id = Objects.requireNonNull(id);
        this.accountId = Objects.requireNonNull(accountId);
        this.nickname = validateNickname(nickname);
        this.avatarUrl = avatarUrl;
        this.steamId = steamId;
        this.steamLinkedAt = steamLinkedAt;
        this.createdAt = Objects.requireNonNull(createdAt);
    }

    public static PlayerProfile create(AccountId accountId, String nickname) {
        return new PlayerProfile(
                PlayerProfileId.generate(),
                accountId,
                nickname,
                null,
                null,
                null,
                Instant.now()
        );
    }

    public static PlayerProfile restore(
            PlayerProfileId id,
            AccountId accountId,
            String nickname,
            String avatarUrl,
            String steamId,
            Instant steamLinkedAt,
            Instant createdAt
    ) {
        return new PlayerProfile(
                id,
                accountId,
                nickname,
                avatarUrl,
                steamId,
                steamLinkedAt,
                createdAt
        );
    }

    private static String validateNickname(String nickname) {
        Objects.requireNonNull(nickname, "Nickname cannot be null");

        String normalized = nickname.trim();

        if (normalized.isBlank()) {
            throw new IllegalArgumentException("Nickname cannot be blank");
        }

        if (normalized.length() > 32) {
            throw new IllegalArgumentException("Nickname cannot exceed 32 characters");
        }

        return normalized;
    }

    public void changeAvatar(String avatarUrl) {
        this.avatarUrl = avatarUrl;
    }

    public void changeNickname(String nickname) {
        this.nickname = validateNickname(nickname);
    }

    public void linkSteam(String steamId, Instant linkedAt) {
        Objects.requireNonNull(steamId, "Steam ID cannot be null");
        Objects.requireNonNull(linkedAt, "Steam link timestamp cannot be null");

        if (!steamId.matches("^\\d{17}$")) {
            throw new IllegalArgumentException("Invalid Steam ID");
        }

        if (this.steamId != null) {
            throw new IllegalStateException("Steam account is already linked");
        }

        this.steamId = steamId;
        this.steamLinkedAt = linkedAt;
    }

    public boolean hasSteamLinked() {
        return steamId != null;
    }

    public PlayerProfileId getId() {
        return id;
    }

    public AccountId getAccountId() {
        return accountId;
    }

    public String getNickname() {
        return nickname;
    }

    public String getAvatarUrl() {
        return avatarUrl;
    }

    public String getSteamId() {
        return steamId;
    }

    public Instant getSteamLinkedAt() {
        return steamLinkedAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
