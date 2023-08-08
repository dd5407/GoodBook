CREATE TABLE `weight`
(
    `id`           int      NOT NULL AUTO_INCREMENT COMMENT '主键',
    `weight`       double   DEFAULT NULL COMMENT '体重（千克）',
    `record_time`  datetime NOT NULL COMMENT '称重时间',
    `create_time`  datetime NOT NULL COMMENT '创建时间',
    `update_time`  datetime DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '修改时间',
    `user_id`      int      NOT NULL COMMENT '创建者',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8
  COMMENT ='体重记录表';