CREATE TABLE teams (
    id UUID PRIMARY KEY,
    name VARCHAR(64) NOT NULL,
    tag VARCHAR(16) NOT NULL,
    captain_profile_id UUID NOT NULL
        REFERENCES player_profiles(id) ON DELETE CASCADE,
    description TEXT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

CREATE UNIQUE INDEX ux_teams_name_lower ON teams(LOWER(name));
CREATE UNIQUE INDEX ux_teams_tag_lower ON teams(LOWER(tag));

CREATE TABLE team_members (
    team_id UUID NOT NULL REFERENCES teams(id) ON DELETE CASCADE,
    player_profile_id UUID NOT NULL UNIQUE
        REFERENCES player_profiles(id) ON DELETE CASCADE,
    role VARCHAR(20) NOT NULL CHECK (role IN ('CAPTAIN', 'MEMBER')),
    joined_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (team_id, player_profile_id)
);

CREATE UNIQUE INDEX ux_team_one_captain
    ON team_members(team_id) WHERE role = 'CAPTAIN';

CREATE INDEX idx_team_members_player_profile
    ON team_members(player_profile_id);

CREATE TABLE team_applications (
    id UUID PRIMARY KEY,
    team_id UUID NOT NULL REFERENCES teams(id) ON DELETE CASCADE,
    player_profile_id UUID NOT NULL
        REFERENCES player_profiles(id) ON DELETE CASCADE,
    message TEXT,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING'
        CHECK (status IN ('PENDING', 'APPROVED', 'REJECTED', 'WITHDRAWN')),
    reviewed_by UUID REFERENCES player_profiles(id),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    reviewed_at TIMESTAMP WITH TIME ZONE
);

CREATE UNIQUE INDEX ux_team_application_pending
    ON team_applications(team_id, player_profile_id) WHERE status = 'PENDING';

CREATE INDEX idx_team_applications_team
    ON team_applications(team_id);

CREATE INDEX idx_team_applications_player
    ON team_applications(player_profile_id);
