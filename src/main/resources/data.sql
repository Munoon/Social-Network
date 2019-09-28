DELETE FROM user_roles;
DELETE FROM users;
DELETE FROM tokens;
ALTER SEQUENCE global_seq RESTART WITH 100;

INSERT INTO users (name, surname, email, password, enabled) VALUES
    ('Admin', 'Admin', 'admin@gmail.com', '{noop}admin', true),
    ('User', 'User', 'user@gmail.com', '{noop}user', true),
    ('New', 'User', 'newuser@gmail.com', '{noop}newUser', false);

INSERT INTO user_roles (role, user_id) VALUES
    ('ROLE_ADMIN', 100),
    ('ROLE_USER', 101),
    ('ROLE_USER', 102);

INSERT INTO tokens (token, type, user_id, creation_date, expiration_date) VALUES
    ('56426919-4642-40fd-b7ad-b530c795a197', 'REGISTER', 102, '09-25-2019T12:00', '09-26-2019T12:00');