create table message (
	id BIGINT AUTO_INCREMENT primary key,
    user_id BIGINT NOT NULL,
    chat_id BIGINT NOT NULL,
    created_date timestamp default current_timestamp,
    foreign key (user_id) references user(id),
    foreign key (chat_id) references chat(id)
);