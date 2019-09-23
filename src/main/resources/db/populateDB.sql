DELETE FROM user_roles;
DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100;

INSERT INTO users (name, surname, email, password) VALUES
    ('Admin', 'Admin', 'admin@gmail.com', '{noop}admin'),
    ('User', 'User', 'user@gmail.com', '{noop}user');

INSERT INTO user_roles (role, user_id) VALUES
    ('ROLE_ADMIN', 100),
    ('ROLE_USER', 101);