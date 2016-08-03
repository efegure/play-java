# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table cm_users (
  email                         varchar(255) not null,
  name                          varchar(255),
  password                      varchar(255),
  is_admin                      boolean,
  constraint pk_cm_users primary key (email)
);


# --- !Downs

drop table if exists cm_users;

