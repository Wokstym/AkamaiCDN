FROM openjdk:15-alpine

COPY target/akamai.jar /usr/src/akamai.jar

RUN apk --no-cache add tcpdump \
 && apk --no-cache add sudo \
 && apk --no-cache add pv \
 && apk add hping3 --update-cache --repository http://dl-cdn.alpinelinux.org/alpine/edge/testing

CMD java -agentlib:jdwp=transport=dt_socket,address=*:8091,server=y,suspend=n -jar usr/src/akamai.jar agh.cs.BackendAkamaiCdnApplication
