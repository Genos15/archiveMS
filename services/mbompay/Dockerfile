FROM openjdk:8-jre
ENV port = 7776
EXPOSE 7776
ADD /target/*.jar app.jar
ENTRYPOINT java $EXTRA_JAR_ARGS -jar app.jar