package com.ffa.tournaments.support.presentation.controller;

import com.ffa.tournaments.player.application.result.NicknameChangeRequestResult;
import com.ffa.tournaments.player.application.service.ReviewNicknameChangeService;
import com.ffa.tournaments.player.domain.model.NicknameChangeRequestStatus;
import com.ffa.tournaments.player.presentation.dto.response.NicknameChangeRequestResponse;
import com.ffa.tournaments.support.domain.valueobject.SupportAccountId;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/support/nickname-requests")
public class SupportNicknameChangeController {

    private final ReviewNicknameChangeService reviewNicknameChangeService;

    public SupportNicknameChangeController(
            ReviewNicknameChangeService reviewNicknameChangeService
    ) {
        this.reviewNicknameChangeService = reviewNicknameChangeService;
    }

    @GetMapping
    public List<NicknameChangeRequestResponse> list(
            @RequestParam(defaultValue = "PENDING") NicknameChangeRequestStatus status
    ) {
        return reviewNicknameChangeService.list(status)
                .stream()
                .map(NicknameChangeRequestResponse::from)
                .toList();
    }

    @PostMapping("/{id}/approve")
    public NicknameChangeRequestResponse approve(
            Authentication authentication,
            @PathVariable UUID id
    ) {
        NicknameChangeRequestResult result = reviewNicknameChangeService.approve(
                id,
                currentSupportAccountId(authentication)
        );

        return NicknameChangeRequestResponse.from(result);
    }

    @PostMapping("/{id}/reject")
    public NicknameChangeRequestResponse reject(
            Authentication authentication,
            @PathVariable UUID id
    ) {
        NicknameChangeRequestResult result = reviewNicknameChangeService.reject(
                id,
                currentSupportAccountId(authentication)
        );

        return NicknameChangeRequestResponse.from(result);
    }

    private SupportAccountId currentSupportAccountId(Authentication authentication) {
        return SupportAccountId.from((UUID) authentication.getPrincipal());
    }
}
