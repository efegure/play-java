# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table billing (
  id                            bigint not null,
  payment_id                    bigint,
  bill                          double,
  constraint uq_billing_payment_id unique (payment_id),
  constraint pk_billing primary key (id)
);
create sequence billing_id_seq;

create table company (
  c_name                        varchar(255) not null,
  representative_email          varchar(255),
  payment_id                    bigint,
  constraint uq_company_representative_email unique (representative_email),
  constraint uq_company_payment_id unique (payment_id),
  constraint pk_company primary key (c_name)
);

create table payment (
  id                            bigint not null,
  company_c_name                varchar(255),
  billing_id                    bigint,
  prepaid_id                    bigint,
  method                        varchar(255),
  constraint uq_payment_company_c_name unique (company_c_name),
  constraint uq_payment_billing_id unique (billing_id),
  constraint uq_payment_prepaid_id unique (prepaid_id),
  constraint pk_payment primary key (id)
);
create sequence payment_id_seq increment by 1;

create table pre_paid (
  id                            bigint not null,
  payment_id                    bigint,
  remainingtime                 double,
  constraint uq_pre_paid_payment_id unique (payment_id),
  constraint pk_pre_paid primary key (id)
);
create sequence prepaid_id_seq;

create table time (
  time_id                       bigint not null,
  login_time                    timestamp,
  logoff_time                   timestamp,
  timetable_id                  bigint,
  constraint pk_time primary key (time_id)
);
create sequence time_id_seq increment by 1;

create table timetable (
  id                            bigint not null,
  online                        boolean,
  user_email                    varchar(255),
  constraint uq_timetable_user_email unique (user_email),
  constraint pk_timetable primary key (id)
);
create sequence hibernate_sequence;

create table cm_users (
  email                         varchar(255) not null,
  name                          varchar(255),
  password                      varchar(255),
  table_id                      bigint,
  company_c_name                varchar(255),
  com_name                      varchar(255),
  is_registered                 boolean,
  is_admin                      boolean,
  constraint uq_cm_users_table_id unique (table_id),
  constraint pk_cm_users primary key (email)
);

alter table billing add constraint fk_billing_payment_id foreign key (payment_id) references payment (id) on delete restrict on update restrict;

alter table company add constraint fk_company_representative_email foreign key (representative_email) references cm_users (email) on delete restrict on update restrict;

alter table company add constraint fk_company_payment_id foreign key (payment_id) references payment (id) on delete restrict on update restrict;

alter table payment add constraint fk_payment_company_c_name foreign key (company_c_name) references company (c_name) on delete restrict on update restrict;

alter table payment add constraint fk_payment_billing_id foreign key (billing_id) references billing (id) on delete restrict on update restrict;

alter table payment add constraint fk_payment_prepaid_id foreign key (prepaid_id) references pre_paid (id) on delete restrict on update restrict;

alter table pre_paid add constraint fk_pre_paid_payment_id foreign key (payment_id) references payment (id) on delete restrict on update restrict;

alter table time add constraint fk_time_timetable_id foreign key (timetable_id) references timetable (id) on delete restrict on update restrict;
create index ix_time_timetable_id on time (timetable_id);

alter table timetable add constraint fk_timetable_user_email foreign key (user_email) references cm_users (email) on delete restrict on update restrict;

alter table cm_users add constraint fk_cm_users_table_id foreign key (table_id) references timetable (id) on delete restrict on update restrict;

alter table cm_users add constraint fk_cm_users_company_c_name foreign key (company_c_name) references company (c_name) on delete restrict on update restrict;
create index ix_cm_users_company_c_name on cm_users (company_c_name);


# --- !Downs

alter table billing drop constraint if exists fk_billing_payment_id;

alter table company drop constraint if exists fk_company_representative_email;

alter table company drop constraint if exists fk_company_payment_id;

alter table payment drop constraint if exists fk_payment_company_c_name;

alter table payment drop constraint if exists fk_payment_billing_id;

alter table payment drop constraint if exists fk_payment_prepaid_id;

alter table pre_paid drop constraint if exists fk_pre_paid_payment_id;

alter table time drop constraint if exists fk_time_timetable_id;
drop index if exists ix_time_timetable_id;

alter table timetable drop constraint if exists fk_timetable_user_email;

alter table cm_users drop constraint if exists fk_cm_users_table_id;

alter table cm_users drop constraint if exists fk_cm_users_company_c_name;
drop index if exists ix_cm_users_company_c_name;

drop table if exists billing;
drop sequence if exists billing_id_seq;

drop table if exists company;

drop table if exists payment;
drop sequence if exists payment_id_seq;

drop table if exists pre_paid;
drop sequence if exists prepaid_id_seq;

drop table if exists time;
drop sequence if exists time_id_seq;

drop table if exists timetable;
drop sequence if exists hibernate_sequence;

drop table if exists cm_users;

