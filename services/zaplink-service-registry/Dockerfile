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
# Replace 'eureka-server-0.0.1-SNAPSHOT.jar' with your actual JAR name
# ---------------------------------------------------
COPY build/libs/zaplink-service-registry-0.0.1-SNAPSHOT.jar app.jar

# ---------------------------------------------------
# STEP 4: Expose the Eureka server port (default 8761)
# ---------------------------------------------------
EXPOSE 8761

# ---------------------------------------------------
# STEP 5: Run the Eureka server
# ---------------------------------------------------
ENTRYPOINT ["java", "-jar", "app.jar"]
