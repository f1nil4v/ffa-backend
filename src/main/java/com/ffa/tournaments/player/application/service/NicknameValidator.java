package com.ffa.tournaments.player.application.service;

import com.ffa.tournaments.player.application.exception.InvalidNicknameException;

import java.util.regex.Pattern;

public final class NicknameValidator {

    private static final Pattern NICKNAME_PATTERN = Pattern.compile("^[A-Za-z0-9_-]{3,32}$");

    private NicknameValidator() {
    }

    public static String normalize(String nickname) {
        String normalized = nickname == null ? "" : nickname.trim();

        if (!NICKNAME_PATTERN.matcher(normalized).matches()) {
            throw new InvalidNicknameException();
        }

        return normalized;
    }
}
