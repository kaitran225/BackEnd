# Build stage
FROM maven:3.9.8-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Copy only necessary files for dependency resolution
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./
RUN chmod +x mvnw && ./mvnw dependency:go-offline -B

# Copy source and build
COPY src ./src
RUN ./mvnw clean package -DskipTests=true -Dmaven.test.skip=true -Dmaven.javadoc.skip=true

# Run stage with minimal image
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Add timezone data and clean up in the same layer
RUN apk --no-cache add tzdata && \
    rm -rf /var/cache/apk/* /tmp/* /var/tmp/* && \
    rm -rf /usr/lib/jvm/java-21-openjdk/lib/src.zip

# Set timezone
ENV TZ=Asia/Ho_Chi_Minh

# Copy only the built JAR
COPY --from=build /app/target/swagger-api-server.jar app.jar

# Expose port 8080
EXPOSE 8080

# Set production profile and minimal memory settings
ENV SPRING_PROFILES_ACTIVE=prod \
    SPRING_MAIN_LAZY_INITIALIZATION=true \
    SPRING_JPA_OPEN_IN_VIEW=false \
    SPRING_DATASOURCE_HIKARI_MAXIMUM_POOL_SIZE=2 \
    SPRING_DATASOURCE_HIKARI_MINIMUM_IDLE=1

# Run with optimized JVM flags for minimal idle memory
ENTRYPOINT ["java", \
    "-XX:+UseContainerSupport", \
    "-Xms32m", \
    "-Xmx150m", \
    "-XX:MaxRAMPercentage=75.0", \
    "-XX:+UseSerialGC", \
    "-Xss256k", \
    "-XX:MetaspaceSize=32m", \
    "-XX:MaxMetaspaceSize=64m", \
    "-XX:+ShrinkHeapInSteps", \
    "-XX:MinHeapFreeRatio=10", \
    "-XX:MaxHeapFreeRatio=20", \
    "-XX:-TieredCompilation", \
    "-XX:+UseStringDeduplication", \
    "-XX:+DisableExplicitGC", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "-jar", \
    "app.jar"]
