FROM maven:3.9.5-openjdk-21 AS build
COPY . .
RUN mvn clean package -DskipTests

FROM openjdk:21-slim
COPY --from=build /target/swagger-api-server.jar swagger-api-server.jar
EXPOSE 8080

ENTRYPOINT ["java", "-jar", "/swagger-api-server.jar"]