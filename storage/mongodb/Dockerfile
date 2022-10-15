FROM mongo
MAINTAINER Arnaud Njoh

EXPOSE 27017:27017
ENV MGDATA /var/lib/mongodb-static/data
VOLUME  ["$MGDATA"]
RUN mkdir -p MGDATA