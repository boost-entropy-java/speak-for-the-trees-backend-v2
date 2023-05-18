ALTER TABLE sites ADD COLUMN owner VARCHAR(10);

UPDATE sites SET owner = 'ROW';
