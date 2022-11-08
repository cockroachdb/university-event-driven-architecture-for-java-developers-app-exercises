CREATE DATABASE movr_users;

CREATE TABLE movr_users.users (
	email STRING PRIMARY KEY,
	last_name STRING NOT NULL,
	first_name STRING NOT NULL,
	phone_numbers STRING[] NOT NULL
);