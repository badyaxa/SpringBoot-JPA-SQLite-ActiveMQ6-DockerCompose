CREATE TABLE IF NOT EXISTS client_info (
    client_id TEXT PRIMARY KEY,
    first_name TEXT NOT NULL,
    last_name TEXT NOT NULL,
    middle_name TEXT,
    address TEXT,
    card_number TEXT
);
