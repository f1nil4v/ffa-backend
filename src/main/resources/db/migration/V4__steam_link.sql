ALTER TABLE player_profiles ADD COLUMN steam_id VARCHAR(17);
ALTER TABLE player_profiles ADD COLUMN steam_linked_at TIMESTAMP WITH TIME ZONE;

CREATE UNIQUE INDEX ux_player_profiles_steam_id
    ON player_profiles(steam_id)
    WHERE steam_id IS NOT NULL;

CREATE TABLE steam_link_states (
    id UUID PRIMARY KEY,
    account_id UUID NOT NULL REFERENCES accounts(id) ON DELETE CASCADE,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP WITH TIME ZONE NOT NULL
);

CREATE INDEX idx_steam_link_states_account_id
    ON steam_link_states(account_id);

CREATE INDEX idx_steam_link_states_expires_at
    ON steam_link_states(expires_at);
