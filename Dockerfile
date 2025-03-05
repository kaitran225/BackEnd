# Use Maven to build the application
FROM maven:3.9.8-eclipse-temurin-21 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline

COPY src ./src
RUN mvn clean package -DskipTests

# Use a smaller JDK runtime for running the application
FROM eclipse-temurin:21-jre
WORKDIR /app

# Copy only the built JAR
COPY --from=build /app/target/swagger-api-server.jar app.jar

# Expose port 8080
EXPOSE 8080

# Set the active Spring profile to production
ENV SPRING_PROFILES_ACTIVE=prod

# Run the application with optimized JVM flags
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:MaxRAMPercentage=90", "-jar", "app.jar"]
