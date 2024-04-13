CREATE TABLE IF NOT EXISTS user_site_reports (
    user_id         INT NOT NULL,
    created_at      TIMESTAMP NOT NULL DEFAULT CURRENT_TIMESTAMP,

    PRIMARY KEY (user_id, created_at),
    CONSTRAINT user_site_reports_users_fk FOREIGN KEY (user_id) REFERENCES users (id)
);
