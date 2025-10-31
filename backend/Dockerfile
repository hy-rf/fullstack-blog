FROM maven:3.9.6-eclipse-temurin-21-alpine AS build

WORKDIR /app

COPY . .

RUN mvn clean package

FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=build /app/api/target/*.jar app.jar

# Run the Spring Boot application
ENTRYPOINT ["java", "-jar", "app.jar", "--spring.profiles.active=sandbox", "-pl", "api"]