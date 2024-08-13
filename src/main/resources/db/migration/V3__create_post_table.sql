create table post(
    id BIGSERIAL PRIMARY KEY,
    category_id BIGINT,
    CONSTRAINT post_category_fk FOREIGN KEY (category_id) REFERENCES category(id),
    created_at TIMESTAMP(6),
    rating INTEGER,
    user_id BIGINT,
    CONSTRAINT post_user_fk FOREIGN KEY (user_id) REFERENCES app_user(id),
    content TEXT,
    title VARCHAR(255)
);