# Multi-Stage Dockerfile
FROM eclipse-temurin:21-jdk-alpine AS builder
WORKDIR /app
COPY .mvn/wrapper .mvn/wrapper
COPY mvnw .
COPY pom.xml .
COPY common/pom.xml common/pom.xml
COPY discovery-service/pom.xml discovery-service/pom.xml
COPY api-gateway/pom.xml api-gateway/pom.xml
COPY user-service/pom.xml user-service/pom.xml
COPY job-service/pom.xml job-service/pom.xml
COPY engagement-service/pom.xml engagement-service/pom.xml
COPY payment-service/pom.xml payment-service/pom.xml
COPY notification-service/pom.xml notification-service/pom.xml
COPY common/src common/src
COPY discovery-service/src discovery-service/src
RUN ./mvnw clean package -pl discovery-service -am -DskipTests
FROM eclipse-temurin:21-jre-alpine
WORKDIR /app
COPY --from=builder /app/discovery-service/target/*.jar app.jar
RUN addgroup -S spring && adduser -S spring -G spring
USER spring:spring
HEALTHCHECK --interval=30s --timeout=3s --start-period=60s --retries=3 CMD wget --no-verbose --tries=1 --spider http://localhost:8080/actuator/health || exit 1
ENTRYPOINT ["java", "-jar", "app.jar"]
