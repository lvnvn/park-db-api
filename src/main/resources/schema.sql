DROP TABLE if EXISTS users CASCADE;
DROP TABLE if EXISTS forums CASCADE;
DROP TABLE if EXISTS threads CASCADE;
DROP TABLE if EXISTS posts CASCADE;
DROP TABLE if EXISTS votes CASCADE;
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
title text UNIQUE,
user_nickname citext REFERENCES users (nickname) on DELETE CASCADE
);

CREATE TABLE if NOT EXISTS threads (
author citext REFERENCES users (nickname) on DELETE CASCADE,
/*created citext,*/
created TIMESTAMPTZ DEFAULT NOW(),
forum citext REFERENCES forums (slug) on DELETE CASCADE,
id serial primary key ,
message citext UNIQUE ,
slug citext,
title citext UNIQUE ,
votes INTEGER DEFAULT 0
);

CREATE TABLE if NOT EXISTS posts (
author citext REFERENCES users (nickname) on DELETE CASCADE,
created TIMESTAMPTZ DEFAULT NOW(),
forum citext REFERENCES forums (slug) on DELETE CASCADE,
id serial primary key ,
edited	boolean,
message citext UNIQUE ,
parent integer DEFAULT 0,
path integer[],
thread integer REFERENCES threads (id) on DELETE CASCADE
);

CREATE TABLE if NOT EXISTS votes (
nickname citext REFERENCES users (nickname) on DELETE CASCADE,
thread INTEGER REFERENCES threads (id) on DELETE CASCADE,
voice integer
);