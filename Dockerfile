# Build the Application
FROM maven:3.8.5-openjdk-17 AS build
WORKDIR /app

# Copy all project files into the image
COPY . .

# Build the JAR file (skipping tests to avoid environment errors)
RUN mvn clean package -DskipTests
#Run the Application

FROM openjdk:17.0.1-jdk-slim
WORKDIR /app

# We rename it to 'app.jar' so we don't worry about version numbers
COPY --from=build /app/target/*.jar app.jar

# Expose port 8080 so Render can access it
EXPOSE 8080

# Command to start the application
ENTRYPOINT ["java", "-jar", "app.jar"]