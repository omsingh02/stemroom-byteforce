-- Stemroom database.
-- Load it once with: mysql -u root -p < database/schema.sql
-- This drops and recreates the stemroom database, so do not run it on data you want to keep.

DROP DATABASE IF EXISTS stemroom;
CREATE DATABASE stemroom CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
USE stemroom;

CREATE TABLE users (
    id            INT AUTO_INCREMENT PRIMARY KEY,
    name          VARCHAR(80)  NOT NULL,
    email         VARCHAR(120) NOT NULL UNIQUE,
    password_hash VARCHAR(160) NOT NULL,
    role          ENUM('ADMIN', 'MUSICIAN') NOT NULL DEFAULT 'MUSICIAN',
    created_at    TIMESTAMP    NOT NULL DEFAULT CURRENT_TIMESTAMP
);

-- One row per project. The owner is the musician who created it.
CREATE TABLE projects (
    id          INT AUTO_INCREMENT PRIMARY KEY,
    title       VARCHAR(120) NOT NULL,
    description TEXT,
    owner_id    INT NOT NULL,
    deadline    DATE,
    status      ENUM('OPEN', 'IN_PROGRESS', 'DONE') NOT NULL DEFAULT 'OPEN',
    created_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (owner_id) REFERENCES users (id) ON DELETE CASCADE
);

-- Musicians added to a project. The owner is not stored here.
CREATE TABLE project_members (
    project_id INT NOT NULL,
    user_id    INT NOT NULL,
    joined_at  TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    PRIMARY KEY (project_id, user_id),
    FOREIGN KEY (project_id) REFERENCES projects (id) ON DELETE CASCADE,
    FOREIGN KEY (user_id)    REFERENCES users (id)    ON DELETE CASCADE
);

-- Songs in a portfolio. Every new song starts as PENDING until an admin reviews it.
CREATE TABLE songs (
    id         INT AUTO_INCREMENT PRIMARY KEY,
    owner_id   INT NOT NULL,
    title      VARCHAR(120) NOT NULL,
    album      VARCHAR(120),
    file_name  VARCHAR(255),
    status     ENUM('PENDING', 'APPROVED', 'REJECTED') NOT NULL DEFAULT 'PENDING',
    created_at TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (owner_id) REFERENCES users (id) ON DELETE CASCADE
);

-- Key and value settings the admin can change while the app runs.
CREATE TABLE settings (
    setting_key   VARCHAR(60)  PRIMARY KEY,
    setting_value VARCHAR(255) NOT NULL
);

INSERT INTO settings (setting_key, setting_value) VALUES
    ('allow_registration', 'true'),
    ('max_projects_per_user', '10');

-- Demo accounts. Passwords: admin123 and musician123.
-- Hash format is salt$SHA-256(salt + password), the same as PasswordUtil.java.
INSERT INTO users (name, email, password_hash, role) VALUES
    ('Site Admin', 'admin@stemroom.local', CONCAT('seed1$', SHA2(CONCAT('seed1', 'admin123'), 256)), 'ADMIN'),
    ('Mira Lane',  'mira@stemroom.local',  CONCAT('seed2$', SHA2(CONCAT('seed2', 'musician123'), 256)), 'MUSICIAN'),
    ('Dev Okafor', 'dev@stemroom.local',   CONCAT('seed3$', SHA2(CONCAT('seed3', 'musician123'), 256)), 'MUSICIAN');

INSERT INTO projects (title, description, owner_id, deadline, status) VALUES
    ('Night Drive EP', 'Four lo-fi tracks recorded at home. Needs a mix pass and a bassline on track two.', 2, '2026-11-20', 'IN_PROGRESS'),
    ('Dev Beats Sketchbook', 'Loose ideas for a jazz trio. Nothing final yet.', 3, NULL, 'OPEN');

INSERT INTO project_members (project_id, user_id) VALUES (1, 3);

INSERT INTO songs (owner_id, title, album, file_name, status) VALUES
    (2, 'Low Light',      'Night Drive', 'low-light-v3.wav',    'APPROVED'),
    (2, 'Empty Platform', NULL,          'empty-platform.wav', 'PENDING'),
    (3, 'Brass Sketch 1', NULL,          'brass-1.mp3',        'PENDING');
