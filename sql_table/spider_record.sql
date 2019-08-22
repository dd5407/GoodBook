CREATE TABLE `spider_record` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `record_num` int(11) NOT NULL COMMENT '记录数',
  `record_type` varchar(32) NOT NULL COMMENT '记录类型（如idiom）',
  `update_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='爬虫记录表，依据此表的记录来决定是否启动爬虫';
