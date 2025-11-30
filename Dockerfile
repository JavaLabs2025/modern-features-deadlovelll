FROM eclipse-temurin:21-jdk AS build

WORKDIR /app

COPY build.gradle.kts settings.gradle.kts gradlew ./
COPY gradle ./gradle

RUN chmod +x gradlew

RUN ./gradlew build -x test --dry-run

COPY src ./src
RUN ./gradlew clean fatJar -x test

FROM eclipse-temurin:21-jre

WORKDIR /app

COPY --from=build /app/build/libs/*-all.jar app.jar

EXPOSE 8080

CMD ["java", "--enable-preview", "-jar", "app.jar"]