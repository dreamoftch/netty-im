
CREATE TABLE chat_message (
	ID BIGSERIAL PRIMARY KEY, source_user_id VARCHAR ( 30 ),
	target_user_id VARCHAR ( 30 ),
	message_content TEXT,
	created_at DATE,
	ack INT DEFAULT 1
);
COMMENT ON TABLE chat_message IS '聊天消息表';
COMMENT ON COLUMN chat_message.source_user_id IS '消息来源用户id';
COMMENT ON COLUMN chat_message.target_user_id IS '消息目标用户id';
COMMENT ON COLUMN chat_message.message_content IS '消息内容';
COMMENT ON COLUMN chat_message.created_at IS '消息发送时间';
COMMENT ON COLUMN chat_message.ack IS '消息是否收到了ack(1.没有收到,2.收到)';