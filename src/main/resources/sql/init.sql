CREATE TABLE `person`
(
    `id`   BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'id',
    `name` VARCHAR(64) NOT NULL COMMENT '名字',
    `age`  INT         NOT NULL DEFAULT 1 COMMENT '年龄'
);

CREATE TABLE `user`
(
    `id`          BIGINT AUTO_INCREMENT PRIMARY KEY COMMENT 'id',
    `username`    VARCHAR(64)  NOT NULL COMMENT '用户',
    `password`    VARCHAR(128) NOT NULL COMMENT '密码',
    `roles`       JSON COMMENT '角色json list',
    `enabled`     TINYINT  DEFAULT 1 COMMENT '是否启用，0-禁用，1-启用',
    `create_time` DATETIME DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间'
);