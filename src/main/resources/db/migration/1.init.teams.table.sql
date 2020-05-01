drop table if exists teams;
create table teams(
    id int primary key auto_increment,
    team_name varchar(100) not null,
    founded date
)