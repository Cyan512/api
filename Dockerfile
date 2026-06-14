FROM maven:3.9.11-eclipse-temurin-21 AS build

WORKDIR /app

COPY . .

RUN mvn clean package

FROM eclipse-temurin:21-jdk-jammy

COPY --from=build /app/target/*.jar app_api.jar

EXPOSE 8081

ENTRYPOINT ["java","-jar","/app_api.jar"]