package com.ffa.tournaments.team.infrastructure.persistence.adapter;

import com.ffa.tournaments.team.domain.model.Team;
import com.ffa.tournaments.team.domain.repository.TeamRepository;
import com.ffa.tournaments.team.infrastructure.persistence.mapper.TeamMapper;
import com.ffa.tournaments.team.application.exception.TeamNameAlreadyTakenException;
import com.ffa.tournaments.team.application.exception.TeamTagAlreadyTakenException;
import com.ffa.tournaments.team.infrastructure.persistence.repository.SpringDataTeamRepository;
import org.postgresql.util.PSQLException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public class TeamRepositoryAdapter implements TeamRepository {

    private final SpringDataTeamRepository jpaRepository;
    private final TeamMapper mapper;

    public TeamRepositoryAdapter(
            SpringDataTeamRepository jpaRepository,
            TeamMapper mapper
    ) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Team save(Team team) {
        try {
            return mapper.toDomain(
                    jpaRepository.saveAndFlush(mapper.toEntity(team))
            );
        } catch (DataIntegrityViolationException exception) {
            if (isUniqueViolation(exception, "ux_teams_name_lower")) {
                throw new TeamNameAlreadyTakenException();
            }

            if (isUniqueViolation(exception, "ux_teams_tag_lower")) {
                throw new TeamTagAlreadyTakenException();
            }

            throw exception;
        }
    }

    private boolean isUniqueViolation(Throwable exception, String constraintName) {
        Throwable cause = exception;

        while (cause != null) {
            if (cause instanceof PSQLException postgresException) {
                return "23505".equals(postgresException.getSQLState())
                        && postgresException.getServerErrorMessage() != null
                        && constraintName.equals(
                        postgresException.getServerErrorMessage().getConstraint()
                );
            }

            cause = cause.getCause();
        }

        return false;
    }

    @Override
    public Optional<Team> findById(UUID id) {
        return jpaRepository.findById(id).map(mapper::toDomain);
    }

    @Override
    public List<Team> findAll() {
        return jpaRepository.findAllByOrderByCreatedAtDesc()
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public boolean existsByNameIgnoreCase(String name) {
        return jpaRepository.existsByNameIgnoreCase(name);
    }

    @Override
    public boolean existsByTagIgnoreCase(String tag) {
        return jpaRepository.existsByTagIgnoreCase(tag);
    }

    @Override
    public void delete(Team team) {
        jpaRepository.delete(mapper.toEntity(team));
    }
}
