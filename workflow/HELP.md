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


数据库升级
---

