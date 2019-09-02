DELETE FROM users;
ALTER SEQUENCE global_seq RESTART WITH 100;

INSERT INTO users (name, email, password) VALUES
    ('Admin', 'admin@gmail.com', 'admin'),
    ('User', 'user@gmail.com', 'user');