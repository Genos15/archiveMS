FROM openjdk:8-jre
ENV port = 8081
EXPOSE 8081
ADD /target/*.jar app.jar
ENTRYPOINT java $EXTRA_JAR_ARGS -jar app.jar