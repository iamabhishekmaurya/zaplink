import { createSlice, PayloadAction } from '@reduxjs/toolkit';

interface UserInfo {
  id: string;
  email: string;
  username: string;
  firstName?: string;
  lastName?: string;
  phoneNumber?: string;
  isVerified?: boolean;
  isActive?: boolean;
  createdAt?: string;
}

interface AuthState {
  user: UserInfo | null;
  token: string | null;
  refreshToken: string | null;
  isAuthenticated: boolean;
  isLoading: boolean;
  isInitialized: boolean;
  error: string | null;
}

const initialState: AuthState = {
  user: null,
  token: typeof window !== 'undefined' ? localStorage.getItem('token') : null,
  refreshToken: typeof window !== 'undefined' ? localStorage.getItem('refreshToken') : null,
  isAuthenticated: false,
  isLoading: false,
  isInitialized: false,
  error: null,
};

const authSlice = createSlice({
  name: 'auth',
  initialState,
  reducers: {
    loginStart: (state) => {
      state.isLoading = true;
      state.error = null;
    },
    loginSuccess: (state, action: PayloadAction<{ user: UserInfo; token: string; refreshToken: string }>) => {
      state.isLoading = false;
      state.isAuthenticated = true;
      state.user = action.payload.user;
      state.token = action.payload.token;
      state.refreshToken = action.payload.refreshToken;
      
      localStorage.setItem('token', action.payload.token);
      localStorage.setItem('refreshToken', action.payload.refreshToken);
    },
    loginFailure: (state, action: PayloadAction<string>) => {
      state.isLoading = false;
      state.error = action.payload;
    },
    logout: (state) => {
      state.user = null;
      state.token = null;
      state.refreshToken = null;
      state.isAuthenticated = false;
      localStorage.removeItem('token');
      localStorage.removeItem('refreshToken');
    },
    setUser: (state, action: PayloadAction<UserInfo | null>) => {
      state.user = action.payload;
      state.isAuthenticated = !!action.payload;
    },
    setInitialized: (state, action: PayloadAction<boolean>) => {
      state.isInitialized = action.payload;
    },
    setAuthData: (state, action: PayloadAction<{ user: UserInfo; token: string; refreshToken: string }>) => {
      state.user = action.payload.user;
      state.token = action.payload.token;
      state.refreshToken = action.payload.refreshToken;
      state.isAuthenticated = true;
    }
  },
});

export const { loginStart, loginSuccess, loginFailure, logout, setUser, setInitialized, setAuthData } = authSlice.actions;
export default authSlice.reducer;
