FROM java:8
ENV port = 2222
EXPOSE 2222:2222
ADD /target/*.jar app.jar
ENTRYPOINT java $EXTRA_JAR_ARGS -jar app.jar discovery