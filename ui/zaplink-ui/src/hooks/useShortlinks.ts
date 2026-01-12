import { useState, useEffect } from 'react';
import { shortlinkService } from '@/lib/api/shortlinkService';
import { ShortLink } from '@/lib/types/apiRequestType';

export const useShortlinks = () => {
  const [shortlinks, setShortlinks] = useState<ShortLink[]>([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);
  const [selectedLinks, setSelectedLinks] = useState<string[]>([]);

  const fetchShortlinks = async () => {
    try {
      setLoading(true);
      setError(null);
      const data = await shortlinkService.getUserLinks();
      setShortlinks(data);
    } catch (err: any) {
      // Handle 401 Unauthorized errors
      if (err.response?.status === 401) {
        // Let the API interceptor handle the redirect
        return;
      }
      setError(err.response?.data?.message || 'Failed to fetch shortlinks');
    } finally {
      setLoading(false);
    }
  };

  const createShortlink = async (data: {
    title: string;
    originalUrl: string;
    platform?: string;
    tags?: string[];
  }) => {
    try {
      const newShortlink = await shortlinkService.createShortLink(data);
      setShortlinks(prev => [newShortlink, ...prev]);
      return newShortlink;
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to create shortlink');
      throw err;
    }
  };

  const getShortlink = async (id: string): Promise<ShortLink> => {
    try {
      const shortlink = await shortlinkService.getShortlink(id);
      return shortlink;
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to fetch shortlink');
      throw err;
    }
  };

  const deleteShortlink = async (id: string) => {
    try {
      await shortlinkService.deleteShortLink(id);
      setShortlinks(prev => prev.filter(link => link.id !== id));
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to delete shortlink');
      throw err;
    }
  };

  const updateShortlink = async (id: string, data: Partial<ShortLink>) => {
    try {
      const updatedShortlink = await shortlinkService.updateShortLink(id, data);
      // Since our workaround creates a new link and deletes the old one,
      // we need to remove the old link and add the new one
      setShortlinks(prev => [
        updatedShortlink,
        ...prev.filter(link => link.id !== id)
      ]);
      return updatedShortlink;
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to update shortlink');
      throw err;
    }
  };

  const copyToClipboard = async (text: string) => {
    try {
      await navigator.clipboard.writeText(text);
      // You could add a toast notification here
    } catch (err) {
      console.error('Failed to copy text: ', err);
      throw err;
    }
  };

  const toggleActive = async (id: string) => {
    try {
      const link = shortlinks.find(l => l.id === id);
      if (link) {
        await updateShortlink(id, { isActive: !link.isActive });
      }
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to toggle shortlink status');
      throw err;
    }
  };

  useEffect(() => {
    fetchShortlinks();
  }, []);

  return {
    shortlinks,
    loading,
    error,
    selectedLinks,
    setSelectedLinks,
    fetchShortlinks,
    getShortlink,
    createShortlink,
    deleteShortlink,
    updateShortlink,
    copyToClipboard,
    toggleActive,
    refetch: fetchShortlinks
  };
};
