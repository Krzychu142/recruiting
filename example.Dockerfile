FROM openjdk:17-jdk-alpine

WORKDIR /app

COPY target/recruiting-0.0.1-SNAPSHOT.jar /app/myapp.jar

COPY src/main/resources/application.properties /app/config/application.properties

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/app/myapp.jar", "--spring.config.location=file:/app/config/application.properties"]
