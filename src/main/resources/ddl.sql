drop table if exists users;
drop table if exists transactions;

create table if not exists  users (
  id int auto_increment unique,
  first_name text not null,
  last_name text not null,
  balance real not null default 0
);

create table if not exists  transactions (
  id int auto_increment unique,
  user_from int,
  user_to int,
  transaction_amount real not null,
  transaction_date timestamp not null default now()
);

