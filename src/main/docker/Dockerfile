FROM openjdk:17-oracle
ENV LANG='en_US.UTF-8' LANGUAGE='en_US:en'

MAINTAINER XuZhenkui

RUN mkdir -p /services/server
RUN mkdir -p /services/server/logs
RUN mkdir -p /services/server/temp

WORKDIR /services/server

EXPOSE 8090

ADD ./target/springboot3-provider-0.0.1-SNAPSHOT.jar ./app.jar

ENTRYPOINT ["java", "-Djava.security.egd=file:/dev/./urandom", "-jar", "app.jar"]
