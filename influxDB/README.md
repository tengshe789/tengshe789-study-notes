##  influxDB
### 什么是influxDB
influxDB是一个分布式时间序列数据库。cAdvisor仅仅显示实时信息，但是不存储监视数据。因此，我们需要提供时序数据库用于存储cAdvisor组件所提供的监控信息，以便显示除实时信息之外的时序数据。
### influxDB安装
（1）下载镜像
```
docker pull tutum/influxdb
```
（2）创建容器
```
docker run ‐di \
‐p 8083:8083 \
‐p 8086:8086 \
‐‐expose 8090 \
‐‐expose 8099 \
‐‐name influxsrv \
tutum/influxdb
```
端口概述： 8083端口:web访问端口 8086:数据写入端口
打开浏览器 http://192.168.184.135:8083/ (ip自觉替换)
###  influxDB常用操作
####  创建数据库
```
CREATE DATABASE "cadvisor"
```
回车创建数据库
```
SHOW DATABASES
```
查看数据库
####  创建用户并授权
创建用户
```
CREATE USER "cadvisor" WITH PASSWORD 'cadvisor' WITH ALL PRIVILEGES
```
查看用户
```
SHOW USRES
```
用户授权
```
grant all privileges on cadvisor to cadvisor
grant WRITE on cadvisor to cadvisor
grant READ on cadvisor to cadvisor
```
####  查看采集的数据
切换到cadvisor数据库，使用以下命令查看采集的数据
```
SHOW MEASUREMENTS
```
如果想采集系统的数据，我们需要使用Cadvisor软件来实现

## More

官方文档 ： https://github.com/influxdata/docs.influxdata.com