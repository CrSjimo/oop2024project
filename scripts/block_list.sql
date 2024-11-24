CREATE TABLE block_list (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,            -- 主键id，使用BIGINT类型
    user1_id BIGINT NOT NULL,                         -- 第一个用户id，使用BIGINT类型
    user2_id BIGINT NOT NULL,                         -- 第二个用户id，使用BIGINT类型
    created_data TIMESTAMP DEFAULT CURRENT_TIMESTAMP,    -- 创建时间，默认为当前时间
    FOREIGN KEY (user1_id) REFERENCES user(id),      -- 外键约束：user1_id 关联 user 表的 id
    FOREIGN KEY (user2_id) REFERENCES user(id)       -- 外键约束：user2_id 关联 user 表的 id
);
