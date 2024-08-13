create table user_role_junction(
    role_id BIGINT,
    user_id BIGINT,
    PRIMARY KEY(role_id, user_id)
);