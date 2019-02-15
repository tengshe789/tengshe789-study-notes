## Open-Falcon
Open-Falcon，小米家的开源监控系统。
### 特点
1、强大灵活的数据采集：自动发现，支持falcon-agent、snmp、支持用户主动push、用户自定义插件支持、opentsdb data model like（timestamp、endpoint、metric、key-value tags）

2、水平扩展能力：支持每个周期上亿次的数据采集、告警判定、历史数据存储和查询

3、高效率的告警策略管理：高效的portal、支持策略模板、模板继承和覆盖、多种告警方式、支持callback调用

4、人性化的告警设置：最大告警次数、告警级别、告警恢复通知、告警暂停、不同时段不同阈值、支持维护周期

5、高效率的graph组件：单机支撑200万metric的上报、归档、存储（周期为1分钟）

6、高效的历史数据query组件：采用rrdtool的数据归档策略，秒级返回上百个metric一年的历史数据

7、dashboard：多维度的数据展示，用户自定义Screen

8、高可用：整个系统无核心单点，易运维，易部署，可水平扩展

9、开发语言： 整个系统的后端，全部golang编写，portal和dashboard使用python编写。



### 社区和文档支持

http://book.open-falcon.org/zh/index.html