# FlexJob

Microservices-based job platform where employers post short-term jobs and employees browse and apply for gigs. Built with Spring Boot 3.x and Spring Cloud.

## Services

### Infrastructure
| Service | Port | Description |
|---------|------|-------------|
| Discovery Service | 8761 | Eureka Server for service discovery |
| Config Service | 8888 | Centralized configuration |
| API Gateway | 8080 | Single entry point with JWT authentication |

### Business
| Service | Port | Description |
|---------|------|-------------|
| User Service | 8081 | Registration, authentication, profiles |
| Job Service | 8082 | Job postings, search, categories |
| Application Service | 8083 | Job applications |
| Booking Service | 8084 | Bookings and time tracking |
| Payment Service | 8085 | Payment processing |
| Notification Service | 8086 | In-app notifications via Kafka events |
| Review Service | 8087 | Post-job reviews and ratings |

## Tech Stack

- **Java 17** / **Spring Boot 3.2.2** / **Maven** (multi-module)
- **PostgreSQL** (one database per service) with **Liquibase** migrations
- **Apache Kafka** for async event-driven communication
- **Spring Cloud** (Eureka, Config, Gateway)
- **Spring Security + JWT** for authentication
- **Docker Compose** for infrastructure

## Architecture

Each business service follows **Hexagonal Architecture** (Ports & Adapters) with CQRS pattern. See individual service READMEs for details.

Communication:
- **Synchronous**: REST APIs via API Gateway
- **Asynchronous**: Kafka events between services

## Quick Start

```bash
# Start infrastructure (PostgreSQL, Kafka, Redis)
docker-compose up -d

# Build all services
mvn clean install

# Start services in order:
# 1. discovery-service  2. config-service  3. api-gateway  4. business services
```

Eureka Dashboard: http://localhost:8761

## Project Structure

```
FlexJob/
├── common/                 # Shared DTOs, events, exceptions
├── discovery-service/      # Eureka Server
├── config-service/         # Spring Cloud Config
├── api-gateway/            # Spring Cloud Gateway
├── user-service/           # User management & auth
├── job-service/            # Job management
├── application-service/    # Application management
├── booking-service/        # Booking & time tracking
├── payment-service/        # Payments
├── notification-service/   # Notifications
├── review-service/         # Reviews & ratings
├── docker-compose.yml
└── pom.xml
```

## Kafka Topics

| Topic | Producer | Consumer |
|-------|----------|----------|
| `job-created` | Job Service | Notification Service |
| `application-created` | Application Service | Notification Service |
| `application-status-changed` | Application Service | Notification Service |
| `booking-completed` | Booking Service | Payment, Notification Service |
| `payment-completed` | Payment Service | Notification Service |
| `review-created` | Review Service | - |
