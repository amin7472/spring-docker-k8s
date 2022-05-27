# select parent image
FROM maven
# copy the source tree and the pom.xml to our new container
COPY ./ ./
# package our application code
RUN mvn clean package
# set the startup command to execute the jar
CMD ["java", "-jar", "target/contact-service-0.0.1-SNAPSHOT.jar"]