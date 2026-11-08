package com.ffa.tournaments.player.presentation.controller;

import com.ffa.tournaments.identity.domain.valueobject.AccountId;
import com.ffa.tournaments.player.application.service.SteamLinkService;
import com.ffa.tournaments.player.presentation.dto.response.SteamLinkStartResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.util.Map;
import java.util.UUID;

@RestController
@RequestMapping("/api/player/steam/link")
public class SteamLinkController {

    private final SteamLinkService steamLinkService;

    public SteamLinkController(SteamLinkService steamLinkService) {
        this.steamLinkService = steamLinkService;
    }

    @GetMapping("/start")
    public SteamLinkStartResponse start(Authentication authentication) {
        AccountId accountId = AccountId.from((UUID) authentication.getPrincipal());

        return new SteamLinkStartResponse(
                steamLinkService.start(accountId).redirectUrl()
        );
    }

    @GetMapping("/callback")
    public ResponseEntity<Void> callback(
            @RequestParam("state") UUID state,
            @RequestParam Map<String, String> parameters
    ) {
        String redirectUrl;

        try {
            redirectUrl = steamLinkService.callback(state, parameters);
        } catch (RuntimeException exception) {
            redirectUrl = steamLinkService.errorRedirectUrl();
        }

        return ResponseEntity.status(HttpStatus.FOUND)
                .location(URI.create(redirectUrl))
                .build();
    }
}
