FROM adoptopenjdk/openjdk8-openj9
ADD network-1.0-SNAPSHOT.jar /home/network-1.0-SNAPSHOT.jar
ENTRYPOINT  ["java","-Djava.security.egd=file:/dev/./urandom","-jar","/home/network-1.0-SNAPSHOT.jar"]
ENV TIMEZONE Asia/Shanghai