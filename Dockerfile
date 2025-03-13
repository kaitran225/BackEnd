FROM maven:3.9.8-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Copy pom.xml first to leverage Docker layer caching
COPY pom.xml .
RUN mvn dependency:go-offline -B

# Copy source code and build with minimal memory usage
COPY src ./src
RUN mvn clean package -DskipTests -Dmaven.test.skip=true -Dmaven.javadoc.skip=true -Dmaven.source.skip=true

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

# Create a non-root user to run the application
RUN addgroup -S appgroup && adduser -S appuser -G appgroup
USER appuser

# Run the application with optimized JVM flags for cloud environments
ENTRYPOINT ["java", \
            "-XX:InitialRAMPercentage=50", \
            "-XX:MaxRAMPercentage=70", \
            "-XX:MinRAMPercentage=50", \
            "-XX:+UseContainerSupport", \
            "-XX:+UseG1GC", \
            "-XX:MaxGCPauseMillis=100", \
            "-XX:+ParallelRefProcEnabled", \
            "-XX:MaxTenuringThreshold=1", \
            "-XX:+DisableExplicitGC", \
            "-Djava.security.egd=file:/dev/./urandom", \
            "-Dserver.tomcat.max-threads=50", \
            "-Dspring.jmx.enabled=false", \
            "-jar", "app.jar"]
