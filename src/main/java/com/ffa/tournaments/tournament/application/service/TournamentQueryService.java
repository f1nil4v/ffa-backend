package com.ffa.tournaments.tournament.application.service;

import com.ffa.tournaments.tournament.application.exception.TournamentNotFoundException;
import com.ffa.tournaments.tournament.application.result.TournamentResult;
import com.ffa.tournaments.tournament.domain.model.TournamentStatus;
import com.ffa.tournaments.tournament.domain.repository.TournamentRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class TournamentQueryService {

    private final TournamentRepository tournamentRepository;

    public TournamentQueryService(TournamentRepository tournamentRepository) {
        this.tournamentRepository = tournamentRepository;
    }

    @Transactional(readOnly = true)
    public List<TournamentResult> list(TournamentStatus status) {
        return (status == null
                ? tournamentRepository.findAll()
                : tournamentRepository.findAllByStatus(status))
                .stream()
                .map(TournamentResult::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public TournamentResult get(UUID tournamentId) {
        return TournamentResult.from(
                tournamentRepository.findById(tournamentId)
                        .orElseThrow(TournamentNotFoundException::new)
        );
    }
}
