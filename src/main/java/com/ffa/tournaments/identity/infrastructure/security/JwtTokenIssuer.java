package com.ffa.tournaments.identity.infrastructure.security;

import com.ffa.tournaments.identity.application.port.TokenIssuer;
import com.ffa.tournaments.identity.domain.valueobject.AccountId;
import com.ffa.tournaments.shared.infrastructure.security.JwtService;
import org.springframework.stereotype.Component;

@Component
public class JwtTokenIssuer implements TokenIssuer {

    private final JwtService jwtService;

    public JwtTokenIssuer(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @Override
    public String generateToken(AccountId accountId) {
        return jwtService.generateToken(accountId);
    }
}