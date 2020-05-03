create table match
(
    id int primary key auto_increment,
    home_team_id int not null,
    away_team_id int not null,
    match_date date,
    foreign key (home_team_id) references team(id),
    foreign key (away_team_id) references team(id)
);
