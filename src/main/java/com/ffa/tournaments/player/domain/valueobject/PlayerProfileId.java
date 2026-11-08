package com.ffa.tournaments.player.domain.valueobject;

import java.util.Objects;
import java.util.UUID;

public record PlayerProfileId(UUID value) {

    public PlayerProfileId {
        Objects.requireNonNull(value, "Player profile ID cannot be null");
    }

    public static PlayerProfileId generate() {
        return new PlayerProfileId(UUID.randomUUID());
    }

    public static PlayerProfileId from(UUID value) {
        return new PlayerProfileId(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}