<<<<<<< HEAD
# User Service

# Use lightweight OpenJDK 17 base image
FROM openjdk:17-alpine
# Set working directory inside container
WORKDIR /app

# Copy the built JAR file for Service Registry.
COPY target/*.jar app.jar

# --- NEW STEP: Copy the secret .env file into the container ---
# This file MUST be present in the Docker build context when 'docker build' runs.
# Expose port 7005 (internal container port)
EXPOSE 7005

# Run the jar file, setting the server port to 7005
CMD ["java", "-jar", "app.jar", "--server.port=7005"]
=======
# User Service

# Use lightweight OpenJDK 17 base image
FROM openjdk:17-alpine
# Set working directory inside container
WORKDIR /app

# Copy the built JAR file for Service Registry.
COPY target/*.jar app.jar

# --- NEW STEP: Copy the secret .env file into the container ---
# This file MUST be present in the Docker build context when 'docker build' runs.
# Expose port 7005 (internal container port)
EXPOSE 7005

# Run the jar file, setting the server port to 7005
CMD ["java", "-jar", "app.jar", "--server.port=7005"]
>>>>>>> cb7b8de00c0cc80532905ecb6387a299f12b6687
