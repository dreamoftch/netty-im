/** 用户表 */

CREATE TABLE account
(
  ID             BIGSERIAL PRIMARY KEY,
  name           VARCHAR(40),
  online_status  int DEFAULT 1,
  last_login_at  timestamp ,
  last_logout_at timestamp ,
  created_at     timestamp
);
COMMENT ON TABLE account IS '聊天用户表';
COMMENT ON COLUMN account.name IS '用户名';
COMMENT ON COLUMN account.online_status IS '在线状态(1.离线,2.在线)';
COMMENT ON COLUMN account.last_login_at IS '最近登录时间';
COMMENT ON COLUMN account.last_logout_at IS '最近登出时间';
COMMENT ON COLUMN account.created_at IS '消息发送时间';


/** 用户消息表表 */

CREATE TABLE message
(
  ID             BIGSERIAL PRIMARY KEY,
  source_user_id bigint,
  target_user_id bigint,
  request_id     VARCHAR(40),
  content        TEXT,
  created_at     timestamp ,
  status            INT DEFAULT 1
);
COMMENT ON TABLE message IS '聊天消息表';
COMMENT ON COLUMN message.source_user_id IS '消息来源用户id';
COMMENT ON COLUMN message.target_user_id IS '消息目标用户id';
COMMENT ON COLUMN message.request_id IS '消息id(客户端生成)';
COMMENT ON COLUMN message.content IS '消息内容';
COMMENT ON COLUMN message.created_at IS '消息发送时间';
COMMENT ON COLUMN message.status IS '消息状态(1.接收者尚未收到,2.接收者收到,3.接收者已读)';

-- 初始化账号用于登录
insert into account(id, name) values(1, '张三');
insert into account(id, name) values(2, '李四');