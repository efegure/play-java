# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table cmusers (
  email                         varchar(255) not null,
  name                          varchar(255),
  password                      varchar(255),
  is_admin                      boolean,
  constraint pk_cmusers primary key (email)
);


# --- !Downs

drop table if exists cmusers;

