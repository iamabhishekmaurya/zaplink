// API Base URLs
export const API_BASE_URLS = {
  QR: '/v1/api/wr/qr',
  AUTH: '/v1/api/auth',
  LINKS: '/v1/api/wr/short/url',
  STATS: '/v1/api/rd/short/stats',
  USER_LINKS: '/v1/api/rd/short/links',
  DYNAMIC_QR: '/v1/api/wr/dyqr',
  DYNAMIC_QR_READ: '/v1/api/rd/dyqr',
  DYNAMIC_QR_IMAGE: '/v1/api/rd/dyqr'
} as const

// API Endpoints
export const API_ENDPOINTS = {
  // QR Code endpoints
  GENERATE_STYLED_QR: `${API_BASE_URLS.QR}/styled`,
  GENERATE_SIMPLE_QR: `${API_BASE_URLS.QR}/simple`,

  // Authentication endpoints
  LOGIN: `${API_BASE_URLS.AUTH}/login`,
  REGISTER: `${API_BASE_URLS.AUTH}/register`,
  VERIFY_EMAIL: `${API_BASE_URLS.AUTH}/verify-email`,
  RESEND_VERIFICATION: `${API_BASE_URLS.AUTH}/resend-verification`,
  REQUEST_PASSWORD_RESET: `${API_BASE_URLS.AUTH}/request-password-reset`,
  RESET_PASSWORD: `${API_BASE_URLS.AUTH}/reset-password`,
  GET_CURRENT_USER: `${API_BASE_URLS.AUTH}/me`,

  // Link management endpoints
  SHORTEN_URL: API_BASE_URLS.LINKS,
  DELETE_LINK: (id: string) => `${API_BASE_URLS.LINKS}/${id}`,

  // Analytics endpoints
  GET_STATS: API_BASE_URLS.STATS,
  GET_USER_LINKS: API_BASE_URLS.USER_LINKS,

  // Dynamic QR endpoints
  DYNAMIC_QR_CREATE: `${API_BASE_URLS.DYNAMIC_QR}`,
  DYNAMIC_QR_READ: API_BASE_URLS.DYNAMIC_QR_READ,
  DYNAMIC_QR_IMAGE: (qrKey: string) => `${API_BASE_URLS.DYNAMIC_QR_IMAGE}/${qrKey}`,
  DYNAMIC_QR_ANALYTICS: (qrKey: string) => `${API_BASE_URLS.DYNAMIC_QR_READ}/${qrKey}/analytics`,

  // Analytics
  ANALYTICS_LINK: (shortUrlKey: string) => `/v1/api/rd/short/${shortUrlKey}/analytics`
} as const

// Default configurations
export const API_CONFIG = {
  TIMEOUT: 10000,
  RETRY_ATTEMPTS: 3,
  HEADERS: {
    'Content-Type': 'application/json'
  }
} as const
