FROM openjdk:8-jre
ENV port = 8082
EXPOSE 8082
ADD /target/*.jar app.jar
ENTRYPOINT java $EXTRA_JAR_ARGS -jar app.jar