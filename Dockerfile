# Сборка jar
FROM gradle:8.7-jdk21-alpine AS build
WORKDIR /app
COPY . .
RUN ./gradlew build -x test

# Runtime слой
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8088
ENTRYPOINT ["java", "-jar", "app.jar"]

