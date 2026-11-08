package com.ffa.tournaments.identity.presentation.controller;

import com.ffa.tournaments.identity.application.service.PasswordResetService;
import com.ffa.tournaments.identity.presentation.dto.request.password.RequestPasswordResetRequest;
import com.ffa.tournaments.identity.presentation.dto.request.password.ResetPasswordRequest;
import com.ffa.tournaments.identity.presentation.dto.response.common.MessageResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/identity/password")
public class PasswordResetController {

    private final PasswordResetService passwordResetService;

    public PasswordResetController(PasswordResetService passwordResetService) {
        this.passwordResetService = passwordResetService;
    }

    @PostMapping("/forgot")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public MessageResponse requestPasswordReset(@Valid @RequestBody RequestPasswordResetRequest request) {
        passwordResetService.requestPasswordReset(request.email());

        return new MessageResponse(
                "If the account exists, a password reset email will be sent"
        );
    }

    @PostMapping("/reset")
    public MessageResponse resetPassword(@Valid @RequestBody ResetPasswordRequest request) {
        passwordResetService.resetPassword(request.token(), request.newPassword());

        return new MessageResponse("Password successfully changed");
    }
}