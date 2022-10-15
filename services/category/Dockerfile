FROM openjdk:8-jre
ENV port = 8084
EXPOSE 8084
ADD /target/*.jar app.jar
ENTRYPOINT java $EXTRA_JAR_ARGS -jar app.jar