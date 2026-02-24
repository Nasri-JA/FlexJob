# Booking Service

Manages job bookings and time tracking for the FlexJob platform.

## Architecture

Clean Architecture (Hexagonal / Ports & Adapters):

```
domain/                       # Domain Layer
  model/                      # Entities (Booking, TimeTracking)
  vo/                         # Value Objects (BookingId, DateRange, WorkingHours, ...)
  enums/                      # BookingStatus
  service/                    # Domain Service
  exception/                  # Domain Exceptions

application/                  # Application Layer
  port/input/                 # Input Ports (Use Cases)
  port/output/                # Output Ports (Repository, Event)
  service/                    # Use Case Implementations
  dto/                        # Commands & Responses

infrastructure/               # Infrastructure Layer
  adapter/input/rest/         # REST Controller
  adapter/input/messaging/    # Kafka Listener
  adapter/output/persistence/ # JPA Entities, Repository, Mapper
  adapter/output/messaging/   # Kafka Event Publisher
  config/                     # Spring Configuration
```

## API Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/bookings | Create booking |
| GET | /api/bookings/{id} | Get by ID |
| GET | /api/bookings/employee/{id} | Get by employee |
| GET | /api/bookings/employer/{id} | Get by employer |
| POST | /api/bookings/{id}/complete | Complete booking |
| POST | /api/bookings/{id}/time-tracking/start | Start time tracking |
| POST | /api/bookings/{id}/time-tracking/end | End time tracking |

## Status Transitions

```
SCHEDULED --> IN_PROGRESS --> COMPLETED (final)
                          --> CANCELLED (final)
          --> CANCELLED (final)
```

## Kafka Events

| Direction | Topic | Description |
|-----------|-------|-------------|
| Consumes | application-status-changed | Auto-creates booking on accepted application |
| Produces | booking-completed | Triggers payment processing |

## Database

PostgreSQL `flexjob_booking` with Liquibase migrations.

Tables: `bookings`, `time_trackings`

## Dependencies

- **common** module (shared DTOs, events, exceptions)
- **discovery-service** (Eureka registration)
