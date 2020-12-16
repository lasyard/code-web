drop table if exists tbl_model;

create table tbl_model (
    id int not null auto_increment,
    name varchar(256),
    primary key(id)
);
