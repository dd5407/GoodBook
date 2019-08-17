CREATE TABLE `idiom` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `word` varchar(64) NOT NULL COMMENT '成语',
  `describe` varchar(255) DEFAULT NULL COMMENT '成语解释',
  `first_pinyin` varchar(10) DEFAULT NULL COMMENT '第一个字拼音',
  `second_pinyin` varchar(10) DEFAULT NULL COMMENT '第二个字拼音',
  `third_pinyin` varchar(10) DEFAULT NULL COMMENT '第三个字拼音',
  `fourth_pinyin` varchar(10) DEFAULT NULL COMMENT '第四个字拼音',
  `level` int(11) DEFAULT NULL COMMENT '难度',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='成语-转屏猜成语、成语接龙';
