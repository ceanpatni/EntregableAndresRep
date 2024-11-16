# Use official OpenJDK 17 image as the base image
FROM eclipse-temurin:17-jdk-focal

# Set working directory
WORKDIR /app

# Copy gradle files first to leverage Docker cache
COPY gradlew .
COPY gradle gradle
COPY build.gradle .
COPY settings.gradle .

# Make gradlew executable
RUN chmod +x gradlew

# Copy the source code
COPY src src

# Copy application.properties
COPY src/main/resources/application.properties src/main/resources/application.properties

# Build the application
RUN ./gradlew build -x test

# Expose the port the app runs on
EXPOSE 8080

# Run the jar file
CMD ["java", "-jar", "build/libs/EntregableAndres-0.0.1-SNAPSHOT.jar"]