FROM tomcat:latest

RUN rm -rf /usr/local/tomcat/webapps/*

COPY ./target/LabOne-0.0.1-SNAPSHOT.war /usr/local/tomcat/webapps/ROOT.war
CMD ["catalina.sh", "run"]