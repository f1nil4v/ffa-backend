package com.ffa.tournaments.support.application.port;

import com.ffa.tournaments.support.domain.valueobject.SupportAccountId;

public interface SupportTokenIssuer {

    String issue(SupportAccountId supportAccountId);
}
