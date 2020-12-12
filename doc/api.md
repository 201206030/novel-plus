
 <h1 class="curproject-name"> 小说精品屋-plus </h1> 
 小说精品屋-plus接口


# 作家模块

## 小说章节分页列表查询接口
<a id=小说章节分页列表查询接口> </a>
### 基本信息

**Path：** /book/queryIndexList

**Method：** GET

**接口描述：**
<p>作家后台章节管理页面需要请求该接口获取小说章节分页列表信息</p>


### 请求参数
**Query**

| 参数名称  |  是否必须 | 示例  | 备注  |
| ------------ | ------------ | ------------ | ------------ |
| bookId | 是  |  1334337530296893441 |  小说ID |
| curr | 否  |  1 |  查询页码，默认1 |
| limit | 否  |  5 |  分页大小，默认5 |

### 返回数据

<table>
  <thead class="ant-table-thead">
    <tr>
      <th key=name>名称</th><th key=type>类型</th><th key=required>是否必须</th><th key=default>默认值</th><th key=desc>备注</th><th key=sub>其他信息</th>
    </tr>
  </thead><tbody className="ant-table-tbody"><tr key=0-0><td key=0><span style="padding-left: 0px"><span style="color: #8c8a8a"></span> code</span></td><td key=1><span>number</span></td><td key=2>必须</td><td key=3></td><td key=4><span style="white-space: pre-wrap">响应状态吗，200表示成功</span></td><td key=5></td></tr><tr key=0-1><td key=0><span style="padding-left: 0px"><span style="color: #8c8a8a"></span> msg</span></td><td key=1><span>string</span></td><td key=2>必须</td><td key=3></td><td key=4><span style="white-space: pre-wrap">响应信息</span></td><td key=5></td></tr><tr key=0-2><td key=0><span style="padding-left: 0px"><span style="color: #8c8a8a"></span> data</span></td><td key=1><span>object</span></td><td key=2>必须</td><td key=3></td><td key=4><span style="white-space: pre-wrap">响应数据</span></td><td key=5></td></tr><tr key=0-2-0><td key=0><span style="padding-left: 20px"><span style="color: #8c8a8a">├─</span> total</span></td><td key=1><span>number</span></td><td key=2>必须</td><td key=3></td><td key=4><span style="white-space: pre-wrap">总数量</span></td><td key=5></td></tr><tr key=0-2-1><td key=0><span style="padding-left: 20px"><span style="color: #8c8a8a">├─</span> list</span></td><td key=1><span>object []</span></td><td key=2>必须</td><td key=3></td><td key=4><span style="white-space: pre-wrap">章节数据集合</span></td><td key=5><p key=3><span style="font-weight: '700'">item 类型: </span><span>object</span></p></td></tr><tr key=0-2-1-0><td key=0><span style="padding-left: 40px"><span style="color: #8c8a8a">├─</span> id</span></td><td key=1><span>string</span></td><td key=2>必须</td><td key=3></td><td key=4><span style="white-space: pre-wrap">章节ID</span></td><td key=5></td></tr><tr key=0-2-1-1><td key=0><span style="padding-left: 40px"><span style="color: #8c8a8a">├─</span> bookId</span></td><td key=1><span>string</span></td><td key=2>必须</td><td key=3></td><td key=4><span style="white-space: pre-wrap">小说ID</span></td><td key=5></td></tr><tr key=0-2-1-2><td key=0><span style="padding-left: 40px"><span style="color: #8c8a8a">├─</span> indexName</span></td><td key=1><span>string</span></td><td key=2>必须</td><td key=3></td><td key=4><span style="white-space: pre-wrap">章节名</span></td><td key=5></td></tr><tr key=0-2-1-3><td key=0><span style="padding-left: 40px"><span style="color: #8c8a8a">├─</span> isVip</span></td><td key=1><span>number</span></td><td key=2>必须</td><td key=3></td><td key=4><span style="white-space: pre-wrap">是否收费，1:收费，0:免费</span></td><td key=5></td></tr><tr key=0-2-1-4><td key=0><span style="padding-left: 40px"><span style="color: #8c8a8a">├─</span> updateTime</span></td><td key=1><span>string</span></td><td key=2>必须</td><td key=3></td><td key=4><span style="white-space: pre-wrap">更新时间</span></td><td key=5></td></tr><tr key=0-2-2><td key=0><span style="padding-left: 20px"><span style="color: #8c8a8a">├─</span> pageNum</span></td><td key=1><span>number</span></td><td key=2>必须</td><td key=3></td><td key=4><span style="white-space: pre-wrap">页码</span></td><td key=5></td></tr><tr key=0-2-3><td key=0><span style="padding-left: 20px"><span style="color: #8c8a8a">├─</span> pageSize</span></td><td key=1><span>number</span></td><td key=2>必须</td><td key=3></td><td key=4><span style="white-space: pre-wrap">分页大小</span></td><td key=5></td></tr><tr key=0-2-4><td key=0><span style="padding-left: 20px"><span style="color: #8c8a8a">├─</span> size</span></td><td key=1><span>number</span></td><td key=2>必须</td><td key=3></td><td key=4><span style="white-space: pre-wrap">当前页数量</span></td><td key=5></td></tr>
               </tbody>
              </table>
            
