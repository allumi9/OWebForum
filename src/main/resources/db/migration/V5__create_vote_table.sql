create table vote(
    comment_id BIGINT,
    CONSTRAINT vote_comment_fk FOREIGN KEY (comment_id) REFERENCES comment(id),
    id BIGSERIAL PRIMARY KEY,
    post_id BIGINT,
    CONSTRAINT vote_post_fk FOREIGN KEY (post_id) REFERENCES post(id),
    voter_id BIGINT,
    CONSTRAINT vote_app_user_fk FOREIGN KEY (voter_id) REFERENCES app_user(id),
    state VARCHAR(255)
);