CREATE TABLE support_accounts (
                                 id UUID PRIMARY KEY,
                                 email VARCHAR(254) NOT NULL UNIQUE,
                                 password_hash VARCHAR(255) NOT NULL,
                                 display_name VARCHAR(100) NOT NULL,
                                 active BOOLEAN NOT NULL DEFAULT TRUE,
                                 created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP
);

ALTER TABLE nickname_change_requests
    DROP CONSTRAINT IF EXISTS nickname_change_requests_reviewed_by_fkey;

ALTER TABLE nickname_change_requests
    ADD CONSTRAINT fk_nickname_requests_reviewed_by
        FOREIGN KEY (reviewed_by) REFERENCES support_accounts(id);

CREATE INDEX idx_nickname_requests_reviewed_by
    ON nickname_change_requests(reviewed_by);
