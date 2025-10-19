# ---------------------------------------------------
# STEP 1: Use official OpenJDK 17 base image
# ---------------------------------------------------
FROM eclipse-temurin:17-jdk-alpine

# ---------------------------------------------------
# STEP 2: Set a working directory inside the container
# ---------------------------------------------------
WORKDIR /app

# ---------------------------------------------------
# STEP 3: Copy the built JAR file into the container
# Replace 'api-gateway-service-0.0.1-SNAPSHOT.jar' with your actual JAR name
# ---------------------------------------------------
COPY build/libs/api-gateway-service-0.0.1-SNAPSHOT.jar app.jar

# ---------------------------------------------------
# STEP 4: Expose the API Gateway service port (default 8000)
# ---------------------------------------------------
EXPOSE 8000

# ---------------------------------------------------
# STEP 5: Run the API Gateway service
# ---------------------------------------------------
ENTRYPOINT ["java", "-jar", "app.jar"]
