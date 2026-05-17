FROM eclipse-temurin:21-jre

WORKDIR /app

COPY novel-crawl.jar /app/

# 默认关闭 ShardingSphere，保证旧版本兼容
ENV JDBC_DRIVER=com.mysql.cj.jdbc.Driver

ENTRYPOINT ["sh","-c","exec java \
-Dspring.datasource.driver-class-name=${JDBC_DRIVER} \
-jar /app/novel-crawl.jar"]