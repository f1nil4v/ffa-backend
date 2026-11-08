package com.ffa.tournaments.tournament.application.service;

import com.ffa.tournaments.identity.domain.valueobject.AccountId;
import com.ffa.tournaments.player.application.service.PlayerProfileProvider;
import com.ffa.tournaments.player.domain.model.PlayerProfile;
import com.ffa.tournaments.player.domain.repository.PlayerProfileRepository;
import com.ffa.tournaments.team.application.exception.NotTeamCaptainException;
import com.ffa.tournaments.team.application.exception.PlayerNotInTeamException;
import com.ffa.tournaments.team.application.exception.TeamNotFoundException;
import com.ffa.tournaments.team.domain.model.Team;
import com.ffa.tournaments.team.domain.model.TeamMember;
import com.ffa.tournaments.team.domain.repository.TeamMemberRepository;
import com.ffa.tournaments.team.domain.repository.TeamRepository;
import com.ffa.tournaments.tournament.application.exception.InvalidTeamRosterException;
import com.ffa.tournaments.tournament.application.exception.InvalidTournamentStatusException;
import com.ffa.tournaments.tournament.application.exception.TournamentCapacityExceededException;
import com.ffa.tournaments.tournament.application.exception.TournamentNotOpenForRegistrationException;
import com.ffa.tournaments.tournament.application.exception.TournamentNotFoundException;
import com.ffa.tournaments.tournament.application.exception.TournamentRegistrationAlreadyExistsException;
import com.ffa.tournaments.tournament.application.exception.TournamentRegistrationNotFoundException;
import com.ffa.tournaments.tournament.application.result.TeamRosterMemberResult;
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
import java.util.Comparator;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class TournamentRegistrationService {

    private final TournamentRepository tournamentRepository;
    private final TournamentRegistrationRepository tournamentRegistrationRepository;
    private final TeamRepository teamRepository;
    private final TeamMemberRepository teamMemberRepository;
    private final PlayerProfileRepository playerProfileRepository;
    private final PlayerProfileProvider playerProfileProvider;
    private final TournamentRosterSnapshotCodec rosterSnapshotCodec;

    public TournamentRegistrationService(
            TournamentRepository tournamentRepository,
            TournamentRegistrationRepository tournamentRegistrationRepository,
            TeamRepository teamRepository,
            TeamMemberRepository teamMemberRepository,
            PlayerProfileRepository playerProfileRepository,
            PlayerProfileProvider playerProfileProvider,
            TournamentRosterSnapshotCodec rosterSnapshotCodec
    ) {
        this.tournamentRepository = tournamentRepository;
        this.tournamentRegistrationRepository = tournamentRegistrationRepository;
        this.teamRepository = teamRepository;
        this.teamMemberRepository = teamMemberRepository;
        this.playerProfileRepository = playerProfileRepository;
        this.playerProfileProvider = playerProfileProvider;
        this.rosterSnapshotCodec = rosterSnapshotCodec;
    }

    @Transactional
    public TournamentRegistrationResult register(
            AccountId accountId,
            UUID tournamentId
    ) {
        CaptainTeam captainTeam = resolveCaptainTeam(accountId);

        Tournament tournament = tournamentRepository.findByIdForUpdate(tournamentId)
                .orElseThrow(TournamentNotFoundException::new);

        if (!tournament.isRegistrationOpen(Instant.now())) {
            throw new TournamentNotOpenForRegistrationException();
        }

        UUID teamId = captainTeam.team().getId();

        if (tournamentRegistrationRepository
                .findActiveByTournamentIdAndTeamId(tournamentId, teamId)
                .isPresent()) {
            throw new TournamentRegistrationAlreadyExistsException();
        }

        if (tournamentRegistrationRepository.countActiveByTournamentId(tournamentId)
                >= tournament.getMaxTeams()) {
            throw new TournamentCapacityExceededException();
        }

        List<TeamRosterMemberResult> roster = buildRoster(teamId);

        TournamentRegistration registration = TournamentRegistration.create(
                tournamentId,
                teamId,
                captainTeam.profile().getId(),
                rosterSnapshotCodec.encode(roster)
        );

        return toResult(tournamentRegistrationRepository.save(registration));
    }

    @Transactional
    public void withdraw(AccountId accountId, UUID tournamentId) {
        CaptainTeam captainTeam = resolveCaptainTeam(accountId);

        Tournament tournament = tournamentRepository.findById(tournamentId)
                .orElseThrow(TournamentNotFoundException::new);

        if (tournament.getStatus() != TournamentStatus.REGISTRATION) {
            throw new InvalidTournamentStatusException(
                    "Tournament registration can only be withdrawn while registration is open"
            );
        }

        TournamentRegistration registration = tournamentRegistrationRepository
                .findActiveByTournamentIdAndTeamId(
                        tournamentId,
                        captainTeam.team().getId()
                )
                .orElseThrow(TournamentRegistrationNotFoundException::new);

        registration.withdraw();

        tournamentRegistrationRepository.save(registration);
    }

    @Transactional(readOnly = true)
    public List<TournamentRegistrationResult> mine(
            AccountId accountId,
            UUID tournamentId
    ) {
        tournamentRepository.findById(tournamentId)
                .orElseThrow(TournamentNotFoundException::new);

        PlayerProfile profile = playerProfileProvider.getByAccountId(accountId);

        Optional<TeamMember> membership = teamMemberRepository
                .findByPlayerProfileId(profile.getId());

        if (membership.isEmpty()) {
            return List.of();
        }

        return tournamentRegistrationRepository
                .findAllByTeamId(membership.get().getTeamId())
                .stream()
                .filter(registration -> registration.getTournamentId().equals(tournamentId))
                .map(this::toResult)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<TournamentRegistrationResult> listApproved(UUID tournamentId) {
        tournamentRepository.findById(tournamentId)
                .orElseThrow(TournamentNotFoundException::new);

        return tournamentRegistrationRepository
                .findAllByTournamentIdAndStatus(
                        tournamentId,
                        TournamentRegistrationStatus.APPROVED
                )
                .stream()
                .map(this::toResult)
                .toList();
    }

    private CaptainTeam resolveCaptainTeam(AccountId accountId) {
        PlayerProfile profile = playerProfileProvider.getByAccountId(accountId);

        TeamMember member = teamMemberRepository.findByPlayerProfileId(profile.getId())
                .orElseThrow(PlayerNotInTeamException::new);

        Team team = teamRepository.findById(member.getTeamId())
                .orElseThrow(TeamNotFoundException::new);

        if (!team.isCaptain(profile.getId())) {
            throw new NotTeamCaptainException();
        }

        return new CaptainTeam(team, profile);
    }

    private List<TeamRosterMemberResult> buildRoster(UUID teamId) {
        List<TeamMember> members = teamMemberRepository.findAllByTeamId(teamId);

        if (members.size() < Team.MIN_ROSTER_SIZE
                || members.size() > Team.MAX_ROSTER_SIZE) {
            throw new InvalidTeamRosterException(
                    "Team roster must contain between "
                            + Team.MIN_ROSTER_SIZE
                            + " and "
                            + Team.MAX_ROSTER_SIZE
                            + " players"
            );
        }

        long captainCount = members.stream()
                .filter(TeamMember::isCaptain)
                .count();

        if (captainCount != 1) {
            throw new InvalidTeamRosterException(
                    "Team roster must contain exactly one captain"
            );
        }

        return members.stream()
                .map(this::toRosterMember)
                .sorted(Comparator.comparing(TeamRosterMemberResult::role).reversed())
                .toList();
    }

    private TeamRosterMemberResult toRosterMember(TeamMember member) {
        PlayerProfile profile = playerProfileRepository
                .findById(member.getPlayerProfileId())
                .orElseThrow(() -> new InvalidTeamRosterException(
                        "Team member profile not found"
                ));

        return new TeamRosterMemberResult(
                profile.getId().value(),
                profile.getNickname(),
                profile.getSteamId(),
                member.getRole().name()
        );
    }

    private TournamentRegistrationResult toResult(TournamentRegistration registration) {
        return TournamentRegistrationResult.from(
                registration,
                rosterSnapshotCodec.decode(registration.getRosterSnapshot())
        );
    }

    private record CaptainTeam(Team team, PlayerProfile profile) {
    }
}
