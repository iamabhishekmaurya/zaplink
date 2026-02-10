-- V3__Fix_bio_links_type_constraint.sql
-- Fix the check constraint for bio link types

-- Drop existing constraints if they exist
ALTER TABLE bio_links DROP CONSTRAINT IF EXISTS bio_links_type_check;
ALTER TABLE bio_links DROP CONSTRAINT IF EXISTS chk_bio_links_type;

-- Add correct constraint with all valid types
ALTER TABLE bio_links ADD CONSTRAINT chk_bio_links_type 
    CHECK (type IN ('LINK', 'PRODUCT', 'SOCIAL', 'EMAIL', 'PHONE', 'EMBED', 'SCHEDULED', 'GATED', 'PAYMENT'));
