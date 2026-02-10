import { useState, useEffect } from 'react';
import { shortlinkService } from '@/services/shortlinkService';
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
    } catch {
      // Silent fail for analytics fetch
    } finally {
      setLoading(false);
    }
  };

  const createShortlink = async (data: {
    title: string;
    originalUrl: string;
    platform?: string;
    tags?: string[];
    rules?: any[];
  }) => {
    try {
      const newShortlink = await shortlinkService.createShortLink(data);
      setShortlinks(prev => [newShortlink, ...prev]);
      return newShortlink;
    } catch {
      setError('Failed to create shortlink');
    }
  };

  const getShortlink = async (id: string): Promise<ShortLink> => {
    try {
      const shortlink = await shortlinkService.getShortlink(id);
      return shortlink;
    } catch {
      setError('Failed to fetch shortlink');
      throw new Error('Failed to fetch shortlink');
    }
  };

  const deleteShortlink = async (id: string) => {
    try {
      const link = shortlinks.find(l => l.id === id);
      if (link) {
        const shortUrlKey = link.shortUrlKey || link.shortlink;
        if (!shortUrlKey) {
          throw new Error('Short URL key is required to delete link');
        }
        await shortlinkService.deleteByKey(shortUrlKey);
        setShortlinks(prev => prev.filter(link => link.id !== id));
      }
    } catch (err: any) {
      setError(err.response?.data?.message || 'Failed to delete shortlink');
      throw err;
    }
  };

  const updateShortlink = async (id: string, data: Partial<ShortLink>) => {
    try {
      const updatedShortlink = await shortlinkService.updateShortLink(id, data);
      setShortlinks(prev => [
        updatedShortlink,
        ...prev.filter(link => link.id !== id)
      ]);
      return updatedShortlink;
    } catch {
      setError('Failed to update shortlink');
      throw new Error('Failed to update shortlink');
    }
  };

  const copyToClipboard = async (text: string) => {
    try {
      await navigator.clipboard.writeText(text);
    } catch {
      // Silent fail for clipboard copy
    }
  };

  const toggleActive = async (id: string) => {
    try {
      const link = shortlinks.find(l => l.id === id);
      if (link && link.shortUrlKey) {
        const newActiveState = !link.isActive;
        const updatedLink = await shortlinkService.toggleStatus(link.shortUrlKey, newActiveState);
        // Update the local state with the toggled status
        setShortlinks(prev =>
          prev.map(l => l.id === id ? { ...l, isActive: newActiveState } : l)
        );
        return updatedLink;
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
