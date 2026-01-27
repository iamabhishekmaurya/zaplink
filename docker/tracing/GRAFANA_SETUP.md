# Grafana Dashboard Setup Guide

## Step 1: Access Grafana

**URL**: http://localhost:3001

The Grafana instance has anonymous admin access enabled for development (no login required).

---

## Step 2: Verify Tempo Datasource

1. Click **⚙️ Configuration** (gear icon) → **Data sources**
2. You should see **Tempo** datasource already configured
3. Click **Tempo** → Click **Save & Test**
4. Should show: ✅ "Data source is working"

---

## Step 3: Explore Traces

### Option A: Search for Traces

1. Click **🧭 Explore** (compass icon) in left sidebar
2. Select **Tempo** from datasource dropdown (top)
3. **Search** tab:
   - **Query type**: Search
   - **Service Name**: Select a service (e.g., `zaplink-core-service`)
   - Click **Run query**
4. View traces in the results

### Option B: TraceQL Query

1. In **Explore** → **Tempo**
2. Switch to **TraceQL** tab
3. Example queries:
   ```traceql
   # All traces
   {}
   
   # Traces from specific service
   {resource.service.name="zaplink-core-service"}
   
   # Traces with errors
   {status=error}
   
   # Slow traces (>1 second)
   {duration>1s}
   ```

---

## Step 4: Create a Dashboard (Optional)

1. Click **+ Create** → **Dashboard**
2. Click **Add visualization**
3. Select **Tempo** datasource
4. Configure panel with TraceQL query
5. Save dashboard

---

## Step 5: Generate Test Traces

### Restart a Service

```bash
# Stop any running service (e.g., core-service)
# Restart it - it will now send traces to Tempo
```

### Make a Request

```bash
# Example: Create a short link
curl -X POST http://localhost:8083/api/v1/urls \
  -H "Content-Type: application/json" \
  -d '{"longUrl": "https://example.com"}'
```

### View the Trace

1. Go back to **Explore** → **Tempo**
2. Search for `zaplink-core-service`
3. Click on a trace to see the full waterfall view

---

## Expected Trace Structure

For a request to create a short URL:
```
zaplink-core-service
  └─ POST /api/v1/urls
     ├─ Database INSERT operation
     └─ Response time metrics
```

---

## Troubleshooting

**No traces appearing?**

1. Check Tempo is running:
   ```bash
   docker-compose ps
   ```

2. Check service logs for OTLP export errors:
   ```bash
   # Look for "OTLP" or "tracing" in service logs
   ```

3. Verify configuration:
   - `application.yml` has correct endpoint: `http://localhost:4318/v1/traces`
   - Dependencies are in `build.gradle`

4. Check Tempo logs:
   ```bash
   docker-compose logs tempo
   ```
