# --- Created by Ebean DDL
# To stop Ebean DDL generation, remove this comment and start using Evolutions

# --- !Ups

create table employee (
  id                        integer auto_increment not null,
  emp_first_name            varchar(255),
  emp_last_name             varchar(255),
  emp_email                 varchar(255),
  emp_id                    integer,
  d_tag                     varchar(255),
  constraint pk_employee primary key (id))
;

create table pantry_mapping (
  id                        integer auto_increment not null,
  res_emp_id                integer,
  resd_emp_id               integer,
  res_date                  bigint,
  start_time                bigint,
  end_time                  bigint,
  d_tag                     varchar(255),
  constraint pk_pantry_mapping primary key (id))
;

create table reservation (
  id                        integer auto_increment not null,
  conf_name                 varchar(255),
  emp_name                  varchar(255),
  emp_id                    integer,
  start_time                bigint,
  end_time                  bigint,
  d_tag                     varchar(255),
  constraint pk_reservation primary key (id))
;

create table user (
  id                        integer auto_increment not null,
  emp_first_name            varchar(255),
  emp_last_name             varchar(255),
  emp_id                    integer,
  emp_email                 varchar(255),
  emp_password              varchar(255),
  constraint pk_user primary key (id))
;




# --- !Downs

SET FOREIGN_KEY_CHECKS=0;

drop table employee;

drop table pantry_mapping;

drop table reservation;

drop table user;

SET FOREIGN_KEY_CHECKS=1;

