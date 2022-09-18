FROM openjdk:8-jre
ENV port = 2222
EXPOSE 2222:2222
ADD /target/*.jar app.jar
ENTRYPOINT java $EXTRA_JAR_ARGS -jar app.jar discovery