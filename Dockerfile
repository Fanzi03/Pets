# Сборка jar
FROM gradle:8.7-jdk21-alpine AS build
WORKDIR /app

COPY build.gradle settings.gradle ./
COPY gradle/ gradle/
COPY gradlew ./

RUN chmod +x ./gradlew 

RUN ./gradlew dependencies --no-daemon 

COPY src/ src/

RUN ./gradlew build -x test --no-daemon

# Runtime слой
FROM eclipse-temurin:21-jre
WORKDIR /app
COPY --from=build /app/build/libs/*.jar app.jar
EXPOSE 8088
ENTRYPOINT ["java", "-jar", "app.jar"]

