## pinpoint

### 是啥

pinpoint是开源在github上的一款APM监控工具，它是用Java编写的，用于大规模分布式系统监控。它对性能的影响最小（只增加约3％资源利用率），安装agent是无侵入式的，只需要在被测试的Tomcat中加上3句话，打下探针，就可以监控整套程序了。

### 所需软件
```
hbase-1.2.6-bin.tar.gz
pinpoint-1.6.2.zip pinpoint源码，使用hbase-create.hbase脚本初始化
hbase
pinpoint-collector-1.6.2.war collector模块，使用tomcat进行部署
pinpoint-web-1.6.2.war web管控台，使用tomcat进行部署
pinpoint-agent-1.6.2.tar.gz 不需要部署，在被监控应用一端
apache-tomcat-8.5.15.zip
```
准备两个tomcat环境，一个部署pinpoint-collector-1.6.2.war，另一个部署pinpoint-web-1.6.2.war
### 安装hbase
#### 环境配置
解压hbase-1.2.6-bin.tar.gz，修改hbase/conf/hbase-site.xml文件内容如下
```
<configuration>
<property>
<name>hbase.rootdir</name>
<value>file:///pinpoint/data/hbase</value>
</property>
<property>
<name>hbase.zookeeper.property.dataDir</name>
<value>/pinpoint/data/zookeeper</value>
</property>
<property>
<name>hbase.master.port</name>
<value>60000</value>
</property>
<property>
<name>hbase.regionserver.port</name>
<value>60020</value>
</property>
</configuration>
```
修改`hbase/conf/hbase-env.sh`文件，设置`JAVA_HOME`环境变量
`export JAVA_HOME=/Library/Java/JavaVirtualMachines/jdk1.8.0_112.jdk/Contents/Home`
#### 启动hbase
`hbase/bin/start-hbase.sh`
#### 执行jps查看是否有HMaster进程
访问 http://127.0.0.1:16010/master-status 查看hbase web管控台
导入hbase初始化脚步
```
hbase/bin/hbase shell /pinpoint-1.6.2/hbase/scripts/hbase-create.hbase
.
.
.
0 row(s) in 1.4160 seconds
0 row(s) in 1.2420 seconds
0 row(s) in 2.2410 seconds
0 row(s) in 1.2400 seconds
0 row(s) in 1.2450 seconds
0 row(s) in 1.2420 seconds
0 row(s) in 1.2360 seconds
0 row(s) in 1.2400 seconds
0 row(s) in 1.2370 seconds
0 row(s) in 2.2450 seconds
0 row(s) in 4.2480 seconds
0 row(s) in 1.2310 seconds
0 row(s) in 1.2430 seconds
0 row(s) in 1.2390 seconds
0 row(s) in 1.2440 seconds
0 row(s) in 1.2440 seconds
TABLE
AgentEvent
AgentInfo
AgentLifeCycle
AgentStat
AgentStatV2
ApiMetaData
ApplicationIndex
ApplicationMapStatisticsCallee_Ver2
ApplicationMapStatisticsCaller_Ver2
ApplicationMapStatisticsSelf_Ver2
ApplicationTraceIndex
HostApplicationMap_Ver2
SqlMetaData_Ver2
StringMetaData
TraceV2
Traces
16 row(s) in 0.0190 seconds
```
#### 进入hbase shell
```
hbase/bin/hbase shell
HBase Shell; enter 'help<RETURN>' for list of supported commands.
Type "exit<RETURN>" to leave the HBase Shell
Version 1.2.6, rUnknown, Mon May 29 02:25:32 CDT 2017
hbase(main):001:0>
```
输入status 'detailed'查看刚才初始化的表，是否存在
```
hbase(main):001:0> status 'detailed'
```
访问 http://127.0.0.1:16010/master-status 查看hbase web管控台

#### 部署collector
解压apache-tomcat-8.5.15.zip并重命名为collector
拷贝pinpoint-collector-1.6.2.war到collector\webapps\目录下并重命名为ROOT.war
涉及到两个配置文件
+ hbase.properties
由于使用的为hbase自带的zookeeper即hbase和zookeeper在同一台机器上，则不需要修改collector/webapps/ROOT/WEB-INF/classes/hbase.properties文件
+ pinpoint-collector.properties
注意观察该配置文件中中的三个端口9994、9995、9996 默认不需要修改，其中9994为collector监听的tcp端口，9995为collector监听的udp端口

执行collector/bin/startup.sh启动collector
部署web管控台
解压apache-tomcat-8.5.15.zip并重命名为web
拷贝pinpoint-web-1.6.2.war到web\webapps\目录下并重命名为ROOT.war
同样留意web/webapps/ROOT/WEB-INF/classes/hbase.properties中的配置，若与hbase在同一台机器则不需要修改
执行web/bin/startup.sh启动web管控台(注意修改tomcat端口号，防止冲突，这里修改端口为8088)
浏览器访问 http://127.0.0.1:8088/ 查看pinpoint管控台
### 监控spring boot应用

这里有一个spring-boot-example.jar的应用，现在要使用pinpoint来对其监控跟踪。操作很简单，分两步
解压pinpoint-agent-1.6.2.tar.gz并对其进行配置
```
tar zxvf pinpoint-agent-1.6.2.tar.gz -C pinpoint-agent
```
修改pinpoint-agent/pinpoint.config中的配置与collector服务一致。此处因为pinpoint-agent与collector在同
一台机器，因此默认配置即可不需要修改。
为spring-boot应用配置pinpoint-agent
启动spring-boot应用时添加如下参数
```
-javaagent:$AGENT_PATH/pinpoint-bootstrap-$VERSION.jar
-Dpinpoint.agentId
-Dpinpoint.applicationName
```
本例中的启动参数为
```
java -javaagent:/pinpoint-agent/pinpoint-bootstrap-1.6.2.jar -Dpinpoint.agentId=spring-boot-app -Dpinpoint.applicationName=spring-boot-app -jar spring-boot-docker-example-1.0.jar
```
访问 http://127.0.0.1:8088/ ，第一次访问可能没有数据，可以先访问下应用，然后在刷新pinpoint管控台即可

## more

https://www.cnblogs.com/yyhh/p/6106472.html