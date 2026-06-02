# Use a production-ready Java 17 image
FROM eclipse-temurin:17-jre-jammy

# Set the working directory inside the container
WORKDIR /app

# Copy the compiled JAR file from your local target/build folder
# (Change 'target/*.jar' to 'build/libs/*.jar' if using Gradle)
COPY target/*.jar app.jar

# Run the Java application
ENTRYPOINT ["java", "-jar", "app.jar"]
# Use a production-ready Java 17 image
FROM eclipse-temurin:17-jre-jammy

# Set the working directory inside the container
WORKDIR /app

# Copy the compiled JAR file from your local target/build folder
# (Change 'target/*.jar' to 'build/libs/*.jar' if using Gradle)
COPY target/*.jar app.jar

# Run the Java application
ENTRYPOINT ["java", "-jar", "app.jar"]
