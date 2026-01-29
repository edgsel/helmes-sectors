-- liquibase formatted sql logicalFilePath:classpath:/db/changelog/2026/1769355705091-hlms-001-init-tables.sql
-- changeset edgsel:hlms-001-init-tables

CREATE EXTENSION IF NOT EXISTS "uuid-ossp";

CREATE TABLE sectors (
    id           BIGINT PRIMARY KEY,
    parent_id    BIGINT REFERENCES sectors(id),
    name         TEXT NOT NULL,
    sector_level INT NOT NULL,
    created_at   TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at   TIMESTAMP WITH TIME ZONE
);

CREATE TABLE users (
    id            BIGINT PRIMARY KEY GENERATED ALWAYS AS IDENTITY,
    username      TEXT NOT NULL UNIQUE,
    password_hash TEXT NOT NULL,
    created_at    TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at    TIMESTAMP WITH TIME ZONE
);

CREATE TABLE applications (
    id              UUID PRIMARY KEY DEFAULT uuid_generate_v4(),
    user_id         BIGINT NOT NULL REFERENCES users(id) ON DELETE CASCADE,
    applicant_name  TEXT NOT NULL,
    agreed_to_terms BOOLEAN NOT NULL DEFAULT FALSE,
    created_at      TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP WITH TIME ZONE
);

CREATE TABLE application_sectors (
    application_id UUID NOT NULL REFERENCES applications(id) ON DELETE CASCADE,
    sector_id      INT NOT NULL REFERENCES sectors(id),
    created_at     TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (application_id, sector_id)
);

CREATE INDEX idx_sectors_parent_id ON sectors(parent_id);
CREATE INDEX idx_sectors_level_id ON sectors(sector_level, id);
CREATE INDEX idx_applications_user_id ON applications(user_id);
CREATE INDEX idx_application_sectors_sector_id ON application_sectors(sector_id);
