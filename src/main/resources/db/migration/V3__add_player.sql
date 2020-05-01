create table player
(
    id int primary key auto_increment,
    first_name varchar(20) not null,
    last_name varchar(20) not null,
    birth_date date,
    team_id int,
    foreign key (team_id) references team(id)
);

