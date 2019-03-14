FROM openjdk:8u191-jdk-alpine
COPY ./target/usineBCA-2.0.0-SNAPSHOT.war /tmp/usineBCA-2.0.0-SNAPSHOT.war
WORKDIR /tmp
RUN chmod 777 usineBCA-2.0.0-SNAPSHOT.war
#ENV JPDA_ADDRESS=5005
#ENV JPDA_TRANSPORT="dt_socket"
#EXPOSE 5005
ENTRYPOINT ["/usr/bin/java"]
CMD ["-jar","-Dserver.port=8080","usineBCA-2.0.0-SNAPSHOT.war"]