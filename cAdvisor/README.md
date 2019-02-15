## cAdvisor
### 啥么是cAdvisor
Google开源的用于监控基础设施应用的工具，它是一个强大的监控工具，不需要任何配置就可以通过运行在Docker主机上的容器来监控Docker容器，而且可以监控Docker主机。
###  cAdvisor安装
（1）下载镜像
`docker pull google/cadvisor`
（2）创建容器

```dockerfile
docker run ‐‐volume=/:/rootfs:ro ‐‐volume=/var/run:/var/run:rw ‐‐
volume=/sys:/sys:ro ‐‐volume=/var/lib/docker/:/var/lib/docker:ro ‐‐
publish=8080:8080 ‐‐detach=true ‐‐link influxsrv:influxsrv ‐‐name=cadvisor
google/cadvisor ‐storage_driver=influxdb ‐storage_driver_db=cadvisor ‐
storage_driver_host=influxsrv:8086
```
WEB前端访问地址
http://192.168.184.135:8080/containers/  (请自觉替换实际ip)
性能指标含义参照如下地址
https://blog.csdn.net/ZHANG_H_A/article/details/53097084
再次查看influxDB，发现已经有很多数据被采集进去了。