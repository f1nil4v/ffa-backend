package com.ffa.tournaments.support.presentation.controller;

import com.ffa.tournaments.support.application.result.SupportLoginResult;
import com.ffa.tournaments.support.application.service.SupportAuthService;
import com.ffa.tournaments.support.presentation.dto.request.SupportLoginRequest;
import com.ffa.tournaments.support.presentation.dto.response.SupportLoginResponse;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/support/auth")
public class SupportAuthController {

    private final SupportAuthService supportAuthService;

    public SupportAuthController(SupportAuthService supportAuthService) {
        this.supportAuthService = supportAuthService;
    }

    @PostMapping("/login")
    public SupportLoginResponse login(@Valid @RequestBody SupportLoginRequest request) {
        SupportLoginResult result = supportAuthService.login(
                request.email(),
                request.password()
        );

        return new SupportLoginResponse(
                result.id(),
                result.email(),
                result.displayName(),
                result.accessToken(),
                result.tokenType()
        );
    }
}
