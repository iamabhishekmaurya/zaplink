-- Create tables for Manager Service Read Model in manager schema

-- Materialized view for team members with user details
CREATE TABLE IF NOT EXISTS manager.team_member_view (
    id BIGINT PRIMARY KEY,
    team_id BIGINT NOT NULL,
    team_name VARCHAR(255) NOT NULL,
    user_id BIGINT NOT NULL,
    username VARCHAR(50) NOT NULL,
    user_email VARCHAR(255) NOT NULL,
    first_name VARCHAR(50),
    last_name VARCHAR(50),
    role VARCHAR(50) NOT NULL,
    status VARCHAR(20) NOT NULL,
    invited_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    joined_at TIMESTAMP WITH TIME ZONE,
    organization_id BIGINT NOT NULL,
    organization_name VARCHAR(255) NOT NULL,
    last_updated TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Materialized view for pending posts
CREATE TABLE IF NOT EXISTS manager.pending_post_view (
    id BIGINT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    content TEXT,
    author_id BIGINT NOT NULL,
    author_name VARCHAR(255) NOT NULL,
    author_email VARCHAR(255) NOT NULL,
    campaign_id BIGINT,
    campaign_name VARCHAR(255),
    team_id BIGINT NOT NULL,
    team_name VARCHAR(255) NOT NULL,
    organization_id BIGINT NOT NULL,
    organization_name VARCHAR(255) NOT NULL,
    submitted_at TIMESTAMP WITH TIME ZONE,
    status VARCHAR(20) NOT NULL,
    last_updated TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Materialized view for influencer campaigns
CREATE TABLE IF NOT EXISTS manager.influencer_campaign_view (
    id BIGINT PRIMARY KEY,
    campaign_id BIGINT NOT NULL,
    campaign_name VARCHAR(255) NOT NULL,
    campaign_description TEXT,
    campaign_status VARCHAR(20) NOT NULL,
    start_date TIMESTAMP WITH TIME ZONE,
    end_date TIMESTAMP WITH TIME ZONE,
    team_member_id BIGINT NOT NULL,
    team_id BIGINT NOT NULL,
    team_name VARCHAR(255) NOT NULL,
    organization_id BIGINT NOT NULL,
    organization_name VARCHAR(255) NOT NULL,
    assignment_status VARCHAR(20) NOT NULL,
    assigned_at TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    completed_at TIMESTAMP WITH TIME ZONE,
    last_updated TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP
);

-- Create indexes for performance
CREATE INDEX IF NOT EXISTS idx_team_member_view_team_id ON manager.team_member_view(team_id);
CREATE INDEX IF NOT EXISTS idx_team_member_view_user_id ON manager.team_member_view(user_id);
CREATE INDEX IF NOT EXISTS idx_team_member_view_organization_id ON manager.team_member_view(organization_id);
CREATE INDEX IF NOT EXISTS idx_team_member_view_role ON manager.team_member_view(role);
CREATE INDEX IF NOT EXISTS idx_team_member_view_status ON manager.team_member_view(status);

CREATE INDEX IF NOT EXISTS idx_pending_post_view_author_id ON manager.pending_post_view(author_id);
CREATE INDEX IF NOT EXISTS idx_pending_post_view_campaign_id ON manager.pending_post_view(campaign_id);
CREATE INDEX IF NOT EXISTS idx_pending_post_view_team_id ON manager.pending_post_view(team_id);
CREATE INDEX IF NOT EXISTS idx_pending_post_view_organization_id ON manager.pending_post_view(organization_id);
CREATE INDEX IF NOT EXISTS idx_pending_post_view_status ON manager.pending_post_view(status);
CREATE INDEX IF NOT EXISTS idx_pending_post_view_submitted_at ON manager.pending_post_view(submitted_at);

CREATE INDEX IF NOT EXISTS idx_influencer_campaign_view_team_member_id ON manager.influencer_campaign_view(team_member_id);
CREATE INDEX IF NOT EXISTS idx_influencer_campaign_view_campaign_id ON manager.influencer_campaign_view(campaign_id);
CREATE INDEX IF NOT EXISTS idx_influencer_campaign_view_team_id ON manager.influencer_campaign_view(team_id);
CREATE INDEX IF NOT EXISTS idx_influencer_campaign_view_organization_id ON manager.influencer_campaign_view(organization_id);
CREATE INDEX IF NOT EXISTS idx_influencer_campaign_view_assignment_status ON manager.influencer_campaign_view(assignment_status);
