// API Base URLs
// API Base URLs
export const API_BASE_URLS = {
  // Auth Service
  AUTH: '/api/auth',

  // Core Service (Write / Operations) - Gateway Path: /api/wr/...
  CORE_URL: '/api/wr/short/url', // specific route in Gateway
  CORE_QR: '/api/wr/qr',
  CORE_DYQR: '/api/wr/dyqr', // Gateway routes this to Manager

  // Manager Service (Read / Analytics) - Gateway Path: /api/rd/...
  SHORT_LINKS: '/api/rd/short/links',
  SHORT_STATS: '/api/rd/short/stats',
  SHORT_ANALYTICS: '/api/rd/short',
  MANAGER_DYQR: '/api/rd/dyqr',

  // Media Service
  MEDIA: '/api/media'
} as const

// API Endpoints
// API Endpoints
export const API_ENDPOINTS = {
  // QR Code endpoints (Core Service)
  GENERATE_STYLED_QR: `${API_BASE_URLS.CORE_QR}/styled`,
  // GENERATE_SIMPLE_QR: `${API_BASE_URLS.CORE_QR}/simple`, // Not present in controller check

  // Authentication endpoints (Auth Service)
  LOGIN: `${API_BASE_URLS.AUTH}/login`,
  REGISTER: `${API_BASE_URLS.AUTH}/register`,
  VERIFY_EMAIL: `${API_BASE_URLS.AUTH}/verify-email`,
  RESEND_VERIFICATION: `${API_BASE_URLS.AUTH}/resend-verification`,
  REQUEST_PASSWORD_RESET: `${API_BASE_URLS.AUTH}/request-password-reset`,
  RESET_PASSWORD: `${API_BASE_URLS.AUTH}/reset-password`,
  GET_CURRENT_USER: `${API_BASE_URLS.AUTH}/me`,

  // Link management endpoints
  // Link management endpoints
  // Creation (Core Service)
  SHORTEN_URL: API_BASE_URLS.CORE_URL,

  // Listing & Deletion (Manager Service)
  GET_USER_LINKS: API_BASE_URLS.SHORT_LINKS,
  DELETE_LINK: (id: string) => `${API_BASE_URLS.SHORT_LINKS}/${id}`,

  // Analytics endpoints (Manager Service)
  GET_STATS: API_BASE_URLS.SHORT_STATS,
  // GET_USER_LINKS is duplicated in original, keeping above

  // Analytics for specific link
  ANALYTICS_LINK: (shortUrlKey: string) => `${API_BASE_URLS.SHORT_ANALYTICS}/${shortUrlKey}/analytics`,

  // Dynamic QR endpoints (Core Service - Write)
  DYNAMIC_QR_CREATE: API_BASE_URLS.CORE_DYQR,
  DYNAMIC_QR_UPDATE_DESTINATION: (qrKey: string) => `${API_BASE_URLS.CORE_DYQR}/${qrKey}/destination`,
  DYNAMIC_QR_TOGGLE_STATUS: (qrKey: string) => `${API_BASE_URLS.CORE_DYQR}/${qrKey}/status`,
  DYNAMIC_QR_DELETE: (qrKey: string) => `${API_BASE_URLS.CORE_DYQR}/${qrKey}`,

  // Dynamic QR endpoints (Manager Service - Read)
  DYNAMIC_QR_LIST: API_BASE_URLS.MANAGER_DYQR,
  DYNAMIC_QR_DETAILS: (qrKey: string) => `${API_BASE_URLS.MANAGER_DYQR}/${qrKey}`,
  DYNAMIC_QR_IMAGE: (qrKey: string) => `${API_BASE_URLS.MANAGER_DYQR}/${qrKey}/image`,
  DYNAMIC_QR_ANALYTICS: (qrKey: string) => `${API_BASE_URLS.MANAGER_DYQR}/${qrKey}/analytics`,
} as const

// Default configurations
export const API_CONFIG = {
  TIMEOUT: 10000,
  RETRY_ATTEMPTS: 3,
  HEADERS: {
    'Content-Type': 'application/json'
  }
} as const
