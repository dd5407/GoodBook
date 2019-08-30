CREATE TABLE `guess_idiom` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `word` varchar(255) NOT NULL COMMENT '成语',
  `means` varchar(1000) NOT NULL COMMENT '释义',
  `from_username` varchar(255) NOT NULL COMMENT '出题人（发送者）',
  `to_username` varchar(255) NOT NULL COMMENT '答题人（接收者）',
  `idiom_status` int(11) NOT NULL COMMENT '成语状态（1-待猜，2-猜中，3-未猜中）',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='猜成语';
