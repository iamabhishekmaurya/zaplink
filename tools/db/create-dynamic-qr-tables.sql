-- Dynamic QR Code Tables Migration Script
-- Execute this script to add dynamic QR functionality to your database

-- Create dynamic_qr_codes table
CREATE TABLE IF NOT EXISTS dynamic_qr_codes (
    id BIGSERIAL PRIMARY KEY,
    qr_key VARCHAR(255) UNIQUE NOT NULL,
    qr_name VARCHAR(255),
    current_destination_url TEXT NOT NULL,
    qr_config JSONB,
    user_email VARCHAR(255),
    campaign_id VARCHAR(255),
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_scans BIGINT DEFAULT 0,
    last_scanned TIMESTAMP
);

-- Create indexes for dynamic_qr_codes
CREATE INDEX IF NOT EXISTS idx_dynamic_qr_key ON dynamic_qr_codes(qr_key);
CREATE INDEX IF NOT EXISTS idx_dynamic_user_email ON dynamic_qr_codes(user_email);
CREATE INDEX IF NOT EXISTS idx_dynamic_campaign_id ON dynamic_qr_codes(campaign_id);
CREATE INDEX IF NOT EXISTS idx_dynamic_is_active ON dynamic_qr_codes(is_active);
CREATE INDEX IF NOT EXISTS idx_dynamic_created_at ON dynamic_qr_codes(created_at);

-- Create qr_scan_analytics table
CREATE TABLE IF NOT EXISTS qr_scan_analytics (
    id BIGSERIAL PRIMARY KEY,
    qr_key VARCHAR(255) NOT NULL,
    ip_address INET,
    user_agent TEXT,
    referrer VARCHAR(500),
    country VARCHAR(100),
    city VARCHAR(100),
    device_type VARCHAR(50),
    browser VARCHAR(100),
    scanned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for qr_scan_analytics
CREATE INDEX IF NOT EXISTS idx_qr_analytics_qr_key ON qr_scan_analytics(qr_key);
CREATE INDEX IF NOT EXISTS idx_qr_analytics_scanned_at ON qr_scan_analytics(scanned_at);
CREATE INDEX IF NOT EXISTS idx_qr_analytics_country ON qr_scan_analytics(country);
CREATE INDEX IF NOT EXISTS idx_qr_analytics_device_type ON qr_scan_analytics(device_type);

-- Create qr_campaigns table
CREATE TABLE IF NOT EXISTS qr_campaigns (
    id BIGSERIAL PRIMARY KEY,
    campaign_id VARCHAR(255) UNIQUE NOT NULL,
    campaign_name VARCHAR(255) NOT NULL,
    user_email VARCHAR(255) NOT NULL,
    description TEXT,
    start_date TIMESTAMP,
    end_date TIMESTAMP,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for qr_campaigns
CREATE INDEX IF NOT EXISTS idx_campaigns_campaign_id ON qr_campaigns(campaign_id);
CREATE INDEX IF NOT EXISTS idx_campaigns_user_email ON qr_campaigns(user_email);
CREATE INDEX IF NOT EXISTS idx_campaigns_status ON qr_campaigns(status);

-- Add foreign key constraints
ALTER TABLE qr_scan_analytics 
ADD CONSTRAINT fk_qr_analytics_qr_key 
FOREIGN KEY (qr_key) REFERENCES dynamic_qr_codes(qr_key) ON DELETE CASCADE;

-- Create trigger to automatically update updated_at timestamp
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ language 'plpgsql';

CREATE TRIGGER update_dynamic_qr_updated_at 
BEFORE UPDATE ON dynamic_qr_codes 
FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();

CREATE TRIGGER update_campaigns_updated_at 
BEFORE UPDATE ON qr_campaigns 
FOR EACH ROW EXECUTE FUNCTION update_updated_at_column();
