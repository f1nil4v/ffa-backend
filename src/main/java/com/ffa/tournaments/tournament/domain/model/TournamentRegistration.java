package com.ffa.tournaments.tournament.domain.model;

import com.ffa.tournaments.player.domain.valueobject.PlayerProfileId;
import com.ffa.tournaments.support.domain.valueobject.SupportAccountId;

import java.time.Instant;
import java.util.Objects;
import java.util.UUID;

public class TournamentRegistration {

    private final UUID id;
    private final UUID tournamentId;
    private final UUID teamId;
    private final PlayerProfileId registeredBy;
    private final String rosterSnapshot;
    private TournamentRegistrationStatus status;
    private Integer seed;
    private SupportAccountId reviewedBy;
    private Instant reviewedAt;
    private final Instant createdAt;

    private TournamentRegistration(
            UUID id,
            UUID tournamentId,
            UUID teamId,
            PlayerProfileId registeredBy,
            String rosterSnapshot,
            TournamentRegistrationStatus status,
            Integer seed,
            SupportAccountId reviewedBy,
            Instant reviewedAt,
            Instant createdAt
    ) {
        this.id = Objects.requireNonNull(id);
        this.tournamentId = Objects.requireNonNull(tournamentId);
        this.teamId = Objects.requireNonNull(teamId);
        this.registeredBy = Objects.requireNonNull(registeredBy);
        this.rosterSnapshot = Objects.requireNonNull(rosterSnapshot);
        this.status = Objects.requireNonNull(status);
        this.seed = seed;
        this.reviewedBy = reviewedBy;
        this.reviewedAt = reviewedAt;
        this.createdAt = Objects.requireNonNull(createdAt);
    }

    public static TournamentRegistration create(
            UUID tournamentId,
            UUID teamId,
            PlayerProfileId registeredBy,
            String rosterSnapshot
    ) {
        return new TournamentRegistration(
                UUID.randomUUID(),
                tournamentId,
                teamId,
                registeredBy,
                rosterSnapshot,
                TournamentRegistrationStatus.PENDING,
                null,
                null,
                null,
                Instant.now()
        );
    }

    public static TournamentRegistration restore(
            UUID id,
            UUID tournamentId,
            UUID teamId,
            PlayerProfileId registeredBy,
            String rosterSnapshot,
            TournamentRegistrationStatus status,
            Integer seed,
            SupportAccountId reviewedBy,
            Instant reviewedAt,
            Instant createdAt
    ) {
        return new TournamentRegistration(
                id,
                tournamentId,
                teamId,
                registeredBy,
                rosterSnapshot,
                status,
                seed,
                reviewedBy,
                reviewedAt,
                createdAt
        );
    }

    public void approve(SupportAccountId reviewerId, Instant now) {
        ensurePending();

        this.status = TournamentRegistrationStatus.APPROVED;
        this.reviewedBy = reviewerId;
        this.reviewedAt = now;
    }

    public void reject(SupportAccountId reviewerId, Instant now) {
        ensurePending();

        this.status = TournamentRegistrationStatus.REJECTED;
        this.reviewedBy = reviewerId;
        this.reviewedAt = now;
    }

    public void disqualify(SupportAccountId reviewerId, Instant now) {
        if (status != TournamentRegistrationStatus.APPROVED) {
            throw new IllegalStateException("Only an approved registration can be disqualified");
        }

        this.status = TournamentRegistrationStatus.DISQUALIFIED;
        this.reviewedBy = reviewerId;
        this.reviewedAt = now;
    }

    public void withdraw() {
        if (status != TournamentRegistrationStatus.PENDING
                && status != TournamentRegistrationStatus.APPROVED) {
            throw new IllegalStateException("Tournament registration cannot be withdrawn");
        }

        this.status = TournamentRegistrationStatus.WITHDRAWN;
        this.reviewedBy = null;
        this.reviewedAt = null;
    }

    private void ensurePending() {
        if (status != TournamentRegistrationStatus.PENDING) {
            throw new IllegalStateException("Tournament registration is not pending");
        }
    }

    public UUID getId() {
        return id;
    }

    public UUID getTournamentId() {
        return tournamentId;
    }

    public UUID getTeamId() {
        return teamId;
    }

    public PlayerProfileId getRegisteredBy() {
        return registeredBy;
    }

    public String getRosterSnapshot() {
        return rosterSnapshot;
    }

    public TournamentRegistrationStatus getStatus() {
        return status;
    }

    public Integer getSeed() {
        return seed;
    }

    public SupportAccountId getReviewedBy() {
        return reviewedBy;
    }

    public Instant getReviewedAt() {
        return reviewedAt;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }
}
