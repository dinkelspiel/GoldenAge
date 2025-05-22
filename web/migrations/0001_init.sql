CREATE TABLE
    IF NOT EXISTS `migrations` (
        `id` INT PRIMARY KEY AUTO_INCREMENT,
        `name` VARCHAR(16) NOT NULL
    );

INSERT INTO
    `migrations` (`name`)
VALUES
    ("0001_init");

CREATE TABLE
    IF NOT EXISTS `users` (
        `id` INT PRIMARY KEY AUTO_INCREMENT,
        `username` VARCHAR(32) NOT NULL UNIQUE,
        `email` VARCHAR(128) NOT NULL UNIQUE,
        `updated_at` TIMESTAMP,
        `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
    );

CREATE TABLE
    IF NOT EXISTS `servers` (
        `id` INT PRIMARY KEY AUTO_INCREMENT,
        `name` VARCHAR(64) NOT NULL UNIQUE,
        `secret` VARCHAR(64) NOT NULL,
        `server_address` VARCHAR(64) NOT NULL,
        `user_id` INT NOT NULL,
        `updated_at` TIMESTAMP,
        `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
    );

CREATE TABLE
    IF NOT EXISTS `statistics` (
        `id` INT PRIMARY KEY AUTO_INCREMENT,
        `player_count` INT NOT NULL,
        `game_version` VARCHAR(16) NOT NULL,
        `server_environment` VARCHAR(32) NOT NULL,
        `operating_system` VARCHAR(32) NOT NULL,
        `arch` VARCHAR(32) NOT NULL,
        `java_version` VARCHAR(16) NOT NULL,
        `server_id` INT NOT NULL,
        `updated_at` TIMESTAMP,
        `created_at` TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
    );