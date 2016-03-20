DROP INDEX IF EXISTS matches__owner_id__index;
DROP INDEX IF EXISTS matches__guests_id__index;
DROP INDEX IF EXISTS matches__next_match_id__index;
DROP TABLE IF EXISTS teams CASCADE;
DROP TABLE IF EXISTS tournament CASCADE;
DROP TABLE IF EXISTS matches CASCADE;


CREATE TABLE teams (
  id    SERIAL PRIMARY KEY,
  title VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE tournament (
  id              SERIAL PRIMARY KEY,
  title           VARCHAR(50) NOT NULL UNIQUE,
  number_of_teams INTEGER     NOT NULL,
  season          VARCHAR(15) NOT NULL
);

CREATE TABLE matches (
  match_id        SERIAL PRIMARY KEY,
  tournament_id   INTEGER     NOT NULL,
  match_data      TIMESTAMPTZ NOT NULL,
  owner_id        INTEGER REFERENCES teams (id),
  guests_id       INTEGER REFERENCES teams (id),
  owner_id_score  INTEGER,
  guests_id_score INTEGER,
  next_match_id   INTEGER REFERENCES matches (match_id),
  status          VARCHAR(20) DEFAULT ('is not played'),
  CHECK (owner_id != guests_id)
);

CREATE INDEX matches__owner_id__index ON matches (owner_id);
CREATE INDEX matches__guests_id__index ON matches (guests_id);
CREATE INDEX matches__next_match_id__index ON matches (next_match_id);


INSERT INTO tournament (id, title, number_of_teams, season) VALUES (1, 'La Liga', 10, '2010/2011');
INSERT INTO tournament (id, title, number_of_teams, season) VALUES (2, 'Copa del Rey', 20, '2010/2011');
INSERT INTO tournament (id, title, number_of_teams, season) VALUES (3, 'Bundesliga', 10, '2011/2012');
INSERT INTO tournament (id, title, number_of_teams, season) VALUES (4, 'Bundesliga-2', 10, '2014/2015');
INSERT INTO tournament (id, title, number_of_teams, season) VALUES (5, 'DFB-Pokal', 10, '2010/2011');
INSERT INTO tournament (id, title, number_of_teams, season) VALUES (6, 'BARCLAYS Premier League', 10, '2010/2011');
INSERT INTO tournament (id, title, number_of_teams, season) VALUES (7, 'The FA Cup', 10, '2010/2011');
INSERT INTO tournament (id, title, number_of_teams, season) VALUES (8, 'RFPL', 10, '2015/2016');
INSERT INTO tournament (id, title, number_of_teams, season) VALUES (9, 'Russian Cup', 10, '2015/2016');
INSERT INTO tournament (id, title, number_of_teams, season) VALUES (10, 'FIFA World Cup Russia', 32, '2018');


INSERT INTO teams (id, title) VALUES (1, 'CSKA Moskva');
INSERT INTO teams (id, title) VALUES (2, 'FK Rostov');
INSERT INTO teams (id, title) VALUES (3, 'Lokomotiv Moskva');
INSERT INTO teams (id, title) VALUES (4, 'FK Krasnodar');
INSERT INTO teams (id, title) VALUES (5, 'Zenit St. Petersburg');
INSERT INTO teams (id, title) VALUES (6, 'Terek Groznyi');
INSERT INTO teams (id, title) VALUES (7, 'Spartak Moskva');
INSERT INTO teams (id, title) VALUES (8, 'Ural Yekaterinenburg');
INSERT INTO teams (id, title) VALUES (9, 'Rubin Kazan');
INSERT INTO teams (id, title) VALUES (10, 'Amkar Perm');
INSERT INTO teams (id, title) VALUES (11, 'Dinamo Moskva');
INSERT INTO teams (id, title) VALUES (12, 'Krylya Sovetov Samara');
INSERT INTO teams (id, title) VALUES (13, 'FC Ufa');
INSERT INTO teams (id, title) VALUES (14, 'Anzhi Makhachkala');
INSERT INTO teams (id, title) VALUES (15, 'Kuban Krasnodar');
INSERT INTO teams (id, title) VALUES (16, 'Mordovya Saransk');


INSERT INTO matches (match_id, tournament_id, match_data) VALUES (1, 9, '2015-09-10');
INSERT INTO matches (match_id, tournament_id, match_data, next_match_id) VALUES (2, 9, '2015-09-05 10:23:00', 1);
INSERT INTO matches (match_id, tournament_id, match_data, next_match_id) VALUES (3, 9, '2015-09-05 10:30:54', 1);
INSERT INTO matches (match_id, tournament_id, match_data, next_match_id) VALUES (4, 9, '2015-09-02 14:23:54', 2);
INSERT INTO matches (match_id, tournament_id, match_data, next_match_id) VALUES (5, 9, '2015-09-02 20:23:54', 2);
INSERT INTO matches (match_id, tournament_id, match_data, next_match_id) VALUES (6, 9, '2015-09-01 20:23:54', 3);
INSERT INTO matches (match_id, tournament_id, match_data, next_match_id) VALUES (7, 9, '2015-08-31 11:23:54', 3);
INSERT INTO matches VALUES (8, 9, '2015-08-31 11:23:54', 1, 2, 2, 3, 5, 'is not played');
INSERT INTO matches VALUES (9, 9, '2015-08-31 11:23:54', 2, 10, 2, 1, 7, 'is not played');

