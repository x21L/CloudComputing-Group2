# setup the tomcat server and copy the war directory to the webapps of the server.
FROM tomcat

COPY target/shopping-cart-1.0.war /usr/local/tomcat/webapps/
# copy jars
COPY mysql-connector-java-8.0.22.jar /usr/local/tomcat
CMD ["catalina.sh", "run"]
