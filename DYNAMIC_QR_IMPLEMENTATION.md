# Dynamic QR Code Implementation

This document outlines the complete implementation of advanced dynamic QR code functionality in the Zaplink application.

## Overview

Dynamic QR codes are QR codes that can be updated after creation without changing the visual code itself. They redirect to a configurable destination URL and provide comprehensive analytics tracking.

## Features Implemented

### Backend Implementation

#### 1. Database Schema
- **dynamic_qr_codes**: Stores QR code metadata and configurations
- **qr_scan_analytics**: Tracks scan data with geolocation and device info
- **qr_campaigns**: Organizes QR codes into campaigns

#### 2. Core Services
- **DynamicQrService**: Manages QR code CRUD operations
- **QrRedirectService**: Handles redirects with analytics tracking
- **CampaignService**: Manages QR campaigns
- **QRService**: Generates styled QR codes (existing)

#### 3. API Endpoints
```
POST   /v1/api/dynamic-qr              - Create dynamic QR
GET    /v1/api/dynamic-qr/{qrKey}      - Get QR details
GET    /v1/api/dynamic-qr              - List user QRs (paginated)
PUT    /v1/api/dynamic-qr/{qrKey}/destination - Update URL
PUT    /v1/api/dynamic-qr/{qrKey}/config      - Update styling
PUT    /v1/api/dynamic-qr/{qrKey}/toggle      - Toggle status
DELETE /v1/api/dynamic-qr/{qrKey}      - Delete QR
GET    /v1/api/dynamic-qr/{qrKey}/analytics   - Get analytics
GET    /v1/api/dynamic-qr/{qrKey}/image       - Generate QR image
GET    /r/{qrKey}                       - Public redirect endpoint
```

### Frontend Implementation

#### 1. React Components
- **DynamicQrDashboard**: Main dashboard with QR grid
- **CreateDynamicQrForm**: QR creation with styling options
- **QRStyleSelector**: QR customization controls
- **QrAnalytics**: Analytics dashboard with charts

#### 2. API Integration
- **DynamicQrApi**: TypeScript API client
- **Real-time updates**: Automatic refresh after operations
- **Error handling**: Comprehensive error states

## Architecture

### Flow Diagram
```
User scans QR → Gateway → Core Service → Redirect Service → Analytics → Destination URL
                    ↓
              QR Image Generation ← QR Service ← Styling Engine
```

### Data Flow
1. **QR Creation**: User configures QR → API saves metadata → Generates QR image
2. **QR Scan**: User scans → Redirect service tracks → Updates analytics → Redirects
3. **QR Update**: User changes URL → API updates metadata → No QR change needed

## Database Schema

### dynamic_qr_codes Table
```sql
CREATE TABLE dynamic_qr_codes (
    id BIGSERIAL PRIMARY KEY,
    qr_key VARCHAR(255) UNIQUE NOT NULL,
    qr_name VARCHAR(255),
    current_destination_url TEXT NOT NULL,
    qr_config JSONB,
    user_email VARCHAR(255),
    campaign_id VARCHAR(255),
    is_active BOOLEAN DEFAULT true,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    total_scans BIGINT DEFAULT 0,
    last_scanned TIMESTAMP
);
```

