CREATE TABLE roles
(
    id   SERIAL PRIMARY KEY,
    name VARCHAR(20) NOT NULL
);

CREATE TABLE user_role
(
    user_id INTEGER NOT NULL,
    role_id INTEGER NOT NULL,
    PRIMARY KEY (user_id, role_id),
    FOREIGN KEY (user_id) REFERENCES users (id),
    FOREIGN KEY (role_id) REFERENCES roles (id)
);

INSERT INTO roles (name)
VALUES ('ADMIN'),
       ('USER');