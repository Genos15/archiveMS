FROM openjdk:8-jre
ENV port = 8085
EXPOSE 8085
ADD /target/*.jar app.jar
ENTRYPOINT java $EXTRA_JAR_ARGS -jar app.jar