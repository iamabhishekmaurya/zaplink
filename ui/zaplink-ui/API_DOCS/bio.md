# Bio Page API Documentation

## Overview
The Bio Page feature uses a mix of Manager Service (Read) and Core Service (Write) endpoints.

### Base URL
`https://api.zaplink.io/api/v1` (or local equivalent)

## Endpoints

### 1. Get Public Bio Page
Retrieves the bio page and active links for public rendering.
- **GET** `/bio/pages/{username}`
- **Response**: `BioPageResponse`
```json
{
  "id": "123",
  "username": "johndoe",
  "title": "John's Links",
  "bioText": "...",
  "themeConfig": { ... },
  "links": [
    { "id": "1", "type": "LINK", "title": "My Blog", "url": "...", "isActive": true }
  ]
}
```

### 2. Get Dashboard Page (Editor)
Retrieves the bio page by ID for the editor.
- **GET** `/bio/page/{id}`
- **Response**: `BioPageResponse`

### 3. Create Bio Page
- **POST** `/bio/page`
- **Body**: `{ "username": "johndoe", "title": "..." }`

### 4. Update Page Settings
- **PUT** `/bio/page/{id}`
- **Body**: `{ "title": "...", "themeConfig": { ... } }`

### 5. Link Management
- **Create**: POST `/bio/link`
  - Body: `{ "pageId": "123", "type": "LINK", "url": "...", "title": "..." }`
- **Update**: PUT `/bio/link/{linkId}`
- **Delete**: DELETE `/bio/link/{linkId}`
- **Reorder**: PUT `/bio/link/{pageId}/reorder`
  - Body: `{ "linkIds": ["2", "1", "3"] }`

### 6. Analytics
- **Track Click**: POST `/bio/links/{linkId}/click`
  - Headers: `keepalive: true`
