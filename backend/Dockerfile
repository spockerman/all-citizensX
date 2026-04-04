# Build context: backend/ (this directory)
FROM maven:3.9.9-eclipse-temurin-17-alpine AS build
WORKDIR /build

COPY pom.xml .
COPY domain/pom.xml domain/
COPY application/pom.xml application/
COPY infrastructure/pom.xml infrastructure/

COPY domain/src domain/src
COPY application/src application/src
COPY infrastructure/src infrastructure/src

RUN mvn -B -q -pl infrastructure -am package -DskipTests \
    && mv infrastructure/target/infrastructure-*.jar /build/app.jar

FROM eclipse-temurin:17-jre-alpine
WORKDIR /app

RUN apk add --no-cache curl

COPY --from=build /build/app.jar /app/app.jar

EXPOSE 9080

# JVM tuning via JAVA_TOOL_OPTIONS or compose `command: [...]`
ENTRYPOINT ["java", "-jar", "/app/app.jar"]
