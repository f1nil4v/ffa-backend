package com.ffa.tournaments.tournament.application.service;

import com.ffa.tournaments.support.domain.valueobject.SupportAccountId;
import com.ffa.tournaments.tournament.application.exception.InvalidTournamentStatusException;
import com.ffa.tournaments.tournament.application.exception.TournamentCapacityExceededException;
import com.ffa.tournaments.tournament.application.exception.TournamentNotFoundException;
import com.ffa.tournaments.tournament.application.exception.TournamentRegistrationNotFoundException;
import com.ffa.tournaments.tournament.application.exception.TournamentRegistrationNotPendingException;
import com.ffa.tournaments.tournament.application.result.TournamentRegistrationResult;
import com.ffa.tournaments.tournament.domain.model.Tournament;
import com.ffa.tournaments.tournament.domain.model.TournamentRegistration;
import com.ffa.tournaments.tournament.domain.model.TournamentRegistrationStatus;
import com.ffa.tournaments.tournament.domain.model.TournamentStatus;
import com.ffa.tournaments.tournament.domain.repository.TournamentRegistrationRepository;
import com.ffa.tournaments.tournament.domain.repository.TournamentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class TournamentRegistrationReviewService {

    private final TournamentRepository tournamentRepository;
    private final TournamentRegistrationRepository tournamentRegistrationRepository;
    private final TournamentRosterSnapshotCodec rosterSnapshotCodec;

    public TournamentRegistrationReviewService(
            TournamentRepository tournamentRepository,
            TournamentRegistrationRepository tournamentRegistrationRepository,
            TournamentRosterSnapshotCodec rosterSnapshotCodec
    ) {
        this.tournamentRepository = tournamentRepository;
        this.tournamentRegistrationRepository = tournamentRegistrationRepository;
        this.rosterSnapshotCodec = rosterSnapshotCodec;
    }

    @Transactional(readOnly = true)
    public List<TournamentRegistrationResult> list(
            UUID tournamentId,
            TournamentRegistrationStatus status
    ) {
        tournamentRepository.findById(tournamentId)
                .orElseThrow(TournamentNotFoundException::new);

        return (status == null
                ? tournamentRegistrationRepository.findAllByTournamentId(tournamentId)
                : tournamentRegistrationRepository.findAllByTournamentIdAndStatus(
                        tournamentId,
                        status
                ))
                .stream()
                .map(this::toResult)
                .toList();
    }

    @Transactional
    public TournamentRegistrationResult approve(
            SupportAccountId reviewerId,
            UUID tournamentId,
            UUID registrationId
    ) {
        Tournament tournament = tournamentRepository.findByIdForUpdate(tournamentId)
                .orElseThrow(TournamentNotFoundException::new);

        ensureReviewAllowed(tournament);

        TournamentRegistration registration = findRegistration(
                tournamentId,
                registrationId
        );

        if (registration.getStatus() != TournamentRegistrationStatus.PENDING) {
            throw new TournamentRegistrationNotPendingException();
        }

        long approvedCount = tournamentRegistrationRepository
                .countByTournamentIdAndStatus(
                        tournamentId,
                        TournamentRegistrationStatus.APPROVED
                );

        if (approvedCount >= tournament.getMaxTeams()) {
            throw new TournamentCapacityExceededException();
        }

        registration.approve(reviewerId, Instant.now());

        return toResult(tournamentRegistrationRepository.save(registration));
    }

    @Transactional
    public TournamentRegistrationResult reject(
            SupportAccountId reviewerId,
            UUID tournamentId,
            UUID registrationId
    ) {
        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(TournamentNotFoundException::new);

        ensureReviewAllowed(tournament);

        TournamentRegistration registration = findRegistration(
                tournamentId,
                registrationId
        );

        if (registration.getStatus() != TournamentRegistrationStatus.PENDING) {
            throw new TournamentRegistrationNotPendingException();
        }

        registration.reject(reviewerId, Instant.now());

        return toResult(tournamentRegistrationRepository.save(registration));
    }

    @Transactional
    public TournamentRegistrationResult disqualify(
            SupportAccountId reviewerId,
            UUID tournamentId,
            UUID registrationId
    ) {
        tournamentRepository.findById(tournamentId)
                .orElseThrow(TournamentNotFoundException::new);

        TournamentRegistration registration = findRegistration(
                tournamentId,
                registrationId
        );

        if (registration.getStatus() != TournamentRegistrationStatus.APPROVED) {
            throw new InvalidTournamentStatusException(
                    "Only an approved registration can be disqualified"
            );
        }

        registration.disqualify(reviewerId, Instant.now());

        return toResult(tournamentRegistrationRepository.save(registration));
    }

    private void ensureReviewAllowed(Tournament tournament) {
        if (tournament.getStatus() != TournamentStatus.REGISTRATION
                && tournament.getStatus() != TournamentStatus.ONGOING) {
            throw new InvalidTournamentStatusException(
                    "Tournament registrations cannot be reviewed in the current status"
            );
        }
    }

    private TournamentRegistration findRegistration(
            UUID tournamentId,
            UUID registrationId
    ) {
        TournamentRegistration registration = tournamentRegistrationRepository
                .findById(registrationId)
                .orElseThrow(TournamentRegistrationNotFoundException::new);

        if (!registration.getTournamentId().equals(tournamentId)) {
            throw new TournamentRegistrationNotFoundException();
        }

        return registration;
    }

    private TournamentRegistrationResult toResult(TournamentRegistration registration) {
        return TournamentRegistrationResult.from(
                registration,
                rosterSnapshotCodec.decode(registration.getRosterSnapshot())
        );
    }
}
