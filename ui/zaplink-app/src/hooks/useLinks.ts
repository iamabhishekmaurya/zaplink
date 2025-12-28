'use client';

import { useDispatch, useSelector } from 'react-redux';
import { RootState } from '@/store';
import { addLink, setLinks, setLoading, setError } from '@/store/slices/linkSlice';
import axios from 'axios';
import api from '@/utils/api';
import { toast } from 'sonner';

export const useLinks = () => {
  const dispatch = useDispatch();
  const { links, recentLinks, isLoading, error } = useSelector((state: RootState) => state.links);

  const shortenUrl = async (originalUrl: string) => {
    dispatch(setLoading(true));
    try {
      const response = await api.post('/shortener/short/url', { originalUrl });
      const newLink = {
        id: response.data.id || Math.random().toString(36).substr(2, 9),
        originalUrl: response.data.originalUrl,
        shortUrl: response.data.url,
        clickCount: 0,
        createdAt: new Date().toISOString()
      };
      
      dispatch(addLink(newLink));
      toast.success('URL shortened successfully!');
      return newLink;
    } catch (err: unknown) {
      let message = 'Failed to shorten URL.';
      if (axios.isAxiosError(err)) {
        message = err.response?.data?.message || message;
      }
      dispatch(setError(message));
      toast.error(message);
      return null;
    } finally {
      dispatch(setLoading(false));
    }
  };

  const fetchUserLinks = async () => {
    dispatch(setLoading(true));
    try {
      // Assuming manager service has an endpoint to get user links
      const response = await api.get('/manager/links');
      const data = Array.isArray(response.data) ? response.data : (response.data.links || []);
      dispatch(setLinks(data));
    } catch (err: unknown) {
      let message = 'Failed to fetch links.';
      if (axios.isAxiosError(err)) {
        message = err.response?.data?.message || message;
      }
      dispatch(setError(message));
    } finally {
      dispatch(setLoading(false));
    }
  };

  return {
    links,
    recentLinks,
    isLoading,
    error,
    shortenUrl,
    fetchUserLinks
  };
};
