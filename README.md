# LAN-Network-Security-Review-System-Based-on-Network-protect-level-2.0
## 项目后端完成情况：后端大部分API编写工作已完成
### 完成的功能列表：
---
**Device:**
1. 增加设备
2. 审批设备
3. 删除设备
4. 查询全部设备/查询某个用户的设备
---
**emergency:**
1. 增加紧急事件
---
**Userinfo:**
1. 增加用户信息
2. 获得用户信息
3. 删除用户信息（会连带删除登陆表中的用户信息）
---
**tip:**
1. 向指定用户发送消息（tip）
---
**overview:**
1. 获得系统概览
---
**login:**
1. 登录
2. 登出
3. 增加登录用户
4. 验证session
5. 筛选出不同用户组
---
**systemlog:**
1. 获取所有日志
2. 根据日志等级获取日志
---
**scan:**
1. 增加/删除扫描任务（nmap -v -sn 快速扫描）
2. 增加/删除漏扫任务（nessus 基础全面扫描主机）
3. 下载报告（统一传送给文件api到前端服务器进行处理）（xml,jpg,html等）
---
**自动化任务:**
1. 定时监控（1小时1次，只扫描服务器，然后根据开放的端口和数据库里的记录进行对比，选出有问题的服务器并向管理员发送消息）
1. 待添加功能：
2. 定时爬虫（360安全报告）（从老代码中复制即可）
3. overview定时添加
3. 其余待定
4. 可能可以做定期session清理
---
##以下是API接口参数
###User接口
接口链接:/api/user/login   
接收参数：   
1. 用户名 username
2. 密码 password 

返回参数：   
1. statuscode 正常情况为200
2. loginstatus 登录情况，值为true/false
3. role 角色属性
4. session 会话验证字段
---
接口链接:/api/user/AddUser   
接收参数：   
1. 用户名 username
2. 密码 password
3. role 角色属性

返回参数：   
1. statuscode 正常情况为200
2. addstatus 登录情况，值为一段消息
3. Username 账户名称
4. url 增加成功则返回登陆界面进行登录
---
接口链接:/api/user/list  
接收参数：   
1. 角色属性 role

返回参数：   
1. data JSONARRAY,包含以下字段{role,username,uuid}
2. statuscode 正常情况为200
3. status success or false;
---
接口链接:/api/user/Logout 
验证方式原因此字段无需后台操作。
---
接口链接:/api/user/validate  
接收参数：   
1. 会话验证字段 session

返回参数：   

1. status success or false
2. uuid 用户唯一指定标记
---
###其他懒得写了，回头再写