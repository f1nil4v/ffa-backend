package com.ffa.tournaments.player.presentation.controller;

import com.ffa.tournaments.identity.domain.valueobject.AccountId;
import com.ffa.tournaments.player.application.result.NicknameChangeRequestResult;
import com.ffa.tournaments.player.application.service.CancelNicknameChangeService;
import com.ffa.tournaments.player.application.service.GetMyNicknameChangeRequestsService;
import com.ffa.tournaments.player.application.service.RequestNicknameChangeService;
import com.ffa.tournaments.player.presentation.dto.request.RequestNicknameChangeRequest;
import com.ffa.tournaments.player.presentation.dto.response.NicknameChangeRequestResponse;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/player/nickname-requests")
public class NicknameChangeController {

    private final RequestNicknameChangeService requestNicknameChangeService;
    private final GetMyNicknameChangeRequestsService getMyNicknameChangeRequestsService;
    private final CancelNicknameChangeService cancelNicknameChangeService;

    public NicknameChangeController(
            RequestNicknameChangeService requestNicknameChangeService,
            GetMyNicknameChangeRequestsService getMyNicknameChangeRequestsService,
            CancelNicknameChangeService cancelNicknameChangeService
    ) {
        this.requestNicknameChangeService = requestNicknameChangeService;
        this.getMyNicknameChangeRequestsService = getMyNicknameChangeRequestsService;
        this.cancelNicknameChangeService = cancelNicknameChangeService;
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NicknameChangeRequestResponse request(
            Authentication authentication,
            @Valid @RequestBody RequestNicknameChangeRequest request
    ) {
        NicknameChangeRequestResult result = requestNicknameChangeService.request(
                currentAccountId(authentication),
                request.newNickname(),
                request.reason()
        );

        return NicknameChangeRequestResponse.from(result);
    }

    @GetMapping
    public List<NicknameChangeRequestResponse> list(Authentication authentication) {
        return getMyNicknameChangeRequestsService.list(currentAccountId(authentication))
                .stream()
                .map(NicknameChangeRequestResponse::from)
                .toList();
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void cancel(Authentication authentication, @PathVariable UUID id) {
        cancelNicknameChangeService.cancel(currentAccountId(authentication), id);
    }

    private AccountId currentAccountId(Authentication authentication) {
        return AccountId.from((UUID) authentication.getPrincipal());
    }
}
