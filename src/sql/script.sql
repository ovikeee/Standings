
DROP INDEX IF EXISTS matches__owner_id__index;
DROP INDEX IF EXISTS matches__guests_id__index;
DROP INDEX IF EXISTS matches__next_match_id__index;
DROP TABLE IF EXISTS teams CASCADE;
DROP TABLE IF EXISTS tournament CASCADE;
DROP TABLE IF EXISTS matches CASCADE;


create table teams(
  id serial PRIMARY KEY ,
  title varchar(100) NOT NULL UNIQUE
);

create table tournament(
  id serial PRIMARY KEY,
  title varchar(50) NOT NULL UNIQUE,
  number_of_teams integer NOT NULL,
  season varchar(15) NOT NULL
);

create table matches(
  id serial PRIMARY KEY ,
  stage varchar(10),
  tournament_id integer NOT NULL REFERENCES tournament(id) ON DELETE CASCADE,
  match_data Timestamptz NOT NULL ,
  owner_id integer REFERENCES teams(id) ON DELETE CASCADE,
  guests_id integer REFERENCES teams(id) ON DELETE CASCADE,
  owner_id_score integer ,
  guests_id_score integer ,
  next_match_id integer REFERENCES matches(id) ON DELETE CASCADE,
  status varchar(20) DEFAULT ('is not played'),
  CHECK(owner_id!=guests_id)
);

CREATE INDEX matches__owner_id__index ON matches(owner_id);
CREATE INDEX matches__guests_id__index ON matches(guests_id);
CREATE INDEX matches__next_match_id__index ON matches(next_match_id);



insert into tournament(id, title, number_of_teams, season) values(1,'La Liga',10,'2010/2011');
insert into tournament(id, title, number_of_teams, season) values(2,'Copa del Rey',20,'2010/2011');
insert into tournament(id, title, number_of_teams, season) values(3,'Bundesliga',10,'2011/2012');
insert into tournament(id, title, number_of_teams, season) values(4,'Bundesliga-2',10,'2014/2015');
insert into tournament(id, title, number_of_teams, season) values(5,'DFB-Pokal',10,'2010/2011');
insert into tournament(id, title, number_of_teams, season) values(6,'BARCLAYS Premier League',10,'2010/2011');
insert into tournament(id, title, number_of_teams, season) values(7,'The FA Cup',10,'2010/2011');
insert into tournament(id, title, number_of_teams, season) values(8,'RFPL',10,'2015/2016');
insert into tournament(id, title, number_of_teams, season) values(9,'Russian Cup',10,'2015/2016');
insert into tournament(id, title, number_of_teams, season) values(10,'FIFA World Cup Russia', 32,'2018');



insert into teams(id, title) values(1,'CSKA Moskva');
insert into teams(id, title) values(2,'FK Rostov');
insert into teams(id, title) values(3,'Lokomotiv Moskva');
insert into teams(id, title) values(4,'FK Krasnodar');
insert into teams(id, title) values(5,'Zenit St. Petersburg');
insert into teams(id, title) values(6,'Terek Groznyi');
insert into teams(id, title) values(7,'Spartak Moskva');
insert into teams(id, title) values(8,'Ural Yekaterinenburg');
insert into teams(id, title) values(9,'Rubin Kazan');
insert into teams(id, title) values(10,'Amkar Perm');
insert into teams(id, title) values(11,'Dinamo Moskva');
insert into teams(id, title) values(12,'Krylya Sovetov Samara');
insert into teams(id, title) values(13,'FC Ufa');
insert into teams(id, title) values(14,'Anzhi Makhachkala');
insert into teams(id, title) values(15,'Kuban Krasnodar');
insert into teams(id, title) values(16,'Mordovya Saransk');


insert into matches( tournament_id, match_data) values(9, '2015-09-10');
insert into matches( tournament_id, match_data, next_match_id) values( 9, '2015-09-05 10:23:00', 1);
insert into matches( tournament_id, match_data, next_match_id) values( 9, '2015-09-05 10:30:54', 1);
insert into matches( tournament_id, match_data, next_match_id) values( 9, '2015-09-02 14:23:54', 2);
insert into matches( tournament_id, match_data, next_match_id) values( 9, '2015-09-02 20:23:54', 2);
insert into matches( tournament_id, match_data, next_match_id) values( 9, '2015-09-01 20:23:54', 3);
insert into matches( tournament_id, match_data, next_match_id) values( 9, '2015-08-31 11:23:54', 3);
insert into matches (stage, tournament_id, match_data,owner_id, guests_id, owner_id_score, guests_id_score,next_match_id, status) values( '1/2',9, '2015-08-31 11:23:54', 1,2,2,3,5,'is not played');
insert into  matches (stage, tournament_id, match_data,owner_id, guests_id, owner_id_score, guests_id_score,next_match_id, status) values( '1/4',9, '2015-08-31 11:23:54', 2,10,2,1,7,'is not played');
insert into  matches (stage, tournament_id, match_data,owner_id, guests_id, owner_id_score, guests_id_score,next_match_id, status) values( '1/4',9, '2015-08-31 11:23:54', 2,10,2,1,7,'is not played') ;

insert into matches( tournament_id, match_data) values(5, '2015-05-10');
insert into matches( tournament_id, match_data, next_match_id) values( 5, '2015-05-05 10:23:00', 11);
insert into matches( tournament_id, match_data, next_match_id) values( 5, '2015-05-05 10:30:54', 11);
insert into matches( tournament_id, match_data, next_match_id) values( 5, '2015-05-02 14:23:54', 12);
insert into matches( tournament_id, match_data, next_match_id) values( 5, '2015-05-02 20:23:54', 12);
insert into matches( tournament_id, match_data, next_match_id) values( 5, '2015-05-01 20:23:54', 13);
insert into matches( tournament_id, match_data, next_match_id) values( 5, '2015-08-31 11:23:54', 13);
insert into matches (stage, tournament_id, match_data,owner_id, guests_id, owner_id_score, guests_id_score,next_match_id, status) values( '1/2',5, '2015-08-31 11:23:54', 1,2,2,3,15,'is not played');
insert into  matches (stage, tournament_id, match_data,owner_id, guests_id, owner_id_score, guests_id_score,next_match_id, status) values( '1/4',5, '2015-08-31 11:23:54', 2,10,2,1,17,'is not played');
insert into  matches (stage, tournament_id, match_data,owner_id, guests_id, owner_id_score, guests_id_score,next_match_id, status) values( '1/4',5, '2015-08-31 11:23:54', 2,10,2,1,17,'is not played') ;
