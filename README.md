# Zaplink — URL Shortening Microservices (Spring Boot)

Zaplink is a microservices-based URL shortening platform built with Spring Boot, Kafka, Eureka Service Discovery, and a combination of Redis and HTTP APIs. The system is designed for scalability, fault tolerance, and separation of concerns. This repository contains a multi-module Gradle project with multiple services that communicate via REST and Kafka.

---

## Table of Contents

- [High-Level Overview](#high-level-overview)
- [Architecture](#architecture)
  - [Services](#services)
  - [Data Flow](#data-flow)
  - [Key Technologies](#key-technologies)
- [Repository Structure](#repository-structure)
- [Service Details](#service-details)
  - [API Gateway Service](#api-gateway-service)
  - [Producer Service](#producer-service)
  - [Consumer Service](#consumer-service)
  - [Manager Service](#manager-service)
  - [Service Registry](#service-registry)
- [Local Development](#local-development)
  - [Prerequisites](#prerequisites)
  - [Running All Services](#running-all-services)
  - [Running Kafka Locally](#running-kafka-locally)
  - [Build and Test](#build-and-test)
- [Configuration](#configuration)
- [API Reference](#api-reference)
  - [Producer API](#producer-api)
  - [Consumer API](#consumer-api)
  - [Manager API](#manager-api)
  - [Gateway Endpoints](#gateway-endpoints)
- [Messaging (Kafka)](#messaging-kafka)
- [Data Model](#data-model)
- [Short URL Generation](#short-url-generation)
- [Error Handling](#error-handling)
- [Logging & Observability](#logging--observability)
- [Testing Strategy](#testing-strategy)
- [Docker & Deployment](#docker--deployment)
- [Kubernetes (Optional)](#kubernetes-optional)
- [Security](#security)
- [Conventions & Guidelines](#conventions--guidelines)
- [Troubleshooting](#troubleshooting)

---

## High-Level Overview

Zaplink provides a short URL for a given long URL. The request is accepted by the Producer service, which publishes a message to Kafka. The Consumer service processes the message and coordinates with the Manager service to persist/cache mappings and return/serve the shortened URLs. The API Gateway routes requests to underlying services and centralizes external API access.


## Architecture

### Services

- api-gateway-service: Routes external requests to internal services (edge service).
- zaplink-producer-service: Accepts requests to create short URLs and emits Kafka events.
- zaplink-consumer-service: Listens to Kafka topics and processes short URL creation events.
- zaplink-manager-service: Business logic for URL storage/lookup, Redis integration.
- zaplink-service-registry: Eureka service registry for service discovery.

### Data Flow

1. Client POSTs a long URL to Producer.
2. Producer validates/transforms and publishes a Kafka message.
3. Consumer listens to Kafka topic and triggers Manager to generate/store short URL mapping.
4. Manager stores mapping (e.g., in Redis) and responds with details.
5. API Gateway can expose unified endpoints or redirect traffic accordingly.

### Key Technologies

- Java 17+ (recommended)
- Spring Boot (Web, Validation, Kafka, Data/Redis as applicable)
- Spring Cloud Netflix (Eureka)
- Kafka (local via Docker Compose)
- Redis (managed by Manager service; local via Docker Compose)
- Gradle (multi-module build)
- JUnit 5, Mockito, Spring Test (MockMvc)


## Repository Structure

```
zaplink/
├─ services/
│  ├─ api-gateway-service/
│  ├─ zaplink-producer-service/
│  ├─ zaplink-consumer-service/
│  ├─ zaplink-manager-service/
│  └─ zaplink-service-registry/
├─ tools/
│  └─ kafka/
│     └─ docker-compose.yml
├─ docker-compose.yml
├─ build-all-services.bat
├─ Tiltfile
└─ README.md
```

Each service is a standalone Spring Boot project with its own build.gradle, Dockerfile, and application.yml.


## Service Details

### API Gateway Service

- Path: `services/api-gateway-service`
- Purpose: Edge routing to downstream services. Central point of entry.
- Entrypoint: `ApiGatewayServiceApplication.java`
- Example Controller: `ApiGatewayController.java` (can forward or provide simple status endpoint)

### Producer Service

- Path: `services/zaplink-producer-service`
- Purpose: Accept short URL creation requests and publish events to Kafka.
- Controller: `ZaplinkProducerController` (POST `/producers/short/url`)
- Providers/Services: `UrlServiceProvider`, `KafkaService`, `UrlProducerService`
- Utilities: `SnowflakeShortUrlKeyUtil`, `StringUtil`, `CommonUtil`
- DTOs: `ShortUrlRequest`, `ShortUrlResponse`
- Tests: `ZaplinkProducerControllerTest` (WebMvcTest)

### Consumer Service

- Path: `services/zaplink-consumer-service`
- Purpose: Consume Kafka events for short URL creation.
- Listener: `service.impl.KafkaListeners`
- DTOs: `BaseRequest`, `ShortUrlConsumerRequest`
- Controller: `ZaplinkConsumerController`
- Entrypoint: `ZaplinkConsumerServiceApplication.java`

### Manager Service

- Path: `services/zaplink-manager-service`
- Purpose: Core business logic for creating, storing, and resolving short URLs.
- Controller: `UrlController`
- Services: `UrlProvider` (interface), `UrlProviderService` (impl), `RedisService`
- Config: `common.config.RedisConnection`
- Entrypoint: `ZaplinkManagerServiceApplication.java`

### Service Registry

- Path: `services/zaplink-service-registry`
- Purpose: Eureka service registry for discovery between services.
- Entrypoint: `ZaplinkServiceRegistryApplication.java`


## Local Development

### Prerequisites

- Java 17+
- Docker Desktop (for Kafka/Redis via compose)
- Git
- Windows (repo includes `.bat` scripts); Linux/Mac compatible with manual gradle commands

### Running All Services

Option A: Individual Gradle runs per service

- From each service folder (e.g., `services/zaplink-producer-service`):
  - Windows: `gradlew.bat bootRun`
  - Linux/Mac: `./gradlew bootRun`

Option B: Build all with a script (Windows)

- Run `build-all-services.bat` to build every module (modify as needed).

### Running Kafka Locally

Option A: Root Docker Compose (multi-service)

- Path: `docker-compose.yml`
- Includes service containers and dependencies (review file for specific services included).
- Run:
  - `docker compose up -d`

Option B: Kafka-only compose

- Path: `tools/kafka/docker-compose.yml`
- Run:
  - `cd tools/kafka`
  - `docker compose up -d`

Ensure environment variables and `application.yml` match the advertised host/ports.

### Build and Test

- Build a service:
  - Windows: `gradlew.bat build`
  - Linux/Mac: `./gradlew build`
- Run tests only:
  - Windows: `gradlew.bat test`
  - Linux/Mac: `./gradlew test`

Example (Producer Service):

```
cd services/zaplink-producer-service
# Windows
gradlew.bat clean test
# Linux/Mac
./gradlew clean test
```


## Configuration

- Each service has `src/main/resources/application.yml` for configuration.
- Common properties:
  - Server ports
  - Spring profiles
  - Kafka bootstrap servers, topics
  - Redis connection (host/port)
  - Eureka registry endpoints

Adjust local `.yml` files to your environment. Example configuration files are present in each service directory.


## API Reference

Note: API Gateway can proxy to downstream services. The following lists primary service-level endpoints.

### Producer API

- POST `/producers/short/url`
  - Request Body (JSON):
    - `longUrl`: string (required)
  - Response (JSON):
    - `url`: string (short URL)
  - Status Codes:
    - 200 OK on success
    - 400 Bad Request on invalid body
    - 415 Unsupported Media Type if `Content-Type` is not `application/json`
    - 5xx on service/internal errors

### Consumer API

- Typically internal (Kafka listeners). A controller `ZaplinkConsumerController` is present and can expose health or diagnostics endpoints if implemented.

### Manager API

- `UrlController` exposes endpoints for URL management and resolution (inspect the controller for exact mappings). Typical endpoints include:
  - POST to generate/store a mapping
  - GET to resolve a short code to long URL

### Gateway Endpoints

- API Gateway may expose a unified path (e.g., `/api/**`) routing to Producer/Manager/Consumer.
- Check `services/api-gateway-service/src/main/resources/application.yml` for route definitions.


## Messaging (Kafka)

- Producer publishes events/messages when short URL creation is requested.
- Consumer listens to a topic (defined in application.yml) and triggers processing.
- Topics, partitions, and replication factors are configured in Kafka compose and application properties.
- Ensure the Kafka bootstrap server address matches the Docker network or localhost depending on where services run.


## Data Model

- ShortUrlRequest (Producer): `{ longUrl: string }`
- ShortUrlResponse (Producer): `{ url: string }`
- ShortUrlConsumerRequest (Consumer): message structure consumed from Kafka for processing
- Manager persists mappings of `shortKey -> longUrl` (likely via Redis)


## Short URL Generation

- Utilities under `zaplink-producer-service`:
  - `SnowflakeShortUrlKeyUtil` and `SnowflakeShortKeyGenerator` suggest time-based unique ID generation
  - `StringUtil`, `CommonUtil` for normalization/validation
- Manager service coordinates final key assignment and storage, ensuring uniqueness and quick lookup.


## Error Handling

- Controller-level exceptions translate to HTTP 4xx/5xx responses
- Service exceptions during Kafka publish or Redis operations are surfaced to clients as 5xx unless handled
- Consider adding `@ControllerAdvice` for consistent error representations across services


## Logging & Observability

- Standard Spring Boot logging configured; levels can be tuned in `application.yml`
- Recommend adding:
  - Correlation IDs (e.g., via filter)
  - Structured logging (JSON) in container deployments
  - Metrics/Tracing (Micrometer, OpenTelemetry) for production


## Testing Strategy

- Unit tests: JUnit 5, Mockito, Spring MVC Test (MockMvc)
  - Example: `ZaplinkProducerControllerTest` uses `@WebMvcTest` and mocks `UrlServiceProvider`
- Integration tests (recommended):
  - With embedded Kafka for consumer/producer flows
  - Testcontainers for Kafka and Redis
- Contract tests between services (optional): Pact/REST Docs

Run tests per service as shown in Build and Test section.


## Docker & Deployment

### Local Docker Compose

- Root-compose to orchestrate services:

```
docker compose up -d
```

- Kafka-only (for development):

```
cd tools/kafka
docker compose up -d
```

### Building Images

From each service directory:

```
# Windows
gradlew.bat bootJar
# Linux/Mac
./gradlew bootJar

# Then build docker image
docker build -t <your-repo>/zaplink-<service>:<tag> .
```

### Environment Variables

- Configure via application.yml or environment variables at runtime
- Common vars:
  - `SPRING_PROFILES_ACTIVE`
  - `KAFKA_BOOTSTRAP_SERVERS`
  - `REDIS_HOST`, `REDIS_PORT`
  - `EUREKA_CLIENT_SERVICEURL_DEFAULTZONE`

### Deployment Targets

- Docker Compose (local, dev)
- Kubernetes (see below) or managed container platforms


## Kubernetes (Optional)

- The `k8s/` folder can be used to store manifests (if provided)
- Typical objects:
  - Deployments, Services, ConfigMaps/Secrets, Ingress
- External dependencies (Kafka, Redis) run as separate Helm charts or managed services


## Security

- Gateway should enforce authentication/authorization if exposed publicly
- Validate input URLs to prevent SSRF and injection attacks
- Use HTTPS (terminate at ingress/load balancer)
- Secrets and credentials should be stored in environment-specific secret stores (K8s Secrets, Vault, etc.)
- Consider rate limiting and abuse detection for public endpoints


## Conventions & Guidelines

- Java code style: standard Spring conventions
- Package-by-feature within each service
- DTOs under `dto` packages, services under `service` packages, controllers under `controller`
- Keep controllers thin; place business logic in services/providers
- Prefer constructor injection over field injection


## Troubleshooting

- Kafka connection errors:
  - Ensure Kafka is running and `bootstrap.servers` is correct
  - For Docker: match advertised listeners to host/container context
- Redis connection failures:
  - Check host/port in Manager service `application.yml`
  - Make sure container is reachable from Manager
- Service discovery issues:
  - Verify Eureka server is up and services are registered
  - Confirm `defaultZone` URL is correct
- Tests failing to run on Windows shells:
  - Use `gradlew.bat` directly in each service directory
  - Example: `cd services\zaplink-producer-service && gradlew.bat test`
- Port conflicts:
  - Adjust `server.port` in each service’s `application.yml`


<!-- ## License

This project is provided as-is for educational and demonstration purposes. Add your license of choice here.

--- -->

## Contributors

- Your Name <you@example.com>

Feel free to open issues or PRs to improve the system, tests, and documentation.
