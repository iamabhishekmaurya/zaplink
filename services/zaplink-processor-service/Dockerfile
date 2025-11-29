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
# Replace 'zaplink-processor-service-0.0.1-SNAPSHOT.jar' with your actual JAR name
# ---------------------------------------------------
COPY build/libs/zaplink-processor-service-0.0.1-SNAPSHOT.jar app.jar

# ---------------------------------------------------
# STEP 4: Expose the zaplink-processor-service port (default 8000)
# ---------------------------------------------------
EXPOSE 8082

# ---------------------------------------------------
# STEP 5: Run the zaplink-processor-service
# ---------------------------------------------------
ENTRYPOINT ["java", "-jar", "app.jar"]
