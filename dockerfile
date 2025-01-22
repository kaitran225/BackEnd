FROM openjdk:21
ADD target/swagger-api-server.jar swagger-api-server.jar
EXPOSE 8080
ENTRYPOINT ["java", "-jar", "/swagger-api-server.jar"]