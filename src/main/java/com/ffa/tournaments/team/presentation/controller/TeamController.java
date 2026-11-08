package com.ffa.tournaments.team.presentation.controller;

import com.ffa.tournaments.identity.domain.valueobject.AccountId;
import com.ffa.tournaments.team.application.result.TeamApplicationResult;
import com.ffa.tournaments.team.application.result.TeamResult;
import com.ffa.tournaments.team.application.service.CreateTeamService;
import com.ffa.tournaments.team.application.service.DeleteTeamService;
import com.ffa.tournaments.team.application.service.TeamApplicationService;
import com.ffa.tournaments.team.application.service.TeamMembershipService;
import com.ffa.tournaments.team.application.service.TeamQueryService;
import com.ffa.tournaments.team.application.service.UpdateTeamService;
import com.ffa.tournaments.team.domain.model.TeamApplicationStatus;
import com.ffa.tournaments.team.presentation.dto.request.ApplyToTeamRequest;
import com.ffa.tournaments.team.presentation.dto.request.CreateTeamRequest;
import com.ffa.tournaments.team.presentation.dto.request.TransferCaptainRequest;
import com.ffa.tournaments.team.presentation.dto.request.UpdateTeamRequest;
import com.ffa.tournaments.team.presentation.dto.response.TeamApplicationResponse;
import com.ffa.tournaments.team.presentation.dto.response.TeamResponse;
import com.ffa.tournaments.team.presentation.dto.response.TeamSummaryResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
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
@RequestMapping("/api/teams")
public class TeamController {

    private final CreateTeamService createTeamService;
    private final UpdateTeamService updateTeamService;
    private final DeleteTeamService deleteTeamService;
    private final TeamQueryService teamQueryService;
    private final TeamApplicationService teamApplicationService;
    private final TeamMembershipService teamMembershipService;

    public TeamController(
            CreateTeamService createTeamService,
            UpdateTeamService updateTeamService,
            DeleteTeamService deleteTeamService,
            TeamQueryService teamQueryService,
            TeamApplicationService teamApplicationService,
            TeamMembershipService teamMembershipService
    ) {
        this.createTeamService = createTeamService;
        this.updateTeamService = updateTeamService;
        this.deleteTeamService = deleteTeamService;
        this.teamQueryService = teamQueryService;
        this.teamApplicationService = teamApplicationService;
        this.teamMembershipService = teamMembershipService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TeamResponse create(
            Authentication authentication,
            @Valid @RequestBody CreateTeamRequest request
    ) {
        TeamResult result = createTeamService.create(
                currentAccountId(authentication),
                request.name(),
                request.tag(),
                request.description()
        );

        return TeamResponse.from(result);
    }

    @GetMapping
    public List<TeamSummaryResponse> list() {
        return teamQueryService.list()
                .stream()
                .map(TeamSummaryResponse::from)
                .toList();
    }

    @GetMapping("/me")
    public TeamResponse me(Authentication authentication) {
        return TeamResponse.from(
                teamQueryService.getByAccount(currentAccountId(authentication))
        );
    }

    @GetMapping("/{id}")
    public TeamResponse get(@PathVariable UUID id) {
        return TeamResponse.from(teamQueryService.get(id));
    }

    @PatchMapping("/{id}")
    public TeamResponse update(
            Authentication authentication,
            @PathVariable UUID id,
            @Valid @RequestBody UpdateTeamRequest request
    ) {
        TeamResult result = updateTeamService.update(
                currentAccountId(authentication),
                id,
                request.name(),
                request.tag(),
                request.description()
        );

        return TeamResponse.from(result);
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void delete(Authentication authentication, @PathVariable UUID id) {
        deleteTeamService.delete(currentAccountId(authentication), id);
    }

    @PostMapping("/{id}/applications")
    @ResponseStatus(HttpStatus.CREATED)
    public TeamApplicationResponse apply(
            Authentication authentication,
            @PathVariable UUID id,
            @Valid @RequestBody ApplyToTeamRequest request
    ) {
        TeamApplicationResult result = teamApplicationService.apply(
                currentAccountId(authentication),
                id,
                request.message()
        );

        return TeamApplicationResponse.from(result);
    }

    @GetMapping("/{id}/applications")
    public List<TeamApplicationResponse> listApplications(
            Authentication authentication,
            @PathVariable UUID id,
            @RequestParam(defaultValue = "PENDING") TeamApplicationStatus status
    ) {
        return teamApplicationService.list(currentAccountId(authentication), id, status)
                .stream()
                .map(TeamApplicationResponse::from)
                .toList();
    }

    @PostMapping("/{id}/applications/{applicationId}/approve")
    public TeamApplicationResponse approve(
            Authentication authentication,
            @PathVariable UUID id,
            @PathVariable UUID applicationId
    ) {
        return TeamApplicationResponse.from(
                teamApplicationService.approve(
                        currentAccountId(authentication),
                        id,
                        applicationId
                )
        );
    }

    @PostMapping("/{id}/applications/{applicationId}/reject")
    public TeamApplicationResponse reject(
            Authentication authentication,
            @PathVariable UUID id,
            @PathVariable UUID applicationId
    ) {
        return TeamApplicationResponse.from(
                teamApplicationService.reject(
                        currentAccountId(authentication),
                        id,
                        applicationId
                )
        );
    }

    @DeleteMapping("/{id}/applications/{applicationId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void withdraw(
            Authentication authentication,
            @PathVariable UUID id,
            @PathVariable UUID applicationId
    ) {
        teamApplicationService.withdraw(
                currentAccountId(authentication),
                id,
                applicationId
        );
    }

    @PostMapping("/{id}/leave")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void leave(Authentication authentication, @PathVariable UUID id) {
        teamMembershipService.leave(currentAccountId(authentication), id);
    }

    @DeleteMapping("/{id}/members/{playerProfileId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void kick(
            Authentication authentication,
            @PathVariable UUID id,
            @PathVariable UUID playerProfileId
    ) {
        teamMembershipService.kick(
                currentAccountId(authentication),
                id,
                playerProfileId
        );
    }

    @PostMapping("/{id}/transfer-captain")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void transferCaptain(
            Authentication authentication,
            @PathVariable UUID id,
            @Valid @RequestBody TransferCaptainRequest request
    ) {
        teamMembershipService.transferCaptain(
                currentAccountId(authentication),
                id,
                request.playerProfileId()
        );
    }

    private AccountId currentAccountId(Authentication authentication) {
        return AccountId.from((UUID) authentication.getPrincipal());
    }
}
