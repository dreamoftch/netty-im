CREATE TABLE message (
	ID BIGSERIAL PRIMARY KEY, source_user_id VARCHAR ( 30 ),
	target_user_id VARCHAR ( 40 ),
	request_id VARCHAR ( 40 ),
	content TEXT,
	created_at DATE,
	ack INT DEFAULT 1
);
COMMENT ON TABLE message IS '聊天消息表';
COMMENT ON COLUMN message.source_user_id IS '消息来源用户id';
COMMENT ON COLUMN message.request_id IS '消息id(客户端生成)';
COMMENT ON COLUMN message.target_user_id IS '消息目标用户id';
COMMENT ON COLUMN message.content IS '消息内容';
COMMENT ON COLUMN message.created_at IS '消息发送时间';
COMMENT ON COLUMN message.ack IS '消息是否收到了ack(1.没有收到,2.收到)';