package com.ffa.tournaments.support.domain.valueobject;

import java.util.Objects;
import java.util.UUID;

public record SupportAccountId(UUID value) {

    public SupportAccountId {
        Objects.requireNonNull(value, "Support account ID cannot be null");
    }

    public static SupportAccountId generate() {
        return new SupportAccountId(UUID.randomUUID());
    }

    public static SupportAccountId from(UUID value) {
        return new SupportAccountId(value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
