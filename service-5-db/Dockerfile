FROM openjdk:17-jdk-slim
WORKDIR /app
COPY target/service-*.jar app.jar
ENTRYPOINT [ "java", "-jar", "app.jar" ]
