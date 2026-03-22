-- ============================================================
-- frontend_db — owned by the Frontend microservice
-- Run this script once to create and populate the database.
-- ============================================================

CREATE DATABASE IF NOT EXISTS frontend_db;
USE frontend_db;

-- ── Users ──────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS Users (
    user_id    INT          NOT NULL AUTO_INCREMENT,
    username   VARCHAR(100) NOT NULL UNIQUE,
    password   VARCHAR(255) NOT NULL,
    email      VARCHAR(150) NOT NULL UNIQUE,
    role       VARCHAR(50)  NOT NULL DEFAULT 'user',
    created_at TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (user_id)
);

-- ── Sample users ───────────────────────────────────────────
-- Passwords are stored in plain text here to match the
-- existing User_CRUD implementation (no hashing yet).
-- Replace with bcrypt hashes before any production use.
INSERT INTO Users (username, password, email, role) VALUES
    ('admin',   'admin123',  'admin@nbascore.com',   'admin'),
    ('testuser','password1', 'test@nbascore.com',    'user');
