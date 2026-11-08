package com.ffa.tournaments.player.infrastructure.steam;

import com.ffa.tournaments.player.application.exception.InvalidSteamOpenIdResponseException;
import com.ffa.tournaments.player.application.port.SteamOpenIdVerifier;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
public class SteamOpenIdVerifierImpl implements SteamOpenIdVerifier {

    private static final String STEAM_OPENID_ENDPOINT =
            "https://steamcommunity.com/openid/login";

    private static final Pattern CLAIMED_ID_PATTERN = Pattern.compile(
            "^https?://steamcommunity\\.com/openid/id/(\\d{17})$"
    );

    private final RestClient restClient;

    public SteamOpenIdVerifierImpl() {
        this.restClient = RestClient.builder().build();
    }

    @Override
    public String verify(Map<String, String> openIdParameters) {
        if (!"id_res".equals(openIdParameters.get("openid.mode"))) {
            throw new InvalidSteamOpenIdResponseException();
        }

        MultiValueMap<String, String> form = new LinkedMultiValueMap<>();

        openIdParameters.forEach((key, value) -> {
            if (key.startsWith("openid.")) {
                form.add(key, value);
            }
        });

        form.set("openid.mode", "check_authentication");

        String response = restClient.post()
                .uri(STEAM_OPENID_ENDPOINT)
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(form)
                .retrieve()
                .body(String.class);

        if (response == null || !isValid(response)) {
            throw new InvalidSteamOpenIdResponseException();
        }

        return extractSteamId(openIdParameters.get("openid.claimed_id"));
    }

    private boolean isValid(String response) {
        return response.replaceAll("\\s+", "").contains("is_valid:true");
    }

    private String extractSteamId(String claimedId) {
        if (claimedId == null) {
            throw new InvalidSteamOpenIdResponseException();
        }

        Matcher matcher = CLAIMED_ID_PATTERN.matcher(claimedId);

        if (!matcher.matches()) {
            throw new InvalidSteamOpenIdResponseException();
        }

        return matcher.group(1);
    }
}
