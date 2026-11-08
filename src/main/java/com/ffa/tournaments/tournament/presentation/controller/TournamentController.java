package com.ffa.tournaments.tournament.presentation.controller;

import com.ffa.tournaments.identity.domain.valueobject.AccountId;
import com.ffa.tournaments.tournament.application.result.TournamentRegistrationResult;
import com.ffa.tournaments.tournament.application.service.TournamentQueryService;
import com.ffa.tournaments.tournament.application.service.TournamentRegistrationService;
import com.ffa.tournaments.tournament.domain.model.TournamentStatus;
import com.ffa.tournaments.tournament.presentation.dto.response.TournamentRegistrationResponse;
import com.ffa.tournaments.tournament.presentation.dto.response.TournamentResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/tournaments")
public class TournamentController {

    private final TournamentQueryService tournamentQueryService;
    private final TournamentRegistrationService tournamentRegistrationService;

    public TournamentController(
            TournamentQueryService tournamentQueryService,
            TournamentRegistrationService tournamentRegistrationService
    ) {
        this.tournamentQueryService = tournamentQueryService;
        this.tournamentRegistrationService = tournamentRegistrationService;
    }

    @GetMapping
    public List<TournamentResponse> list(
            @RequestParam(required = false) TournamentStatus status
    ) {
        return tournamentQueryService.list(status)
                .stream()
                .map(TournamentResponse::from)
                .toList();
    }

    @GetMapping("/{id}")
    public TournamentResponse get(@PathVariable UUID id) {
        return TournamentResponse.from(tournamentQueryService.get(id));
    }

    @GetMapping("/{id}/registrations")
    public List<TournamentRegistrationResponse> listApprovedRegistrations(
            @PathVariable UUID id
    ) {
        return tournamentRegistrationService.listApproved(id)
                .stream()
                .map(TournamentRegistrationResponse::from)
                .toList();
    }

    @GetMapping("/{id}/registrations/me")
    public List<TournamentRegistrationResponse> mine(
            Authentication authentication,
            @PathVariable UUID id
    ) {
        return tournamentRegistrationService.mine(currentAccountId(authentication), id)
                .stream()
                .map(TournamentRegistrationResponse::from)
                .toList();
    }

    @PostMapping("/{id}/registrations")
    @ResponseStatus(HttpStatus.CREATED)
    public TournamentRegistrationResponse register(
            Authentication authentication,
            @PathVariable UUID id
    ) {
        TournamentRegistrationResult result = tournamentRegistrationService.register(
                currentAccountId(authentication),
                id
        );

        return TournamentRegistrationResponse.from(result);
    }

    @DeleteMapping("/{id}/registrations")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void withdraw(Authentication authentication, @PathVariable UUID id) {
        tournamentRegistrationService.withdraw(currentAccountId(authentication), id);
    }

    private AccountId currentAccountId(Authentication authentication) {
        return AccountId.from((UUID) authentication.getPrincipal());
    }
}
