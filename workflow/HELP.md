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





