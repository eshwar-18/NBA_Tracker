-- ============================================================
-- searchgames_db — owned by the SearchGames microservice
-- Run this script once to create and populate the database.
-- ============================================================

CREATE DATABASE IF NOT EXISTS searchgames_db;
USE searchgames_db;

-- ── Teams ──────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS Teams (
    team_id   INT          NOT NULL AUTO_INCREMENT,
    name      VARCHAR(100) NOT NULL,
    city      VARCHAR(100) NOT NULL,
    PRIMARY KEY (team_id)
);

-- ── Players ────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS Players (
    player_id INT          NOT NULL AUTO_INCREMENT,
    name      VARCHAR(100) NOT NULL,
    team_id   INT,
    PRIMARY KEY (player_id),
    FOREIGN KEY (team_id) REFERENCES Teams(team_id)
);

-- ── Games ──────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS Games (
    game_id      INT         NOT NULL AUTO_INCREMENT,
    game_date    DATE        NOT NULL,
    location     VARCHAR(150),
    home_team_id INT         NOT NULL,
    away_team_id INT         NOT NULL,
    home_score   INT         NOT NULL DEFAULT 0,
    away_score   INT         NOT NULL DEFAULT 0,
    status       VARCHAR(50) NOT NULL DEFAULT 'Upcoming',
    PRIMARY KEY (game_id),
    FOREIGN KEY (home_team_id) REFERENCES Teams(team_id),
    FOREIGN KEY (away_team_id) REFERENCES Teams(team_id)
);

-- ── PlayerGameAppearances ──────────────────────────────────
CREATE TABLE IF NOT EXISTS PlayerGameAppearances (
    game_id   INT NOT NULL,
    player_id INT NOT NULL,
    PRIMARY KEY (game_id, player_id),
    FOREIGN KEY (game_id)   REFERENCES Games(game_id),
    FOREIGN KEY (player_id) REFERENCES Players(player_id)
);

-- ── Teams (2 Warriors, 3 Bulls, 4 Heat, 5 Celtics, 6 Nets, 7 Bucks) ─────────
INSERT IGNORE INTO Teams (team_id, name, city) VALUES
    (2, 'Warriors', 'San Francisco'),
    (3, 'Bulls',    'Chicago'),
    (4, 'Heat',     'Miami'),
    (5, 'Celtics',  'Boston'),
    (6, 'Nets',     'Brooklyn'),
    (7, 'Bucks',    'Milwaukee');

SET FOREIGN_KEY_CHECKS=0;
DELETE FROM PlayerGameAppearances WHERE game_id IN (101,102,103,104);
DELETE FROM Games  WHERE game_id    IN (101,102,103,104);
DELETE FROM Players WHERE player_id BETWEEN 201 AND 234;
SET FOREIGN_KEY_CHECKS=1;

INSERT INTO Games (game_id, game_date, location, home_team_id, away_team_id, home_score, away_score, status) VALUES
    (101, '2025-11-29', 'Fiserv Forum',    7, 6, 116,  99, 'Final'),
    (102, '2026-01-16', 'Barclays Center', 6, 3, 112, 109, 'Final'),
    (103, '2026-01-19', 'Chase Center',    2, 4, 135, 112, 'Final'),
    (104, '2026-01-23', 'Barclays Center', 6, 5, 126, 130, 'Final');

INSERT INTO Players (player_id, name, team_id) VALUES
    (201, 'Giannis Antetokounmpo', 7),
    (202, 'Myles Turner',          7),
    (203, 'Kevin Porter Jr.',      7),
    (204, 'AJ Green',              7),
    (205, 'Ryan Rollins',          7),
    (206, 'Danny Wolf',            6),
    (207, 'Ziaire Williams',       6),
    (208, 'Noah Clowney',          6),
    (209, 'Nic Claxton',           6),
    (210, 'Tyrese Martin',         6),
    (211, 'Michael Porter Jr.',    6),
    (212, 'Cam Thomas',            6),
    (213, 'Matas Buzelis',         3),
    (214, 'Nikola Vucevic',        3),
    (215, 'Coby White',            3),
    (216, 'Tre Jones',             3),
    (217, 'Jalen Smith',           3),
    (218, 'Ayo Dosunmu',           3),
    (219, 'Stephen Curry',         2),
    (220, 'Jimmy Butler III',      2),
    (221, 'Quinten Post',          2),
    (222, 'Moses Moody',           2),
    (223, 'Brandin Podziemski',    2),
    (224, 'Bam Adebayo',           4),
    (225, 'Andrew Wiggins',        4),
    (226, 'Norman Powell',         4),
    (227, 'Davion Mitchell',       4),
    (228, 'Jaime Jaquez Jr.',      4),
    (229, 'Jaylen Brown',          5),
    (230, 'Payton Pritchard',      5),
    (231, 'Sam Hauser',            5);

INSERT INTO PlayerGameAppearances (game_id, player_id) VALUES
    -- Game 101: Nets @ Bucks
    (101,201),(101,202),(101,203),(101,204),(101,205),
    (101,206),(101,207),(101,208),(101,209),(101,210),
    -- Game 102: Bulls @ Nets
    (102,211),(102,208),(102,209),(102,212),
    (102,213),(102,214),(102,215),(102,216),(102,217),(102,218),
    -- Game 103: Heat @ Warriors
    (103,219),(103,220),(103,221),(103,222),(103,223),
    (103,224),(103,225),(103,226),(103,227),(103,228),
    -- Game 104: Celtics @ Nets
    (104,229),(104,230),(104,231),
    (104,211),(104,208),(104,209),(104,212),(104,206);
