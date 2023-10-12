FROM openjdk:8
ADD novel-front-4.2.0.jar /root
ENV dburl=""
ENV username=""
ENV	password=""
ENV redishost = ""
ENV redisport = ""
ENV redispwd = ""
ENTRYPOINT ["sh","-c","java -Dspring.datasource.url=${dburl} -Dspring.datasource.username=${username} -Dspring.datasource.password=${password} -Dspring.redis.host=${redishost} -Dspring.redis.port=${redisport} -Dspring.redis.password=${redispwd} -jar /root/novel-front-4.2.0.jar"]
