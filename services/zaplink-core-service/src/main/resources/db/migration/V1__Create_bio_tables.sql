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

-- Create bio_links table
CREATE TABLE bio_links (
    id BIGSERIAL PRIMARY KEY,
    page_id BIGINT NOT NULL,
    title VARCHAR(200) NOT NULL,
    url VARCHAR(2048),
    type VARCHAR(20) NOT NULL,
    is_active BOOLEAN NOT NULL DEFAULT TRUE,
    sort_order INTEGER NOT NULL DEFAULT 0,
    price DECIMAL(10,2),
    currency VARCHAR(3),
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_bio_links_page_id 
        FOREIGN KEY (page_id) REFERENCES bio_pages(id) 
        ON DELETE CASCADE
);

-- Create index on page_id for fast lookups
CREATE INDEX idx_bio_links_page_id ON bio_links(page_id);

-- Create composite index on page_id and sort_order for ordered queries
CREATE INDEX idx_bio_links_page_sort_order ON bio_links(page_id, sort_order);

-- Create index on page_id and is_active for filtering active links
CREATE INDEX idx_bio_links_page_active ON bio_links(page_id, is_active);

-- Add check constraint for type
ALTER TABLE bio_links ADD CONSTRAINT chk_bio_links_type 
    CHECK (type IN ('LINK', 'PRODUCT', 'SOCIAL', 'EMAIL', 'PHONE'));

-- Add check constraint for currency (must be present if price is present)
ALTER TABLE bio_links ADD CONSTRAINT chk_bio_links_currency 
    CHECK ((price IS NULL) OR (currency IS NOT NULL));
