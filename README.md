# DemoProject

# **项目说明**

本例子简单的展示SpringBoot+Maven+Mybatis+Dubbo+Kafka+Redis

其中，kafka示例展示了如何向kafka发送单个字符串和自定义对象消息，如何消费字符串和自定义对象。

**可持续补充**：MongoDB、ES、RabbitMQ、RocketMQ等中间件的使用，以及加入nginx或者网关服务

**运维知识**：Docker、K8S、Jenkins、Nexus、Habor、GitLab

**项目架构总览**

![架构](.\projectsrc\picture\架构.jpg)

# **运行环境**

zookeeper-3.5.9、kafka-2.13-3.2.1、jdk8、nacos-server 2.5.0

# **参考资料**

spring+dubbo搭建：https://juejin.cn/post/7159776981771354119

kafka环境搭建，需要分别安装zookeeper和kafka。

本例采用docker安装kafka环境，参考：https://blog.csdn.net/m0_64210833/article/details/134199061



# **项目启动**

自行安装kafka环境、redis、nacos，配置数据库好后，启动项目即可，或者参考如下

## 1、安装nacos

启动nacos，下载安装包后。命令窗口进入解压后的bin目录，输入命令，启动nacos

```
startup.cmd -m standalone
```

## **2、Docker安装zookeeper**

1、拉取镜像

```
docker pull wurstmeister/zookeeper
```

2、启动zookeeper

```
docker run -d --name zookeeper -p 2181:2181 wurstmeister/zookeeper
```

3、进入zookeeper容器（本次示例可不用进入，到第2步即可）

```
docker exec -it zookeeper /bin/bash
```

## 3、**Docker安装kafka**

1、拉取镜像

```
docker pull wurstmeister/kafka
```

2、启动容器

```
docker run -d --name kafka -p 9092:9092 --link zookeeper:zookeeper --env KAFKA_ZOOKEEPER_CONNECT=zookeeper:2181 --env KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092 --env KAFKA_LISTENERS=PLAINTEXT://0.0.0.0:9092 --env KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR=1 wurstmeister/kafka
```

命令说明

这里，我们链接了ZooKeeper容器，并且设置了几个环境变量来配置Kafka。

在这个命令中：

**--name** kafka: 设置容器的名字为“kafka”。

**-p** 9092:9092: 将容器的9092端口映射到宿主机的9092端口。

**--link** zookeeper:zookeeper: 连接到名为“zookeeper”的另一个Docker容器，并且在当前的容器中可以通过zookeeper这个别名来访问它。

**--env** KAFKA_ZOOKEEPER_CONNECT=zookeeper:2181: 设置环境变量，指定ZooKeeper的连接字符串。

**--env** KAFKA_ADVERTISED_LISTENERS=PLAINTEXT://localhost:9092: 设置环境变量，指定Kafka的advertised listeners。

**--env** KAFKA_LISTENERS=PLAINTEXT://0.0.0.0:9092: 设置环境变量，指定Kafka的listeners。

**--env** KAFKA_OFFSETS_TOPIC_REPLICATION_FACTOR=1: 设置环境变量，指定offsets topic的副本因子。

wurstmeister/kafka: 使用的Docker镜像名字。

## 4、数据库表

```sql
CREATE TABLE `point`  (
  `id` int(11) UNSIGNED NOT NULL AUTO_INCREMENT COMMENT '自增id',
  `account_id` varchar(32) CHARACTER SET latin1 COLLATE latin1_swedish_ci NOT NULL COMMENT '用户id',
  `point` int(11) NOT NULL COMMENT '积分',
  `create_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
  `update_time` datetime NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
  PRIMARY KEY (`id`) USING BTREE
) ENGINE = InnoDB;
```

