    -- Populate discount table
    INSERT INTO discount (id, percentage)
    SELECT id, percentage FROM (
        VALUES (1, 0.0), (2, 5.0), (3, 10.0), (4, 20.0)
    ) AS v(id, percentage)
    WHERE NOT EXISTS (SELECT 1 FROM discount WHERE id = v.id);

    -- Initialize tax rate
    INSERT INTO tax_rate (id, rate)
    SELECT 1, 22.0
    WHERE NOT EXISTS (SELECT 1 FROM tax_rate WHERE id = 1);

    -- Populate users table
    INSERT INTO users (username, password, role)
    SELECT 'user', '$2a$10$XURPShQNCsLjp1ESc2laoObo9QZDhxz73hJPaEv7/cBha4pk0AgP.', 'USER'
    WHERE NOT EXISTS (SELECT 1 FROM users WHERE username = 'user');

    -- Update cart_items table: set default discount_id to 1 and update existing NULLs
    ALTER TABLE cart_items ALTER COLUMN discount_id SET DEFAULT 1;
    UPDATE cart_items SET discount_id = 1 WHERE discount_id IS NULL;