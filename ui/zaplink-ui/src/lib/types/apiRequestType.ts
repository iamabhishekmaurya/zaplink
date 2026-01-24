export interface QRConfigType {
  data: string;
  size: number;
  margin: number;
  errorCorrectionLevel: string;
  transparentBackground: boolean;
  backgroundColor: string;
  body: {
    shape: string;
    color: string;
    colorDark?: string;
    gradientLinear: boolean;
  };
  eye: {
    shape: string;
    colorOuter: string;
    colorInner: string;
  };
  logo?: {
    logoPath?: string;
    sizeRatio: number;
    padding: number;
    backgroundColor: string;
    backgroundEnabled: boolean;
    backgroundRounded: boolean;
    backgroundCornerRadius: number;
    removeQuietZone: boolean;
    marginSize: number;
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
}

export interface GetUserLinksResponse extends BaseResponse {
  data: ShortLink[];
}

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
}

export interface PageResponse<T> {
  content: T[];
  totalPages: number;
  totalElements: number;
  size: number;
  number: number;
}

