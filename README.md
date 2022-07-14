[![index]( https://youdoc.github.io/img/kuaidl4.png )]( https://www.kuaidaili.com/?ref=mdpz65syhqup )

<p align="center">
    <a href='https://github.com/201206030/novel-plus'><img alt="Github stars" src="https://img.shields.io/github/stars/201206030/novel-plus?logo=github"></a>
    <a href='https://github.com/201206030/novel-plus'><img alt="Github forks" src="https://img.shields.io/github/forks/201206030/novel-plus?logo=github"></a>
    <a href='https://gitee.com/novel_dev_team/novel-plus'><img alt="Gitee stars" src="https://gitee.com/novel_dev_team/novel-plus/badge/star.svg?theme=gitee"></a>
    <a href='https://gitee.com/novel_dev_team/novel-plus'><img alt="Gitee forks" src="https://gitee.com/novel_dev_team/novel-plus/badge/fork.svg?theme=gitee"></a>
    <a href="https://github.com/201206030/novel-plus"><img src="https://visitor-badge.glitch.me/badge?page_id=201206030.novel-plus" alt="visitors"></a>
</p>

#### 官网

https://201206030.github.io

#### 学习版

[基于 Spring Boot 3 + Vue 3 开发的前后端分离学习型小说项目](https://github.com/201206030/novel)

#### 微服务版

[Github](https://github.com/201206030/novel-cloud) | [码云](https://gitee.com/novel_dev_team/novel-cloud)

#### 演示地址

[点击前往](http://47.106.243.172:8888/)

#### 项目介绍

novel-plus 是一个多端（PC、WAP）阅读，功能完善的原创文学 CMS
系统。由前台门户系统、作家后台管理系统、平台后台管理系统和爬虫管理系统等多个子系统构成，包括小说推荐、作品检索、小说排行、小说阅读、小说评论、会员中心、作家专区等功能，支持自定义多模版、可拓展的多种小说内容存储方式（内置数据库分表存储和
TXT 文本存储）、阅读主题切换、多爬虫源自动采集和更新数据、会员充值、订阅模式、新闻发布和实时统计报表。

#### 项目结构

```
novel-plus -- 父工程
├── novel-common -- 通用模块
├── novel-front -- 前台门户&作家后台管理子系统（可拆分）
├── novel-crawl -- 爬虫管理子系统
├── novel-admin -- 平台后台管理子系统
└── templates -- 前端模版
```

#### 技术选型

| 技术                  | 说明
|---------------------| ---------------------------
| Spring Boot         | Spring 应用快速开发脚手架
| MyBatis             | 持久层 ORM 框架
| MyBatis Dynamic SQL | Mybatis 动态 sql
| PageHelper          | MyBatis 分页插件
| MyBatisGenerator    | 持久层代码生成插件
| Sharding-Jdbc       | 代码层分库分表中间件
| JJWT                | JWT 登录支持
| SpringSecurity      | 安全框架
| Shiro               | 安全框架
| Ehcache             | Java 进程内缓存框架(默认缓存)
| Redis               | 分布式缓存(缓存替换方案，默认关闭，一行配置开启)
| Elasticsearch       | 搜索引擎(搜索增强方案，默认关闭，一行配置开启)
| RabbitMQ            | 消息队列(流量削峰，默认关闭，一行配置开启)
| Aliyun OSS          | 阿里云对象存储服务(图片存储方式之一，一行配置即可切换)
| FastDFS             | 开源轻量级分布式文件系统(图片存储方式之一，一行配置即可切换)
| Redisson            | 实现分布式锁
| Lombok              | 简化对象封装工具
| Docker              | 应用容器引擎
| MySQL               | 数据库服务
| Thymeleaf           | 模板引擎
| Layui               | 前端 UI 框架

#### 橙色主题模版截图

##### PC站截图

1. 首页

![img](https://s3.ax1x.com/2020/12/27/r5400A.png)

2. 分类索引页

![img](https://oscimg.oschina.net/oscnet/up-d0b2e03129bfae47b8bb96a491b73d383c5.png)

3. 搜索页

![img](https://s3.ax1x.com/2020/12/27/r5TO8x.png)

![img](https://oscimg.oschina.net/oscnet/up-ed5f689557718924acac76bc3ebca36afcb.png)

4. 排行榜

![img](https://oscimg.oschina.net/oscnet/up-78d5a68586cd92a86c669311f414508f922.png)

5. 详情页

![img](https://oscimg.oschina.net/oscnet/up-8be2495a2869f93626b0c9c1df6f329747a.png)

6. 阅读页

![img](https://oscimg.oschina.net/oscnet/up-517c84148d2db8e11717a8bbecc57fa1be7.png)

7. 用户中心

![img](https://oscimg.oschina.net/oscnet/up-805a30e7a663a3fd5cb39a7ea26bc132a01.png)

8. 充值

![img](https://oscimg.oschina.net/oscnet/up-5a601b0b3af3224d0bebcfe12fc15075d34.png)

![img](https://oscimg.oschina.net/oscnet/up-face25d02c07b05b2ce954cc4bf4ee6a0cc.png)

9. 作家专区

![img](https://oscimg.oschina.net/oscnet/up-30766372cc7f56480ff1d7d55198204f6ea.png)

![img](https://s3.ax1x.com/2020/11/17/DVFiQI.png)

![img](https://s1.ax1x.com/2020/11/09/B7X5oF.png)

![img](https://s1.ax1x.com/2020/11/09/B7XLsx.png)

10. 购买

![img](https://oscimg.oschina.net/oscnet/up-ce0f585efd82a9670335f118cf5897c85e9.png)

![img](https://oscimg.oschina.net/oscnet/up-f849960f4c1303fea77d26e64fc505a7180.png)

##### 手机站截图

1. 首页

   <img src="https://s3.ax1x.com/2020/12/27/r5447n.jpg" alt="index" width="300" />

2. 小说列表页

   <img src="https://s3.ax1x.com/2020/12/27/r55xKg.jpg" alt="微信图片_20190904181558" width="300" />

3. 小说详情页

   <img src="https://s3.ax1x.com/2020/12/28/roZWOf.jpg" alt="QQ图片20191018161901" width="300" />

4. 小说阅读页

   <img src="https://s3.ax1x.com/2020/12/27/r55Stx.jpg" alt="QQ图片20191018161901" width="300" />

##### 爬虫管理系统截图

![img](https://s1.ax1x.com/2020/11/03/BsOgbD.png)

![img](https://s1.ax1x.com/2020/11/03/BsOHr8.png)

##### 后台管理系统截图

![img](https://oscimg.oschina.net/oscnet/up-0552343538674a22a64819834100558f39b.png)

![img](https://s3.ax1x.com/2020/12/01/DWgLNT.png)

![img](https://s3.ax1x.com/2020/12/01/DfmRCd.png)

![img](https://oscimg.oschina.net/oscnet/up-faf5dda7320674c29a1772bc0c81d74762e.png)

#### 深色主题模版截图

##### PC站截图

1. 首页

   ![index](https://static.oschina.net/uploads/img/202006/24151811_wIus.png)

##### 手机站截图

1. 首页

   ![index](https://static.oschina.net/uploads/img/202006/24151812_OOob.jpg)

4. 小说详情页

   ![微信图片_20190904181558](https://static.oschina.net/uploads/img/202006/24151812_ZosF.png)

5. 目录页

   ![QQ图片20191018161901](https://static.oschina.net/uploads/img/202006/24151812_Krva.png)

5. 小说阅读页

   ![QQ图片20191018161901](https://static.oschina.net/uploads/img/202006/24151813_fDgT.png)

#### 蓝色主题模版截图（更新中）

![QQ图片20191018161901](https://s3.ax1x.com/2020/12/27/r5Fe0A.png)

#### 源码安装教程

- JDK 安装

  建议[安装 JDK 8](https://docs.oracle.com/javase/8/docs/technotes/guides/install/linux_jdk.html)

- MySQL 安装：

    1. [安装 MySQL 服务](https://dev.mysql.com/doc/refman/8.0/en/linux-installation.html)
    2. 修改 MySQL`max_allowed_packet `配置（建议 100 M）
    3. 新建数据库，设置编码为 utf8mb4
    4. 执行 doc/sql/novel_plus.sql 脚本文件

- Maven 安装

  [安装 Apache Maven](https://maven.apache.org/install.html)

- 源码打包

  novel-plus 根目录下执行打包命令`mvn clean package -Dmaven.test.skip`

- 爬虫安装

    1. 上传 novel-crawl/target/build/novel-crawl.zip 压缩包到 Linux 服务器的 novel-crawl 目录
    2. 使用`unzip novel-crawl.zip`命令解压 novel-crawl.zip
    3. 修改 `config/application-common-prod.yml` 文件中的数据库配置
    4. 修改 `config/application-common-prod.yml` 文件中的管理员账号密码
    5. 根据需要[设置爬虫的代理IP](https://github.com/201206030/novel-plus/wiki/%E7%88%AC%E8%99%AB%E7%A8%8B%E5%BA%8F-HTTP-%E4%BB%A3%E7%90%86%E8%AE%BE%E7%BD%AE)
    6. novel-crawl 目录下使用`bin/novel-crawl.sh start`命令启动爬虫程序
    7. 打开浏览器，默认`8083`端口访问
    8. 选择已有或新增爬虫源（支持自定义爬虫规则），点击`开启`按钮，开始采集小说数据
    9. novel-crawl 目录下使用`bin/novel-crawl.sh stop`命令停止爬虫程序
    10. novel-crawl 目录下使用`bin/novel-crawl.sh restart`命令重启爬虫程序
    11. novel-crawl 目录下使用`bin/novel-crawl.sh status`命令查看爬虫程序的运行状态

- 前台安装

    1. 上传 novel-front/target/build/novel-front.zip 压缩包到 Linux 服务器的 novel-front 目录
    2. 使用`unzip novel-front.zip`命令解压 novel-front.zip
    3. 修改 `config/application-common-prod.yml` 文件中的数据库配置
    4. 修改 `config/application-website.yml` 配置文件中的网站信息
    5. novel-front 目录下使用`bin/novel-front.sh start`命令启动前台程序
    6. 打开浏览器，默认`8085`端口访问
    7. novel-front 目录下使用`bin/novel-front.sh stop`命令停止前台程序
    8. novel-front 目录下使用`bin/novel-front.sh restart`命令重启前台程序
    9. novel-front 目录下使用`bin/novel-front.sh status`命令查看前台程序的运行状态

- 后台安装

    1. 上传 novel-admin/target/build/novel-admin.zip 压缩包到 Linux 服务器的 novel-admin 目录
    2. 使用`unzip novel-admin.zip`命令解压 novel-admin.zip
    3. 修改 `config/application-prod.yml` 文件中的数据库配置
    4. novel-admin 目录下使用`bin/novel-admin.sh start`命令启动后台程序
    5. 打开浏览器，默认`8088`端口访问
    6. novel-admin 目录下使用`bin/novel-admin.sh stop`命令停止后台程序
    7. novel-admin 目录下使用`bin/novel-admin.sh restart`命令重启后台程序
    8. novel-admin 目录下使用`bin/novel-admin.sh status`命令查看后台程序的运行状态

**注：部分环境（如新版 Mac OS 系统）获取不到主机 IP，需要手动修改 hosts 文件，增加 IP-主机名（通过 hostname 命令查看主机名）的映射。**

#### 代码仓库

Github 仓库地址： https://github.com/201206030/novel-plus

Gitee 仓库地址： https://gitee.com/novel_dev_team/novel-plus

#### QQ 交流群

[点击前往官网查看](https://xiongxyang.gitee.io/service.htm)

#### 微信交流群

微信群二维码会过期，所以每周在公众号更新一次，请关注公众号“**xxyopen**”回复关键词“**微信群**”获取。

问问题的三要素

1. 说明背景，使用了哪个模块，要做什么？

2. 怎么输入或操作的得到了什么结果？ 截图，日志

3. 哪里不明白或有什么疑问 ？

#### 微信公众号（发布最新更新资讯、最新前端模版、最新爬虫规则、技术文档等）

![mini-code](https://s3.ax1x.com/2020/12/03/DoImOx.png)

#### 赞赏支持

开源项目不易，若此项目能得到你的青睐，那么你可以赞赏支持作者持续开发与维护。

- 服务器的费用也是一笔开销
- 为用户提供更好的开发环境
- 一杯咖啡

![mini-code](https://s1.ax1x.com/2020/10/31/BUQJwq.png)

#### 免责声明

本项目提供的爬虫工具仅用于采集项目初期的测试数据，请勿用于商业盈利。 用户使用本系统从事任何违法违规的事情，一切后果由用户自行承担，作者不承担任何责任。
