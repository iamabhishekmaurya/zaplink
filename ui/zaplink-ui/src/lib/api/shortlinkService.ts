import api from "@/lib/api/client";
import { API_ENDPOINTS } from '@/lib/constant/apiConstant';
import { ShortLink, GetUserLinksResponse } from '@/lib/types/apiRequestType';

export interface StatsResponse {
  totalLinks: number;
  totalClicks: number;
  activelinks: number;
  topRegion: string;
  avgCtr: number;
  clickTrend: { name: string; value: number }[];
  referrers: { name: string; value: number | string }[];
}

// Utility function to extract title from URL
export const extractTitleFromUrl = (url: string): string => {
  try {
    const urlObj = new URL(url);
    const hostname = urlObj.hostname;
    const pathname = urlObj.pathname;

    // Extract domain name
    const domain = hostname.replace('www.', '');

    // For YouTube, extract channel/video title
    if (domain.includes('youtube.com')) {
      return 'YouTube Channel';
    }

    // For GitHub, extract repo name
    if (domain.includes('github.com')) {
      const pathParts = pathname.split('/').filter(Boolean);
      if (pathParts.length >= 2) {
        return `${pathParts[0]}/${pathParts[1]}`;
      }
      return 'GitHub Repository';
    }

    // For other platforms, use domain
    return domain.charAt(0).toUpperCase() + domain.slice(1);
  } catch {
    return 'Untitled Link';
  }
};

// Utility function to extract platform from URL
export const extractPlatformFromUrl = (url: string): string => {
  try {
    const urlObj = new URL(url);
    const hostname = urlObj.hostname;

    if (hostname.includes('youtube.com')) return 'YouTube';
    if (hostname.includes('github.com')) return 'GitHub';
    if (hostname.includes('twitter.com')) return 'Twitter';
    if (hostname.includes('linkedin.com')) return 'LinkedIn';
    if (hostname.includes('instagram.com')) return 'Instagram';
    if (hostname.includes('facebook.com')) return 'Facebook';

    return 'Website';
  } catch {
    return 'Unknown';
  }
};

export const shortlinkService = {
  // Get all short links for the current user
  getUserLinks: async (): Promise<ShortLink[]> => {
    try {
      const response = await api.get<any>(API_ENDPOINTS.GET_USER_LINKS);

      // Handle different response structures
      let linksData = response.data;

      // If response has nested data structure
      if (response.data && response.data.data && Array.isArray(response.data.data)) {
        linksData = response.data.data;
      }

      // Transform API response to match our ShortLink interface
      const transformedLinks = linksData.map((item: any) => ({
        id: item.id,
        title: item.title || extractTitleFromUrl(item.originalUrl),
        shortlink: item.shortUrl || item.shortUrlKey,
        originalUrl: item.originalUrl,
        tags: item.tags || [],
        platform: item.platform || extractPlatformFromUrl(item.originalUrl),
        hasAnalytics: item.hasAnalytics !== false,
        isActive: item.isActive !== false,
        createdAt: item.createdAt,
        updatedAt: item.updatedAt || item.createdAt,
        userId: item.userId || 'current-user',
        clicks: item.clickCount || item.clicks || 0,
        shortUrlKey: item.shortUrlKey
      }));

      return transformedLinks;
    } catch (error) {
      console.error('Error fetching user links:', error);
      throw error;
    }
  },

  // Create a new short link
  createShortLink: async (data: {
    title: string;
    originalUrl: string;
    platform?: string;
    tags?: string[];
    rules?: any[];
  }) => {
    try {
      const response = await api.post(API_ENDPOINTS.SHORTEN_URL, data);
      return response.data;
    } catch (error) {
      console.error('Error creating short link:', error);
      throw error;
    }
  },

  // Delete a short link
  deleteShortLink: async (id: string) => {
    try {
      await api.delete(API_ENDPOINTS.DELETE_LINK(id));
    } catch (error) {
      console.error('Error deleting short link:', error);
      throw error;
    }
  },

  // Update a short link (workaround - create new and delete old)
  updateShortLink: async (id: string, data: {
    title?: string;
    platform?: string;
    tags?: string[];
    rules?: any[];
  }) => {
    try {
      // Get the existing link to preserve the original URL
      const existingLinks = await shortlinkService.getUserLinks();

      // Try both string and number comparison for robust ID matching
      const existingLink = existingLinks.find(link =>
        link.id === id ||
        link.id.toString() === id ||
        String(link.id) === String(id)
      );

      if (!existingLink) {
        throw new Error(`Short link with ID ${id} not found`);
      }

      // Create a new link with the updated data
      const newLinkData = {
        title: data.title || existingLink.title,
        originalUrl: existingLink.originalUrl,
        platform: data.platform || existingLink.platform,
        tags: data.tags || existingLink.tags,
        rules: "rules" in data ? (data as any).rules : existingLink.rules
      };

      const newLink = await shortlinkService.createShortLink(newLinkData);

      // Delete the old link using the same ID comparison
      const linkToDelete = existingLinks.find(link =>
        link.id === id ||
        link.id.toString() === id ||
        String(link.id) === String(id)
      );

      if (linkToDelete) {
        await shortlinkService.deleteShortLink(linkToDelete.id);
      }

      return newLink;
    } catch (error) {
      console.error('Error updating short link:', error);
      throw error;
    }
  },

  // Get a single short link by ID (workaround since backend doesn't have single link endpoint)
  getShortlink: async (id: string): Promise<ShortLink> => {
    try {
      // Fetch all links and find the one with matching ID
      const allLinks = await shortlinkService.getUserLinks();

      // Try both string and number comparison for robust ID matching
      const link = allLinks.find(item =>
        item.id === id ||
        item.id.toString() === id ||
        String(item.id) === String(id)
      );

      if (!link) {
        throw new Error(`Short link with ID ${id} not found`);
      }

      return link;
    } catch (error) {
      console.error('Error fetching short link:', error);
      throw error;
    }
  },

  // Get link analytics (not supported by backend yet)
  getLinkAnalytics: async (id: string) => {
    try {
      throw new Error('Analytics functionality is not yet supported by the backend API');
    } catch (error) {
      console.error('Error fetching link analytics:', error);
      throw error;
    }
  },

  // Get user stats (total links, clicks, active links, trends)
  getUserStats: async () => {
    try {
      // Assuming API_ENDPOINTS.GET_USER_STATS is defined as "/short/stats" or similar depending on your constant file
      // If not defined, I'll assume "/short/stats" based on the controller code I saw
      const response = await api.get(API_ENDPOINTS.GET_STATS);
      return response.data;
    } catch (error) {
      console.error('Error fetching user stats:', error);
      throw error;
    }
  }
};
