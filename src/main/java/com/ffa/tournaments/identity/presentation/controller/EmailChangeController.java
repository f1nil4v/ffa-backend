package com.ffa.tournaments.identity.presentation.controller;

import com.ffa.tournaments.identity.application.result.EmailChangeRequestResult;
import com.ffa.tournaments.identity.application.service.EmailChangeService;
import com.ffa.tournaments.identity.domain.valueobject.AccountId;
import com.ffa.tournaments.identity.presentation.dto.request.email.ChangeEmailRequest;
import com.ffa.tournaments.identity.presentation.dto.request.email.ConfirmEmailChangeRequest;
import com.ffa.tournaments.identity.presentation.dto.response.email.EmailChangeRequestResponse;
import com.ffa.tournaments.identity.presentation.dto.response.email.EmailChangeStatusResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;

@RestController
@RequestMapping("/api/identity/email/change")
public class EmailChangeController {

    private final EmailChangeService emailChangeService;

    public EmailChangeController(EmailChangeService emailChangeService) {
        this.emailChangeService = emailChangeService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.ACCEPTED)
    public EmailChangeRequestResponse request(
            Authentication authentication,
            @Valid @RequestBody ChangeEmailRequest request
    ) {
        EmailChangeRequestResult result = emailChangeService.request(
                currentAccountId(authentication),
                request.newEmail()
        );

        return EmailChangeRequestResponse.from(result);
    }

    @PostMapping("/confirm")
    public EmailChangeRequestResponse confirm(
            @Valid @RequestBody ConfirmEmailChangeRequest request
    ) {
        return EmailChangeRequestResponse.from(
                emailChangeService.confirm(request.token())
        );
    }

    @GetMapping("/status")
    public EmailChangeStatusResponse status(Authentication authentication) {
        return EmailChangeStatusResponse.from(
                emailChangeService.status(currentAccountId(authentication))
        );
    }

    private AccountId currentAccountId(Authentication authentication) {
        return AccountId.from((UUID) authentication.getPrincipal());
    }
}
