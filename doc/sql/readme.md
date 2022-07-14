1. novel_plus.sql 为全量 sql 文件，yyyyMMdd.sql 为增量 sql 文件
2. 第一次安装只需要执行 novel_plus.sql 文件即可
3. 后续版本升级需要根据上次代码版本的时间，执行该日期之后的增量 sql 文件（简单来说就是 sql 文件夹中相较于上次多出来的 sql 文件）