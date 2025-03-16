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

# Set production profile
ENV SPRING_PROFILES_ACTIVE=prod

# Run with optimized JVM flags for containers
ENTRYPOINT ["java", \
    "-XX:+UseContainerSupport", \
    "-XX:+AlwaysActAsServerClassMachine", \
    "-Xmx128m", \
    "-Xms128m", \
    "-XX:MaxRAMPercentage=75.0", \
    "-XX:+UseSerialGC", \
    "-Xss256k", \
    "-XX:MaxMetaspaceSize=64m", \
    "-XX:CompressedClassSpaceSize=32m", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "-XX:+UseStringDeduplication", \
    "-XX:+DisableExplicitGC", \
    "-XX:SoftRefLRUPolicyMSPerMB=0", \
    "-Dspring.config.location=classpath:/application-prod.yaml", \
    "-jar", \
    "app.jar"]
