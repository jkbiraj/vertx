# Docker example using fatjar
# - docker build -t example/vertx .
# docker run -t -i -p 8888:8888 example/vertx

# https://hub.docker.com/_/amazoncorreto

FROM adoptopenjdk:17-jre-hotspot

# Alternative https://hub.dockrt.com/_/amazoncorreto
# FROM amazoncorreto:11

ENV FAT_JAR vertx-1.0.0-SNAPSHOT-fat.jar
ENV APP_HOME /use/app

EXPOSE 8888

copy build/libs/$FAT_JAR $APP_HOME

WORKDIR $APP_HOME
ENTRYPOINT ["sh", "-c"]
CMD ["exec java -jar $FAT_JAR"]
