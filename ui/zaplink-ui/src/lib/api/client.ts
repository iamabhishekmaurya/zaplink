import axios from 'axios';
import Cookies from 'js-cookie';

const api = axios.create({
  baseURL: process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8090',
  headers: {
    'Content-Type': 'application/json',
  },
  withCredentials: true, // Enable cookies
});

// Request interceptor to add the access token to headers
api.interceptors.request.use(
  (config) => {
    if (typeof window !== 'undefined') {
      const token = localStorage.getItem('token');
      if (token && config.headers) {
        config.headers.Authorization = `Bearer ${token}`;
      }
    }
    return config;
  },
  (error) => Promise.reject(error)
);

// Response interceptor to handle token expiration/refresh
api.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest = error.config;
    
    // Handle 401 Unauthorized errors - immediate redirect
    if (error.response?.status === 401) {
      // Clear tokens from both localStorage and cookies
      if (typeof window !== 'undefined') {
        localStorage.removeItem('token');
        localStorage.removeItem('refreshToken');
        Cookies.remove('token', { path: '/' });
        Cookies.remove('refreshToken', { path: '/' });
        window.location.replace('/login');
      }
      return Promise.reject(error);
    }
    
    // Handle other errors that might need token refresh
    if (error.response?.status === 403 && !originalRequest._retry) {
      originalRequest._retry = true;
      
      try {
        const refreshToken = localStorage.getItem('refreshToken');
        if (refreshToken) {
          // Attempt to refresh the token using the Refresh endpoint
          const response = await axios.post(`${api.defaults.baseURL}/auth/refresh`, null, {
            params: { refreshToken },
          });
          
          const { accessToken, refreshToken: newRefreshToken } = response.data;
          
          localStorage.setItem('token', accessToken);
          localStorage.setItem('refreshToken', newRefreshToken);
          
          originalRequest.headers.Authorization = `Bearer ${accessToken}`;
          return api(originalRequest);
        }
      } catch {
        // Refresh token failed, logout user
        if (typeof window !== 'undefined') {
          localStorage.removeItem('token');
          localStorage.removeItem('refreshToken');
          Cookies.remove('token', { path: '/' });
          Cookies.remove('refreshToken', { path: '/' });
          window.location.replace('/login');
        }
      }
    }
    
    return Promise.reject(error);
  }
);

export default api;
