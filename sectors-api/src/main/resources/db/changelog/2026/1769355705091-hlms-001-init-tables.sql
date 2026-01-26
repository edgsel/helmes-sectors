-- liquibase formatted sql logicalFilePath:classpath:/db/changelog/2026/1769355705091-hlms-001-init-tables.sql
-- changeset edgsel:hlms-001-init-tables

CREATE TABLE sectors (
    id                INT PRIMARY KEY,
    parent_id         INT REFERENCES sectors(id),
    name              TEXT NOT NULL,
    sector_level      INT NOT NULL,
    created_at        TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at        TIMESTAMP WITH TIME ZONE
);

CREATE TABLE user_data (
    id              INT GENERATED ALWAYS AS IDENTITY PRIMARY KEY,
    name            TEXT NOT NULL,
    sector_id       INT NOT NULL REFERENCES sectors(id),
    agreed_to_terms BOOLEAN NOT NULL DEFAULT FALSE,
    created_at      TIMESTAMP WITH TIME ZONE DEFAULT CURRENT_TIMESTAMP,
    updated_at      TIMESTAMP WITH TIME ZONE
);

CREATE INDEX idx_sectors_parent_id ON sectors(parent_id);
CREATE INDEX idx_user_data_sector_id ON user_data(sector_id);
