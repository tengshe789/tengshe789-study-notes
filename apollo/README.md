## 配置中心

Apollo（阿波罗）是携程框架部门研发的配置管理平台，能够集中化管理应用不同环境、不同集群的配置，配置修改后能够实时推送到应用端，并且具备规范的权限、流程治理等特性。



### 与 Spring Cloud Config 的对比

Spring Cloud Config的精妙之处在于它的配置存储于Git，这就天然的把配置的修改、权限、版本等问题隔离在外。通过这个设计使得Spring Cloud Config整体很简单，不过也带来了一些不便之处。

下面尝试做一个简单的小结：

| 功能点           | Apollo                                                       | Spring Cloud Config                                  | 备注                                                         |
| ---------------- | ------------------------------------------------------------ | ---------------------------------------------------- | ------------------------------------------------------------ |
| 配置界面         | 一个界面管理不同环境、不同集群配置                           | 无，需要通过git操作                                  |                                                              |
| 配置生效时间     | 实时                                                         | 重启生效，或手动refresh生效                          | Spring Cloud Config需要通过Git webhook，加上额外的消息队列才能支持实时生效 |
| 版本管理         | 界面上直接提供发布历史和回滚按钮                             | 无，需要通过git操作                                  |                                                              |
| 灰度发布         | 支持                                                         | 不支持                                               |                                                              |
| 授权、审核、审计 | 界面上直接支持，而且支持修改、发布权限分离                   | 需要通过git仓库设置，且不支持修改、发布权限分离      |                                                              |
| 实例配置监控     | 可以方便的看到当前哪些客户端在使用哪些配置                   | 不支持                                               |                                                              |
| 配置获取性能     | 快，通过数据库访问，还有缓存支持                             | 较慢，需要从git clone repository，然后从文件系统读取 |                                                              |
| 客户端支持       | 原生支持所有Java和.Net应用，提供API支持其它语言应用，同时也支持Spring annotation获取配置 | 支持Spring应用，提供annotation获取配置               | Apollo的适用范围更广一些                                     |