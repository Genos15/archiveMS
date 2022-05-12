FROM java:8
ENV port = 8111
EXPOSE 8111:8111
ADD /target/*.jar app.jar
ENTRYPOINT java $EXTRA_JAR_ARGS -jar app.jar config