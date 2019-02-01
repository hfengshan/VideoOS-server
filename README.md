# VideoOS server
VideoOS server 后端部署指南

## 准备工作

### 环境
* Maven 3+
* Jdk 1.8+
* Mysql 5.6.5+
* Redis 2.4.2+

VideoOS 后端系统目前兼容市面主流的MQ情况，如下：

|  | RabbitMQ | MQTT | MNS | Kafka |
| ------------ | ------------- | ------------ | ------------ | ------------ |
| 版本 | RabbitMQ 3.6.1+ | MQTT 3.1+ | 阿里云商业版 | Kafka 2.1.4+ |
| 是否支持 | 支持【默认必选】  | 支持【默认必选】 | 支持【可选方案】 | 支持【可选方案】 |

VideoOS 后端系统兼容主流文件存储系统情况，如下：

|  | OSS | 七牛云 |
| ------------ | ------------ | ------------ |
| 版本 | 阿里云商业版 | 七牛云商业版 |
| 是否支持 | 支持【默认必选】| 暂不支持 |

## 部署步骤

### 初始化 VideoOS 后端数据库

可以直接下载项目源码，获取`video_platform`初始化数据脚本，并且执行即可，正常情况下会生成16张表  

附件中 VideoOS 所有的脚本`DDL.sql`以及文件 VideoOS 所有的脚本`DML.sql`，两个脚本文件。  
appliction.yml配置文件如下：
```yml
spring:
  datasource:
    url: jdbc:mysql://rm-xxxx.mysql.rds.aliyuncs.com:3306/video_platform
    username: test
    password: 123456
    driver-class-name: com.mysql.jdbc.Driver
  mqtt:
    username: xxxxx
    password: xxxxx
    hostUrl: tcp://10.xx.xx.xx:1883
    nameHost: xxxx.videojj.com
    clientId: mqttjs_e8022a4d0b
    defaultTopic: ceshi
    timeout: 10
    keepalive: 20
mybatis:
  mapper-locations: classpath:mapper/*.xml
    redis:
      shiro:
        host: r-xxxxx.redis.rds.aliyuncs.com
        port: 6379
        timeout: 0
        password: xxxx
    video:
      oss:
        endpoint: http://oss-cn-xxxx.aliyuncs.com/
        key: xxxx
        secret: xxxx
        bucketName: videojj-os
      sftp:
        username: xxxx
        password: xxxx
        host: 10.xx.xx.xxx
        port: 22
        rootPath: /data/static/
      common:
        filePath: /var/log/
        preKey: dev/
        cron: 0 30 23 * * ? *
        fileTool: SftpUtil
        fileDomainName: http://os-xxx-xxxx.videojj.com/  #如果是本地文件服务就填写这个
```

### 运行环境安装ffmpeg

windows下手动下载ffmpeg官方地址[http://www.ffmpeg.org/download.html](http://www.ffmpeg.org/download.html)，默认安装即可。  
debian Linux 安装方法：
```shell
apt-get install ffmpeg
```
Ubuntu Linux安装方法：
```shell
apt-get install ffmpeg
```

### 打包
打包方式可以使用maven，到项目的根目录下，比如`/User/cxy/videoos`，然后执行命令
```shell
mvn clean package
```
主要两个jar包，一个是videoportal-1.0.0.jar（后端控制台），还有一个是videopublicapi-1.0.0.jar（提供给移动端的接口）。 
注意：一定要确保各个配置都更新好了之后再打包，否则启动的时候会有报错。

## 运行参数
```shell
java -Xms4096m -Xmx4096m -Xss256k -XX:MetaspaceSize=128m -XX:MaxMetaspaceSize=384m -XX:NewSize=1536m -XX:MaxNewSize=1536m -XX:SurvivorRatio=22 –Denv = dev –jar xxx.jar
```
启动成功之后，  
执行curl http://xxx.xxx.xxx.xxx:xx/videoos/index 测试videoportal是否可以正常使用  
执行curl http://xxx.xxx.xxx.xxx:xx/videoos-api/index 测试videopublicapi是否可以正常使用  
![](https://wiki.videojj.com/download/attachments/2196191/image2019-2-1%2017%3A38%3A36.png?version=1&modificationDate=1549013916936&api=v2)

## 业务日志
业务日志默认存在路径是/var/log，请确保该路径下的写文件权限。

## 后端架构图
![](https://wiki.videojj.com/download/attachments/2196191/image2019-1-21%2014_38_50.png?version=1&modificationDate=1549011604585&api=v2)

## 网络架构图
![](https://wiki.videojj.com/download/attachments/2196191/image2019-1-21%2014_41_41.png?version=1&modificationDate=1549011738272&api=v2)


