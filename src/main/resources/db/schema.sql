CREATE SCHEMA IF NOT EXISTS solvd_events;

SET SCHEMA 'solvd_events';

DROP TABLE IF EXISTS users;
CREATE TABLE users (
                   id SERIAL,
                   username VARCHAR(50) NOT NULL,
                   password VARCHAR(255) NOT NULL,
                   first_name VARCHAR(255) NOT NULL,
                   last_name VARCHAR(255) NOT NULL,
                   session_id VARCHAR(50) NULL,
                   session_expired_in TIMESTAMP NULL,
                   PRIMARY KEY (id));
CREATE UNIQUE INDEX USERS_USERNAME_UNIQUE ON users (username);

DROP TABLE IF EXISTS events;
CREATE TABLE events (
                       id SERIAL,
                       owner_id INT NOT NULL,
                       date TIMESTAMP NOT NULL,
                       type VARCHAR(20) NOT NULL,
                       schedule TEXT NOT NULL,
                       PRIMARY KEY (id),
                       CONSTRAINT fk_EVENTS_USERS1
                           FOREIGN KEY (owner_id)
                               REFERENCES users (id)
                               ON DELETE NO ACTION
                               ON UPDATE NO ACTION);