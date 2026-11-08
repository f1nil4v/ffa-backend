CREATE TABLE tournaments (
    id UUID PRIMARY KEY,
    name VARCHAR(128) NOT NULL,
    description TEXT,
    format VARCHAR(32) NOT NULL
        CHECK (format IN ('SINGLE_ELIMINATION')),
    status VARCHAR(20) NOT NULL DEFAULT 'DRAFT'
        CHECK (status IN ('DRAFT', 'REGISTRATION', 'ONGOING', 'FINISHED', 'CANCELLED')),
    max_teams INT NOT NULL CHECK (max_teams BETWEEN 2 AND 128),
    registration_opens_at TIMESTAMP WITH TIME ZONE NOT NULL,
    registration_closes_at TIMESTAMP WITH TIME ZONE NOT NULL,
    starts_at TIMESTAMP WITH TIME ZONE,
    created_by UUID NOT NULL REFERENCES support_accounts(id),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT chk_tournament_registration_window
        CHECK (registration_closes_at > registration_opens_at),
    CONSTRAINT chk_tournament_start
        CHECK (starts_at IS NULL OR starts_at >= registration_closes_at)
);

CREATE INDEX idx_tournaments_status ON tournaments(status);

CREATE TABLE tournament_registrations (
    id UUID PRIMARY KEY,
    tournament_id UUID NOT NULL REFERENCES tournaments(id) ON DELETE CASCADE,
    team_id UUID NOT NULL REFERENCES teams(id),
    registered_by UUID NOT NULL REFERENCES player_profiles(id),
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING'
        CHECK (status IN ('PENDING', 'APPROVED', 'REJECTED', 'WITHDRAWN', 'DISQUALIFIED')),
    roster_snapshot JSONB NOT NULL,
    seed INT,
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    reviewed_by UUID REFERENCES support_accounts(id),
    reviewed_at TIMESTAMP WITH TIME ZONE,
    CONSTRAINT chk_tournament_registration_review
        CHECK (
            (status IN ('APPROVED', 'REJECTED', 'DISQUALIFIED')
                AND reviewed_by IS NOT NULL
                AND reviewed_at IS NOT NULL)
                OR
            (status IN ('PENDING', 'WITHDRAWN')
                AND reviewed_by IS NULL
                AND reviewed_at IS NULL)
        )
);

CREATE UNIQUE INDEX ux_tournament_registration_active
    ON tournament_registrations(tournament_id, team_id)
    WHERE status IN ('PENDING', 'APPROVED');

CREATE INDEX idx_tournament_registrations_tournament
    ON tournament_registrations(tournament_id);

CREATE INDEX idx_tournament_registrations_team
    ON tournament_registrations(team_id);
