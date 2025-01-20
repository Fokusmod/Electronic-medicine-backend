FROM openjdk:17-jdk-slim
ARG JAR_FILE=target/*.jar
COPY ./target/medicine-0.0.1-SNAPSHOT.jar app.jar

COPY . .

ENTRYPOINT ["java", "-jar", "/app.jar"]