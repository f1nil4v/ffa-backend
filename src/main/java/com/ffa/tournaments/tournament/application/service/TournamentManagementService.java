package com.ffa.tournaments.tournament.application.service;

import com.ffa.tournaments.support.domain.valueobject.SupportAccountId;
import com.ffa.tournaments.tournament.application.exception.InvalidTournamentStatusException;
import com.ffa.tournaments.tournament.application.exception.TournamentNotEditableException;
import com.ffa.tournaments.tournament.application.exception.TournamentNotFoundException;
import com.ffa.tournaments.tournament.application.result.TournamentResult;
import com.ffa.tournaments.tournament.domain.model.Tournament;
import com.ffa.tournaments.tournament.domain.model.TournamentFormat;
import com.ffa.tournaments.tournament.domain.model.TournamentStatus;
import com.ffa.tournaments.tournament.domain.repository.TournamentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.UUID;

@Service
public class TournamentManagementService {

    private final TournamentRepository tournamentRepository;

    public TournamentManagementService(TournamentRepository tournamentRepository) {
        this.tournamentRepository = tournamentRepository;
    }

    @Transactional
    public TournamentResult create(
            SupportAccountId creatorId,
            String name,
            String description,
            TournamentFormat format,
            int maxTeams,
            Instant registrationOpensAt,
            Instant registrationClosesAt,
            Instant startsAt
    ) {
        Tournament tournament = Tournament.create(
                name,
                description,
                format,
                maxTeams,
                registrationOpensAt,
                registrationClosesAt,
                startsAt,
                creatorId
        );

        return TournamentResult.from(tournamentRepository.save(tournament));
    }

    @Transactional
    public TournamentResult update(
            UUID tournamentId,
            String name,
            String description,
            TournamentFormat format,
            int maxTeams,
            Instant registrationOpensAt,
            Instant registrationClosesAt,
            Instant startsAt
    ) {
        Tournament tournament = find(tournamentId);

        if (tournament.getStatus() != TournamentStatus.DRAFT) {
            throw new TournamentNotEditableException();
        }

        tournament.update(
                name,
                description,
                format,
                maxTeams,
                registrationOpensAt,
                registrationClosesAt,
                startsAt
        );

        return TournamentResult.from(tournamentRepository.save(tournament));
    }

    @Transactional
    public TournamentResult openRegistration(UUID tournamentId) {
        Tournament tournament = find(tournamentId);

        if (tournament.getStatus() != TournamentStatus.DRAFT) {
            throw new InvalidTournamentStatusException(
                    "Tournament registration can only be opened from DRAFT status"
            );
        }

        tournament.openRegistration();

        return TournamentResult.from(tournamentRepository.save(tournament));
    }

    @Transactional
    public TournamentResult closeRegistration(UUID tournamentId) {
        Tournament tournament = find(tournamentId);

        if (tournament.getStatus() != TournamentStatus.REGISTRATION) {
            throw new InvalidTournamentStatusException(
                    "Tournament registration is not open"
            );
        }

        tournament.closeRegistration();

        return TournamentResult.from(tournamentRepository.save(tournament));
    }

    @Transactional
    public TournamentResult finish(UUID tournamentId) {
        Tournament tournament = find(tournamentId);

        if (tournament.getStatus() != TournamentStatus.ONGOING) {
            throw new InvalidTournamentStatusException(
                    "Tournament is not ongoing"
            );
        }

        tournament.finish();

        return TournamentResult.from(tournamentRepository.save(tournament));
    }

    @Transactional
    public TournamentResult cancel(UUID tournamentId) {
        Tournament tournament = find(tournamentId);

        if (tournament.getStatus() != TournamentStatus.DRAFT
                && tournament.getStatus() != TournamentStatus.REGISTRATION) {
            throw new InvalidTournamentStatusException(
                    "Tournament cannot be cancelled in its current status"
            );
        }

        tournament.cancel();

        return TournamentResult.from(tournamentRepository.save(tournament));
    }

    private Tournament find(UUID tournamentId) {
        return tournamentRepository.findById(tournamentId)
                .orElseThrow(TournamentNotFoundException::new);
    }
}
