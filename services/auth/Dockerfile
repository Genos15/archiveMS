FROM java:8
ENV port = 7777
EXPOSE 7777
ADD /target/*.jar app.jar
ENTRYPOINT java $EXTRA_JAR_ARGS -jar app.jar