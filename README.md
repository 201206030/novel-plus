[![index]( https://youdoc.github.io/img/tencent.jpg )]( https://cloud.tencent.com/act/cps/redirect?redirect=2446&cps_key=736e609d66e0ac4e57813316cec6fd0b&from=console )

<p align="center">
    <a href='https://github.com/201206030/novel-plus'><img alt="Github stars" src="https://img.shields.io/github/stars/201206030/novel-plus?logo=github"></a>
    <a href='https://github.com/201206030/novel-plus'><img alt="Github forks" src="https://img.shields.io/github/forks/201206030/novel-plus?logo=github"></a>
    <a href='https://gitee.com/novel_dev_team/novel-plus'><img alt="Gitee stars" src="https://gitee.com/novel_dev_team/novel-plus/badge/star.svg?theme=gitee"></a>
    <a href='https://gitee.com/novel_dev_team/novel-plus'><img alt="Gitee forks" src="https://gitee.com/novel_dev_team/novel-plus/badge/fork.svg?theme=gitee"></a>
    <a href="https://github.com/201206030/novel-plus"><img src="https://visitor-badge.glitch.me/badge?page_id=201206030.novel-plus" alt="visitors"></a>
</p>

## 官网

https://novel.xxyopen.com

## 学习版

[基于 Spring Boot 3 + Vue 3 开发的前后端分离学习型小说项目](https://github.com/201206030/novel)

## 微服务版

[GitHub](https://github.com/201206030/novel-cloud) | [码云](https://gitee.com/novel_dev_team/novel-cloud)

## 演示地址

[点击前往](http://47.106.243.172:8888/)

## 项目介绍

novel-plus 是一个多端（PC、WAP）阅读，功能完善的原创文学 CMS
系统。由前台门户系统、作家后台管理系统、平台后台管理系统和爬虫管理系统等多个子系统构成，包括小说推荐、作品检索、小说排行、小说阅读、小说评论、会员中心、作家专区等功能，支持自定义多模版、可拓展的多种小说内容存储方式（内置数据库分表存储和
TXT 文本存储）、阅读主题切换、多爬虫源自动采集和更新数据、会员充值、订阅模式、新闻发布和实时统计报表。

## 项目结构

```
novel-plus -- 父工程
├── novel-common -- 通用模块
├── novel-front -- 前台门户&作家后台
├── novel-crawl -- 爬虫
├── novel-admin -- 管理后台
└── templates -- 前端模版
```

## 技术选型

| 技术                  | 说明
|---------------------| ---------------------------
| Spring Boot         | Spring 应用快速开发脚手架
| MyBatis             | 持久层 ORM 框架
| MyBatis Dynamic SQL | Mybatis 动态 sql
| PageHelper          | MyBatis 分页插件
| MyBatis Generator    | 持久层代码生成插件
| Sharding-JDBC       | 代码层分库分表中间件
| JJWT                | JWT 登录支持
| Spring Security      | 安全框架
| Apache Shiro               | 安全框架
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

## 项目截图

### 橙色主题模版截图

#### PC站截图

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

#### 手机站截图

<p align="center">

   <img src="https://s3.ax1x.com/2020/12/27/r5447n.jpg"  width="300" />
   <img src="https://s3.ax1x.com/2020/12/27/r55xKg.jpg"  width="300" />
   <img src="https://s3.ax1x.com/2020/12/28/roZWOf.jpg"  width="300" />
   <img src="https://s3.ax1x.com/2020/12/27/r55Stx.jpg"  width="300" />



</p>

#### 爬虫管理系统截图

![img](https://s1.ax1x.com/2020/11/03/BsOgbD.png)

![img](https://s1.ax1x.com/2020/11/03/BsOHr8.png)

#### 后台管理系统截图

![img](https://oscimg.oschina.net/oscnet/up-0552343538674a22a64819834100558f39b.png)

![img](https://s3.ax1x.com/2020/12/01/DWgLNT.png)

![img](https://s3.ax1x.com/2020/12/01/DfmRCd.png)

![img](https://oscimg.oschina.net/oscnet/up-faf5dda7320674c29a1772bc0c81d74762e.png)

### 深色主题模版截图

#### PC站截图

1. 首页

   ![index](https://static.oschina.net/uploads/img/202006/24151811_wIus.png)

#### 手机站截图

<p align="center">
      <img src="https://static.oschina.net/uploads/img/202006/24151812_OOob.jpg"  width="300" />
      <img src="https://static.oschina.net/uploads/img/202006/24151812_ZosF.png"  width="300" />
      <img src="https://static.oschina.net/uploads/img/202006/24151812_Krva.png"  width="300" />
      <img src="https://static.oschina.net/uploads/img/202006/24151813_fDgT.png"  width="300" />

</p>

### 蓝色主题模版截图（更新中）

![QQ图片20191018161901](https://s3.ax1x.com/2020/12/27/r5Fe0A.png)


## 源码安装教程

[点此查看](http://xxyopen.com/2022/07/18/doc/novel-plus/install/source.html)

## 包安装教程

[点此查看](https://xxyopen.com/2022/07/18/doc/novel-plus/install/package.html)

## 宝塔安装教程

[点此查看](https://xxyopen.com/2022/07/18/doc/novel-plus/install/bt.html)

## 增值服务

[点此查看](https://novel.xxyopen.com/service.htm)

## 更多

- [谷歌搜索](https://www.google.com/search?q=%E5%B0%8F%E8%AF%B4%E7%B2%BE%E5%93%81%E5%B1%8B)

## 代码仓库

GitHub 仓库地址： https://github.com/201206030/novel-plus

Gitee 仓库地址： https://gitee.com/novel_dev_team/novel-plus

## QQ 交流群

[点击前往官网查看](https://xiongxyang.gitee.io/service.htm)

## 微信交流群

微信群人数超过 200 人不能分享二维码，请关注公众号“**xxyopen**”回复关键词“**微信群**”。

## 微信公众号

发布最新更新资讯、最新前端模版、最新爬虫规则、文档教程等。

![img](https://youdoc.github.io/img/qrcode_for_gh.jpg)

## 赞赏支持

开源项目不易，若此项目能得到你的青睐，那么你可以赞赏支持作者持续开发与维护。

- 服务器的费用也是一笔开销
- 编写更完备的文档教程
- 发布更多前端模版和爬虫规则
- 一杯咖啡

![img](https://s1.ax1x.com/2020/10/31/BUQJwq.png)

## 免责声明

本项目提供的爬虫工具仅用于采集项目初期的测试数据，请勿用于商业盈利。 用户使用本系统从事任何违法违规的事情，一切后果由用户自行承担，作者不承担任何责任。
