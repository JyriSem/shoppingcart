-- Populate discount table
INSERT INTO discount (percentage)
SELECT percentage FROM (VALUES (0.0), (5.0), (10.0), (20.0)) AS v(percentage)
WHERE NOT EXISTS (SELECT 1 FROM discount WHERE percentage = v.percentage);

-- Initialize tax rate
INSERT INTO tax_rate (id, rate)
SELECT 1, 22.0
WHERE NOT EXISTS (SELECT 1 FROM tax_rate WHERE id = 1);

-- Populate users table
INSERT INTO users (username, password, role)
SELECT 'user', '$2a$10$XURPShQNCsLjp1ESc2laoObo9QZDhxz73hJPaEv7/cBha4pk0AgP.', 'USER'
WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'user');