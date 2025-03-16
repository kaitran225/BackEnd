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
ENTRYPOINT ["java", \
    "-XX:+UseContainerSupport",\
    "-XX:+AlwaysActAsServerClassMachine",\
    "-Xmx128m",\
    "-Xms128m",\
    "-Xss256k",\
    "-XX:MaxRAMPercentage=75.0",\
    "-XX:+UseSerialGC",\
    "-XX:MaxMetaspaceSize=128m",\
    "-XX:CompressedClassSpaceSize=64m",\
    "-XX:+UseStringDeduplication",\
    "-XX:+DisableExplicitGC",\
    "-XX:SoftRefLRUPolicyMSPerMB=0",\
    "-jar", \
    "app.jar"]
