# Use an official OpenJDK runtime as a parent image
FROM eclipse-temurin:17-jdk-jammy

# Set the working directory inside the container
WORKDIR /app

# Copy the compiled JAR file into the container at /app
# The JAR file is created by Maven in the 'target' directory
COPY target/*.jar app.jar

# Make port 8080 available to the world outside this container
# Back4App will use this port
EXPOSE 8080

# Run the JAR file when the container launches
ENTRYPOINT ["java", "-jar", "app.jar"]