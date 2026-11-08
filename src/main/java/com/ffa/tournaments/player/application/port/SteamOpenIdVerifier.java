package com.ffa.tournaments.player.application.port;

import java.util.Map;

public interface SteamOpenIdVerifier {

    /**
     * Verifies the OpenID 2.0 response with Steam and returns the SteamID64.
     */
    String verify(Map<String, String> openIdParameters);
}
