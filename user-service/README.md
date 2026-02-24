# User Service

Microservice for user management and authentication in the FlexJob platform. Handles registration, login, JWT token generation, and user profiles.

## Architecture

Hexagonal Architecture (Ports & Adapters) with CQRS pattern.

```
com.flexjob.user
├── domain/                     # Core business logic
│   ├── model/                  # Domain entities (User, UserProfile, UserType)
│   ├── vo/                     # Value Objects (UserId, Email)
│   ├── service/                # Domain services
│   └── exception/              # Domain exceptions
├── application/                # Use cases
│   ├── port/
│   │   ├── input/              # Input ports (use case interfaces)
│   │   └── output/             # Output ports (repository/event interfaces)
│   ├── dto/
│   │   ├── command/            # Input DTOs (RegisterUserCommand, LoginCommand)
│   │   └── response/           # Output DTOs (UserResponse, LoginResponse, ProfileResponse)
│   └── service/                # Use case implementations + JWT token generator
├── infrastructure/             # External adapters
│   ├── adapter/
│   │   ├── input/rest/         # REST controllers, request DTOs, mappers
│   │   └── output/
│   │       ├── persistence/    # JPA entities, repositories, mappers
│   │       ├── messaging/      # Kafka event publisher
│   │       └── security/       # JWT token provider
│   └── config/                 # Security, JWT filter, OpenAPI, Bean config
```

## API Endpoints

### Authentication
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/v1/auth/register` | Register a new user |
| POST | `/api/v1/auth/login` | Login and get JWT token |

### Users (authenticated)
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/v1/users/me` | Get current user profile |
| GET | `/api/v1/users/{id}` | Get user by ID |

## Events

Publishes `UserRegisteredEvent` to Kafka on user registration.

## Database

PostgreSQL (`flexjob_user`) with Liquibase migrations.

Tables: `users`, `user_profiles`
