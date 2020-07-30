CREATE TABLE `spider_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `config_name` varchar(255) NOT NULL COMMENT '配置名',
  `config_value` varchar(255) NOT NULL COMMENT '配置值',
  `type` varchar(255) NOT NULL COMMENT '爬虫类型',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `uni_idx` (`config_name`,`type`) USING BTREE
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='爬虫配置';
