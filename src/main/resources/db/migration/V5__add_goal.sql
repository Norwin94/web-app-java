create table goal
(
    id int primary key auto_increment,
    minute int not null,
    match_id int not null,
    player_id int not null,
    foreign key (match_id) references match(id),
    foreign key (player_id) references player(id)
);
