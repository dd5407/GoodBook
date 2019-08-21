CREATE TABLE `idiom` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `word` varchar(64) NOT NULL COMMENT '成语',
  `means` varchar(1000) DEFAULT NULL COMMENT '成语解释',
  `phonetic_pinyin` varchar(64) DEFAULT NULL COMMENT '注音拼音',
  `first_pinyin` varchar(10) DEFAULT NULL COMMENT '第一个字拼音',
  `second_pinyin` varchar(10) DEFAULT NULL COMMENT '第二个字拼音',
  `third_pinyin` varchar(10) DEFAULT NULL COMMENT '第三个字拼音',
  `fourth_pinyin` varchar(10) DEFAULT NULL COMMENT '第四个字拼音',
  `level` int(11) DEFAULT NULL COMMENT '难度',
  `pass_count` int(11) DEFAULT NULL COMMENT '猜中次数',
  `miss_count` int(11) DEFAULT NULL COMMENT '没猜中次数',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` timestamp NULL DEFAULT NULL COMMENT '更新时间',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8 COMMENT='成语-转屏猜成语、成语接龙';