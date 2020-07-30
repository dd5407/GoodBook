CREATE TABLE `poem` (
  `id` int(11) NOT NULL AUTO_INCREMENT COMMENT '主键id',
  `title` varchar(255) NOT NULL COMMENT '诗词名',
  `author` varchar(255) NOT NULL COMMENT '作者',
  `dynasty` varchar(255) DEFAULT NULL COMMENT '朝代',
  `body` text NOT NULL COMMENT '诗词主体',
  `means` varchar(4000) DEFAULT NULL COMMENT '译文',
  `author_detail` varchar(4000) DEFAULT NULL COMMENT '作者介绍',
  `author_baike_link` varchar(255) DEFAULT NULL COMMENT '作者介绍百度百科链接',
  `create_time` timestamp NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  PRIMARY KEY (`id`),
  KEY `main_idx` (`title`,`author`,`dynasty`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8 COMMENT='诗词';
