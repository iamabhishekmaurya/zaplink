# Quick Start: Grafana Trace Visualization

## 🚀 Open Grafana Now

1. **Open your web browser** (Chrome, Edge, Firefox, etc.)
2. **Navigate to**: http://localhost:3001
3. **You're in!** (No login needed - anonymous admin access)

---

## ✅ Step-by-Step: View Your First Trace

### 1. Verify Tempo Connection

- Click **⚙️ (gear icon)** on left sidebar → **Data sources**
- You should see **Tempo** listed
- Click **Tempo** → Click **Save & test** button
- ✅ Should show: "Data source is working"

![image](https://grafana.com/static/img/tempo/tempo-explore.png)

### 2. Explore Traces

- Click **🧭 (compass icon)** on left sidebar → This opens **Explore**
- At the top, make sure **Tempo** is selected in datasource dropdown
- You'll see three tabs: **Search**, **TraceQL**, **Service Graph**

### 3. Search for Traces (Easiest Method)

**In the Search tab:**
- **Service Name**: Select `zaplink-core-service` (or any  other service)
- **Span Name**: Leave empty (shows all)
- **Tags**: Leave empty
- Click **Run query** button

**Result**: You'll see a list of traces (if any have been generated)

### 4. View Trace Details

- Click on any trace in the results
- You'll see a **waterfall diagram** showing:
  - Total duration
  - Each span (operation) in the trace
  - Timing breakdown
  - Service dependencies

---

## 🔍 TraceQL Queries (Advanced)

Switch to **TraceQL** tab and try these queries:

```traceql
# Show all traces
{}

# Traces from specific service
{resource.service.name="zaplink-core-service"}

# Only slow traces (over 1 second)
{duration > 1s}

# Traces with errors
{status = error}

# HTTP POST requests
{span.http.method = "POST"}
```

---

## 📊 Create a Dashboard (Optional)

1. Click **+ (plus icon)** on left → **Create** → **Dashboard**
2. Click **Add visualization**
3. Select **Tempo** as datasource
4. **Query type**: Use "TraceQL" or "Search"
5. Enter query: `{resource.service.name="zaplink-core-service"}`
6. **Visualization type**: Change to "Table" or "Stat"
7. **Panel title**: "Recent Traces"
8. Click **Apply**
9. Click **Save dashboard** (top right)
10. Name it: "Zaplink Traces"

---

## 🧪 Generate Test Traces

### Option 1: Restart a Service

```bash
# Stop one of the running services (e.g., core-service)
# Just stop and restart it in your IDE

# The service will send traces on startup and on every request
```

### Option 2: Make an API Request

```bash
# Example: Create a short URL via core-service
curl -X POST http://localhost:8083/api/v1/urls \
  -H "Content-Type: application/json" \
  -d '{"longUrl": "https://example.com", "customAlias": "test123"}'
```

### View the Trace

1. Go back to **Explore** → **Tempo**
2. Click **Run query** (or refresh)
3. You should see new traces appear
4. Click on a trace to see the full waterfall

---

## 🎯 What You Should See

A typical trace for creating a short URL will show:

```
📍 zaplink-core-service
  └─ POST /api/v1/urls (10ms)
     ├─ Database: INSERT INTO urls (5ms)
     ├─ Cache: Redis SET (2ms)
     └─ Response serialization (1ms)
```

Each span shows:
- **Service name**
- **Operation name**
- **Duration**
- **Start/End time**
- **Tags** (HTTP method, status code, etc.)

---

## 🐛 Troubleshooting

### No Traces Appearing?

1. **Check Docker containers are running:**
   ```bash
   cd docker/tracing
   docker-compose ps
   ```
   ✅ Both `zaplink-tempo` and `zaplink-grafana` should be "Up"

2. **Restart a service** to generate traces

3. **Check service logs** for errors:
   - Look for "OTLP" or "tracing" errors in console

4. **Verify Tempo logs:**
   ```bash
   docker-compose logs tempo
   ```
   Should show: "OTLP receiver started"

### Datasource Not Working?

- Open http://localhost:3200 (Tempo API)
- Should show: `{}`
- This means Tempo is running

---

## 🎉 Next Steps

Once you see traces in Grafana:

1. **Restart all services** so they all send traces
2. **Make requests** to different services
3. **Watch traces** flow through your microservices
4. **Proceed to Phase 3**: Kafka trace propagation

---

**Need help?** Check `GRAFANA_SETUP.md` for detailed instructions.
