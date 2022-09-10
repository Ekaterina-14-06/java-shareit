DROP TABLE IF EXISTS items;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS item_requests;
DROP TABLE IF EXISTS bookings;
DROP TABLE IF EXISTS statuses;
DROP TABLE IF EXISTS reviews;

CREATE TABLE IF NOT EXISTS users (
    user_id bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar NOT NULL,
    email varchar NOT NULL
);

CREATE TABLE IF NOT EXISTS item_requests (
    item_request_id bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    description varchar NOT NULL,
    user_id bigint REFERENCES users (id) NOT NULL,
    created date NOT NULL
);

CREATE TABLE IF NOT EXISTS items (
    item_id bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar NOT NULL,
    description varchar,
    user_id bigint REFERENCES users (id) NOT NULL,
    available boolean NOT NULL,
    request_id bigint REFERENCES item_requests (id)
);

CREATE TABLE IF NOT EXISTS statuses (
    status_id bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    name varchar NOT NULL
    description varchar
);

CREATE TABLE IF NOT EXISTS bookings (
    booking_id bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    start date NOT NULL,
    end date NOT NULL,
    item_id bigint REFERENCES items (id) NOT NULL,
    user_id bigint REFERENCES users (id) NOT NULL,
    status_id bigint REFERENCES statuses (id) NOT NULL
);

CREATE TABLE IF NOT EXISTS reviews (
    review_id bigint GENERATED BY DEFAULT AS IDENTITY PRIMARY KEY,
    description varchar NOT NULL,
    item_id bigint REFERENCES items (id) NOT NULL,
    user_id bigint REFERENCES users (id) NOT NULL,
    review_date date NOT NULL,
    evaluation boolean
    booking_id bigint REFERENCES booking (id) NOT NULL
);