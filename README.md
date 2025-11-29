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
  - [Shortner Service](#shortner-service)
  - [Processor Service](#processor-service)
  - [Manager Service](#manager-service)
  - [Service Registry](#service-registry)
- [Local Development](#local-development)
  - [Prerequisites](#prerequisites)
  - [Running All Services](#running-all-services)
  - [Running Kafka Locally](#running-kafka-locally)
  - [Build and Test](#build-and-test)
- [Configuration](#configuration)
- [API Reference](#api-reference)
  - [Shortner API](#shortner-api)
  - [Processor API](#processor-api)
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

Zaplink provides a short URL for a given long URL. The request is accepted by the shortner service, which publishes a message to Kafka. The processor service processes the message and coordinates with the Manager service to persist/cache mappings and return/serve the shortened URLs. The API Gateway routes requests to underlying services and centralizes external API access.


## Architecture

### Services

- api-gateway-service: Routes external requests to internal services (edge service).
- zaplink-shortner-service: Accepts requests to create short URLs and emits Kafka events.
- zaplink-processor-service: Listens to Kafka topics and processes short URL creation events.
- zaplink-manager-service: Business logic for URL storage/lookup, Redis integration.
- zaplink-service-registry: Eureka service registry for service discovery.

### Data Flow

1. Client POSTs a long URL to shortner.
2. shortner validates/transforms and publishes a Kafka message.
3. processor listens to Kafka topic and triggers Manager to generate/store short URL mapping.
4. Manager stores mapping (e.g., in Redis) and responds with details.
5. API Gateway can expose unified endpoints or redirect traffic accordingly.

### Key Technologies

- Java 17+ (required)
- Spring Boot 3.5.6 (Web, Validation, Kafka, Data/Redis as applicable)
- Spring Cloud 2025.0.0 (Netflix Eureka)
- Kafka (local via Docker Compose)
- Redis (managed by Manager service; local via Docker Compose)
- Gradle (multi-module build)
- JUnit 5, Mockito, Spring Test (MockMvc)


## Repository Structure

```
zaplink/
├─ services/
│  ├─ api-gateway-service/
│  ├─ zaplink-shortner-service/
│  ├─ zaplink-processor-service/
│  ├─ zaplink-manager-service/
│  └─ zaplink-service-registry/
├─ k8s/
│  ├─ api-gateway-deployment.yaml
│  ├─ kafka-deployment.yaml
│  ├─ kafka-storage.yml
│  ├─ kafka-ui-deployment.yaml
│  ├─ kustomization.yml
│  ├─ redis-deployment.yaml
│  ├─ redis-storage.yml
│  ├─ zaplink-config.yml
│  ├─ zaplink-manager-deployment.yaml
│  ├─ zaplink-processor-deployment.yaml
│  ├─ zaplink-service-registry-deployment.yaml
│  └─ zaplink-shortner-deployment.yaml
├─ tools/
│  ├─ kafka/
│  │  └─ docker-compose.yml
│  └─ redis/
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

### Shortner Service

- Path: `services/zaplink-shortner-service`
- Purpose: Accept short URL creation requests and publish events to Kafka.
- Controller: `ZaplinkShortnerController` (POST `/producers/short/url`)
- Services: `UrlShortnerService`
- DTOs: `ShortnerRequest`, `ShortnerResponse`
- Tests: `ZaplinkShortnerControllerTest` (WebMvcTest)
- Entrypoint: `ZaplinkShortnerApplication.java`

### Processor Service

- Path: `services/zaplink-processor-service`
- Purpose: Consume Kafka events for short URL creation.
- Listener: `service.impl.KafkaListeners`
- DTOs: `BaseRequest`, `ShortUrlprocessorRequest`
- Controller: `ZaplinkprocessorController`
- Entrypoint: `ZaplinkprocessorServiceApplication.java`

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

- Java 17+ (required)
- Docker Desktop (for Kafka/Redis via compose)
- Git
- Windows (repo includes `.bat` scripts); Linux/Mac compatible with manual gradle commands

### Running All Services

Option A: Individual Gradle runs per service

- From each service folder (e.g., `services/zaplink-shortner-service`):
  - Windows: `gradlew.bat bootRun`
  - Linux/Mac: `./gradlew bootRun`

Option B: Build all with a script (Windows)

- Run `build-all-services.bat` to build every module (modify as needed).

Option C: Docker Compose (includes all services)

- Path: `docker-compose.yml`
- Includes all Zaplink microservices (service registry, gateway, shortner, processor, manager)
- Run:
  - `docker compose up -d`
- Note: External dependencies (Kafka, Redis) need to be started separately

### Running Kafka Locally

Option A: Root Docker Compose (multi-service)

- Path: `docker-compose.yml`
- Includes Zaplink microservices only (service registry, gateway, shortner, processor, manager)
- Run:
  - `docker compose up -d`

Option B: Kafka-only compose

- Path: `tools/kafka/docker-compose.yml`
- Run:
  - `cd tools/kafka`
  - `docker compose up -d`

Option C: Redis-only compose

- Path: `tools/redis/docker-compose.yml`
- Run:
  - `cd tools/redis`
  - `docker compose up -d`

Option D: Full stack with dependencies

Run Kafka and Redis first, then the Zaplink services:
```
cd tools/kafka && docker compose up -d
cd ../redis && docker compose up -d
cd ../../ && docker compose up -d
```

Ensure environment variables and `application.yml` match the advertised host/ports.

### Build and Test

- Build a service:
  - Windows: `gradlew.bat build`
  - Linux/Mac: `./gradlew build`
- Run tests only:
  - Windows: `gradlew.bat test`
  - Linux/Mac: `./gradlew test`

Example (shortner Service):

```
cd services/zaplink-shortner-service
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

### shortner API

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
- GET `/producers/short/url/{key}`
  - Path Parameter:
    - `key`: string (short URL key)
  - Response (JSON):
    - `url`: string (short URL)
  - Status Codes:
    - 200 OK on success
    - 404 Not Found if key not found
    - 5xx on service/internal errors

### processor API

- Typically internal (Kafka listeners). A controller `ZaplinkprocessorController` is present and can expose health or diagnostics endpoints if implemented.

### Manager API

- `UrlController` exposes endpoints for URL management and resolution (inspect the controller for exact mappings). Typical endpoints include:
  - POST to generate/store a mapping
  - GET to resolve a short code to long URL

### Gateway Endpoints

- API Gateway may expose a unified path (e.g., `/api/**`) routing to shortner/Manager/processor.
- Check `services/api-gateway-service/src/main/resources/application.yml` for route definitions.


## Messaging (Kafka)

- shortner publishes events/messages when short URL creation is requested.
- processor listens to a topic (defined in application.yml) and triggers processing.
- Topics, partitions, and replication factors are configured in Kafka compose and application properties.
- Ensure the Kafka bootstrap server address matches the Docker network or localhost depending on where services run.


## Data Model

- ShortnerRequest (shortner): `{ longUrl: string }`
- ShortnerResponse (shortner): `{ url: string }`
- ShortUrlprocessorRequest (processor): message structure consumed from Kafka for processing
- Manager persists mappings of `shortKey -> longUrl` (likely via Redis)


## Short URL Generation

- Utilities under `zaplink-shortner-service`:
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
  - Example: `ZaplinkshortnerControllerTest` uses `@WebMvcTest` and mocks `UrlServiceProvider`
- Integration tests (recommended):
  - With embedded Kafka for processor/shortner flows
  - Testcontainers for Kafka and Redis
- Contract tests between services (optional): Pact/REST Docs

Run tests per service as shown in Build and Test section.


## Tiltfile Usage

The `Tiltfile` provides a streamlined development experience for Kubernetes deployment:

### Prerequisites
- Tilt installed (`https://docs.tilt.dev/install.html`)
- Kubernetes cluster (local or remote)
- Docker daemon running

### Usage
```bash
# Start all services with Tilt
tilt up

# View the Tilt dashboard (opens in browser)
# Monitor logs, builds, and service status
```

### What Tilt Does
- Automatically builds Docker images for all services
- Applies Kubernetes manifests using Kustomize
- Sets up port forwarding for easy local access:
  - API Gateway: localhost:8090
  - Service Registry: localhost:8761
  - Shortner Service: localhost:8081
  - Processor Service: localhost:8082
  - Manager Service: localhost:8083
  - Redis: localhost:30093
  - Kafka: localhost:30192
  - Kafka UI: localhost:8080
- Watches for file changes and rebuilds/redeploys automatically
- Provides unified logging across all services

### Stopping Services
```bash
# Stop Tilt and clean up resources
tilt down
```

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

The `k8s/` folder contains Kubernetes manifests for deploying all services and dependencies:

### Deployment Files
- `api-gateway-deployment.yaml` - API Gateway service deployment
- `zaplink-service-registry-deployment.yaml` - Eureka service registry
- `zaplink-shortner-deployment.yaml` - Shortner service deployment
- `zaplink-processor-deployment.yaml` - Processor service deployment
- `zaplink-manager-deployment.yaml` - Manager service deployment
- `kafka-deployment.yaml` - Kafka cluster deployment
- `kafka-ui-deployment.yaml` - Kafka UI for monitoring
- `redis-deployment.yaml` - Redis cache deployment

### Storage & Configuration
- `kafka-storage.yml` - Persistent storage for Kafka
- `redis-storage.yml` - Persistent storage for Redis
- `zaplink-config.yml` - Configuration maps for services
- `kustomization.yml` - Kustomize configuration for managing all resources

### Deployment Options

Option A: Using Kustomize (recommended)
```bash
kubectl apply -k k8s/
```

Option B: Using Tilt for development
```bash
tilt up
```

Option C: Individual manifests
```bash
kubectl apply -f k8s/zaplink-config.yml
kubectl apply -f k8s/kafka-storage.yml
kubectl apply -f k8s/redis-storage.yml
kubectl apply -f k8s/kafka-deployment.yaml
kubectl apply -f k8s/redis-deployment.yaml
kubectl apply -f k8s/zaplink-service-registry-deployment.yaml
kubectl apply -f k8s/zaplink-manager-deployment.yaml
kubectl apply -f k8s/zaplink-processor-deployment.yaml
kubectl apply -f k8s/zaplink-shortner-deployment.yaml
kubectl apply -f k8s/api-gateway-deployment.yaml
kubectl apply -f k8s/kafka-ui-deployment.yaml
```

### Service Ports
- API Gateway: 8090
- Service Registry: 8761
- Shortner Service: 8081
- Processor Service: 8082
- Manager Service: 8083
- Kafka: 30192
- Kafka UI: 8080
- Redis: 30093


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
  - Example: `cd services\zaplink-shortner-service && gradlew.bat test`
- Port conflicts:
  - Adjust `server.port` in each service’s `application.yml`


<!-- ## License

This project is provided as-is for educational and demonstration purposes. Add your license of choice here.

--- -->

<!-- ## Contributors

- Your Name <you@example.com>

Feel free to open issues or PRs to improve the system, tests, and documentation. -->
