# Use an official OpenJDK runtime as the base image
FROM openjdk:17-alpine

# Expose the port that Spring Boot will run on
EXPOSE 9000

# Add the application's JAR file to the container
# Assuming the JAR file will be in the 'target' directory
ADD target/*.jar online_food_ordering.jar

# Run the JAR file
ENTRYPOINT ["java", "-jar", "/online_food_ordering.jar"]
