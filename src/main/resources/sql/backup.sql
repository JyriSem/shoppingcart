-- Delete a user from the users table
DELETE FROM users WHERE username = 'desired_username';
-- Revalue the discount percentage for a specific discount
UPDATE discount 
SET percentage = '10' 
WHERE id = 1;
-- Delete user 'name' and all related cart items and logs
DELETE FROM cart_item_logs WHERE user_id = (SELECT id FROM users WHERE username = 'name');
DELETE FROM users WHERE username = 'name';
--Delete user 'name' by setting id to NULL to keep logs
UPDATE cart_item_logs SET user_id = NULL WHERE user_id = (SELECT id FROM users WHERE username = 'name');
DELETE FROM users WHERE username = 'name';
--Drop the nameless table
DROP TABLE IF EXISTS nameless CASCADE;


-- Drop all tables if they exist (in reverse order of dependencies to avoid foreign key constraints)
DROP TABLE IF EXISTS cart_item_logs;
DROP TABLE IF EXISTS cart_items;
DROP TABLE IF EXISTS tax_rate;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS discount;

-- Create users table
CREATE TABLE users (
    id BIGSERIAL PRIMARY KEY,
    username VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    role VARCHAR(50) NOT NULL DEFAULT 'USER'
);

-- Create discount table
CREATE TABLE discount (
    id BIGSERIAL PRIMARY KEY,
    percentage DOUBLE PRECISION NOT NULL CHECK (percentage >= 0)
);

-- Create tax_rate table
CREATE TABLE tax_rate (
    id BIGINT PRIMARY KEY,
    rate DOUBLE PRECISION NOT NULL CHECK (rate >= 0)
);

-- Create cart_items table
CREATE TABLE cart_items (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    name VARCHAR(255) NOT NULL,
    price DOUBLE PRECISION NOT NULL CHECK (price > 0),
    quantity INTEGER NOT NULL CHECK (quantity >= 1),
    discount_id BIGINT,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE,
    FOREIGN KEY (discount_id) REFERENCES discount(id) ON DELETE SET NULL
);

-- Create cart_item_logs table
CREATE TABLE cart_item_logs (
    id BIGSERIAL PRIMARY KEY,
    user_id BIGINT NOT NULL,
    product_name VARCHAR(255) NOT NULL,
    removed_at TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

-- Insert initial data
-- Populate discount table with 0%, 5%, 10%, 20%
INSERT INTO discount (percentage)
VALUES (0.0), (5.0), (10.0), (20.0)
ON CONFLICT DO NOTHING;

-- Initialize tax rate with 22%
INSERT INTO tax_rate (id, rate)
VALUES (1, 22.0)
ON CONFLICT DO NOTHING;

-- Insert a default user (password: 'password', hashed with BCrypt)
INSERT INTO users (username, password, role)
VALUES ('user', '$2a$10$XURPShQNCsLjp1ESc2laoObo9QZDhxz73hJPaEv7/cBha4pk0AgP.', 'USER')
ON CONFLICT DO NOTHING;

--structure for the table
--          ┌ ┐─│└ ┘├ ┤ ┬ ┴ ┼