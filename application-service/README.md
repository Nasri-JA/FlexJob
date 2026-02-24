# Application Service

Manages job applications, status transitions and messaging between employees and employers.

## Architecture

Clean Architecture (Hexagonal / Ports & Adapters):

```
domain/                     # Domain Layer
  model/                    # Entities (Application, ApplicationMessage)
  vo/                       # Value Objects (ApplicationId, MessageText)
  service/                  # Domain Service
  exception/                # Domain Exceptions

application/                # Application Layer
  port/input/               # Input Ports (Use Cases)
  port/output/              # Output Ports (Repository, Event, REST)
  service/                  # Use Case Implementations
  dto/                      # Commands & Responses

infrastructure/             # Infrastructure Layer
  adapter/input/rest/       # REST Controller + DTOs
  adapter/output/persistence/ # JPA Entities, Repository, Mapper
  adapter/output/rest/      # Job Service REST Adapter
  adapter/output/messaging/ # Kafka Event Publisher
  config/                   # Spring Configuration
```

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/applications | Create application |
| GET | /api/applications/{id} | Get by ID |
| GET | /api/applications/job/{jobId} | Get by job |
| GET | /api/applications/employee/{id} | Get by employee |
| GET | /api/applications/employer/{id} | Get by employer |
| PUT | /api/applications/{id}/status | Update status |
| POST | /api/applications/{id}/messages | Send message |

Authentication via `X-User-Id` header (set by API Gateway).

## Status Transitions

```
PENDING --> ACCEPTED (final)
        --> REJECTED (final)
        --> WITHDRAWN (final)
```

## Kafka Events

| Topic | Event | Trigger |
|-------|-------|---------|
| application-created | ApplicationCreatedEvent | New application |
| application-status-changed | ApplicationStatusChangedEvent | Status update |

## Database

PostgreSQL `flexjob_application` with Liquibase migrations.

Tables: `applications`, `application_messages`

## Dependencies

- **common** module (shared DTOs, events, exceptions)
- **job-service** (job validation via REST)
- **discovery-service** (Eureka registration)
