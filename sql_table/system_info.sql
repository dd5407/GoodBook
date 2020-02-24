CREATE TABLE `system_info` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `cpu_usage` double(10,3) DEFAULT NULL COMMENT 'CPU使用率，百分比',
  `swap_memory_used` bigint(20) DEFAULT NULL COMMENT '已使用swap空间，字节',
  `swap_memory_total` bigint(20) DEFAULT NULL COMMENT '总swap空间，字节',
  `swap_memory_usage` double(10,3) DEFAULT NULL COMMENT 'swap使用率，百分比',
  `memory_used` bigint(20) DEFAULT NULL COMMENT '已使用总内存，字节',
  `memory_total` bigint(20) DEFAULT NULL COMMENT '总内存，字节',
  `memory_usage` double(10,3) DEFAULT NULL COMMENT '总内存使用率，百分比',
  `disk_used` bigint(20) DEFAULT NULL COMMENT '已使用磁盘空间，字节',
  `disk_total` bigint(20) DEFAULT NULL COMMENT '总磁盘容量，字节',
  `disk_usage` double(10,3) DEFAULT NULL COMMENT '磁盘使用率，百分比',
  `ip` varchar(128) CHARACTER SET utf8mb4 DEFAULT NULL COMMENT '服务器IP',
  `create_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '监控时间',
  PRIMARY KEY (`id`),
  KEY `ip_idx` (`ip`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='系统信息监控';
