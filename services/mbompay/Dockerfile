FROM openjdk:8-jre
ENV port = 8080
EXPOSE 8080
ADD /target/*.jar app.jar
ENTRYPOINT java $EXTRA_JAR_ARGS -jar app.jar