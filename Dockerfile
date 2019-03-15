FROM openjdk:8u191-jdk-alpine
COPY ./target/usineBCA.war /tmp/
WORKDIR /tmp
RUN chmod 777 usineBCA.war
#ENV JPDA_ADDRESS=5005
#ENV JPDA_TRANSPORT="dt_socket"
#EXPOSE 5005
ENTRYPOINT ["/usr/bin/java"]
CMD ["-jar","-Dserver.port=8080","usineBCA.war"]