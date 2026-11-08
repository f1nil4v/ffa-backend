package com.ffa.tournaments.tournament.domain.model;

import com.ffa.tournaments.player.domain.valueobject.PlayerProfileId;
import com.ffa.tournaments.support.domain.valueobject.SupportAccountId;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

class TournamentRegistrationTest {

    private static final UUID TOURNAMENT_ID = UUID.randomUUID();
    private static final UUID TEAM_ID = UUID.randomUUID();
    private static final PlayerProfileId CAPTAIN_ID = PlayerProfileId.generate();
    private static final SupportAccountId SUPPORT_ID = SupportAccountId.generate();
    private static final String ROSTER = "[]";

    @Test
    void createsPendingRegistration() {
        TournamentRegistration registration = createRegistration();

        assertEquals(TournamentRegistrationStatus.PENDING, registration.getStatus());
        assertNull(registration.getReviewedBy());
        assertNull(registration.getReviewedAt());
        assertEquals(TOURNAMENT_ID, registration.getTournamentId());
        assertEquals(TEAM_ID, registration.getTeamId());
    }

    @Test
    void approveStoresReviewer() {
        TournamentRegistration registration = createRegistration();
        Instant now = Instant.now();

        registration.approve(SUPPORT_ID, now);

        assertEquals(TournamentRegistrationStatus.APPROVED, registration.getStatus());
        assertEquals(SUPPORT_ID, registration.getReviewedBy());
        assertEquals(now, registration.getReviewedAt());
    }

    @Test
    void rejectStoresReviewer() {
        TournamentRegistration registration = createRegistration();
        Instant now = Instant.now();

        registration.reject(SUPPORT_ID, now);

        assertEquals(TournamentRegistrationStatus.REJECTED, registration.getStatus());
        assertEquals(SUPPORT_ID, registration.getReviewedBy());
        assertEquals(now, registration.getReviewedAt());
    }

    @Test
    void cannotApproveTwice() {
        TournamentRegistration registration = createRegistration();
        registration.approve(SUPPORT_ID, Instant.now());

        assertThrows(
                IllegalStateException.class,
                () -> registration.approve(SUPPORT_ID, Instant.now())
        );
    }

    @Test
    void approvedRegistrationCanBeWithdrawn() {
        TournamentRegistration registration = createRegistration();
        registration.approve(SUPPORT_ID, Instant.now());

        registration.withdraw();

        assertEquals(TournamentRegistrationStatus.WITHDRAWN, registration.getStatus());
        assertNull(registration.getReviewedBy());
        assertNull(registration.getReviewedAt());
    }

    @Test
    void rejectedRegistrationCannotBeWithdrawn() {
        TournamentRegistration registration = createRegistration();
        registration.reject(SUPPORT_ID, Instant.now());

        assertThrows(IllegalStateException.class, registration::withdraw);
    }

    @Test
    void onlyApprovedRegistrationCanBeDisqualified() {
        TournamentRegistration pending = createRegistration();

        assertThrows(
                IllegalStateException.class,
                () -> pending.disqualify(SUPPORT_ID, Instant.now())
        );

        pending.approve(SUPPORT_ID, Instant.now());
        pending.disqualify(SUPPORT_ID, Instant.now());

        assertEquals(TournamentRegistrationStatus.DISQUALIFIED, pending.getStatus());
        assertNotNull(pending.getReviewedBy());
    }

    private TournamentRegistration createRegistration() {
        return TournamentRegistration.create(
                TOURNAMENT_ID,
                TEAM_ID,
                CAPTAIN_ID,
                ROSTER
        );
    }
}
