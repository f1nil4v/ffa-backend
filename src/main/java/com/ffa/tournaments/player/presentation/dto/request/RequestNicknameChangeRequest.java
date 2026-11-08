package com.ffa.tournaments.player.presentation.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record RequestNicknameChangeRequest(

        @NotBlank
        @Size(min = 3, max = 32)
        @Pattern(regexp = "^[A-Za-z0-9_-]+$")
        String newNickname,

        @NotBlank
        @Size(max = 1000)
        String reason
) {
}
