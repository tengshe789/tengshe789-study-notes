## 配置中心

在分布式系统中，每一个功能模块都能拆分成一个独立的服务，一次请求的完成，可能会调用很多个服务协调来完成，为了方便服务配置文件统一管理，更易于部署、维护，所以就需要分布式配置中心组件了。

在spring cloud中，有分布式配置中心组件spring cloud config，它支持配置文件放在在配置服务的内存中，也支持放在远程Git仓库里。

我们的项目就属于将配置文件放在在配置服务的内存中。当需要更改配置时，只需要更改配置中心里面的配置，这时每个微服务实例就自动进行相应配置。

### 配置中心官方文档

https://springcloud.cc/spring-cloud-config.html

### 微服务中开启配置中心

#### maven依赖

在微服务中添加相关依赖，需要注意的是，由于新版`spring cloud`对于配置中心没有指定的版本管理，故在配置时需显示的指明config客户端的版本。这里使用的是`<version>2.0.2.RELEASE</version>`。

```xml
		<dependency>
			<groupId>org.springframework.cloud</groupId>
			<artifactId>spring-cloud-starter-config</artifactId>
			<version>2.0.2.RELEASE</version>
		</dependency>
```

#### 编辑配置文件

在`resources`目录下新建`bootstrap.yml`并删除老的`application.yml`。参考如下配置：

```yaml
server:
  port: 8765

spring:
  application:
    name: xbrl-cloud-gateway # 微服务的名称
  # 配置中心
  cloud:
    config:
      fail-fast: true # 配置客户端快速失败（在没有连接配置服务端时直接启动失败）
      name: xbrl-cloud-gateway # 定位远程配置资源 "name" = ${spring.application.name}
      profile: dev # 定位远程配置资源 "profile" = ${spring.profiles.active} (actually Environment.getActiveProfiles())
      discovery: # 通过consul定位配置中心
        enabled: true
        service-id: xbrl-cloud-config
    consul: # 将此服务注册到consul
      host: 127.0.0.1
      port: 8500
      discovery:
        tags: version=2.0
        healthCheckPath: /actuator/health
        healthCheckInterval: 5s
        instance-id: ${spring.application.name}:comma,separated,profiles:${server.port}
```

### 配置中心配置文件说明

#### 结构

```
├─ resources
│  │  
│  ├─ config---------------- config文件夹中存放着其他微服务模块的配置文件
│  │  ├─ application-dev.yml---------------- 公共的配置文件
│  │  ├─ xbrl-cloud-gateway-dev.yml---------------- 服务网关的配置文件
│  │  
│  ├─ bootstrap.yml----------------- 配置中心自身的配置文件
```



#### 公共的配置文件

`spring cloud config`会自动读取公共配置文件和各个微服务专属的配置文件。如有公共的配置文件，请将他配置到这里。

#### 各个微服务专属的配置文件

##### 配置文件命名

命名格式为{application}-{profile}-{label}`。如果要为网关写一个配置文件，则需要命名为xbrl-cloud-gateway-dev.yml`。

命名规则如下：

- {application} 对应微服务config客户端的"spring.application.name"属性
- {profile} 对应客户端的 "spring.profiles.active"属性(逗号分隔的列表)
- {label} 对应服务端属性,这个属性能标示一组配置文件的版本

