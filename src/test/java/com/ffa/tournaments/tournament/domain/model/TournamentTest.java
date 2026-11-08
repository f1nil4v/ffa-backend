package com.ffa.tournaments.tournament.domain.model;

import com.ffa.tournaments.support.domain.valueobject.SupportAccountId;
import org.junit.jupiter.api.Test;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TournamentTest {

    private static final SupportAccountId SUPPORT_ID = SupportAccountId.generate();
    private static final Instant OPENS_AT = Instant.parse("2026-01-01T00:00:00Z");
    private static final Instant CLOSES_AT = Instant.parse("2026-01-10T00:00:00Z");
    private static final Instant STARTS_AT = Instant.parse("2026-01-11T00:00:00Z");

    @Test
    void createsTournamentInDraftStatus() {
        Tournament tournament = createTournament();

        assertEquals(TournamentStatus.DRAFT, tournament.getStatus());
        assertEquals(TournamentFormat.SINGLE_ELIMINATION, tournament.getFormat());
        assertEquals(16, tournament.getMaxTeams());
        assertEquals(SUPPORT_ID, tournament.getCreatedBy());
    }

    @Test
    void movesThroughRegistrationAndFinishStates() {
        Tournament tournament = createTournament();

        tournament.openRegistration();
        assertEquals(TournamentStatus.REGISTRATION, tournament.getStatus());

        tournament.closeRegistration();
        assertEquals(TournamentStatus.ONGOING, tournament.getStatus());

        tournament.finish();
        assertEquals(TournamentStatus.FINISHED, tournament.getStatus());
    }

    @Test
    void registrationIsOpenOnlyInsideConfiguredWindow() {
        Tournament tournament = createTournament();
        tournament.openRegistration();

        assertFalse(tournament.isRegistrationOpen(OPENS_AT.minusSeconds(1)));
        assertTrue(tournament.isRegistrationOpen(OPENS_AT.plusSeconds(60)));
        assertFalse(tournament.isRegistrationOpen(CLOSES_AT));
    }

    @Test
    void registrationIsNotOpenWhileDraft() {
        Tournament tournament = createTournament();

        assertFalse(tournament.isRegistrationOpen(OPENS_AT.plusSeconds(60)));
    }

    @Test
    void rejectsInvalidTimeWindow() {
        assertThrows(
                IllegalArgumentException.class,
                () -> Tournament.create(
                        "FFA Cup",
                        null,
                        TournamentFormat.SINGLE_ELIMINATION,
                        16,
                        CLOSES_AT,
                        OPENS_AT,
                        STARTS_AT,
                        SUPPORT_ID
                )
        );
    }

    @Test
    void rejectsStartBeforeRegistrationCloses() {
        assertThrows(
                IllegalArgumentException.class,
                () -> Tournament.create(
                        "FFA Cup",
                        null,
                        TournamentFormat.SINGLE_ELIMINATION,
                        16,
                        OPENS_AT,
                        CLOSES_AT,
                        CLOSES_AT.minusSeconds(1),
                        SUPPORT_ID
                )
        );
    }

    @Test
    void updateIsOnlyAllowedInDraft() {
        Tournament tournament = createTournament();
        tournament.openRegistration();

        assertThrows(
                IllegalStateException.class,
                () -> tournament.update(
                        "New name",
                        null,
                        TournamentFormat.SINGLE_ELIMINATION,
                        8,
                        OPENS_AT,
                        CLOSES_AT,
                        STARTS_AT
                )
        );
    }

    @Test
    void cancelIsAllowedBeforeStart() {
        Tournament draft = createTournament();
        draft.cancel();
        assertEquals(TournamentStatus.CANCELLED, draft.getStatus());

        Tournament registration = createTournament();
        registration.openRegistration();
        registration.cancel();
        assertEquals(TournamentStatus.CANCELLED, registration.getStatus());
    }

    @Test
    void cancelIsRejectedAfterStart() {
        Tournament tournament = createTournament();
        tournament.openRegistration();
        tournament.closeRegistration();

        assertThrows(IllegalStateException.class, tournament::cancel);
    }

    @Test
    void rejectsMaxTeamsOutOfRange() {
        assertThrows(
                IllegalArgumentException.class,
                () -> Tournament.create(
                        "FFA Cup",
                        null,
                        TournamentFormat.SINGLE_ELIMINATION,
                        1,
                        OPENS_AT,
                        CLOSES_AT,
                        STARTS_AT,
                        SUPPORT_ID
                )
        );
    }

    private Tournament createTournament() {
        return Tournament.create(
                "FFA Cup",
                "Season one",
                TournamentFormat.SINGLE_ELIMINATION,
                16,
                OPENS_AT,
                CLOSES_AT,
                STARTS_AT,
                SUPPORT_ID
        );
    }
}
