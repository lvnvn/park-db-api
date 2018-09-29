DROP TABLE if EXISTS users CASCADE;
DROP TABLE if EXISTS books CASCADE;
DROP TABLE if EXISTS departments CASCADE;
DROP TABLE if EXISTS assignments CASCADE;
CREATE extension if not exists citext;

CREATE TABLE if NOT EXISTS users (
nickname citext,
fullname citext,
email citext UNIQUE,
age integer,
id serial primary key
);

CREATE TABLE if NOT EXISTS departments (
name citext unique,
id serial primary key,
books integer
);

CREATE TABLE if NOT EXISTS books (
department citext REFERENCES departments (name),
id serial primary key,
author citext,
title citext ,
publisher citext
);

CREATE TABLE if NOT EXISTS assignments (
bookid integer REFERENCES books (id),
userid integer REFERENCES users (id),
due TIMESTAMPTZ DEFAULT NOW(),
active boolean default true
);
