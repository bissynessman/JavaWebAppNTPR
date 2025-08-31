FROM eclipse-temurin:23-jdk-alpine
WORKDIR /app
COPY target/core-1.0.0.jar core.jar
ENTRYPOINT ["java", "-jar", "core.jar"]