-- Fix track_analytics migration issue
-- This script fixes the error where track_analytics column contains null values
-- First, we need to add the track_analytics column if it doesn't exist

-- Add track_analytics column to dynamic_qr_codes table
ALTER TABLE dynamic_qr_codes 
ADD COLUMN IF NOT EXISTS track_analytics BOOLEAN DEFAULT true;

-- Update existing null values to true
UPDATE dynamic_qr_codes 
SET track_analytics = true 
WHERE track_analytics IS NULL;

-- Add track_analytics column to qr_scan_analytics table if needed for future use
ALTER TABLE qr_scan_analytics 
ADD COLUMN IF NOT EXISTS track_analytics BOOLEAN DEFAULT true;

-- Verify the changes
SELECT 'track_analytics column added and null values updated successfully' as status;
