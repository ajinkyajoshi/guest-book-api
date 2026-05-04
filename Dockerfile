FROM maven:3.9-eclipse-temurin-17 AS build
WORKDIR /app
COPY pom.xml .
RUN mvn dependency:go-offline -B
COPY src ./src
RUN mvn package -DskipTests -B

FROM guestbook-base:latest
USER root
RUN apk add --no-cache openjdk17-jre
USER appuser
ARG GIT_COMMIT=unknown
ARG GIT_BRANCH=unknown
ARG BUILD_TIME=unknown
ENV GIT_COMMIT=${GIT_COMMIT} \
    GIT_BRANCH=${GIT_BRANCH} \
    BUILD_TIME=${BUILD_TIME} \
    SERVICE_NAME=guest-book-api \
    SERVICE_VERSION=1.0.0 \
    APP_CMD="java -jar /app/app.jar"
COPY --from=build /app/target/*.jar /app/app.jar
EXPOSE 8080
