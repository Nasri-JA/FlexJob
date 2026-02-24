# Job Service

Microservice for managing job postings, categories, and requirements in the FlexJob platform.

## Architecture

Hexagonal Architecture (Ports & Adapters) with CQRS pattern.

```
com.flexjob.job
├── domain/                     # Core business logic
│   ├── model/                  # Domain entities (Job, JobCategory, JobRequirement)
│   ├── vo/                     # Value Objects (JobId, JobTitle, HourlyRate, ...)
│   ├── service/                # Domain services
│   └── exception/              # Domain exceptions
├── application/                # Use cases
│   ├── port/
│   │   ├── input/              # Input ports (use case interfaces)
│   │   └── output/             # Output ports (repository/event interfaces)
│   ├── dto/
│   │   ├── command/            # Input DTOs (CreateJobCommand, SearchJobsCommand, ...)
│   │   └── response/           # Output DTOs (JobResponse, CategoryResponse)
│   └── service/                # Use case implementations
├── infrastructure/             # External adapters
│   ├── adapter/
│   │   ├── input/rest/         # REST controllers, request DTOs, mappers
│   │   └── output/
│   │       ├── persistence/    # JPA entities, repositories, mappers
│   │       └── messaging/      # Kafka event publisher
│   └── config/                 # Spring configuration
├── config/                     # Kafka config
└── enums/                      # JobStatus enum
```

## API Endpoints

### Jobs
| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | `/api/jobs` | Create a job (Header: `X-User-Id`) |
| GET | `/api/jobs` | Get all jobs (optional: `?employerId=`) |
| GET | `/api/jobs/{id}` | Get job by ID |
| PUT | `/api/jobs/{id}` | Update a job |
| DELETE | `/api/jobs/{id}` | Cancel a job |
| POST | `/api/jobs/search` | Search with filters |

### Categories
| Method | Endpoint | Description |
|--------|----------|-------------|
| GET | `/api/categories` | Get all categories |
| GET | `/api/categories/{id}` | Get category by ID |
| POST | `/api/categories` | Create a category |

## Events

Publishes `JobCreatedEvent` to Kafka topic `job-created` on job creation.

## Database

PostgreSQL (`flexjob_job`) with Liquibase migrations.

Tables: `jobs`, `job_categories`, `job_requirements`
