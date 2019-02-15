## 什么是Grafana
Grafana是一个可视化面板（Dashboard），有着非常漂亮的图表和布局展示，功能齐全的度量仪表盘和图形编辑器。支持Graphite、zabbix、InfluxDB、Prometheus和OpenTSDB作为数据源。

Grafana主要特性：灵活丰富的图形化选项；可以混合多种风格；支持白天和夜间模式；多个数据源。
###  Grafana安装
（1）下载镜像
`docker pull grafana/grafana`
（2）创建容器
```
docker run ‐d ‐p 3001:3000 ‐e INFLUXDB_HOST=influxsrv ‐e
INFLUXDB_PORT=8086 ‐e INFLUXDB_NAME=cadvisor ‐e INFLUXDB_USER=cadvisor ‐e
INFLUXDB_PASS=cadvisor ‐‐link influxsrv:influxsrv ‐‐name grafana
grafana/grafana
```
注意，`INFLUXDB`配置请与上文我的写的配置相同。
（3）访问
http://192.168.184.135:3001 （自觉更换ip）
用户名密码均为`admin`

### 使用

//TODO