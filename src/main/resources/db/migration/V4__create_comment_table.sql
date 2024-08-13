create table comment(
    rating INT,
    app_user_id BIGINT,
    CONSTRAINT comment_user_fk FOREIGN KEY (app_user_id) REFERENCES app_user(id),
    created_at TIMESTAMP(6),
    id BIGSERIAL PRIMARY KEY,
    original_comment_id BIGINT,
    CONSTRAINT comment_original_comment_fk FOREIGN KEY (original_comment_id) REFERENCES comment(id),
    original_post_id BIGINT,
    CONSTRAINT comment_original_post_fk FOREIGN KEY (original_post_id) REFERENCES post(id),
    content VARCHAR(255)
);