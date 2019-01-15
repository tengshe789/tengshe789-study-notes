# gogs

## 什么是gogs

Gogs 是一款极易搭建的自助 Git 服务。

### 目标

Gogs 的目标是打造一个最简单、最快速和最轻松的方式搭建自助 Git 服务。使用 Go 语
言开发使得 Gogs 能够通过独立的二进制分发，并且支持 Go 语言支持的 所有平台，包
括 Linux、Mac OS X、Windows 以及 ARM 平台。

#### 地址

https://gitee.com/Unknown/gogs

## Gogs安装与配置
```$xslt
docker run -d --name=gogs -p 10022:22 -p 3000:3000 -v /var/gogsdata:/data gogs/gogs
```
###  安装

（1）下载镜像
`docker pull gogs/gogs`
（2）创建容器
``

### 配置

假设我的centos虚拟机IP为192.168.184.135 完成以下步骤
（1）在地址栏输入http://192.168.184.135:3000 会进入首次运行安装程序页面，我们
可以选择一种数据库作为gogs数据的存储，最简单的是选择SQLite3。如果对于规模较大
的公司，可以选择MySQL。点击“立即安装”
这里的域名要设置为centos的IP地址,安装后显示主界面