FROM openjdk:latest

COPY target/akamai.jar /usr/src/akamai.jar

CMD java -agentlib:jdwp=transport=dt_socket,address=*:8000,server=y,suspend=n -jar usr/src/akamai.jar agh.cs.BackendAkamaiCdnApplication
