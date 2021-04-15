CREATE TABLE `system_config` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `enable_system_info_collection` char(1) CHARACTER SET utf8mb4 NOT NULL COMMENT '监控开关',
  `ip` varchar(128) CHARACTER SET utf8mb4 NOT NULL COMMENT '监控服务器ip',
  `system_info_max_save_day` int(11) DEFAULT NULL COMMENT '系统监控数据最大保存天数',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '配置创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '配置更新时间',
  PRIMARY KEY (`id`),
  UNIQUE KEY `ip_unique` (`ip`) COMMENT 'ip唯一索引'
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统配置';
