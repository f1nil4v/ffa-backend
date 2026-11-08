package com.ffa.tournaments.player.application.port;

import com.ffa.tournaments.identity.domain.valueobject.AccountId;

public interface PlayerProfileCreator {
    void create(AccountId accountId, String nickname);
}