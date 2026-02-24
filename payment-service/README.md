# Payment Service

Microservice for payment processing and transaction management in the FlexJob platform.

## Architecture

Hexagonal Architecture (Ports & Adapters) with CQRS pattern.

```
com.flexjob.payment
├── domain/                     # Core business logic
│   ├── model/                  # Domain entities (Payment, Transaction)
│   ├── vo/                     # Value Objects (PaymentId, Money)
│   ├── enums/                  # PaymentStatus enum
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
│   │   ├── input/
│   │   │   ├── rest/           # REST controller
│   │   │   └── messaging/      # Kafka listener (booking-completed)
│   │   └── output/
│   │       ├── persistence/    # JPA entity, repository, mapper
│   │       └── messaging/      # Kafka event publisher
│   └── config/                 # Spring configuration
└── enums/                      # TransactionType enum
```

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/payments` | Create a payment |
| GET | `/api/payments/{id}` | Get payment by ID |
| GET | `/api/bookings/{bookingId}/payments` | Get payments for a booking |
| POST | `/api/payments/{id}/refund` | Refund a payment |

## Kafka

- **Consumer**: `booking-completed` - Automatically creates payment when a booking is completed
- **Producer**: `payment-completed` - Publishes event after payment processing

## Database

PostgreSQL (`flexjob_payment`) with Liquibase migrations.

Table: `payments`
