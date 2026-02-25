# Use a slightly newer Maven image for better stability
FROM maven:3.9.6-eclipse-temurin-17 AS build
WORKDIR /app

# 1. Copy only the pom.xml (the "blueprint")
COPY pom.xml .

# 2. Use 'resolve' instead of 'go-offline' 
# -Dsilent keeps the logs clean so you can see if it actually hangs
RUN mvn dependency:resolve -B -Dsilent

# 3. Copy source and build
COPY src ./src
RUN mvn clean package -DskipTests -B

# Stage 2: Run (using Alpine for a much smaller image size)
FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

# Copy the jar from the build stage
COPY --from=build /app/target/*.jar ar7.jar

EXPOSE 8080

# Use non-root user for security (Optional but recommended)
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring

ENTRYPOINT ["java", "-jar", "ar7.jar"]