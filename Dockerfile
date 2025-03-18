# Build stage
FROM maven:3.9.8-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Copy pom.xml first to leverage Docker layer caching
COPY pom.xml .
RUN mvn dependency:resolve

# Copy source code
COPY src ./src

# Build the application
RUN mvn clean package -DskipTests

FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

ENV TZ=Asia/Ho_Chi_Minh

# Copy only the built JAR
COPY --from=build /app/target/swagger-api-server.jar app.jar

# Expose port 8080
EXPOSE 8080

# Set production profile and minimal memory settings
ENV SPRING_PROFILES_ACTIVE=prod

# Run with optimized JVM flags for minimal idle memory
ENTRYPOINT ["java", \
    "-XX:+UseContainerSupport", \
    "-Xms32m", \
    "-Xmx192m", \
    "-XX:MaxRAMPercentage=75.0", \
    "-XX:+UseSerialGC", \
    "-Xss256k", \
    "-XX:MetaspaceSize=128m", \
    "-XX:MaxMetaspaceSize=128m", \
    "-XX:+ShrinkHeapInSteps", \
    "-XX:MinHeapFreeRatio=10", \
    "-XX:MaxHeapFreeRatio=20", \
    "-jar", \
    "app.jar"]
