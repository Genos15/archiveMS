FROM openjdk:8-jre
ENV port 8083
EXPOSE 8083
ADD /target/*.jar app.jar
ENTRYPOINT java $EXTRA_JAR_ARGS -jar app.jar