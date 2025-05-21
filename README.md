# GoodBook
本项目最初是一个web在线单词本，陆陆续续写了一些杂七杂八的功能。支持多用户。后续也会不定时新增功能或修复bug。

## 目前包含的功能：

1.**单词本**。可以自己添加单词（中英文、音标、例句）。

2.**体重记录**。可以记录体重和称重时间，支持查看体重变化折线图。

3.**数青蛙**。无聊写的功能，可以快速查询几只青蛙几张嘴几个眼睛几条腿。疯狂模式可以解锁数量限制，容易爆内存，慎用！

4.**成语**。支持随机展示成语（包含成语、拼音、释义），成语接龙，成语查询，猜成语（发送给系统内的其他用户猜，对方根据释义猜成语）。
以上功能均需要开启成语爬虫配置，爬虫配置开启后，需要重启项目，启动时会自动爬取成语数据。 

开启方法：目前暂不支持网页配置，需要在数据库spider_config表中手动添加。 
执行以下sql语句：
```
INSERT INTO `goodbook`.`spider_config` (`config_name`, `config_value`, `type`) VALUES ('enable', 'true', 'idiom_baidu');
INSERT INTO `goodbook`.`spider_config` (`config_name`, `config_value`, `type`) VALUES ('enable', 'true', 'idiom_hanyu');
```

6.**抽奖**。可以自己添加选项，随机抽取一个选项。（我经常在想不好吃什么的时候用这个功能 ^_^）

7.**随机生成器**。目前仅支持随机生成人名。后续可能会添加其他功能。

8.**运维监控**。统计服务器的各种指标，如cpu、内存、磁盘使用率。（需要开启监控才会显示）。目前暂不支持页面配置，需要手动在数据库配置，配置完成后重启服务生效。

执行以下sql语句开启监控：
```
INSERT INTO `goodbook`.`system_config` (`enable_system_info_collection`, `ip`, `system_info_max_save_day`) VALUES ('1', '127.0.0.1', 30);
```
- ip:优先填本机ip，如果无效，试试127.0.0.1
- system_info_max_save_day:系统信息保存天数

## 技术栈：

前端：thymeleaf+jquery+bootstrap（没用什么前端框架，基本上是原生js）

后端：springboot+mybatis+mysql

爬虫：htmlUnit+jsoup
