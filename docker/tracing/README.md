# Zaplink Distributed Tracing Stack

This directory contains the Docker Compose setup for the distributed tracing infrastructure.

## Components

- **Grafana Tempo**: Trace storage and query backend (OTLP-native)
- **Grafana**: Trace visualization UI

## Quick Start

```bash
# Start tracing stack
cd docker/tracing
docker-compose up -d

# View logs
docker-compose logs -f

# Stop stack
docker-compose down

# Stop and remove volumes (clean slate)
docker-compose down -v
```

## Access

- **Grafana UI**: http://localhost:3001
  - Pre-configured with Tempo datasource
  - Anonymous access enabled for development
  
- **Tempo OTLP Endpoints**:
  - HTTP: `http://localhost:4318` (used by services)
  - gRPC: `http://localhost:4317`
  - API: `http://localhost:3200`

## Verify Setup

1. Start the stack: `docker-compose up -d`
2. Open Grafana: http://localhost:3001
3. Navigate to **Explore** → Select **Tempo** datasource
4. Once services are instrumented, you'll see traces here

## Troubleshooting

```bash
# Check if containers are running
docker-compose ps

# View Tempo logs
docker-compose logs tempo

# View Grafana logs
docker-compose logs grafana

# Restart services
docker-compose restart
```