### qr_scan_analytics Table
```sql
CREATE TABLE qr_scan_analytics (
    id BIGSERIAL PRIMARY KEY,
    qr_key VARCHAR(255) NOT NULL,
    ip_address INET,
    user_agent TEXT,
    referrer VARCHAR(500),
    country VARCHAR(100),
    city VARCHAR(100),
    device_type VARCHAR(50),
    browser VARCHAR(100),
    scanned_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

### qr_campaigns Table
```sql
CREATE TABLE qr_campaigns (
    id BIGSERIAL PRIMARY KEY,
    campaign_id VARCHAR(255) UNIQUE NOT NULL,
    campaign_name VARCHAR(255) NOT NULL,
    user_email VARCHAR(255) NOT NULL,
    description TEXT,
    start_date TIMESTAMP,
    end_date TIMESTAMP,
    status VARCHAR(20) DEFAULT 'ACTIVE',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
```

## API Gateway Configuration

Updated routes in `GatewayConfig.java`:
```java
.route( "core-dynamic-qr",
        r -> r.path( basePath + "/dynamic-qr/**" )
                .filters( f -> f.stripPrefix(0) )
                .uri( "http://localhost:8081" ) )
.route( "manager-short",
        r -> r.path( "/r/**" )
                .filters( f -> f
                        .rewritePath( "/r/(?<segment>.*)", "/r/${segment}" )
                        .addResponseHeader( "X-Zaplink-Processing-Time", LocalDateTime.now().toString() )
                        .addResponseHeader( "X-Zaplink-Mode", "HYBRID" ) )
                .uri( "http://localhost:8081" ) )
```

## Frontend Components

### DynamicQrDashboard
- Grid layout showing QR codes with images
- Search and pagination functionality
- Quick actions (view analytics, edit, delete, toggle status)
- Real-time scan counts

### CreateDynamicQrForm
- Step-by-step QR creation process
- Live preview of QR styling
- Campaign assignment
- Advanced styling options

### QRStyleSelector
- Body shape selection (square, rounded, circle, dot, liquid)
- Eye shape customization
- Color and gradient options
- Logo upload and positioning
- Size and margin controls

### QrAnalytics
- Overview statistics (total, today, this week, this month)
- Geographic distribution
- Device and browser breakdowns
- Daily scan trends
- Interactive charts

## Installation & Setup

### 1. Database Migration
```bash
# Execute the migration script
psql -d zaplink_db -f tools/db/create-dynamic-qr-tables.sql
```

### 2. Backend Services
```bash
# Start core service (port 8081)
cd services/zaplink-core-service
./mvnw spring-boot:run

# Start API gateway (port 8090)
cd services/api-gateway-service
./mvnw spring-boot:run
```

### 3. Frontend Application
```bash
cd ui/zaplink-app
npm install
npm run dev
```

## Usage Examples

### Create a Dynamic QR
```typescript
const request: CreateDynamicQrRequest = {
  qrName: "Restaurant Menu",
  destinationUrl: "https://restaurant.com/menu",
  campaignId: "campaign-123",
  qrConfig: {
    data: "https://restaurant.com/menu",
    size: 300,
    margin: 4,
    errorCorrectionLevel: "H",
    transparentBackground: false,
    backgroundColor: "#FFFFFF",
    body: {
      shape: "ROUNDED",
      color: "#000000",
      gradientLinear: true
    },
    eye: {
      shape: "ROUNDED",
      colorOuter: "#000000",
      colorInner: "#000000"
    }
  }
};

const response = await DynamicQrApi.createDynamicQr(request);
```

### Update Destination URL
```typescript
const updateRequest: UpdateDestinationRequest = {
  destinationUrl: "https://restaurant.com/new-menu"
};

const updated = await DynamicQrApi.updateDestination(qrKey, updateRequest);
```

### Get Analytics
```typescript
const analytics = await DynamicQrApi.getQrAnalytics(qrKey);
console.log(`Total scans: ${analytics.totalScans}`);
console.log(`Top country: ${analytics.countryStats[0].country}`);
```

## Advanced Features

### 1. QR Styling
- Multiple body shapes (square, rounded, circle, dot, liquid)
- Custom eye shapes (square, rounded, circle, leaf)
- Color gradients and transparency
- Logo integration with background options

### 2. Analytics Tracking
- Real-time scan counting
- Geographic location tracking
- Device and browser detection
- Referrer tracking
- Daily trend analysis

### 3. Campaign Management
- Organize QR codes by campaigns
- Campaign-level analytics
- Bulk operations
- Campaign status management

### 4. Performance Optimization
- Efficient QR generation with caching
- Database indexing for fast queries
- Async analytics tracking
- Image optimization

## Security Considerations

1. **Input Validation**: All user inputs validated on both client and server
2. **Rate Limiting**: API endpoints protected from abuse
3. **CORS**: Proper cross-origin configuration
4. **Data Privacy**: Analytics data anonymized where appropriate
5. **Access Control**: User-specific data isolation

## Monitoring & Logging

- Comprehensive logging with structured format
- Performance metrics for QR generation
- Analytics processing monitoring
- Error tracking and alerting

## Future Enhancements

1. **A/B Testing**: Test multiple destinations
2. **Geotargeting**: Location-based redirects
3. **Time-based Rules**: Schedule URL changes
4. **Advanced Analytics**: Heat maps, conversion tracking
5. **QR Templates**: Predefined styling presets
6. **Bulk Operations**: Import/export QR codes
7. **API Rate Limiting**: Enhanced protection
8. **CDN Integration**: Faster QR image delivery

## Troubleshooting

### Common Issues

1. **QR Not Scanning**: Check error correction level and contrast
2. **Slow Generation**: Optimize QR config or increase resources
3. **Analytics Not Updating**: Verify redirect service is running
4. **Database Errors**: Check connection and permissions

### Debug Mode
Enable debug logging in `application.properties`:
```properties
logging.level.io.zaplink.core=DEBUG
```

## Contributing

When adding new features:
1. Update database schema with migration scripts
2. Add comprehensive tests
3. Update API documentation
4. Consider backward compatibility
5. Add frontend components with proper error handling

## Support

For issues and questions:
1. Check application logs
2. Verify database connectivity
3. Test API endpoints directly
4. Review this documentation
5. Contact development team
