# Stage 1: Build the Grails application
FROM gradle:7.6.0-jdk11 AS build-stage
WORKDIR /opt/grails-app
COPY . .
RUN gradle bootJar --no-daemon

# Stage 2: Create a lightweight image to run the Grails application
FROM eclipse-temurin:11-jre-jammy
WORKDIR /opt/grails-app
COPY --from=build-stage /opt/grails-app/build/libs/*.jar ./app.jar
ENTRYPOINT ["java", "-jar", "app.jar"]
EXPOSE 8080
