export interface QRConfigType {
  data: string;
  size: number;
  margin: number;
  error_correction_level: string;
  transparent_background: boolean;
  background_color: string;
  body: {
    shape: string;
    color: string;
    color_dark?: string;
    gradient_linear: boolean;
  };
  eye: {
    shape: string;
    color_outer: string;
    color_inner: string;
  };
  logo?: {
    logo_path?: string;
    size_ratio: number;
    padding: number;
    background_color: string;
    background_enabled: boolean;
    background_rounded: boolean;
    background_corner_radius: number;
    remove_quiet_zone: boolean;
    margin_size: number;
  };
}

export interface UserInfo {
  id: string;
  email: string;
  username: string;
  firstName?: string;
  lastName?: string;
  phoneNumber?: string;
  verified: boolean;
  active?: boolean;
  createdAt?: string;
}

export interface AuthState {
  user: UserInfo | null;
  token: string | null;
  refreshToken: string | null;
  isAuthenticated: boolean;
  isLoading: boolean;
  isInitialized: boolean;
  error: string | null;
}

export interface LoginRequest {
  email: string;
  password?: string;
}

export interface RegisterRequest {
  username: string;
  email: string;
  password?: string;
  confirmPassword?: string;
  firstName?: string;
  lastName?: string;
}

export interface ForgotPasswordRequest {
  email: string;
}

export interface ResetPasswordRequest {
  token: string;
  newPassword?: string;
  confirmPassword?: string;
}

export interface BaseResponse {
  success: boolean;
  message: string;
  timestamp: string;
}

export interface LoginResponse {
  accessToken: string;
  refreshToken: string;
  userInfo: UserInfo;
}

export interface UserRegistrationResponse {
  userId: number;
  username: string;
  email: string;
  message: string;
}

export interface VerifyEmailResponse {
  success: boolean;
  message: string;
  accessToken?: string;
  refreshToken?: string;
  userInfo?: UserInfo;
}

export interface RedirectRuleDto {
  dimension: 'DEVICE_TYPE' | 'OS' | 'COUNTRY';
  value: string;
  destination_url: string;
  priority: number;
}

// ============ ShortLink Types ============
// API Response (snake_case - from backend)
export interface LinkApiResponse {
  id: number;
  short_url_key: string;
  original_url: string;
  short_url: string;
  created_at: string;
  click_count: number;
  status: string;
  rules?: RedirectRuleDto[];
  tags?: string[];
  title?: string;
  platform?: string;
}

// Frontend Type (camelCase)
export interface ShortLink {
  id: string;
  title: string;
  shortlink: string;
  originalUrl: string;
  tags: string[];
  platform: string;
  hasAnalytics: boolean;
  isActive: boolean;
  createdAt: string;
  updatedAt: string;
  userId: string;
  clicks?: number;
  shortUrlKey?: string;
  rules?: RedirectRuleDto[];
}

export interface GetUserLinksResponse extends BaseResponse {
  data: ShortLink[];
}

// ============ DynamicQR Types ============
// API Response (snake_case - from backend)
export interface DynamicQrApiResponse {
  id: number;
  qr_key: string;
  qr_name: string;
  current_destination_url: string;
  qr_image_url: string;
  redirect_url: string;
  campaign_id?: string;
  user_email: string;
  is_active: boolean;
  total_scans: number;
  created_at: string;
  updated_at: string;
  last_scanned?: string;
  rules?: RedirectRuleDto[];
  qr_config?: QRConfigType;
  allowed_domains?: string[];
  password?: string;
  scan_limit?: number;
  expiration_date?: string;
  track_analytics?: boolean;
}

// Frontend Type (camelCase)
export interface DynamicQrResponse {
  id: number;
  qrKey: string;
  qrName: string;
  currentDestinationUrl: string;
  qrImageUrl: string;
  redirectUrl: string;
  campaignId?: string;
  userEmail: string;
  isActive: boolean;
  totalScans: number;
  createdAt: string;
  updatedAt: string;
  lastScanned?: string;
  rules?: RedirectRuleDto[];
  qrConfig?: QRConfigType;
  allowedDomains?: string[];
  password?: string;
  scanLimit?: number;
  expirationDate?: string;
  trackAnalytics?: boolean;
}

// ============ Stats Types ============
// API Response (snake_case - from backend)
export interface StatsApiResponse {
  total_links: number;
  total_clicks: number;
  active_links: number;
  top_region: string;
  avg_ctr: number;
  click_trend: { name: string; value: number }[];
  referrers: { name: string; value: number | string }[];
}

// Frontend Type (camelCase)
export interface StatsResponse {
  totalLinks: number;
  totalClicks: number;
  activeLinks: number;
  topRegion: string;
  avgCtr: number;
  clickTrend: { name: string; value: number }[];
  referrers: { name: string; value: number | string }[];
}

export interface PageResponse<T> {
  content: T[];
  totalPages: number;
  totalElements: number;
  size: number;
  number: number;
}

