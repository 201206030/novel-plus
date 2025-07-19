<p align="center">
    <a href="https://cloud.tencent.com/act/cps/redirect?redirect=2446&cps_key=736e609d66e0ac4e57813316cec6fd0b&from=console"><img src="https://youdoc.github.io/img/tencent.jpg" alt="AD" ></a>
</p>
<p align="center">
    <a href='https://github.com/201206030/novel-plus'><img alt="Github stars" src="https://img.shields.io/github/stars/201206030/novel-plus?logo=github"></a>
    <a href='https://github.com/201206030/novel-plus'><img alt="Github forks" src="https://img.shields.io/github/forks/201206030/novel-plus?logo=github"></a>
    <a href='https://gitee.com/novel_dev_team/novel-plus'><img alt="Gitee stars" src="https://gitee.com/novel_dev_team/novel-plus/badge/star.svg?theme=gitee"></a>
    <a href='https://gitee.com/novel_dev_team/novel-plus'><img alt="Gitee forks" src="https://gitee.com/novel_dev_team/novel-plus/badge/fork.svg?theme=gitee"></a>
</p>

<p align="center">
      👉 <a href='https://novel.xxyopen.com'>官网</a>  |  👉 <a href='http://117.72.165.13:8888'>演示站点</a>  |  👉 <a href='https://docs.xxyopen.com/course/novelplus/1.html'>安装教程</a>
</p> 

## 项目介绍

novel-plus 是一个多端（PC、WAP）阅读，功能完善的原创文学 CMS
系统。由前台门户系统、作家后台管理系统、平台后台管理系统和爬虫管理系统等多个子系统构成，包括小说推荐、作品检索、小说排行、小说阅读、小说评论、会员中心、作家专区等功能，支持自定义多模版、可拓展的多种小说内容存储方式（内置数据库分表存储和
TXT 文本存储）、阅读主题切换、多爬虫源自动采集和更新数据、AI写作、会员充值、订阅模式、新闻发布和实时统计报表。

## 项目地址

- 学习版：[GitHub](https://github.com/201206030/novel) ｜ [码云](https://gitee.com/novel_dev_team/novel)
  ｜ [保姆级教程](https://docs.xxyopen.com) 
- **应用版**：[GitHub](https://github.com/201206030/novel-plus) ｜ [码云](https://gitee.com/novel_dev_team/novel-plus) ｜ [演示站点](http://117.72.165.13:8888)
- 微服务版：[GitHub](https://github.com/201206030/novel-cloud) ｜ [码云](https://gitee.com/novel_dev_team/novel-cloud)

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
|---------------------|---------------------
| Spring Boot         | Spring 应用快速开发脚手架    
| Spring AI           | Spring 官方 AI 框架     
| MyBatis             | 持久层 ORM 框架          
| MyBatis Dynamic SQL | Mybatis 动态 sql      
| PageHelper          | MyBatis 分页插件        
| MyBatis Generator   | 持久层代码生成插件           
| Sharding-JDBC       | 代码层分库分表中间件          
| JJWT                | JWT 登录支持            
| Spring Security     | 安全框架                
| Apache Shiro        | 安全框架                
| Redis               | 缓存方案                
| Aliyun OSS          | 阿里云对象存储服务（图片存储备选方案） 
| Lombok              | 简化对象封装工具            
| Docker              | 应用容器引擎              
| MySQL               | 数据库服务               
| Thymeleaf           | 模板引擎                
| Layui               | 前端 UI 框架            

## 项目截图

### 绿色主题模版

[![点击查看大图](https://www.xxyopen.com/images/green_novel.png)](https://www.xxyopen.com/images/green_novel.png)
[![点击查看大图](https://www.xxyopen.com/images/resource/os/novel-plus/green3.png)](https://www.xxyopen.com/images/resource/os/novel-plus/green3.png)
[![点击查看大图](https://www.xxyopen.com/images/resource/os/novel-plus/green2.png)](https://www.xxyopen.com/images/resource/os/novel-plus/green2.png)

## 演示视频

https://www.bilibili.com/video/BV18e41197xs

## AI 功能

novel-plus 5.x 已集成 Spring 官方最新发布的 Spring AI 框架，并推出多项 AI 功能：

1. v5.0.0 版本在小说章节发布页面的文本编辑器中集成了多项智能编辑功能，包括 AI 扩写、缩写、续写及文本润色等。这些功能的设计灵感来源于百家号文章编辑器中的 AI 助手。
2. v5.1.0 版本在小说发布页面，新增 AI 生成封面图功能。若作家未上传自定义封面图，系统将根据小说信息自动生成封面图。

目前，AI 功能仍处于实验阶段，仅实现了基础的核心功能。我们非常重视用户的实际使用体验和反馈，未来将根据用户需求和使用情况，持续优化和调整该功能。如果用户反馈积极，我们计划进一步开发更高级的 AI 功能，例如自动生成有声小说、智能情节推荐等，以全面提升 novel-plus 的创作能力和用户体验。

我们将持续关注 AI 技术的发展，并致力于将其与小说创作场景深度融合，为用户带来更智能、更便捷的创作工具。

novel-plus 项目默认使用的是第三方大模型服务平台[硅基流动](https://cloud.siliconflow.cn/i/DOgMRH9S)提供的 API（兼容 OpenAI 的相关接口，可直接通过 Spring AI 框架调用），采用的 AI 模型有对话模型`deepseek-ai/DeepSeek-R1-0528-Qwen3-8B`（DeepSeek-R1 的蒸馏版本，免费使用）和生图模型`Kwai-Kolors/Kolors`（快手 Kolors 团队开发的文本到图像生成模型，免费使用）。只需注册一个硅基流动账号，创建一个 API 密钥，并将其添加到 novel-plus 项目 novel-front 模块的 yaml 配置文件中，即可体验 novel-plus 项目的 AI 写作功能。

```yaml
spring:
  ai:
    openai:
      image:
        enabled: true
        base-url: https://api.siliconflow.cn
        api-key: sk-rrrupturhdofbiqzjutduuiceecpvfqlnvmgcyiaipbdikoi
        options:
          model: Kwai-Kolors/Kolors
          response_format: URL
      api-key: sk-rrrupturhdofbiqzjutduuiceecpvfqlnvmgcyiaipbdikoi
      base-url: https://api.siliconflow.cn
      chat:
        options:
          model: deepseek-ai/DeepSeek-R1-0528-Qwen3-8B
```

⚠️ novel-plus 项目默认使用的都是免费 AI 模型，生成效果有限。如果对生成内容有更高的要求，建议选用付费的 AI 模型。

## 增值服务

👉 [了解详情](https://novel.xxyopen.com/service.htm)

## 微信公众号

发布最新更新动态、最新前端模版、最新爬虫规则、文档教程等。

![](https://youdoc.github.io/img/qrcode_for_gh.jpg)

## 赞赏支持

开源项目不易，若此项目能得到你的青睐，那么你可以赞赏支持作者持续开发与维护。

- 服务器的费用也是一笔开销
- 编写更完备的文档教程
- 发布更多前端模版和爬虫规则
- 一杯咖啡

![](https://s1.ax1x.com/2020/10/31/BUQJwq.png)

## 免责声明

本项目提供的爬虫工具仅用于采集项目初期的测试数据，请勿用于商业盈利。 用户使用本系统从事任何违法违规的事情，一切后果由用户自行承担，作者不承担任何责任。


