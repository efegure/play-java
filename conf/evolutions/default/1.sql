# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table time (
  id                            varchar(255) not null,
  login_time                    timestamp,
  logoff_time                   timestamp,
  table_id                      varchar(255),
  constraint pk_time primary key (id)
);

create table time_table (
  id                            varchar(255) not null,
  online                        boolean,
  user_email                    varchar(255),
  constraint uq_time_table_user_email unique (user_email),
  constraint pk_time_table primary key (id)
);

create table cm_users (
  email                         varchar(255) not null,
  name                          varchar(255),
  password                      varchar(255),
  table_id                      varchar(255),
  is_admin                      boolean,
  constraint uq_cm_users_table_id unique (table_id),
  constraint pk_cm_users primary key (email)
);

alter table time add constraint fk_time_table_id foreign key (table_id) references time_table (id) on delete restrict on update restrict;
create index ix_time_table_id on time (table_id);

alter table time_table add constraint fk_time_table_user_email foreign key (user_email) references cm_users (email) on delete restrict on update restrict;

alter table cm_users add constraint fk_cm_users_table_id foreign key (table_id) references time_table (id) on delete restrict on update restrict;


# --- !Downs

alter table time drop constraint if exists fk_time_table_id;
drop index if exists ix_time_table_id;

alter table time_table drop constraint if exists fk_time_table_user_email;

alter table cm_users drop constraint if exists fk_cm_users_table_id;

drop table if exists time;

drop table if exists time_table;

drop table if exists cm_users;

