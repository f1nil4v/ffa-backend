package com.ffa.tournaments.player.presentation.controller;

import com.ffa.tournaments.identity.domain.valueobject.AccountId;
import com.ffa.tournaments.player.application.port.PlayerProfileCreator;
import com.ffa.tournaments.player.application.service.GetPlayerProfileService;
import com.ffa.tournaments.player.presentation.dto.request.CreatePlayerProfileRequest;
import com.ffa.tournaments.player.presentation.dto.response.PlayerProfileResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/player")
public class PlayerController {

    private final PlayerProfileCreator playerProfileCreator;
    private final GetPlayerProfileService getPlayerProfileService;

    public PlayerController(
            PlayerProfileCreator playerProfileCreator,
            GetPlayerProfileService getPlayerProfileService
    ) {
        this.playerProfileCreator = playerProfileCreator;
        this.getPlayerProfileService = getPlayerProfileService;
    }

    @GetMapping("/me")
    public PlayerProfileResponse me(Authentication authentication) {
        UUID accountId = (UUID) authentication.getPrincipal();

        var result = getPlayerProfileService.get(AccountId.from(accountId));

        return new PlayerProfileResponse(
                result.profileExists(),
                result.id(),
                result.nickname(),
                result.avatarUrl(),
                result.steamId()
        );
    }

    @PostMapping("/profile")
    @ResponseStatus(HttpStatus.CREATED)
    public void createProfile(
            Authentication authentication,
            @Valid @RequestBody CreatePlayerProfileRequest request
    ) {
        UUID accountId = (UUID) authentication.getPrincipal();

        playerProfileCreator.create(
                AccountId.from(accountId),
                request.nickname()
        );
    }
}