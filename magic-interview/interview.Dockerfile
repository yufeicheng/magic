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
CMD  ["java", "-jar", "/app/magic_interview.jar"]