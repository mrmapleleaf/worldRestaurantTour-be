create table if not exists Countries (
    id int not null auto_increment,
    name varchar(50) default null,
    next boolean default false,
    completed boolean default false,
    created_at timestamp not null default current_timestamp,
    updated_at timestamp not null default current_timestamp,
    primary key (id)
);

create table if not exists Restaurants (
    id int not null auto_increment,
    name varchar(50) default null,
    thoughts varchar(200) default null,
    url varchar(200) default null,
    country_id int not null,
    created_at timestamp not null default current_timestamp,
    updated_at timestamp not null default current_timestamp,
    primary key (id)
);