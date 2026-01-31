-- Create bio_pages table
CREATE TABLE bio_pages (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(50) NOT NULL UNIQUE,
    owner_id VARCHAR(255) NOT NULL,
    theme_config JSONB,
    avatar_url VARCHAR(500),
    bio_text VARCHAR(500),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- Create index on username for fast lookups
CREATE INDEX idx_bio_pages_username ON bio_pages(username);

-- Create index on owner_id for user-specific queries
CREATE INDEX idx_bio_pages_owner_id ON bio_pages(owner_id);

-- Add trigger to automatically update updated_at
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_bio_pages_updated_at 
    BEFORE UPDATE ON bio_pages 
    FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
