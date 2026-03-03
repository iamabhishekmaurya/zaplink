import axios from 'axios';
import Cookies from 'js-cookie';

const apiBase = process.env.NEXT_PUBLIC_API_BASE || process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8090';

// On server, ensure we have an absolute URL
const baseURL = (typeof window === 'undefined' && apiBase.startsWith('/'))
  ? `http://localhost:8090${apiBase}`
  : apiBase;

const api = axios.create({
  baseURL,
  headers: {
    'Content-Type': 'application/json',
    'X-API-Version': '1',
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

    // Attempt token refresh on 401 Unauthorized, but only once per request
    if (error.response?.status === 401 && !originalRequest._retry) {
      originalRequest._retry = true;

      if (typeof window !== 'undefined') {
        const refreshToken = localStorage.getItem('refreshToken');

        if (refreshToken) {
          try {
            // Call the refresh endpoint with the stored refresh token
            const refreshResponse = await axios.post(
              `${api.defaults.baseURL}/auth/refresh`,
              null,
              { params: { refreshToken } }
            );

            const { accessToken, refreshToken: newRefreshToken } = refreshResponse.data;

            // Store the new tokens
            localStorage.setItem('token', accessToken);
            localStorage.setItem('refreshToken', newRefreshToken);
            Cookies.set('token', accessToken, { expires: 7, path: '/', sameSite: 'Strict' });
            Cookies.set('refreshToken', newRefreshToken, { expires: 30, path: '/', sameSite: 'Strict' });

            // Retry the original request with the new access token
            originalRequest.headers.Authorization = `Bearer ${accessToken}`;
            return api(originalRequest);
          } catch {
            // Refresh failed — clear everything and redirect to login
            localStorage.removeItem('token');
            localStorage.removeItem('refreshToken');
            Cookies.remove('token', { path: '/' });
            Cookies.remove('refreshToken', { path: '/' });

            if (window.location.pathname !== '/login' && window.location.pathname !== '/signup') {
              window.location.replace('/login');
            }
          }
        } else {
          // No refresh token available — redirect to login
          if (window.location.pathname !== '/login' && window.location.pathname !== '/signup') {
            window.location.replace('/login');
          }
        }
      }
    }

    return Promise.reject(error);
  }
);

export default api;
