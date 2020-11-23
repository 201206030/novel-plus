[![index]( https://s1.ax1x.com/2020/07/03/NOSA5q.jpg )]( https://cloud.tencent.com/act/cps/redirect?redirect=1052&cps_key=736e609d66e0ac4e57813316cec6fd0b&from=console )

# 小说精品屋-plus

#### 新项目：小说精品屋-微服务版

基于小说精品屋-plus构建的Spring Cloud 微服务小说门户平台。

Gitee仓库地址： https://gitee.com/xiongxyang/novel-cloud

GitHub仓库地址： https://github.com/201206030/novel-cloud

#### 示例网站

[网站1，点击前往](http://www.bqmfxs.cn)

[网站2，点击前往](http://iyuedu.space)

[网站3，点击前往](http://suxiange.com)

[网站4，点击前往](http://ruoshu.com)

[网站5，点击前往](https://www.jingjiao.net)

[网站6，点击前往](http://www.shucheng.in)

#### 前言

小说精品屋-plus致力于打造一个完整的商用小说门户平台，使用前建议先阅读此文档。

#### 项目介绍

小说精品屋-plus是在[小说精品屋](https://github.com/201206030/fiction_house)的基础上，去除了漫画和弹幕模块，专注于小说，是一个多端（PC、移动）阅读、功能完善的小说原创/爬虫网站项目，既包含了作家专区供原创作者上传小说，又提供了爬虫工具通过规则多线程全自动采集任意小说网站数据（已兼容99%的小说网站），新书自动入库，老书自动更新。

小说精品屋-plus重新进行了数据库设计、代码重构和功能增强，提升了程序整体的可读性和性能，增加了很多商用特性。主要升级如下：

- [x] 数据库重新设计，结构调整。
- [x] 服务端代码重构，MyBatis3升级为MyBatis3DynamicSql。
- [x] 移动站与PC站站点分离，浏览器自动识别跳转。
- [x] PC站UI更新。
- [x] 新闻模块。
- [x] 排行榜。
- [x] 小说评论模块。
- [x] 阅读主题模块。
- [x] 作家专区。
- [x] 充值。
- [x] 订阅。
- [x] 后台管理系统。
- [x] 爬虫管理系统。

#### 项目结构

```
novel-plus -- 父工程
├── novel-common -- 通用模块
├── novel-front -- 前台门户系统
├── novel-crawl -- 爬虫管理系统
└── novel-admin -- 后台管理系统
```

#### 技术选型

| 技术                 | 说明                                                         
| -------------------- | ---------------------------
| SpringBoot           | Spring应用快速开发脚手架     
| MyBatis              | 持久层ORM框架 
| MyBatis Dynamic SQL  | Mybatis动态sql
| PageHelper           | MyBatis分页插件
| MyBatisGenerator     | 持久层代码生成插件
| Sharding-Jdbc        | 代码层分库分表中间件
| JJWT                 | JWT登录支持  
| SpringSecurity       | 安全框架                           
| Shiro                | 安全框架  
| Ehcache              | Java进程内缓存框架(默认缓存)  
| Redis                | 分布式缓存(缓存替换方案，默认关闭，一行配置开启)                               
| ElasticSearch        | 搜索引擎(搜索增强方案，默认关闭，一行配置开启)                      
| RabbitMq             | 消息队列(流量削峰，默认关闭，一行配置开启)  
| OSS                  | 阿里云对象存储服务(图片存储方式之一，一行配置即可切换) 
| FastDfs              |开源轻量级分布式文件系统(图片存储方式之一，一行配置即可切换)                      
| Redisson             | 实现分布式锁                                       
| Lombok               | 简化对象封装工具                                                                               
| Docker               | 应用容器引擎   
| Mysql                | 数据库服务   
| Thymeleaf            | 模板引擎     
| Layui                | 前端UI                    
                 

#### PC站截图

1. 首页

![img](https://oscimg.oschina.net/oscnet/up-bbb1c7f72e183327bff754a9fa8bb75223e.png)

2. 分类索引页

![img](https://oscimg.oschina.net/oscnet/up-d0b2e03129bfae47b8bb96a491b73d383c5.png)

3. 搜索页

![img](https://gitee.com/xiongxyang/novel-plus/raw/release_v2.1.0/assets/QQ20200520-215756.png)

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

#### 手机站截图

1. 首页

   ![index](https://gitee.com/xiongxyang/novel-plus/raw/release_v2.1.0/assets/QQ%E5%9B%BE%E7%89%8720191018162208.jpg)

2. 小说详情页

   ![微信图片_20190904181558](https://gitee.com/xiongxyang/novel-plus/raw/release_v2.1.0/assets/%E5%BE%AE%E4%BF%A1%E5%9B%BE%E7%89%87_20190904181558.png)

3. 目录页

   ![QQ图片20191018161901](https://gitee.com/xiongxyang/novel-plus/raw/release_v2.1.0/assets/QQ%E5%9B%BE%E7%89%8720191108022250.png)

4. 小说阅读页

   ![QQ图片20191018161901](https://gitee.com/xiongxyang/novel-plus/raw/release_v2.1.0/assets/QQ%E5%9B%BE%E7%89%8720191018161901.png)

#### 爬虫管理系统截图

![img](https://s1.ax1x.com/2020/11/03/BsOgbD.png)

![img](https://s1.ax1x.com/2020/11/03/BsOHr8.png)

#### 后台管理系统截图

![img](https://oscimg.oschina.net/oscnet/up-0552343538674a22a64819834100558f39b.png)

![img](https://oscimg.oschina.net/oscnet/up-faf5dda7320674c29a1772bc0c81d74762e.png)

#### 安装步骤

##### 数据库安装：

1. 安装MySQL软件。
2. 修改MySQL`max_allowed_packet `配置（建议100M）。
3. 新建数据库，设置编码为utf8mb4。
4. 执行sql/novel_plus.sql脚本文件。

##### 爬虫管理系统安装：

1.  修改novel-common模块下application-dev.yml文件中的数据库的配置。
2. 修改novel-crawl模块下application.yml文件中的管理员账号密码。
3. 启动程序，打开浏览器，默认8081端口访问。
4. 选择已有或新增爬虫源（支持自定义爬虫规则），点击`开启`按钮，开始爬取小说数据。

##### 前台小说门户安装：

1. 修改novel-common模块下application-dev.yml文件中的数据库的配置。
2. 启动程序，打开浏览器，默认8080端口访问。

**喜欢此项目的可以给我的GitHub和Gitee加个Star支持一下 。**

#### 其他安装教程

包安装教程：[点击前往](https://my.oschina.net/java2nb/blog/4272630) 

宝塔安装教程（非官方）：[点击前往](https://www.daniao.org/9166.html) 

docker安装教程：[点击前往](https://my.oschina.net/java2nb/blog/4271989)

#### 代码仓库

 Gitee仓库地址： https://gitee.com/xiongxyang/novel-plus

 GitHub仓库地址： https://github.com/201206030/novel-plus 

#### QQ交流群

![mini-code](https://s1.ax1x.com/2020/10/31/BUQVeI.png)

#### 赞赏支持

开源项目不易，若此项目能得到你的青睐，那么你可以赞赏支持作者持续开发与维护。

- 服务器的费用也是一笔开销
- 为用户提供更好的开发环境
- 一杯咖啡 

![mini-code](https://s1.ax1x.com/2020/10/31/BUQJwq.png)

### 免责声明

本项目提供的爬虫工具仅用于采集项目初期的测试数据，请勿用于商业盈利。
用户使用本系统从事任何违法违规的事情，一切后果由用户自行承担，作者不承担任何责任。

#### 备注

精品小说屋所有相关项目均已在开源中国公开，感兴趣的可进入[开源中国](https://www.oschina.net/p/fiction_house)按关键字`精品小说屋`搜索。

[![index](https://s1.ax1x.com/2020/07/03/NOSuMF.jpg)](https://www.aliyun.com/minisite/goods?userCode=uf4nasee )
