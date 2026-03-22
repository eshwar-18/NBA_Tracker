-- ============================================================
-- playerstats_db — owned by the PlayerStats microservice
-- Run this script once to create and populate the database.
-- ============================================================

CREATE DATABASE IF NOT EXISTS playerstats_db;
USE playerstats_db;

-- ── Players ────────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS Players (
    player_id INT          NOT NULL AUTO_INCREMENT,
    name      VARCHAR(100) NOT NULL,
    PRIMARY KEY (player_id)
);

-- ── PlayerStats ────────────────────────────────────────────
CREATE TABLE IF NOT EXISTS PlayerStats (
    stat_id        INT NOT NULL AUTO_INCREMENT,
    game_id        INT NOT NULL,
    player_id      INT NOT NULL,
    points         INT NOT NULL DEFAULT 0,
    assists        INT NOT NULL DEFAULT 0,
    rebounds       INT NOT NULL DEFAULT 0,
    minutes_played INT NOT NULL DEFAULT 0,
    PRIMARY KEY (stat_id),
    UNIQUE KEY uq_game_player (game_id, player_id),
    FOREIGN KEY (player_id) REFERENCES Players(player_id)
);

DELETE FROM PlayerStats WHERE game_id IN (101,102,103,104);
DELETE FROM Players WHERE player_id BETWEEN 201 AND 231;

INSERT INTO Players (player_id, name) VALUES
    (201, 'Giannis Antetokounmpo'),
    (202, 'Myles Turner'),
    (203, 'Kevin Porter Jr.'),
    (204, 'AJ Green'),
    (205, 'Ryan Rollins'),
    (206, 'Danny Wolf'),
    (207, 'Ziaire Williams'),
    (208, 'Noah Clowney'),
    (209, 'Nic Claxton'),
    (210, 'Tyrese Martin'),
    (211, 'Michael Porter Jr.'),
    (212, 'Cam Thomas'),
    (213, 'Matas Buzelis'),
    (214, 'Nikola Vucevic'),
    (215, 'Coby White'),
    (216, 'Tre Jones'),
    (217, 'Jalen Smith'),
    (218, 'Ayo Dosunmu'),
    (219, 'Stephen Curry'),
    (220, 'Jimmy Butler III'),
    (221, 'Quinten Post'),
    (222, 'Moses Moody'),
    (223, 'Brandin Podziemski'),
    (224, 'Bam Adebayo'),
    (225, 'Andrew Wiggins'),
    (226, 'Norman Powell'),
    (227, 'Davion Mitchell'),
    (228, 'Jaime Jaquez Jr.'),
    (229, 'Jaylen Brown'),
    (230, 'Payton Pritchard'),
    (231, 'Sam Hauser');

INSERT INTO PlayerStats (game_id, player_id, points, assists, rebounds, minutes_played) VALUES
    -- Game 101: Nets @ Bucks, 2025-11-29
    (101, 201, 29, 2,  8, 19),
    (101, 202, 10, 2,  3, 22),
    (101, 203, 13, 6,  4, 25),
    (101, 204, 15, 5,  3, 23),
    (101, 205, 10, 4,  2, 31),
    (101, 206, 22, 4,  4, 30),
    (101, 207, 10, 1,  2, 19),
    (101, 208,  6, 1,  1, 21),
    (101, 209,  7, 3,  4, 25),
    (101, 210,  9, 1,  6, 24),
    -- Game 102: Bulls @ Nets, 2026-01-16
    (102, 211, 26, 1,  7, 32),
    (102, 208, 23, 1, 11, 28),
    (102, 209,  7, 5, 14, 29),
    (102, 212,  8, 4,  1, 25),
    (102, 213, 15, 3,  6, 32),
    (102, 214, 19, 5,  6, 33),
    (102, 215, 17, 4,  4, 33),
    (102, 216, 11, 5,  5, 30),
    (102, 217, 14, 4, 13, 35),
    (102, 218, 18, 1,  1, 20),
    -- Game 103: Heat @ Warriors, 2026-01-19
    (103, 219, 19, 11,  3, 28),
    (103, 220, 17,  4,  3, 21),
    (103, 221, 15,  3,  9, 21),
    (103, 222, 13,  2,  4, 29),
    (103, 223, 24,  4,  6, 30),
    (103, 224,  4,  3, 12, 26),
    (103, 225, 18,  1,  5, 27),
    (103, 226, 21,  3,  3, 30),
    (103, 227,  7,  3,  6, 25),
    (103, 228, 10,  9,  5, 26),
    -- Game 104: Celtics @ Nets, 2026-01-23
    (104, 229, 27, 12, 10, 46),
    (104, 230, 32,  4,  4, 40),
    (104, 231, 19,  3,  4, 39),
    (104, 211, 30,  4,  8, 42),
    (104, 208, 15,  1,  9, 43),
    (104, 209, 18,  4,  9, 39),
    (104, 212, 21,  2,  3, 37),
    (104, 206,  4,  2,  6, 18);
