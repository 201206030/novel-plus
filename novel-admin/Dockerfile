FROM java:8
ADD novel-admin-1.0.0.jar /root
ENV dburl=""
ENV username=""
ENV	password=""
ENTRYPOINT ["sh","-c","java -Dspring.datasource.url=${dburl} -Dspring.datasource.username=${username} -Dspring.datasource.password=${password} -jar /root/novel-admin-1.0.0.jar"]