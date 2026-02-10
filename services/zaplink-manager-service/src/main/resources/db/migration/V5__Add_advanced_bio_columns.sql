-- V5__Add_advanced_bio_columns.sql
-- Advanced Bio Page feature enhancements

-- ============================================
-- 1. Add new columns to bio_pages table
-- ============================================

-- Add title for display name separate from username
ALTER TABLE bio_pages ADD COLUMN IF NOT EXISTS title VARCHAR(100);

-- Add cover image URL for banner
ALTER TABLE bio_pages ADD COLUMN IF NOT EXISTS cover_url VARCHAR(500);

-- Add SEO metadata as JSON
ALTER TABLE bio_pages ADD COLUMN IF NOT EXISTS seo_meta JSONB;

-- Add public visibility toggle (default true for backwards compatibility)
ALTER TABLE bio_pages ADD COLUMN IF NOT EXISTS is_public BOOLEAN NOT NULL DEFAULT TRUE;

-- Create index on is_public for filtering
CREATE INDEX IF NOT EXISTS idx_bio_pages_is_public ON bio_pages(is_public);

-- ============================================
-- 2. Add new columns to bio_links table
-- ============================================

-- Add metadata JSON for flexible link-specific data (thumbnails, SKU, oEmbed, etc.)
ALTER TABLE bio_links ADD COLUMN IF NOT EXISTS metadata JSONB;

-- Add schedule_from for scheduled link visibility start
ALTER TABLE bio_links ADD COLUMN IF NOT EXISTS schedule_from TIMESTAMP;

-- Add schedule_to for scheduled link visibility end
ALTER TABLE bio_links ADD COLUMN IF NOT EXISTS schedule_to TIMESTAMP;

-- Add custom icon URL
ALTER TABLE bio_links ADD COLUMN IF NOT EXISTS icon_url VARCHAR(500);

-- Add thumbnail/preview image URL
ALTER TABLE bio_links ADD COLUMN IF NOT EXISTS thumbnail_url VARCHAR(500);

-- Create index on schedule columns for filtering scheduled links
CREATE INDEX IF NOT EXISTS idx_bio_links_schedule ON bio_links(schedule_from, schedule_to);

-- ============================================
-- 3. Update link type constraint to include new types
-- ============================================

-- Drop the old constraint
ALTER TABLE bio_links DROP CONSTRAINT IF EXISTS chk_bio_links_type;

-- Add new constraint with expanded types
ALTER TABLE bio_links ADD CONSTRAINT chk_bio_links_type 
    CHECK (type IN ('LINK', 'PRODUCT', 'SOCIAL', 'EMAIL', 'PHONE', 'EMBED', 'SCHEDULED', 'GATED', 'PAYMENT'));

-- ============================================
-- 4. Create bio_clicks table for analytics
-- ============================================

CREATE TABLE IF NOT EXISTS bio_clicks (
    id BIGSERIAL PRIMARY KEY,
    link_id BIGINT NOT NULL,
    page_id BIGINT NOT NULL,
    referrer VARCHAR(2048),
    user_agent VARCHAR(1000),
    ip_hash VARCHAR(64), -- SHA-256 hash for privacy
    country_code VARCHAR(2),
    device_type VARCHAR(20), -- mobile, tablet, desktop
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    
    CONSTRAINT fk_bio_clicks_link_id 
        FOREIGN KEY (link_id) REFERENCES bio_links(id) 
        ON DELETE CASCADE,
    CONSTRAINT fk_bio_clicks_page_id 
        FOREIGN KEY (page_id) REFERENCES bio_pages(id) 
        ON DELETE CASCADE
);

-- Create indexes for efficient analytics queries
CREATE INDEX IF NOT EXISTS idx_bio_clicks_link_id ON bio_clicks(link_id);
CREATE INDEX IF NOT EXISTS idx_bio_clicks_page_id ON bio_clicks(page_id);
CREATE INDEX IF NOT EXISTS idx_bio_clicks_created_at ON bio_clicks(created_at);
CREATE INDEX IF NOT EXISTS idx_bio_clicks_page_created ON bio_clicks(page_id, created_at);

-- ============================================
-- 5. Add comments for documentation
-- ============================================

COMMENT ON COLUMN bio_pages.title IS 'Display title for the bio page (separate from username)';
COMMENT ON COLUMN bio_pages.cover_url IS 'URL to cover/banner image';
COMMENT ON COLUMN bio_pages.seo_meta IS 'SEO metadata JSON: {title, description, keywords, jsonLd}';
COMMENT ON COLUMN bio_pages.is_public IS 'Whether the bio page is publicly visible';

COMMENT ON COLUMN bio_links.metadata IS 'Flexible JSON for link-specific data (thumbnails, SKU, oEmbed data, etc.)';
COMMENT ON COLUMN bio_links.schedule_from IS 'Start timestamp for scheduled link visibility';
COMMENT ON COLUMN bio_links.schedule_to IS 'End timestamp for scheduled link visibility';
COMMENT ON COLUMN bio_links.icon_url IS 'Custom icon URL for the link';
COMMENT ON COLUMN bio_links.thumbnail_url IS 'Thumbnail/preview image URL for the link';

COMMENT ON TABLE bio_clicks IS 'Analytics table for tracking link clicks';
COMMENT ON COLUMN bio_clicks.ip_hash IS 'SHA-256 hash of IP address for privacy-preserving analytics';
