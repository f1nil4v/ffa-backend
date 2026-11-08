package com.ffa.tournaments.tournament.application.service;

import com.ffa.tournaments.tournament.application.result.TeamRosterMemberResult;
import org.junit.jupiter.api.Test;
import tools.jackson.databind.json.JsonMapper;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TournamentRosterSnapshotCodecTest {

    private final TournamentRosterSnapshotCodec codec =
            new TournamentRosterSnapshotCodec(JsonMapper.builder().build());

    @Test
    void roundTripsRosterSnapshot() {
        TeamRosterMemberResult captain = new TeamRosterMemberResult(
                UUID.randomUUID(),
                "captain",
                "76561198000000001",
                "CAPTAIN"
        );

        TeamRosterMemberResult member = new TeamRosterMemberResult(
                UUID.randomUUID(),
                "member",
                null,
                "MEMBER"
        );

        String encoded = codec.encode(List.of(captain, member));

        assertEquals(List.of(captain, member), codec.decode(encoded));
    }
}
