FROM maven:3.6.1-jdk-8-alpine

# 设置工作目录
WORKDIR /app

# 拷贝 pom.xml 文件到镜像
#COPY pom.xml /app/pom.xml

# 保存依赖的
#RUN mvn dependency:go-offline

#COPY src /app

#RUN mvn clean compile package -Dmaven.test.skip=true

ADD target/magic_interview.jar /app

# -Duser.timezone=GMT+08: 日志显示时间差8小时
CMD  ["java", "-jar", "-Duser.timezone=GMT+08","/app/magic_interview.jar"]

# 项目部署到docker时，修改
#1 .MagicInterviewApplication: @PropertySource。
#2. application.yml: 对应端口的日志文件；redis 连接。
#3. application-dao-dev.yml: 数据库连接。