## 小说章节删除接口
<a id=小说章节删除接口> </a>
### 基本信息

**Path：** /author/deleteIndex/{indexId}

**Method：** DELETE

**接口描述：**
<p>作家后台章节管理页面点击删除按钮请求该接口删除小说章节内容</p>


### 请求参数
**Headers**

| 参数名称  | 参数值  |  是否必须 | 示例  | 备注  |
| ------------ | ------------ | ------------ | ------------ | ------------ |
| Content-Type  |  application/x-www-form-urlencoded | 是  |   |   |
| Authorization  |   | 是  |  eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE2MDgzNDg0NzksInN1YiI6IntcImlkXCI6MTI1NTA2MDMyODMyMjAyNzUyMCxcInVzZXJuYW1lXCI6XCIxMzU2MDQyMTMyNFwiLFwibmlja05hbWVcIjpcIjEzNTYwNDIxMzI0XCJ9IiwiY3JlYXRlZCI6MTYwNzc0MzY3OTkxM30.0qhwis_zPb6t8wGNejMhDZ2iHCL9Tgh2UHd1gcQBCp8t6RW3ggSwtfo4l_RgMT_v8jOkLW91GzTVWlNnTE6LCA |  认证JWT，请求登录接口成功后返回 |
**路径参数**

| 参数名称 | 示例  | 备注  |
| ------------ | ------------ | ------------ |
| indexId |  1337603246936645632 |  章节ID |

### 返回数据

<table>
  <thead class="ant-table-thead">
    <tr>
      <th key=name>名称</th><th key=type>类型</th><th key=required>是否必须</th><th key=default>默认值</th><th key=desc>备注</th><th key=sub>其他信息</th>
    </tr>
  </thead><tbody className="ant-table-tbody"><tr key=0-0><td key=0><span style="padding-left: 0px"><span style="color: #8c8a8a"></span> code</span></td><td key=1><span>number</span></td><td key=2>必须</td><td key=3></td><td key=4><span style="white-space: pre-wrap">响应状态吗，200表示成功</span></td><td key=5></td></tr><tr key=0-1><td key=0><span style="padding-left: 0px"><span style="color: #8c8a8a"></span> msg</span></td><td key=1><span>string</span></td><td key=2>必须</td><td key=3></td><td key=4><span style="white-space: pre-wrap">响应信息</span></td><td key=5></td></tr>
               </tbody>
              </table>
            
## 小说章节发布接口
<a id=小说章节发布接口> </a>
### 基本信息

**Path：** /author/addBookContent

**Method：** POST

**接口描述：**
<p>作家后台章节发布页面点击提交按钮请求该接口新增小说章节内容</p>


### 请求参数
**Headers**

| 参数名称  | 参数值  |  是否必须 | 示例  | 备注  |
| ------------ | ------------ | ------------ | ------------ | ------------ |
| Content-Type  |  application/x-www-form-urlencoded | 是  |   |   |
| Authorization  |   | 是  |  eyJhbGciOiJIUzUxMiJ9.eyJleHAiOjE2MDgzNDg0NzksInN1YiI6IntcImlkXCI6MTI1NTA2MDMyODMyMjAyNzUyMCxcInVzZXJuYW1lXCI6XCIxMzU2MDQyMTMyNFwiLFwibmlja05hbWVcIjpcIjEzNTYwNDIxMzI0XCJ9IiwiY3JlYXRlZCI6MTYwNzc0MzY3OTkxM30.0qhwis_zPb6t8wGNejMhDZ2iHCL9Tgh2UHd1gcQBCp8t6RW3ggSwtfo4l_RgMT_v8jOkLW91GzTVWlNnTE6LCA |  认证JWT，请求登录接口成功后返回 |
**Body**

| 参数名称  | 参数类型  |  是否必须 | 示例  | 备注  |
| ------------ | ------------ | ------------ | ------------ | ------------ |
| bookId | text  |  是 |  1334337530296893441  |  小说ID |
| indexName | text  |  是 |  第六章未婚妻（下）  |  章节名 |
| content | text  |  是 |  开始之时，李七夜还是生疏无比，那怕他对于刀术的所有奥义了然于胸，但是，他出刀之时依然会颤抖，无法达到妙及巅毫的要求。  |  章节内容 |
| isVip | text  |  是 |  1  |  是否收费，1:收费，0:免费 |



### 返回数据

<table>
  <thead class="ant-table-thead">
    <tr>
      <th key=name>名称</th><th key=type>类型</th><th key=required>是否必须</th><th key=default>默认值</th><th key=desc>备注</th><th key=sub>其他信息</th>
    </tr>
  </thead><tbody className="ant-table-tbody"><tr key=0-0><td key=0><span style="padding-left: 0px"><span style="color: #8c8a8a"></span> code</span></td><td key=1><span>number</span></td><td key=2>必须</td><td key=3></td><td key=4><span style="white-space: pre-wrap">响应状态吗，200表示成功</span></td><td key=5></td></tr><tr key=0-1><td key=0><span style="padding-left: 0px"><span style="color: #8c8a8a"></span> msg</span></td><td key=1><span>string</span></td><td key=2>必须</td><td key=3></td><td key=4><span style="white-space: pre-wrap">响应信息</span></td><td key=5></td></tr>
               </tbody>
              </table>
            