FROM maven:3.9.8-eclipse-temurin-21 AS build
COPY . .
RUN mvn clean package -DskipTests

# Use OpenJDK for runtime
FROM openjdk:21
WORKDIR /app
COPY --from=build /app/target/swagger-api-server.jar swagger-api-server.jar

# Expose port 8080 (Render detects automatically)
EXPOSE 8080

# Set environment variables (these should be passed at runtime)
ENV DATABASE_URL=${DATABASE_URL}
ENV DATABASE_HOST=${DATABASE_HOST}
ENV DATABASE_PORT=${DATABASE_PORT}
ENV DATABASE_NAME=${DATABASE_NAME}
ENV DATABASE_USER=${DATABASE_USER}
ENV DATABASE_PASSWORD=${DATABASE_PASSWORD}
ENV JWT_SECRET=${JWT_SECRET}

# Run the application
ENTRYPOINT ["java", "-jar", "swagger-api-server.jar"]

