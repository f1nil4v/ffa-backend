package com.ffa.tournaments.support.infrastructure.security;

import com.ffa.tournaments.shared.infrastructure.security.JwtService;
import com.ffa.tournaments.support.application.port.SupportTokenIssuer;
import com.ffa.tournaments.support.domain.valueobject.SupportAccountId;
import org.springframework.stereotype.Component;

@Component
public class JwtSupportTokenIssuer implements SupportTokenIssuer {

    private final JwtService jwtService;

    public JwtSupportTokenIssuer(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public String issue(SupportAccountId supportAccountId) {
        return jwtService.generateToken(supportAccountId.value(), JwtService.ROLE_SUPPORT);
    }
}
