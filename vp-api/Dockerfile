FROM maven:latest AS build
WORKDIR /app
COPY pom.xml ./
RUN mvn dependency:go-offline
COPY . ./
RUN mvn clean package

FROM openjdk:17-ea-jdk-oracle
COPY --from=build /app/target/*.jar /app.jar
CMD ["java", "-jar", "-Dspring.profiles.active=prod", "/app.jar"]
