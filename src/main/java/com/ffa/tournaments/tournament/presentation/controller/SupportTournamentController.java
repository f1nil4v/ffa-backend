package com.ffa.tournaments.tournament.presentation.controller;

import com.ffa.tournaments.support.domain.valueobject.SupportAccountId;
import com.ffa.tournaments.tournament.application.result.TournamentRegistrationResult;
import com.ffa.tournaments.tournament.application.result.TournamentResult;
import com.ffa.tournaments.tournament.application.service.TournamentManagementService;
import com.ffa.tournaments.tournament.application.service.TournamentQueryService;
import com.ffa.tournaments.tournament.application.service.TournamentRegistrationReviewService;
import com.ffa.tournaments.tournament.domain.model.TournamentRegistrationStatus;
import com.ffa.tournaments.tournament.domain.model.TournamentStatus;
import com.ffa.tournaments.tournament.presentation.dto.request.CreateTournamentRequest;
import com.ffa.tournaments.tournament.presentation.dto.request.UpdateTournamentRequest;
import com.ffa.tournaments.tournament.presentation.dto.response.TournamentRegistrationResponse;
import com.ffa.tournaments.tournament.presentation.dto.response.TournamentResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/support/tournaments")
public class SupportTournamentController {

    private final TournamentManagementService tournamentManagementService;
    private final TournamentQueryService tournamentQueryService;
    private final TournamentRegistrationReviewService registrationReviewService;

    public SupportTournamentController(
            TournamentManagementService tournamentManagementService,
            TournamentQueryService tournamentQueryService,
            TournamentRegistrationReviewService registrationReviewService
    ) {
        this.tournamentManagementService = tournamentManagementService;
        this.tournamentQueryService = tournamentQueryService;
        this.registrationReviewService = registrationReviewService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TournamentResponse create(
            Authentication authentication,
            @Valid @RequestBody CreateTournamentRequest request
    ) {
        TournamentResult result = tournamentManagementService.create(
                currentSupportAccountId(authentication),
                request.name(),
                request.description(),
                request.format(),
                request.maxTeams(),
                request.registrationOpensAt(),
                request.registrationClosesAt(),
                request.startsAt()
        );

        return TournamentResponse.from(result);
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

    @PatchMapping("/{id}")
    public TournamentResponse update(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateTournamentRequest request
    ) {
        TournamentResult result = tournamentManagementService.update(
                id,
                request.name(),
                request.description(),
                request.format(),
                request.maxTeams(),
                request.registrationOpensAt(),
                request.registrationClosesAt(),
                request.startsAt()
        );

        return TournamentResponse.from(result);
    }

    @PostMapping("/{id}/open-registration")
    public TournamentResponse openRegistration(@PathVariable UUID id) {
        return TournamentResponse.from(
                tournamentManagementService.openRegistration(id)
        );
    }

    @PostMapping("/{id}/close-registration")
    public TournamentResponse closeRegistration(@PathVariable UUID id) {
        return TournamentResponse.from(
                tournamentManagementService.closeRegistration(id)
        );
    }

    @PostMapping("/{id}/finish")
    public TournamentResponse finish(@PathVariable UUID id) {
        return TournamentResponse.from(tournamentManagementService.finish(id));
    }

    @PostMapping("/{id}/cancel")
    public TournamentResponse cancel(@PathVariable UUID id) {
        return TournamentResponse.from(tournamentManagementService.cancel(id));
    }

    @GetMapping("/{id}/registrations")
    public List<TournamentRegistrationResponse> listRegistrations(
            @PathVariable UUID id,
            @RequestParam(required = false) TournamentRegistrationStatus status
    ) {
        return registrationReviewService.list(id, status)
                .stream()
                .map(TournamentRegistrationResponse::from)
                .toList();
    }

    @PostMapping("/{id}/registrations/{registrationId}/approve")
    public TournamentRegistrationResponse approve(
            Authentication authentication,
            @PathVariable UUID id,
            @PathVariable UUID registrationId
    ) {
        TournamentRegistrationResult result = registrationReviewService.approve(
                currentSupportAccountId(authentication),
                id,
                registrationId
        );

        return TournamentRegistrationResponse.from(result);
    }

    @PostMapping("/{id}/registrations/{registrationId}/reject")
    public TournamentRegistrationResponse reject(
            Authentication authentication,
            @PathVariable UUID id,
            @PathVariable UUID registrationId
    ) {
        TournamentRegistrationResult result = registrationReviewService.reject(
                currentSupportAccountId(authentication),
                id,
                registrationId
        );

        return TournamentRegistrationResponse.from(result);
    }

    @PostMapping("/{id}/registrations/{registrationId}/disqualify")
    public TournamentRegistrationResponse disqualify(
            Authentication authentication,
            @PathVariable UUID id,
            @PathVariable UUID registrationId
    ) {
        TournamentRegistrationResult result = registrationReviewService.disqualify(
                currentSupportAccountId(authentication),
                id,
                registrationId
        );

        return TournamentRegistrationResponse.from(result);
    }

    private SupportAccountId currentSupportAccountId(Authentication authentication) {
        return SupportAccountId.from((UUID) authentication.getPrincipal());
    }
}
