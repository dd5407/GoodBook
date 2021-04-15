CREATE TABLE `word_book` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `english` varchar(255) NOT NULL COMMENT '英文单词',
  `chinese` varchar(255) NOT NULL COMMENT '中文单词',
  `phonetic` varchar(255) DEFAULT NULL COMMENT '音标',
  `example` varchar(255) DEFAULT NULL COMMENT '例句',
  `create_time` timestamp NULL DEFAULT NULL COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  `creator` varchar(255) DEFAULT NULL COMMENT '创建人',
  PRIMARY KEY (`id`),
  KEY `word_idx` (`english`,`chinese`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=32 DEFAULT CHARSET=utf8 COMMENT='单词本';
