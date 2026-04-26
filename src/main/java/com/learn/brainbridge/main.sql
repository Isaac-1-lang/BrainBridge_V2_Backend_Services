    -- =====================================================
    -- DATABASE SCHEMA: Users, Organizations, Teams, Projects, Ideas
    -- =====================================================

    -- -------------------------
    -- ENUMS
    -- -------------------------
    CREATE TYPE organization_type AS ENUM ('SCHOOL', 'HUB', 'COMPANY', 'OTHER');
    CREATE TYPE team_member_role AS ENUM ('OWNER', 'ADMIN', 'MEMBER');
    CREATE TYPE project_status AS ENUM ('DRAFT', 'ACTIVE', 'ARCHIVED', 'DELETED');
    CREATE TYPE project_visibility AS ENUM ('PUBLIC', 'ORG_ONLY', 'PRIVATE');
    CREATE TYPE project_member_role AS ENUM ('OWNER', 'MAINTAINER', 'CONTRIBUTOR', 'VIEWER');
    CREATE TYPE idea_status AS ENUM ('DRAFT', 'PUBLISHED', 'PROMOTED', 'ARCHIVED');
    CREATE TYPE comment_target_type AS ENUM ('PROJECT', 'IDEA');
    CREATE TYPE activity_action AS ENUM ('CREATED_PROJECT', 'UPDATED_PROJECT', 'COMMENTED', 'JOINED_TEAM');
    CREATE TYPE activity_entity AS ENUM ('PROJECT', 'IDEA', 'TEAM', 'COMMENT', 'USER');
    CREATE TYPE analytics_entity AS ENUM ('PROJECT', 'IDEA', 'TEAM', 'COMMENT', 'USER');
    CREATE TYPE notification_type AS ENUM ('COMMENT', 'MENTION', 'INVITE', 'SYSTEM');

    -- -------------------------
    -- ORGANIZATIONS
    -- -------------------------
    CREATE TABLE organizations (
        id BIGSERIAL PRIMARY KEY,
        name VARCHAR(255) UNIQUE NOT NULL,
        type organization_type NOT NULL,
        logo_url TEXT,
        created_at TIMESTAMPTZ DEFAULT NOW(),
        updated_at TIMESTAMPTZ DEFAULT NOW()
    );

    -- -------------------------
    -- USERS
    -- -------------------------
    CREATE TABLE users (
        id BIGSERIAL PRIMARY KEY,
        email VARCHAR(255) UNIQUE NOT NULL,
        username VARCHAR(255) UNIQUE NOT NULL,
        password_hash VARCHAR(255) NOT NULL,
        first_name VARCHAR(255),
        last_name VARCHAR(255),
        profile_image_url TEXT,
        phone VARCHAR(50),
        biography TEXT,
        organization_id BIGINT REFERENCES organizations(id) ON DELETE SET NULL,
        is_active BOOLEAN DEFAULT TRUE,
        is_email_verified BOOLEAN DEFAULT FALSE,
        created_at TIMESTAMPTZ DEFAULT NOW(),
        updated_at TIMESTAMPTZ DEFAULT NOW()
    );
    CREATE INDEX idx_users_org ON users(organization_id);

    -- -------------------------
    -- TEAMS
    -- -------------------------
    CREATE TABLE teams (
        id BIGSERIAL PRIMARY KEY,
        name VARCHAR(255) NOT NULL,
        description TEXT,
        organization_id BIGINT REFERENCES organizations(id) ON DELETE SET NULL,
        created_by BIGINT REFERENCES users(id) ON DELETE SET NULL,
        created_at TIMESTAMPTZ DEFAULT NOW(),
        updated_at TIMESTAMPTZ DEFAULT NOW(),
        UNIQUE (name, organization_id)
    );

    -- -------------------------
    -- TEAM MEMBERS
    -- -------------------------
    CREATE TABLE team_members (
        team_id BIGINT REFERENCES teams(id) ON DELETE CASCADE,
        user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
        role team_member_role NOT NULL,
        joined_at TIMESTAMPTZ DEFAULT NOW(),
        PRIMARY KEY (team_id, user_id)
    );
    CREATE INDEX idx_team_members_user ON team_members(user_id);

    -- -------------------------
    -- PROJECTS
    -- -------------------------
    CREATE TABLE projects (
        id BIGSERIAL PRIMARY KEY,
        title VARCHAR(255) NOT NULL,
        description TEXT,
        status project_status NOT NULL DEFAULT 'DRAFT',
        visibility project_visibility NOT NULL DEFAULT 'PRIVATE',
        owner_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
        team_id BIGINT REFERENCES teams(id) ON DELETE SET NULL,
        organization_id BIGINT REFERENCES organizations(id) ON DELETE SET NULL,
        source_idea_id BIGINT REFERENCES ideas(id) ON DELETE SET NULL,
        cover_image_url TEXT,
        repo_url TEXT,
        demo_url TEXT,
        start_date DATE,
        end_date DATE,
        created_at TIMESTAMPTZ DEFAULT NOW(),
        updated_at TIMESTAMPTZ DEFAULT NOW()
    );
    CREATE INDEX idx_projects_owner ON projects(owner_id);
    CREATE INDEX idx_projects_team ON projects(team_id);
    CREATE INDEX idx_projects_org ON projects(organization_id);
    CREATE INDEX idx_projects_status_visibility ON projects(status, visibility);

    -- -------------------------
    -- PROJECT MEMBERS
    -- -------------------------
    CREATE TABLE project_members (
        project_id BIGINT REFERENCES projects(id) ON DELETE CASCADE,
        user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
        role project_member_role NOT NULL,
        joined_at TIMESTAMPTZ DEFAULT NOW(),
        PRIMARY KEY (project_id, user_id)
    );
    CREATE INDEX idx_project_members_user ON project_members(user_id);

    -- -------------------------
    -- IDEAS
    -- -------------------------
    CREATE TABLE ideas (
        id BIGSERIAL PRIMARY KEY,
        title VARCHAR(255) NOT NULL,
        summary TEXT,
        status idea_status NOT NULL DEFAULT 'DRAFT',
        author_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
        organization_id BIGINT REFERENCES organizations(id) ON DELETE SET NULL,
        created_at TIMESTAMPTZ DEFAULT NOW(),
        updated_at TIMESTAMPTZ DEFAULT NOW()
    );

    -- -------------------------
    -- COMMENTS
    -- -------------------------
    CREATE TABLE comments (
        id BIGSERIAL PRIMARY KEY,
        body TEXT NOT NULL,
        author_id BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
        project_id BIGINT REFERENCES projects(id) ON DELETE CASCADE,
        idea_id BIGINT REFERENCES ideas(id) ON DELETE CASCADE,
        parent_id BIGINT REFERENCES comments(id) ON DELETE CASCADE,
        created_at TIMESTAMPTZ DEFAULT NOW(),
        updated_at TIMESTAMPTZ DEFAULT NOW()
    );
    CREATE INDEX idx_comments_project ON comments(project_id);
    CREATE INDEX idx_comments_idea ON comments(idea_id);
    CREATE INDEX idx_comments_parent ON comments(parent_id);

    -- -------------------------
    -- ATTACHMENTS
    -- -------------------------
    CREATE TABLE attachments (
        id BIGSERIAL PRIMARY KEY,
        url TEXT,
        file_name VARCHAR(255),
        mime_type VARCHAR(100),
        size_bytes BIGINT,
        project_id BIGINT REFERENCES projects(id) ON DELETE CASCADE,
        idea_id BIGINT REFERENCES ideas(id) ON DELETE CASCADE,
        uploaded_by BIGINT REFERENCES users(id) ON DELETE SET NULL,
        created_at TIMESTAMPTZ DEFAULT NOW()
    );

    -- -------------------------
    -- TAGS
    -- -------------------------
    CREATE TABLE tags (
        id BIGSERIAL PRIMARY KEY,
        name VARCHAR(255) UNIQUE NOT NULL
    );

    -- -------------------------
    -- PROJECT_TAGS
    -- -------------------------
    CREATE TABLE project_tags (
        project_id BIGINT REFERENCES projects(id) ON DELETE CASCADE,
        tag_id BIGINT REFERENCES tags(id) ON DELETE CASCADE,
        PRIMARY KEY (project_id, tag_id)
    );

    -- -------------------------
    -- ACTIVITIES
    -- -------------------------
    CREATE TABLE activities (
        id BIGSERIAL PRIMARY KEY,
        actor_id BIGINT REFERENCES users(id) ON DELETE SET NULL,
        action activity_action NOT NULL,
        entity_type activity_entity NOT NULL,
        entity_id BIGINT NOT NULL,
        metadata JSONB,
        created_at TIMESTAMPTZ DEFAULT NOW()
    );
    CREATE INDEX idx_activities_entity ON activities(entity_type, entity_id);
    CREATE INDEX idx_activities_actor ON activities(actor_id);

    -- -------------------------
    -- ANALYTICS_EVENTS
    -- -------------------------
    CREATE TABLE analytics_events (
        id BIGSERIAL PRIMARY KEY,
        user_id BIGINT REFERENCES users(id) ON DELETE SET NULL,
        event_type VARCHAR(100) NOT NULL,
        entity_type analytics_entity,
        entity_id BIGINT,
        context JSONB,
        created_at TIMESTAMPTZ DEFAULT NOW()
    );

    -- -------------------------
    -- NOTIFICATIONS
    -- -------------------------
    CREATE TABLE notifications (
        id BIGSERIAL PRIMARY KEY,
        user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
        type notification_type NOT NULL,
        title VARCHAR(255),
        body TEXT,
        is_read BOOLEAN DEFAULT FALSE,
        created_at TIMESTAMPTZ DEFAULT NOW()
    );
    CREATE INDEX idx_notifications_user_read ON notifications(user_id, is_read);

    -- -------------------------
    -- AUTH SUPPORT
    -- -------------------------
    CREATE TABLE password_reset_tokens (
        id BIGSERIAL PRIMARY KEY,
        user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
        token VARCHAR(255) NOT NULL,
        expires_at TIMESTAMPTZ NOT NULL,
        used BOOLEAN DEFAULT FALSE
    );

    CREATE TABLE email_verification_tokens (
        id BIGSERIAL PRIMARY KEY,
        user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
        token VARCHAR(255) NOT NULL,
        expires_at TIMESTAMPTZ NOT NULL,
        used BOOLEAN DEFAULT FALSE
    );

    CREATE TABLE refresh_tokens (
        id BIGSERIAL PRIMARY KEY,
        user_id BIGINT REFERENCES users(id) ON DELETE CASCADE,
        token VARCHAR(255) NOT NULL,
        expires_at TIMESTAMPTZ NOT NULL,
        user_agent VARCHAR(255),
        ip VARCHAR(50)
    );
