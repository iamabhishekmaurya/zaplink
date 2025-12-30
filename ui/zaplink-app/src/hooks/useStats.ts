'use client';

import { useState } from 'react';
import api from '@/utils/api';
import axios from 'axios';

export interface StatsData {
  totalClicks: number;
  totalLinks: number;
  avgCtr: number;
  topRegion: string;
  clickTrend: { name: string; clicks: number }[];
  referrers: { name: string; value: number }[];
  devices: { name: string; value: number; color: string }[];
  browsers: { name: string; val: number; icon: string; color: string }[];
}

export const useStats = () => {
  const [stats, setStats] = useState<StatsData | null>(null);
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const fetchStats = async () => {
    setIsLoading(true);
    setError(null);
    try {
      const response = await api.get('/manager/stats');
      setStats(response.data);
    } catch (err: unknown) {
      let message = 'Failed to fetch statistics.';
      if (axios.isAxiosError(err)) {
        message = err.response?.data?.message || message;
      }
      setError(message);
    } finally {
      setIsLoading(false);
    }
  };

  return {
    stats,
    isLoading,
    error,
    fetchStats
  };
};
