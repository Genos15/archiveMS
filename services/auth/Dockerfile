FROM openjdk:8-jre
ENV port = 7777
EXPOSE 7777
ADD /target/*.jar app.jar
ENTRYPOINT java $EXTRA_JAR_ARGS -jar app.jar
#EXPOSE 80/udp
#EXPOSE 80 443