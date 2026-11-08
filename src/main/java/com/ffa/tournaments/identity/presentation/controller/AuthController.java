package com.ffa.tournaments.identity.presentation.controller;

import com.ffa.tournaments.identity.application.command.LoginCommand;
import com.ffa.tournaments.identity.application.command.RegisterCommand;
import com.ffa.tournaments.identity.application.result.LoginResult;
import com.ffa.tournaments.identity.application.result.RegisterResult;
import com.ffa.tournaments.identity.application.service.AuthService;
import com.ffa.tournaments.identity.application.service.GetCurrentAccountService;
import com.ffa.tournaments.identity.presentation.dto.request.auth.LoginRequest;
import com.ffa.tournaments.identity.presentation.dto.request.auth.RegisterRequest;
import com.ffa.tournaments.identity.presentation.dto.response.account.CurrentAccountResponse;
import com.ffa.tournaments.identity.presentation.dto.response.auth.LoginResponse;
import com.ffa.tournaments.identity.presentation.dto.response.auth.RegisterResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;
    private final GetCurrentAccountService getCurrentAccountService;

    public AuthController(AuthService authService, GetCurrentAccountService getCurrentAccountService) {
        this.authService = authService;
        this.getCurrentAccountService = getCurrentAccountService;
    }

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public RegisterResponse register(@Valid @RequestBody RegisterRequest request) {
        RegisterResult result = authService.register(new RegisterCommand(request.email(), request.password()));
        return new RegisterResponse(result.id(), result.email(), result.emailVerified());
    }

    @PostMapping("/login")
    public LoginResponse login(@Valid @RequestBody LoginRequest request) {
        LoginResult result = authService.login(new LoginCommand(request.email(), request.password()));
        return new LoginResponse(result.id(), result.email(), result.accessToken(), result.tokenType());
    }

    @GetMapping("/me")
    public CurrentAccountResponse me(Authentication authentication) {
        UUID accountId = (UUID) authentication.getPrincipal();
        var result = getCurrentAccountService.get(accountId);

        return new CurrentAccountResponse(result.id(), result.email());
    }
}