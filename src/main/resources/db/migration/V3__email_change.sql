CREATE TABLE email_change_requests (
    id UUID PRIMARY KEY,
    account_id UUID NOT NULL REFERENCES accounts(id) ON DELETE CASCADE,
    current_email VARCHAR(254) NOT NULL,
    new_email VARCHAR(254) NOT NULL,
    old_token_hash VARCHAR(64) NOT NULL,
    new_token_hash VARCHAR(64) NOT NULL,
    old_confirmed_at TIMESTAMP WITH TIME ZONE,
    new_confirmed_at TIMESTAMP WITH TIME ZONE,
    status VARCHAR(20) NOT NULL DEFAULT 'PENDING'
        CHECK (status IN ('PENDING', 'COMPLETED', 'CANCELLED', 'EXPIRED')),
    created_at TIMESTAMP WITH TIME ZONE NOT NULL DEFAULT CURRENT_TIMESTAMP,
    expires_at TIMESTAMP WITH TIME ZONE NOT NULL,
    CONSTRAINT chk_email_change_confirmed
        CHECK (status <> 'COMPLETED' OR (old_confirmed_at IS NOT NULL AND new_confirmed_at IS NOT NULL))
);

CREATE INDEX idx_email_change_requests_account_id
    ON email_change_requests(account_id);

CREATE UNIQUE INDEX ux_email_change_pending
    ON email_change_requests(account_id)
    WHERE status = 'PENDING';
