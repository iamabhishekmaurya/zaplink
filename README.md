# Zaplink — URL Shortening & Link in Bio Platform (Spring Boot)

Zaplink is a comprehensive microservices-based platform that provides both URL shortening and "Link in Bio" functionality. Built with Spring Boot, Kafka, Eureka Service Discovery, and a combination of Redis and HTTP APIs, the system is designed for scalability, fault tolerance, and separation of concerns. This repository contains a multi-module Gradle project with multiple services that communicate via REST and Kafka.

## 🆕 BioPage Feature

Zaplink now includes a full-featured "Link in Bio" CMS similar to Linktree/Later, allowing users to:
- Create customizable bio pages with unique usernames
- Add and manage multiple types of links (website, social media, products, email, phone)
- Support for product links with pricing and currency
- Drag-and-drop link reordering
- Theme customization
- Public bio page rendering at `zap.link/{username}`

---

## Table of Contents

- [High-Level Overview](#high-level-overview)
- [BioPage Feature](#biopage-feature)
- [Architecture](#architecture)
  - [Services](#services)
  - [Data Flow](#data-flow)
  - [Key Technologies](#key-technologies)
- [Repository Structure](#repository-structure)
- [Service Details](#service-details)
  - [API Gateway Service](#api-gateway-service)
  - [core Service](#core-service)
  - [Processor Service](#processor-service)
  - [Manager Service](#manager-service)
  - [Redirect Service](#redirect-service)
  - [Service Registry](#service-registry)
- [Local Development](#local-development)
  - [Prerequisites](#prerequisites)
  - [Running All Services](#running-all-services)
  - [Running Kafka Locally](#running-kafka-locally)
  - [Build and Test](#build-and-test)
- [Configuration](#configuration)
- [API Reference](#api-reference)
  - [core API](#core-api)
  - [Processor API](#processor-api)
  - [Manager API](#manager-api)
  - [Redirect Service API](#redirect-service-api)
  - [BioPage API](#biopage-api)
  - [Gateway Endpoints](#gateway-endpoints)
- [Messaging (Kafka)](#messaging-kafka)
- [Data Model](#data-model)
- [BioPage Data Model](#biopage-data-model)
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

Zaplink provides a short URL for a given long URL. The request is accepted by the core service, which publishes a message to Kafka. The processor service processes the message and coordinates with the Manager service to persist/cache mappings and return/serve the shortened URLs. The API Gateway routes requests to underlying services and centralizes external API access.

In addition, Zaplink now provides a complete "Link in Bio" solution where users can create customizable bio pages with multiple links, similar to Linktree or Later.

## BioPage Feature

The BioPage feature transforms Zaplink into a full-featured Link in Bio CMS with the following capabilities:

### Core Features
- **Custom Bio Pages**: Users create pages with unique usernames (e.g., `zap.link/johndoe`)
- **Multiple Link Types**: Support for website links, social media, products, email, and phone
- **Product Commerce**: Product links with pricing and currency support (stubbed for future payment integration)
- **Drag & Drop Reordering**: Intuitive link management with sort ordering
- **Theme Customization**: JSON-based theme configuration for personalized appearance
- **Public Rendering**: Beautiful public bio pages accessible via clean URLs

### Technical Implementation
- **Database**: PostgreSQL with Flyway migrations for `bio_pages` and `bio_links` tables
- **APIs**: Complete CRUD operations in manager service
- **Public Endpoint**: Redirect service provides `GET /v1/bio/{username}` 
- **Frontend**: React-based management UI and public page rendering
- **Validation**: Unique usernames, required fields, and data integrity

### Data Structure
```sql
bio_pages:
- id, username (unique), owner_id, theme_config, avatar_url, bio_text

bio_links:
- id, page_id, title, url, type, is_active, sort_order, price, currency
```

### Usage Examples
```bash
# Create bio page
POST /api/v1/bio-pages
{"username": "johndoe", "bio_text": "Software developer"}

# Add product link
POST /api/v1/bio-links
{"page_id": 1, "title": "My Course", "type": "PRODUCT", "price": 99.99, "currency": "USD"}

# Get public bio page
GET /v1/bio/johndoe
```


## Architecture

### Services

- api-gateway-service: Routes external requests to internal services (edge service).
- zaplink-core-service: Accepts requests to create short URLs and emits Kafka events.
- zaplink-processor-service: Listens to Kafka topics and processes short URL creation events.
- zaplink-manager-service: Business logic for URL storage/lookup, Redis integration, and BioPage management.
- zaplink-redirect-service: High-performance redirects and public BioPage serving.
- zaplink-service-registry: Eureka service registry for service discovery.

### Data Flow

1. Client POSTs a long URL to core.
2. core validates/transforms and publishes a Kafka message.
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
│  ├─ zaplink-core-service/
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
│  └─ zaplink-core-deployment.yaml
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

### core Service

- Path: `services/zaplink-core-service`
- Purpose: Accept short URL creation requests and publish events to Kafka.
- Controller: `ZaplinkcoreController` (POST `/producers/short/url`)
- Services: `UrlcoreService`
- DTOs: `coreRequest`, `coreResponse`
- Tests: `ZaplinkcoreControllerTest` (WebMvcTest)
- Entrypoint: `ZaplinkcoreApplication.java`

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

- From each service folder (e.g., `services/zaplink-core-service`):
  - Windows: `gradlew.bat bootRun`
  - Linux/Mac: `./gradlew bootRun`

Option B: Build all with a script (Windows)

- Run `build-all-services.bat` to build every module (modify as needed).

Option C: Docker Compose (includes all services)

- Path: `docker-compose.yml`
- Includes all Zaplink microservices (service registry, gateway, core, processor, manager)
- Run:
  - `docker compose up -d`
- Note: External dependencies (Kafka, Redis) need to be started separately

### Running Kafka Locally

Option A: Root Docker Compose (multi-service)

- Path: `docker-compose.yml`
- Includes Zaplink microservices only (service registry, gateway, core, processor, manager)
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

Example (core Service):

```
cd services/zaplink-core-service
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

### core API

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

### Redirect Service API

- GET `/v1/bio/{username}`
  - Path Parameter:
    - `username`: string (unique bio page username)
  - Response (JSON):
    ```json
    {
      "username": "johndoe",
      "avatar_url": "https://example.com/avatar.jpg",
      "bio_text": "Software developer...",
      "theme_config": "{\"primaryColor\": \"#3b82f6\"}",
      "links": [
        {
          "title": "My Portfolio",
          "url": "https://johndoe.dev",
          "type": "LINK",
          "is_active": true,
          "sort_order": 0,
          "price": null,
          "currency": null
        },
        {
          "title": "Premium Course",
          "type": "PRODUCT",
          "is_active": true,
          "sort_order": 1,
          "price": 99.99,
          "currency": "USD"
        }
      ]
    }
    ```
  - Status Codes:
    - 200 OK on success
    - 404 Not Found if username not found
    - 5xx on service/internal errors

### BioPage API

#### BioPage Management (Manager Service)

- POST `/api/v1/bio-pages`
  - Request Body (JSON):
    ```json
    {
      "username": "johndoe",
      "owner_id": "user123",
      "bio_text": "Software developer...",
      "avatar_url": "https://example.com/avatar.jpg",
      "theme_config": "{\"primaryColor\": \"#3b82f6\"}"
    }
    ```
  - Response (JSON): Complete BioPage object with ID
  - Status Codes: 201 Created, 400 Bad Request, 409 Conflict (username exists)

- GET `/api/v1/bio-pages/{id}`
  - Get bio page by ID
  - Response: Complete BioPage object with links

- GET `/api/v1/bio-pages/username/{username}`
  - Get bio page by username (includes links)
  - Response: Complete BioPage object with links

- GET `/api/v1/bio-pages/owner/{ownerId}`
  - Get all bio pages for an owner
  - Response: Array of BioPage objects

- PUT `/api/v1/bio-pages/{id}`
  - Update bio page (bio_text, avatar_url, theme_config)
  - Request Body: Update fields only
  - Response: Updated BioPage object

- DELETE `/api/v1/bio-pages/{id}`
  - Delete bio page and all associated links
  - Status Codes: 204 No Content

#### BioLink Management (Manager Service)

- POST `/api/v1/bio-links`
  - Request Body (JSON):
    ```json
    {
      "page_id": 1,
      "title": "My Website",
      "url": "https://example.com",
      "type": "LINK",
      "sort_order": 0,
      "is_active": true
    }
    ```
  - Product link example:
    ```json
    {
      "page_id": 1,
      "title": "My Course",
      "type": "PRODUCT",
      "price": 99.99,
      "currency": "USD",
      "sort_order": 1
    }
    ```

- GET `/api/v1/bio-links/{id}`
  - Get bio link by ID

- GET `/api/v1/bio-links/page/{pageId}`
  - Get all links for a page (sorted by sort_order)

- GET `/api/v1/bio-links/page/{pageId}/active`
  - Get only active links for a page

- PUT `/api/v1/bio-links/{id}`
  - Update bio link
  - Request Body: Update fields only

- DELETE `/api/v1/bio-links/{id}`
  - Delete bio link

- PUT `/api/v1/bio-links/page/{pageId}/reorder`
  - Reorder links on a page
  - Request Body:
    ```json
    {
      "linkOrders": [
        {"linkId": 1, "sortOrder": 0},
        {"linkId": 2, "sortOrder": 1}
      ]
    }
    ```

### Gateway Endpoints

- API Gateway may expose a unified path (e.g., `/api/**`) routing to core/Manager/processor.
- Check `services/api-gateway-service/src/main/resources/application.yml` for route definitions.


## Messaging (Kafka)

- core publishes events/messages when short URL creation is requested.
- processor listens to a topic (defined in application.yml) and triggers processing.
- Topics, partitions, and replication factors are configured in Kafka compose and application properties.
- Ensure the Kafka bootstrap server address matches the Docker network or localhost depending on where services run.


## Data Model

- coreRequest (core): `{ longUrl: string }`
- coreResponse (core): `{ url: string }`
- ShortUrlprocessorRequest (processor): message structure consumed from Kafka for processing
- Manager persists mappings of `shortKey -> longUrl` (likely via Redis)


## Short URL Generation

- Utilities under `zaplink-core-service`:
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
  - Example: `ZaplinkcoreControllerTest` uses `@WebMvcTest` and mocks `UrlServiceProvider`
- Integration tests (recommended):
  - With embedded Kafka for processor/core flows
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
  - core Service: localhost:8081
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
- `zaplink-core-deployment.yaml` - core service deployment
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
kubectl apply -f k8s/zaplink-core-deployment.yaml
kubectl apply -f k8s/api-gateway-deployment.yaml
kubectl apply -f k8s/kafka-ui-deployment.yaml
```

### Service Ports
- API Gateway: 8090
- Service Registry: 8761
- core Service: 8081
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
  - Example: `cd services\zaplink-core-service && gradlew.bat test`
- Port conflicts:
  - Adjust `server.port` in each service’s `application.yml`


<!-- ## License

This project is provided as-is for educational and demonstration purposes. Add your license of choice here.

--- -->

<!-- ## Contributors

- Your Name <you@example.com>

Feel free to open issues or PRs to improve the system, tests, and documentation. -->
