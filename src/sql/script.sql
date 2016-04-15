
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
  number_of_teams integer
);

create table matches(
  id serial PRIMARY KEY ,
  stage varchar(10),
  tournament_id integer NOT NULL REFERENCES tournament(id) ON DELETE CASCADE,
  match_data Timestamptz,
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



insert into tournament( title, number_of_teams) values('La Liga(2015/2016)',10);
insert into tournament( title, number_of_teams) values('Copa del Rey(2015/2016)',20);
insert into tournament( title, number_of_teams) values('Bundesliga(2015/2016)',10);
insert into tournament( title, number_of_teams) values('Bundesliga-2(2015/2016)',10);
insert into tournament( title, number_of_teams) values('DFB-Pokal(2010/2011)',10);
insert into tournament( title, number_of_teams) values('BARCLAYS Premier League(2015/2016)',10);
insert into tournament( title, number_of_teams) values('The FA Cup(2015/2016)',10);
insert into tournament( title, number_of_teams) values('RFPL(2015/2016)',10);
insert into tournament( title, number_of_teams) values('Russian Cup(2015/2016)',10);
insert into tournament( title, number_of_teams) values('FIFA World Cup Russia(2018)', 32);



insert into teams( title) values('CSKA Moskva');
insert into teams( title) values('FK Rostov');
insert into teams( title) values('Lokomotiv Moskva');
insert into teams( title) values('FK Krasnodar');
insert into teams( title) values('Zenit St. Petersburg');
insert into teams( title) values('Terek Groznyi');
insert into teams( title) values('Spartak Moskva');
insert into teams( title) values('Ural Yekaterinenburg');
insert into teams( title) values('Rubin Kazan');
insert into teams( title) values('Amkar Perm');
insert into teams( title) values('Dinamo Moskva');
insert into teams( title) values('Krylya Sovetov Samara');
insert into teams( title) values('FC Ufa');
insert into teams( title) values('Anzhi Makhachkala');
insert into teams( title) values('Kuban Krasnodar');
insert into teams( title) values('Mordovya Saransk');


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
 