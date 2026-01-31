# BioPage Functionality Verification

This document provides step-by-step instructions to verify the BioPage functionality implementation.

## Prerequisites

1. Ensure all services are running:
   - PostgreSQL database (localhost:5432)
   - zaplink-manager-service (localhost:8083)
   - zaplink-redirect-service (localhost:8085)
   - zaplink-ui (localhost:3000)

## Database Setup

1. The Flyway migrations should automatically run when the manager service starts:
   - `V1__Create_bio_pages_table.sql`
   - `V2__Create_bio_links_table.sql`
   - `V3__Insert_test_data.sql`

2. Verify tables exist in PostgreSQL:
   ```sql
   \c zaplink_core
   \dt
   -- Should show: bio_pages, bio_links
   ```

## API Verification

### 1. Test BioPage Creation (Manager Service)

```bash
# Create a new bio page
curl -X POST http://localhost:8083/api/v1/bio-pages \
  -H "Content-Type: application/json" \
  -d '{
    "username": "testuser",
    "owner_id": "test123",
    "bio_text": "Test bio page",
    "avatar_url": "https://example.com/avatar.jpg"
  }'
```

Expected response:
```json
{
  "id": 3,
  "username": "testuser",
  "owner_id": "test123",
  "bio_text": "Test bio page",
  "avatar_url": "https://example.com/avatar.jpg",
  "created_at": "2024-01-30T...",
  "updated_at": "2024-01-30T...",
  "links": []
}
```

### 2. Test BioLink Creation

```bash
# Create a bio link
curl -X POST http://localhost:8083/api/v1/bio-links \
  -H "Content-Type: application/json" \
  -d '{
    "page_id": 3,
    "title": "My Website",
    "url": "https://example.com",
    "type": "LINK",
    "sort_order": 0
  }'
```

### 3. Test BioLink Reordering

```bash
# Reorder links
curl -X PUT http://localhost:8083/api/v1/bio-links/page/3/reorder \
  -H "Content-Type: application/json" \
  -d '{
    "linkOrders": [
      {"linkId": 1, "sortOrder": 1},
      {"linkId": 2, "sortOrder": 0}
    ]
  }'
```

### 4. Test Redirect Service Bio Endpoint

```bash
# Get bio page by username
curl http://localhost:8085/v1/bio/johndoe
```

Expected response structure:
```json
{
  "username": "johndoe",
  "avatar_url": "https://picsum.photos/seed/johndoe/200/200.jpg",
  "bio_text": "Software developer passionate about building amazing products. Check out my links below!",
  "theme_config": "{\"primaryColor\": \"#3b82f6\", \"backgroundColor\": \"#f8fafc\", \"textColor\": \"#1e293b\"}",
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
      "title": "GitHub Profile",
      "url": "https://github.com/johndoe",
      "type": "SOCIAL",
      "is_active": true,
      "sort_order": 1,
      "price": null,
      "currency": null
    },
    {
      "title": "Premium Course - Web Development",
      "url": null,
      "type": "PRODUCT",
      "is_active": true,
      "sort_order": 2,
      "price": 99.99,
      "currency": "USD"
    }
  ]
}
```

## Frontend Verification

### 1. Access BioPage Management UI

Navigate to: http://localhost:3000/dashboard/bio-page

You should see:
- List of existing bio pages (johndoe, janesmith)
- "Create New Page" button
- Ability to manage links for each page

### 2. Test Public BioPage Rendering

Navigate to: http://localhost:3000/johndoe

You should see:
- Beautiful bio page with avatar
- Bio text
- Sorted list of links
- Product links showing price
- Theme customization applied

### 3. Test Non-existent Username

Navigate to: http://localhost:3000/nonexistentuser

You should see:
- "Page Not Found" error page
- Option to go home

## Test Data Summary

The test data includes:

### BioPage: johndoe
- Username: johndoe
- Bio: "Software developer passionate about building amazing products..."
- 6 links including 1 product ($99.99 USD)
- Blue theme

### BioPage: janesmith  
- Username: janesmith
- Bio: "UX/UI Designer creating beautiful digital experiences..."
- 4 links including 1 product ($49.00 USD)
- Pink theme

## Verification Checklist

- [ ] Database tables created successfully
- [ ] Test data inserted correctly
- [ ] Manager service CRUD APIs work
- [ ] Redirect service bio endpoint returns correct JSON
- [ ] Links are sorted by sort_order
- [ ] Product links include price and currency
- [ ] Frontend management UI loads and functions
- [ ] Public bio pages render correctly
- [ ] Theme customization works
- [ ] Non-existent usernames show error page

## Troubleshooting

1. **Database Issues**: Check PostgreSQL connection and Flyway logs
2. **Service Communication**: Verify manager service URL in redirect service config
3. **Frontend API Calls**: Check browser console for network errors
4. **Theme Not Applied**: Verify JSON format in theme_config field

## Production Readiness

The implementation includes:
- ✅ Proper validation (unique username, required fields)
- ✅ Database constraints and indexes
- ✅ Error handling and user feedback
- ✅ Responsive UI design
- ✅ Drag-and-drop link reordering
- ✅ Product support with pricing
- ✅ Theme customization
- ✅ Public and admin interfaces
