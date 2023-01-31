CREATE TABLE IF NOT EXISTS site_images
(
    id            SERIAL       NOT NULL PRIMARY KEY,
    site_entry_id INT          NOT NULL,
    uploader_id   INT          NOT NULl,
    uploaded_at   TIMESTAMP    NOT NULL,
    image_url     VARCHAR(500) NOT NULL,

    CONSTRAINT site_images_uploader_id_fk FOREIGN KEY (uploader_id) REFERENCES users (id),
    CONSTRAINT site_images_site_entry_id_fk FOREIGN KEY (site_entry_id) REFERENCES site_entries (id)
);

ALTER TABLE sites
DROP COLUMN picture;
