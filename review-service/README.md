# Review Service

Microservice for managing reviews and ratings in the FlexJob platform.

## Architecture

Hexagonal Architecture (Ports & Adapters) with CQRS pattern.

```
com.flexjob.review
├── domain/                     # Core business logic
│   ├── model/                  # Domain entity (Review)
│   ├── vo/                     # Value Objects (ReviewId, Rating, ReviewComment)
│   ├── service/                # Domain services
│   └── exception/              # Domain exceptions
├── application/                # Use cases
│   ├── port/
│   │   ├── input/              # Input ports (use case interfaces)
│   │   └── output/             # Output ports (repository/event interfaces)
│   ├── dto/
│   │   ├── command/            # Input DTOs
│   │   └── response/           # Output DTOs
│   └── service/                # Use case implementations
├── infrastructure/             # External adapters
│   ├── adapter/
│   │   ├── input/rest/         # REST controller, request DTOs
│   │   └── output/
│   │       ├── persistence/    # JPA entity, repository, mapper
│   │       └── messaging/      # Kafka event publisher
│   └── config/                 # Spring configuration
```

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/reviews` | Create a review (Header: `X-User-Id`) |
| GET | `/api/reviews/{id}` | Get review by ID |
| GET | `/api/bookings/{bookingId}/reviews` | Get reviews for a booking |
| GET | `/api/users/{userId}/reviews` | Get reviews for a user |
| GET | `/api/users/{userId}/average-rating` | Get average rating |

## Events

Publishes `ReviewCreatedEvent` to Kafka topic `review-created` on review creation.

## Database

PostgreSQL (`flexjob_review`) with Liquibase migrations.

Table: `reviews`
