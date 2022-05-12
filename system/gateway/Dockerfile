FROM java:8
ENV port 8765
EXPOSE 8765:8765
ADD /target/*.jar app.jar
ENTRYPOINT java $EXTRA_JAR_ARGS -jar app.jar gateway