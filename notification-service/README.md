# Notification Service

Microservice for managing in-app notifications in the FlexJob platform. Listens to events from other services via Kafka and creates notifications for users.

## Architecture

Hexagonal Architecture (Ports & Adapters) with CQRS pattern.

```
com.flexjob.notification
├── domain/                     # Core business logic
│   ├── model/                  # Domain entity (Notification)
│   ├── vo/                     # Value Objects (NotificationId, NotificationMessage, ReadStatus)
│   ├── enums/                  # NotificationType enum
│   ├── service/                # Domain services
│   └── exception/              # Domain exceptions
├── application/                # Use cases
│   ├── port/
│   │   ├── input/              # Input ports (use case interfaces)
│   │   └── output/             # Output ports (repository interfaces)
│   ├── dto/
│   │   ├── command/            # Input DTOs
│   │   └── response/           # Output DTOs
│   └── service/                # Use case implementations
├── infrastructure/             # External adapters
│   ├── adapter/
│   │   ├── input/
│   │   │   ├── rest/           # REST controller, request DTOs
│   │   │   └── messaging/      # Kafka listeners
│   │   └── output/
│   │       └── persistence/    # JPA entity, repository, mapper
│   └── config/                 # Spring configuration
└── config/                     # Kafka config
```

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/notifications` | Get user notifications (Header: `X-User-Id`) |
| GET | `/api/notifications/unread` | Get unread notifications |
| GET | `/api/notifications/unread/count` | Get unread count |
| PUT | `/api/notifications/{id}/read` | Mark as read |
| DELETE | `/api/notifications/{id}` | Delete notification |

## Kafka Topics (Consumer)

| Topic | Event |
|-------|-------|
| `job-created` | Job creation |
| `application-created` | New application |
| `application-status-changed` | Application accepted/rejected |
| `booking-completed` | Booking completion |
| `payment-completed` | Payment processed |

## Database

PostgreSQL (`flexjob_notification`) with Liquibase migrations.

Table: `notifications`
