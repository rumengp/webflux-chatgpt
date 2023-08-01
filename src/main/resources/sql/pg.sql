CREATE DATABASE chat_gpt;

-- 用户表
CREATE TABLE users
(
    id          BIGSERIAL PRIMARY KEY,
    username    VARCHAR(64)  NOT NULL,
    password    VARCHAR(128) NOT NULL,
    roles       JSON,
    enabled     SMALLINT  DEFAULT 1,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE users IS '用户表';
COMMENT ON COLUMN users.roles IS '用户角色列表["user", "admin"]';
COMMENT ON COLUMN users.enabled IS '用户是否启用。1 启用 0 禁用';

-- 聊天角色
CREATE TABLE chat_role
(
    id              BIGSERIAL PRIMARY KEY,
    type            VARCHAR(16)  NOT NULL,
    username        VARCHAR(64)  NOT NULL,
    command         VARCHAR(256) NOT NULL,
    model           VARCHAR(32)  NOT NULL,
    nick_name       VARCHAR(32)  NOT NULL,
    mark            VARCHAR(128),
    welcome         VARCHAR(64),
    picture         VARCHAR(256),
    context_num     INT          NOT NULL DEFAULT 8,
    max_replay      INT          NOT NULL DEFAULT 1600,
    property_random FLOAT        NOT NULL DEFAULT 0.6,
    property_word   FLOAT        NOT NULL DEFAULT 1,
    property_topic  INT          NOT NULL DEFAULT 0,
    property_repeat INT          NOT NULL DEFAULT 0,
    create_time     TIMESTAMP             DEFAULT CURRENT_TIMESTAMP,
    update_time     TIMESTAMP             DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE chat_role IS '聊天角色表';
COMMENT ON COLUMN chat_role.type IS '角色类型，SYSTEM，USER';
COMMENT ON COLUMN chat_role.username IS '角色所属的用户名称';
COMMENT ON COLUMN chat_role.command IS '角色指令';
COMMENT ON COLUMN chat_role.model IS '使用哪个模型';
COMMENT ON COLUMN chat_role.nick_name IS '角色昵称';
COMMENT ON COLUMN chat_role.mark IS '备注';
COMMENT ON COLUMN chat_role.welcome IS '打招呼用语';
COMMENT ON COLUMN chat_role.picture IS '头像链接';
COMMENT ON COLUMN chat_role.context_num IS '聊天上下文数量';
COMMENT ON COLUMN chat_role.max_replay IS '限制最大回复数量';
COMMENT ON COLUMN chat_role.property_random IS '随机属性0-1';
COMMENT ON COLUMN chat_role.property_word IS '词汇属性0-1';
COMMENT ON COLUMN chat_role.property_topic IS '话题属性(-2,2)';
COMMENT ON COLUMN chat_role.property_repeat IS '重复属性(-2,2)';


-- 聊天
CREATE TABLE chat
(
    id              BIGSERIAL PRIMARY KEY,
    username        VARCHAR(64)  NOT NULL,
    role_id         BIGINT       NOT NULL,
    title           VARCHAR(32),
    command         VARCHAR(256) NOT NULL,
    model           VARCHAR(32)  NOT NULL,
    context_num     INT          NOT NULL DEFAULT 8,
    max_replay      INT          NOT NULL DEFAULT 1600,
    property_random FLOAT        NOT NULL DEFAULT 0.6,
    property_word   FLOAT        NOT NULL DEFAULT 1,
    property_topic  INT          NOT NULL DEFAULT 0,
    property_repeat INT          NOT NULL DEFAULT 0,
    create_time     TIMESTAMP             DEFAULT CURRENT_TIMESTAMP,
    update_time     TIMESTAMP             DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE chat IS '聊天表';
COMMENT ON COLUMN chat.username IS '角色所属的用户名称';
COMMENT ON COLUMN chat.command IS '角色指令';
COMMENT ON COLUMN chat.model IS '使用哪个模型';
COMMENT ON COLUMN chat.context_num IS '聊天上下文数量';
COMMENT ON COLUMN chat.max_replay IS '限制最大回复数量';
COMMENT ON COLUMN chat.property_random IS '随机属性0-1';
COMMENT ON COLUMN chat.property_word IS '词汇属性0-1';
COMMENT ON COLUMN chat.property_topic IS '话题属性(-2,2)';
COMMENT ON COLUMN chat.property_repeat IS '重复属性(-2,2)';

-- message
CREATE TABLE chat_message
(
    id          BIGSERIAL PRIMARY KEY,
    username    VARCHAR(64) NOT NULL,
    chat_id     BIGINT      NOT NULL,
    type        VARCHAR(16) NOT NULL,
    content     TEXT        NOT NULL,
    create_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    update_time TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

COMMENT ON TABLE chat_message IS '聊天消息表';
COMMENT ON COLUMN chat_message.username IS '角色所属的用户名称';
COMMENT ON COLUMN chat_message.chat_id IS '聊天所属角色';
COMMENT ON COLUMN chat_message.type IS '消息类型，USER,ASSISTANT';
COMMENT ON COLUMN chat_message.content IS '聊天内容';
