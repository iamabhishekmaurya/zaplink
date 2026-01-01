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
# Replace 'zaplink-core-service-0.0.1-SNAPSHOT.jar' with your actual JAR name
# ---------------------------------------------------
COPY build/libs/zaplink-core-service-0.0.1-SNAPSHOT.jar app.jar

# ---------------------------------------------------
# STEP 4: Expose the zaplink-core-service port (default 8000)
# ---------------------------------------------------
EXPOSE 8081

# ---------------------------------------------------
# STEP 5: Run the zaplink-core-service
# ---------------------------------------------------
ENTRYPOINT ["java", "-jar", "app.jar"]
