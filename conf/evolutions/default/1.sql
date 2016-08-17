# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table time (
  time_id                       bigint not null,
  login_time                    timestamp,
  logoff_time                   timestamp,
  timetable_id                  bigint,
  constraint pk_time primary key (time_id)
);
create sequence time_seq;

create table timetable (
  timetable_id                  bigint not null,
  online                        boolean,
  user_email                    varchar(255),
  constraint uq_timetable_user_email unique (user_email),
  constraint pk_timetable primary key (timetable_id)
);
create sequence TIMETABLE_seq;

create table cm_users (
  email                         varchar(255) not null,
  name                          varchar(255),
  password                      varchar(255),
  table_timetable_id            bigint,
  is_registered                 boolean,
  is_admin                      boolean,
  constraint uq_cm_users_table_timetable_id unique (table_timetable_id),
  constraint pk_cm_users primary key (email)
);

alter table time add constraint fk_time_timetable_id foreign key (timetable_id) references timetable (timetable_id) on delete restrict on update restrict;
create index ix_time_timetable_id on time (timetable_id);

alter table timetable add constraint fk_timetable_user_email foreign key (user_email) references cm_users (email) on delete restrict on update restrict;

alter table cm_users add constraint fk_cm_users_table_timetable_id foreign key (table_timetable_id) references timetable (timetable_id) on delete restrict on update restrict;


# --- !Downs

alter table time drop constraint if exists fk_time_timetable_id;
drop index if exists ix_time_timetable_id;

alter table timetable drop constraint if exists fk_timetable_user_email;

alter table cm_users drop constraint if exists fk_cm_users_table_timetable_id;

drop table if exists time;
drop sequence if exists time_seq;

drop table if exists timetable;
drop sequence if exists TIMETABLE_seq;

drop table if exists cm_users;

