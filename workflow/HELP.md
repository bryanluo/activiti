SpringBoot + Activiti7
---

环境：
---
idea + JDK1.8

依赖:
---
```xml
<dependencies>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-web</artifactId>
    </dependency>
    <dependency>
        <groupId>org.mybatis.spring.boot</groupId>
        <artifactId>mybatis-spring-boot-starter</artifactId>
        <version>2.1.3</version>
    </dependency>
    
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-configuration-processor</artifactId>
        <optional>true</optional>
    </dependency>
    <dependency>
        <groupId>org.projectlombok</groupId>
        <artifactId>lombok</artifactId>
        <optional>true</optional>
    </dependency>
    <dependency>
        <groupId>org.springframework.boot</groupId>
        <artifactId>spring-boot-starter-test</artifactId>
        <scope>test</scope>
        <exclusions>
            <exclusion>
                <groupId>org.junit.vintage</groupId>
                <artifactId>junit-vintage-engine</artifactId>
            </exclusion>
        </exclusions>
    </dependency>
    
    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
    </dependency>
    
    <dependency>
        <groupId>org.activiti</groupId>
        <artifactId>activiti-spring-boot-starter</artifactId>
    </dependency>
    </dependencies>
```

yaml 配置：
--- 

```yaml
server:
  port: 8080
# 数据源配置
spring:
  datasource:
    username: root
    password: server@123
    driver-class-name: com.mysql.cj.jdbc.Driver
    url: jdbc:mysql://myserver:3306/activities?useUnicode=true&characterEncoding=utf-8&useSSL=false
  activiti:
    database-schema: activities
    # 启动时检查数据库表， 不存在则创建
    database-schema-update: true
    # 什么情况下使用历史表， full表示全部记录历史
    history-level: full
    # 使用历史表， 不为true 历史表将不会被创建
    db-history-used: true
```

测试
---
配置好后， 查看数据库的表是否创建成功


理解数据库命名
---

Activiti 的表都以ACT_开头。 第二部分是表示表的用途的两个字母标识。 用途也和服务的API对应。


* ACT_RE_*: RE 表示 repository。 这个前缀表包含了流程定义和流程静态资源（图片，规则等等）

* ACT_RU_*: RU 表示 runtime。 这些运行时的表， 包含流程实例， 任务， 变量，异步任务等运行中的数据。
Activity 只会在流程实例执行过程中保存这些数据， 流程结束的时候会删除这些记录。 这样运行时表可以一直
很小很快。

* ACT_ID_*: ID 表示 identity。 这些表包含身份信息， 比如用户，组等等。

* ACT_HI_*: HI 表示 history。 这些表包含历史数据， 比如历史任务， 变量， 实例等等。

* ACT_GE_*: 通用数据， 用于不同场景下。


API
---

| 服务名 | 作用 |
| --- | --- |
| RepositoryService | 流程图的部署、修改、删除 |
| RuntimeService | 流程的运行 |
| TaskService | 流程相关任务 |
| HistoryService | 查询历史记录 |
| FormService | 页面表单的服务器 |
| IdentityService | 对工作流的用户管理表操作 |
| ManagerService | 管理器 |

## 主要类

ProcessDefinition 
---

操作表： act_re_procdef

流程定义类. 可以从这里获得资源文件等。 当流程图被部署之后， 查询出来的数据就是流程定义的数据

ProcessInstance
---

代表流程定义的执行实例。 例如小明请了一天的假，他就必须发出一个流程实例的申请。 一个流程实例包括
了所有的运行节点。 我们可以利用这个对象来了解当前流程实例的进度等信息。 流程实例就表示一个流程从
开始到结束的最大流程分支， 即一个流程中实例只有一个。

Execution
---

Activities 用这个对象去描述流程执行的每一个节点。 在没有并发的情况下， Execution 就是同 ProcessInstance
流程按照流程定义的规则执行的一次的过程， 就可以表示执行对象Execution。


重要SQL
---

```sql

# RepositoryService

select * from ACT_GE_BYTEARRAY; # 二进制文件表
select * from ACT_RE_DEPLOYMENT; # 流程部署表
select * from ACT_RE_PROCDEF; # 流程定义
select * from ACT_GE_PROPERTY; # 工作流的ID算法和版本信息表

# RuntimeService

select * from ACT_RU_EXECUTION; # 流程启动一次只要没有执行完， 就会有一条数据
select * from ACT_RU_TASK; # 可能有多条数据
select * from ACT_RU_VARIABLE; # 记录流程运行时的流程变量
select * from ACT_RU_IDENTITYLINK; # 存放流程办理人的信息

# historyService
select * from ACT_HI_PROCINST; # 历史流程实例
select * from ACT_HI_TASKINST; # 历史任务实例
select * from ACT_HI_ACTINST; #历史活动节点表
select * from ACT_HI_VARINST; # 历史流程变量表
select * from ACT_HI_IDENTITYLINK; # 历史办理人表
select * from ACT_HI_COMMENT; # 批注表
select * from ACT_HI_ATTACHMENT; # 附件表
```