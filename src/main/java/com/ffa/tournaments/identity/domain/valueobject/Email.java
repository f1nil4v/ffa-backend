package com.ffa.tournaments.identity.domain.valueobject;

import java.util.Locale;
import java.util.Objects;

public record Email(String value) {

    public Email {
        Objects.requireNonNull(value, "Email cannot be null");

        value = value.trim().toLowerCase(Locale.ROOT);

        if (value.isBlank()) {
            throw new IllegalArgumentException("Email cannot be blank");
        }

        if (!value.matches("^[^\\s@]+@[^\\s@]+\\.[^\\s@]+$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
    }

    @Override
    public String toString() {
        return value;
    }
}
