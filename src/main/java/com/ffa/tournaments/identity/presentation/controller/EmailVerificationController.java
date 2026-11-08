package com.ffa.tournaments.identity.presentation.controller;

import com.ffa.tournaments.identity.application.service.EmailVerificationService;
import com.ffa.tournaments.identity.presentation.dto.request.email.ResendVerificationRequest;
import com.ffa.tournaments.identity.presentation.dto.response.common.MessageResponse;
import com.ffa.tournaments.identity.presentation.dto.request.email.VerifyEmailRequest;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/identity/email")
public class EmailVerificationController {

    private final EmailVerificationService emailVerificationService;

    public EmailVerificationController(EmailVerificationService emailVerificationService) {
        this.emailVerificationService = emailVerificationService;
    }

    @PostMapping("/verify")
    public MessageResponse verifyEmail(@Valid @RequestBody VerifyEmailRequest request) {
        emailVerificationService.verifyEmail(request.token());
        return new MessageResponse("Email successfully verified");
    }

    @PostMapping("/resend")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public MessageResponse resendVerificationEmail(@Valid @RequestBody ResendVerificationRequest request) {
        emailVerificationService.resendVerificationEmail(request.email());
        return new MessageResponse("If the account exists and is not verified, a verification email will be sent");
    }
}