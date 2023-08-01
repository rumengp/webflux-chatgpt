DROP TABLE IF EXISTS `users`;
CREATE TABLE `users`
(
    `id`          BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'id',
    `username`    VARCHAR(64)  NOT NULL COMMENT '用户',
    `password`    VARCHAR(128) NOT NULL COMMENT '密码',
    `roles`       JSON COMMENT '角色json list',
    `enabled`     TINYINT  DEFAULT 1 COMMENT '是否启用，0-禁用，1-启用',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    CONSTRAINT uk_username UNIQUE (username)
);

DROP TABLE IF EXISTS `chat_role`;
CREATE TABLE `chat_role`
(
    `id`              BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'id',
    `type`            VARCHAR(16)  NOT NULL COMMENT '角色类型，SYSTEM，USER',
    `username`        VARCHAR(64)  NOT NULL COMMENT '角色所属的用户名称',
    `command`         VARCHAR(256) NOT NULL COMMENT '角色指令',
    `model`           VARCHAR(32)  NOT NULL COMMENT '使用哪个模型',
    `nick_name`       VARCHAR(32)  NOT NULL COMMENT '角色昵称',
    `mark`            VARCHAR(128) COMMENT '备注',
    `welcome`         VARCHAR(64) COMMENT '打招呼用语',
    `picture`         VARCHAR(256) COMMENT '头像链接',
    `context_num`     INT          NOT NULL DEFAULT 8 COMMENT '聊天上下文数量',
    `max_replay`      INT          NOT NULL DEFAULT 1600 COMMENT '限制最大回复数量',
    `property_random` FLOAT        NOT NULL DEFAULT 0.6 COMMENT '随机属性0-1',
    `property_word`   FLOAT        NOT NULL DEFAULT 1 COMMENT '词汇属性0-1',
    `property_topic`  INT          NOT NULL DEFAULT 0 COMMENT '话题属性(-2,2)',
    `property_repeat` INT          NOT NULL DEFAULT 0 COMMENT '重复属性(-2,2)',
    `create_time`     TIMESTAMP             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     TIMESTAMP             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
);

DROP TABLE IF EXISTS `chat`;
CREATE TABLE `chat`
(
    `id`              BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'id',
    `username`        VARCHAR(64)  NOT NULL COMMENT '角色所属的用户名称',
    `role_id`         BIGINT       NOT NULL COMMENT '聊天所属角色',
    `title`           VARCHAR(32) COMMENT '聊天标题',
    `command`         VARCHAR(256) NOT NULL COMMENT '角色指令',
    `model`           VARCHAR(32)  NOT NULL COMMENT '使用哪个模型',
    `context_num`     INT          NOT NULL DEFAULT 8 COMMENT '聊天上下文数量',
    `max_replay`      INT          NOT NULL DEFAULT 1600 COMMENT '限制最大回复数量',
    `property_random` FLOAT        NOT NULL DEFAULT 0.6 COMMENT '随机属性0-1',
    `property_word`   FLOAT        NOT NULL DEFAULT 1 COMMENT '词汇属性0-1',
    `property_topic`  INT          NOT NULL DEFAULT 0 COMMENT '话题属性(-2,2)',
    `property_repeat` INT          NOT NULL DEFAULT 0 COMMENT '重复属性(-2,2)',
    `create_time`     TIMESTAMP             DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time`     TIMESTAMP             DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
);

DROP TABLE IF EXISTS `chat_message`;
CREATE TABLE `chat_message`
(
    `id`          BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'id',
    `username`        VARCHAR(64)  NOT NULL COMMENT '角色所属的用户名称',
    `chat_id`     BIGINT      NOT NULL COMMENT '聊天所属角色',
    `type`        VARCHAR(16) NOT NULL COMMENT '消息类型，USER,ASSISTANT',
    `content`     TEXT        NOT NULL COMMENT '聊天内容',
    `create_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
);