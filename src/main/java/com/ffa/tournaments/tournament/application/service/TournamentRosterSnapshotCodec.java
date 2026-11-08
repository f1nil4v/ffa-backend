package com.ffa.tournaments.tournament.application.service;

import com.ffa.tournaments.tournament.application.result.TeamRosterMemberResult;
import org.springframework.stereotype.Component;
import tools.jackson.core.JacksonException;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;

@Component
public class TournamentRosterSnapshotCodec {

    private static final TypeReference<List<TeamRosterMemberResult>> ROSTER_TYPE =
            new TypeReference<>() {
            };

    private final JsonMapper jsonMapper;

    public TournamentRosterSnapshotCodec(JsonMapper jsonMapper) {
        this.jsonMapper = jsonMapper;
    }

    public String encode(List<TeamRosterMemberResult> roster) {
        try {
            return jsonMapper.writeValueAsString(roster);
        } catch (JacksonException exception) {
            throw new IllegalStateException("Failed to serialize tournament roster", exception);
        }
    }

    public List<TeamRosterMemberResult> decode(String rosterSnapshot) {
        try {
            return jsonMapper.readValue(rosterSnapshot, ROSTER_TYPE);
        } catch (JacksonException exception) {
            throw new IllegalStateException("Failed to read tournament roster", exception);
        }
    }
}
