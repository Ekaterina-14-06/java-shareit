DROP TABLE IF EXISTS items;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS item_requests;
DROP TABLE IF EXISTS bookings;
DROP TABLE IF EXISTS statuses;
DROP TABLE IF EXISTS reviews;

CREATE TABLE IF NOT EXISTS users (
    id bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar NOT NULL,
    email varchar NOT NULL
);

CREATE TABLE IF NOT EXISTS item_requests (
    id bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    description varchar NOT NULL,
    requestor bigint REFERENCES users (id) NOT NULL,
    created date NOT NULL
);

CREATE TABLE IF NOT EXISTS items (
    id bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar NOT NULL,
    description varchar,
    owner bigint REFERENCES users (id) NOT NULL,
    available boolean NOT NULL,
    request bigint REFERENCES item_requests (id)
);

CREATE TABLE IF NOT EXISTS statuses (
    id bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar NOT NULL
    description varchar
);

CREATE TABLE IF NOT EXISTS bookings (
    id bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    start date NOT NULL,
    end date NOT NULL,
    item bigint REFERENCES items (id) NOT NULL,
    booker bigint REFERENCES users (id) NOT NULL,
    status bigint REFERENCES statuses (id) NOT NULL
);

CREATE TABLE IF NOT EXISTS reviews (
    id bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    description varchar NOT NULL,
    item bigint REFERENCES items (id) NOT NULL,
    reviewer bigint REFERENCES users (id) NOT NULL,
    review_date date NOT NULL,
    evaluation boolean
    booking bigint REFERENCES booking (id) NOT NULL
);