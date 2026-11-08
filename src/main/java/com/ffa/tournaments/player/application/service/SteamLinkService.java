package com.ffa.tournaments.player.application.service;

import com.ffa.tournaments.identity.domain.valueobject.AccountId;
import com.ffa.tournaments.player.application.exception.PlayerProfileNotFoundException;
import com.ffa.tournaments.player.application.exception.SteamAccountAlreadyLinkedException;
import com.ffa.tournaments.player.application.exception.SteamLinkStateNotFoundException;
import com.ffa.tournaments.player.application.port.SteamOpenIdVerifier;
import com.ffa.tournaments.player.application.result.SteamLinkStartResult;
import com.ffa.tournaments.player.domain.model.PlayerProfile;
import com.ffa.tournaments.player.domain.model.SteamLinkState;
import com.ffa.tournaments.player.domain.repository.PlayerProfileRepository;
import com.ffa.tournaments.player.domain.repository.SteamLinkStateRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.Instant;
import java.util.Map;
import java.util.UUID;

@Service
public class SteamLinkService {

    private static final String STEAM_OPENID_ENDPOINT =
            "https://steamcommunity.com/openid/login";

    private static final long STATE_TTL_SECONDS = 600;

    private final PlayerProfileRepository playerProfileRepository;
    private final SteamLinkStateRepository steamLinkStateRepository;
    private final SteamOpenIdVerifier steamOpenIdVerifier;
    private final String realm;
    private final String returnUrl;
    private final String successUrl;
    private final String errorUrl;

    public SteamLinkService(
            PlayerProfileRepository playerProfileRepository,
            SteamLinkStateRepository steamLinkStateRepository,
            SteamOpenIdVerifier steamOpenIdVerifier,
            @Value("${app.steam.realm}") String realm,
            @Value("${app.steam.return-url}") String returnUrl,
            @Value("${app.steam.frontend-success-url}") String successUrl,
            @Value("${app.steam.frontend-error-url}") String errorUrl
    ) {
        this.playerProfileRepository = playerProfileRepository;
        this.steamLinkStateRepository = steamLinkStateRepository;
        this.steamOpenIdVerifier = steamOpenIdVerifier;
        this.realm = realm;
        this.returnUrl = returnUrl;
        this.successUrl = successUrl;
        this.errorUrl = errorUrl;
    }

    @Transactional
    public SteamLinkStartResult start(AccountId accountId) {
        PlayerProfile profile = playerProfileRepository.findByAccountId(accountId)
                .orElseThrow(PlayerProfileNotFoundException::new);

        if (profile.hasSteamLinked()) {
            throw new SteamAccountAlreadyLinkedException();
        }

        SteamLinkState state = SteamLinkState.create(
                accountId,
                Instant.now().plusSeconds(STATE_TTL_SECONDS)
        );

        steamLinkStateRepository.save(state);

        return new SteamLinkStartResult(buildSteamRedirectUrl(state.getId()));
    }

    @Transactional
    public String callback(UUID stateId, Map<String, String> openIdParameters) {
        SteamLinkState state = steamLinkStateRepository.findById(stateId)
                .orElseThrow(SteamLinkStateNotFoundException::new);

        Instant now = Instant.now();

        if (state.isExpired(now)) {
            steamLinkStateRepository.deleteById(stateId);
            throw new SteamLinkStateNotFoundException();
        }

        PlayerProfile profile = playerProfileRepository.findByAccountId(state.getAccountId())
                .orElseThrow(PlayerProfileNotFoundException::new);

        if (profile.hasSteamLinked()) {
            throw new SteamAccountAlreadyLinkedException();
        }

        String steamId = steamOpenIdVerifier.verify(openIdParameters);

        if (playerProfileRepository.existsBySteamId(steamId)) {
            throw new SteamAccountAlreadyLinkedException();
        }

        profile.linkSteam(steamId, now);
        playerProfileRepository.save(profile);

        steamLinkStateRepository.deleteById(stateId);

        return successUrl;
    }

    public String errorRedirectUrl() {
        return errorUrl;
    }

    private String buildSteamRedirectUrl(UUID stateId) {
        String returnTo = UriComponentsBuilder.fromUriString(returnUrl)
                .queryParam("state", stateId)
                .build()
                .toUriString();

        return UriComponentsBuilder.fromUriString(STEAM_OPENID_ENDPOINT)
                .queryParam("openid.ns", "http://specs.openid.net/auth/2.0")
                .queryParam("openid.mode", "checkid_setup")
                .queryParam("openid.return_to", returnTo)
                .queryParam("openid.realm", realm)
                .queryParam("openid.identity", "http://specs.openid.net/auth/2.0/identifier_select")
                .queryParam("openid.claimed_id", "http://specs.openid.net/auth/2.0/identifier_select")
                .encode()
                .toUriString();
    }
}
