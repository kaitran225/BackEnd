# Use Maven to build the application
FROM maven:3.9.8-eclipse-temurin-21-alpine AS build
WORKDIR /app

# Copy only necessary files for dependency resolution
COPY pom.xml .
COPY .mvn/ .mvn/
COPY mvnw mvnw.cmd ./
RUN ./mvnw dependency:go-offline

# Copy source and build
COPY src ./src
RUN ./mvnw clean package -DskipTests -Dmaven.test.skip=true

# Use a smaller JDK runtime for running the application
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app

# Install only essential packages
RUN apk add --no-cache tzdata \
    && rm -rf /var/cache/apk/*

# Set timezone to prevent time issues
ENV TZ=Asia/Ho_Chi_Minh

# Copy only the built JAR
COPY --from=build /app/target/swagger-api-server.jar app.jar

# Remove unnecessary files
RUN rm -rf /var/cache/apk/* /tmp/* /var/tmp/* && \
    rm -rf /usr/lib/jvm/java-21-openjdk/lib/src.zip

# Expose port 8080
EXPOSE 8080

# Set the active Spring profile to production
ENV SPRING_PROFILES_ACTIVE=prod

# Run the application with optimized JVM flags
ENTRYPOINT ["java", \
    "-XX:+UseContainerSupport", \
    "-XX:+AlwaysActAsServerClassMachine", \
    "-Xmx128m", \
    "-Xms128m", \
    "-XX:MaxRAMPercentage=75.0", \
    "-XX:+UseSerialGC", \
    "-Xss256k", \
    "-XX:MaxMetaspaceSize=128m", \
    "-XX:CompressedClassSpaceSize=64m", \
    "-Djava.security.egd=file:/dev/./urandom", \
    "-XX:+UseStringDeduplication", \
    "-XX:+DisableExplicitGC", \
    "-XX:SoftRefLRUPolicyMSPerMB=0", \
    "-Dspring.config.location=classpath:/application.yml", \
    "-jar", \
    "app.jar"]
