DROP TABLE if EXISTS users CASCADE;
DROP TABLE if EXISTS forums CASCADE;
DROP TABLE if EXISTS threads CASCADE;
CREATE extension if not exists citext;

CREATE TABLE if NOT EXISTS users (
nickname citext unique,
fullname citext,
email citext UNIQUE,
about citext
);

CREATE TABLE if NOT EXISTS forums (
posts INTEGER ,
slug citext UNIQUE,
threads INTEGER ,
title citext UNIQUE,
user_nickname citext REFERENCES users (nickname) on DELETE CASCADE
);

CREATE TABLE if NOT EXISTS threads (
author citext REFERENCES users (nickname) on DELETE CASCADE,
created citext,
forum citext REFERENCES forums (slug) on DELETE CASCADE,
id serial primary key ,
message citext UNIQUE ,
slug citext,
title citext UNIQUE ,
votes INTEGER
);