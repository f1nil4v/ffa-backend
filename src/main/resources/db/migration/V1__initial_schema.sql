CREATE TABLE accounts (
                          id UUID PRIMARY KEY,
                          email VARCHAR(254) NOT NULL UNIQUE,
                          password_hash VARCHAR(255) NOT NULL,
                          email_verified BOOLEAN NOT NULL DEFAULT FALSE,
                          created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE player_profiles (
                                 id UUID PRIMARY KEY,
                                 account_id UUID NOT NULL UNIQUE REFERENCES accounts(id) ON DELETE CASCADE,
                                 nickname VARCHAR(32) NOT NULL,
                                 avatar_url TEXT,
                                 created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE UNIQUE INDEX idx_player_profiles_nickname_lower
    ON player_profiles (LOWER(nickname));

CREATE TABLE email_verification_tokens (
                                           id UUID PRIMARY KEY,
                                           account_id UUID NOT NULL REFERENCES accounts(id) ON DELETE CASCADE,
                                           token_hash VARCHAR(64) NOT NULL UNIQUE,
                                           expires_at TIMESTAMP WITH TIME ZONE NOT NULL,
                                           used_at TIMESTAMP WITH TIME ZONE,
                                           created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_email_verification_tokens_account_id
    ON email_verification_tokens(account_id);

CREATE INDEX idx_email_verification_tokens_expires_at
    ON email_verification_tokens(expires_at);

CREATE TABLE password_reset_tokens (
                                       id UUID PRIMARY KEY,
                                       account_id UUID NOT NULL REFERENCES accounts(id) ON DELETE CASCADE,
                                       token_hash VARCHAR(64) NOT NULL UNIQUE,
                                       expires_at TIMESTAMP WITH TIME ZONE NOT NULL,
                                       used_at TIMESTAMP WITH TIME ZONE,
                                       created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_password_reset_tokens_account_id
    ON password_reset_tokens(account_id);

CREATE INDEX idx_password_reset_tokens_expires_at
    ON password_reset_tokens(expires_at);

CREATE TABLE player_nickname_history (
                                         id UUID PRIMARY KEY,

                                         player_profile_id UUID NOT NULL
                                             REFERENCES player_profiles(id),

                                         nickname VARCHAR(32) NOT NULL,

                                         valid_from TIMESTAMPTZ NOT NULL,
                                         valid_to TIMESTAMPTZ,

                                         CONSTRAINT chk_nickname_history_period
                                             CHECK (valid_to IS NULL OR valid_to > valid_from)
);

CREATE INDEX idx_nickname_history_player
    ON player_nickname_history(player_profile_id);

CREATE UNIQUE INDEX idx_nickname_history_current
    ON player_nickname_history(player_profile_id)
    WHERE valid_to IS NULL;

CREATE TABLE nickname_change_requests (
                                          id UUID PRIMARY KEY,

                                          player_profile_id UUID NOT NULL
                                              REFERENCES player_profiles(id),

                                          old_nickname VARCHAR(32) NOT NULL,
                                          new_nickname VARCHAR(32) NOT NULL,
                                          reason TEXT NOT NULL,

                                          status VARCHAR(20) NOT NULL DEFAULT 'PENDING',

                                          reviewed_by UUID REFERENCES accounts(id),

                                          created_at TIMESTAMPTZ NOT NULL,
                                          reviewed_at TIMESTAMPTZ,

                                          CONSTRAINT chk_nickname_request_status
                                              CHECK (status IN (
                                                                'PENDING',
                                                                'APPROVED',
                                                                'REJECTED',
                                                                'CANCELLED'
                                                  )),

                                          CONSTRAINT chk_nickname_request_review
                                              CHECK (
                                                  (status IN ('APPROVED', 'REJECTED')
                                                      AND reviewed_by IS NOT NULL
                                                      AND reviewed_at IS NOT NULL)
                                                      OR
                                                  (status IN ('PENDING', 'CANCELLED')
                                                      AND reviewed_by IS NULL
                                                      AND reviewed_at IS NULL)
                                                  )
);

CREATE INDEX idx_nickname_requests_player
    ON nickname_change_requests(player_profile_id);

CREATE INDEX idx_nickname_requests_status
    ON nickname_change_requests(status);

CREATE UNIQUE INDEX idx_nickname_requests_pending
    ON nickname_change_requests(player_profile_id)
    WHERE status = 'PENDING';