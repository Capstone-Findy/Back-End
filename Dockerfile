# Use JDK 17 runtime
FROM eclipse-temurin:17-jdk-alpine

# Set working directory
WORKDIR /app

# Copy the built jar
COPY build/libs/*.jar app.jar

# Run the jar
ENTRYPOINT ["java", "-jar", "app.jar"]
