'use client';

import { useDispatch, useSelector } from 'react-redux';
import { RootState } from '@/store';
import { loginStart, loginSuccess, loginFailure, logout } from '@/store/slices/authSlice';
import axios from 'axios';
import api from '@/utils/api';
import { toast } from 'sonner';
import { useRouter } from 'next/navigation';

export const useAuth = () => {
  const dispatch = useDispatch();
  const router = useRouter();
  const { user, isAuthenticated, isLoading, error } = useSelector((state: RootState) => state.auth);

  const login = async (credentials: Record<string, string>) => {
    dispatch(loginStart());
    try {
      const response = await api.post('/auth/login', credentials);
      const { userInfo, accessToken, refreshToken } = response.data;
      
      dispatch(loginSuccess({ 
        user: userInfo, 
        token: accessToken, 
        refreshToken 
      }));
      
      toast.success('Successfully logged in!');
      router.push('/dashboard');
    } catch (err: unknown) {
      let message = 'Login failed. Please check your credentials.';
      if (axios.isAxiosError(err)) {
        message = err.response?.data?.message || message;
      }
      dispatch(loginFailure(message));
      toast.error(message);
    }
  };

  const signup = async (userData: Record<string, string>) => {
    dispatch(loginStart());
    try {
      await api.post('/auth/register', userData);
      toast.success('Account created! Please verify your email.');
      router.push('/login');
    } catch (err: unknown) {
      let message = 'Registration failed.';
      if (axios.isAxiosError(err)) {
        message = err.response?.data?.message || message;
      }
      dispatch(loginFailure(message));
      toast.error(message);
    }
  };

  const handleLogout = () => {
    dispatch(logout());
    toast.info('Logged out successfully');
    router.push('/login');
  };

  return {
    user,
    isAuthenticated,
    isLoading,
    error,
    login,
    signup,
    logout: handleLogout
  };
};
