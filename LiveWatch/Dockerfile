FROM eclipse-temurin:17-jdk

WORKDIR /app

COPY target/LiveWatch-0.0.1-SNAPSHOT.jar /app/LiveWatch-0.0.1-SNAPSHOT.jar

EXPOSE 8080

ENTRYPOINT["java", "-jar", "LiveWatch-0.0.1-SNAPSHOT.jar"]