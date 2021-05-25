FROM openjdk:15-alpine

COPY target/akamai_server.jar /usr/src/akamai_server.jar

CMD java -agentlib:jdwp=transport=dt_socket,address=*:8081,server=y,suspend=n -jar usr/src/akamai_server.jar agh.cs.RemoteServerAkamaiCDN
