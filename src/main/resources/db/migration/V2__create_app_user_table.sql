CREATE TABLE app_user (
    id BIGSERIAL PRIMARY KEY,
    date_of_registration DATE,
    enabled BOOLEAN,
    karma INTEGER,
    locked BOOLEAN,
    email VARCHAR(255),
    password VARCHAR(255),
    username VARCHAR(255)
);