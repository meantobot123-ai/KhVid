# --- Stage 1: Build the application ---
# Use a Maven image to build the project
FROM maven:3.8.5-openjdk-17 AS build

# Set the working directory
WORKDIR /app

# Copy the project files into the container
COPY . .

# Run the Maven package command to build the .jar file
RUN mvn clean package -DskipTests


# --- Stage 2: Create the final, runnable image ---
# Use a lightweight Java runtime image
FROM eclipse-temurin:17-jre-jammy

# Set the working directory
WORKDIR /app

# Copy ONLY the built .jar file from the 'build' stage
COPY --from=build /app/target/*.jar app.jar

# Expose the port the app runs on
EXPOSE 8080

# The command to run the application
ENTRYPOINT ["java", "-jar", "app.jar"]