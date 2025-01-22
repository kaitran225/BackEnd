# Use the official Maven image to build the application
FROM maven:3.9.9-openjdk-21 AS build

# Set the working directory
WORKDIR /app

# Copy the pom.xml and download dependencies
COPY pom.xml .
COPY src ./src
RUN mvn clean package -DskipTests

# Use the official OpenJDK image to run the application
FROM openjdk:21-jdk-slim

# Set the working directory
WORKDIR /app

# Copy the built jar file from the build stage
COPY --from=build /app/target/BackEnd-0.0.2-SNAPSHOT.war ./BackEnd.war

# Expose the application port
EXPOSE 8080

# Command to run the application
CMD ["java", "-jar", "BackEnd.war"]
