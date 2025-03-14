# Use Maven to build the application
FROM maven:3.9.8-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Copy pom.xml first to leverage Docker layer caching
COPY pom.xml .
RUN mvn dependency:resolve

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

# Use a smaller JDK runtime for running the application
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Set timezone to prevent time issues
ENV TZ=Asia/Ho_Chi_Minh

# Copy only the built JAR
COPY --from=build /app/target/swagger-api-server.jar app.jar

# Expose port 8080
EXPOSE 8080

# Set the active Spring profile to production
ENV SPRING_PROFILES_ACTIVE=prod

# Run the application with optimized JVM flags
ENTRYPOINT ["java", "-XX:+UseContainerSupport", "-XX:+AlwaysActAsServerClassMachine", "-XX:MaxRAM=256m","-XX:+UseSerialGC","-Xss216k","-jar","app.jar"]
