FROM maven
COPY ./ ./
RUN mvn clean package
CMD ["java", "-jar", "target/contact-service-0.0.1-SNAPSHOT.jar"]