ALTER TABLE site_entries ADD COLUMN created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP;

UPDATE site_entries SET created_at = updated_at;
