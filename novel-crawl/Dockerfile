FROM java:8
ADD novel-crawl-1.1.0.jar /root
ENV dburl=""
ENV username=""
ENV	password=""
ENTRYPOINT ["sh","-c","java -Dspring.datasource.url=${dburl} -Dspring.datasource.username=${username} -Dspring.datasource.password=${password} -jar /root/novel-crawl-1.1.0.jar"]