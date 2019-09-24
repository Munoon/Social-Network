DELETE FROM user_roles;
DELETE FROM users;
DELETE FROM tokens;
ALTER SEQUENCE global_seq RESTART WITH 100;

INSERT INTO users (name, email, enabled, password)
VALUES ('Admin', 'admin@gmail.com', true, '{noop}admin'),
       ('User', 'user@gmail.com', true, '{noop}user'),
       ('NewUser', 'newUser@gmail.com', false, '{noop}newUser');

INSERT INTO user_roles (role, user_id)
VALUES ('ROLE_ADMIN', 100),
       ('ROLE_USER', 101),
       ('ROLE_USER', 102);