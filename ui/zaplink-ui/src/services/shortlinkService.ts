import api from "@/services/client";
import { API_ENDPOINTS } from '@/lib/constants/apiConstant';
import { ShortLink, LinkApiResponse, StatsApiResponse, StatsResponse, RedirectRuleDto } from '@/lib/types/apiRequestType';

// ============ Transformation Functions ============
function transformLinkResponse(apiLink: LinkApiResponse): ShortLink {
  return {
    id: String(apiLink.id),
    title: apiLink.title || extractTitleFromUrl(apiLink.original_url),
    shortlink: apiLink.short_url || apiLink.short_url_key,
    originalUrl: apiLink.original_url,
    tags: apiLink.tags || [],
    platform: apiLink.platform || extractPlatformFromUrl(apiLink.original_url),
    hasAnalytics: true,
    isActive: apiLink.status === 'ACTIVE',
    createdAt: apiLink.created_at,
    updatedAt: apiLink.created_at, // API doesn't have updatedAt, use createdAt
    userId: 'current-user',
    clicks: apiLink.click_count || 0,
    shortUrlKey: apiLink.short_url_key,
    rules: apiLink.rules || []
  };
}

function transformStatsResponse(apiStats: StatsApiResponse): StatsResponse {
  return {
    totalLinks: apiStats.total_links,
    totalClicks: apiStats.total_clicks,
    activeLinks: apiStats.active_links,
    topRegion: apiStats.top_region,
    avgCtr: apiStats.avg_ctr,
    clickTrend: apiStats.click_trend,
    referrers: apiStats.referrers
  };
}

// ============ Utility Functions ============
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

// ============ Request Types ============
export interface CreateShortLinkRequest {
  original_url: string;
  title?: string;
  platform?: string;
  tags?: string[];
  rules?: RedirectRuleDto[];
  traceId?: string;
}

export interface UpdateShortLinkRequest {
  short_url_key: string;
  title?: string;
  platform?: string;
  tags?: string[];
  rules?: RedirectRuleDto[];
}

// ============ Service ============
export const shortlinkService = {
  // Get all short links for the current user
  getUserLinks: async (): Promise<ShortLink[]> => {
    try {
      const response = await api.get<LinkApiResponse[] | { data: LinkApiResponse[] }>(API_ENDPOINTS.GET_USER_LINKS);

      // Handle different response structures
      let linksData: LinkApiResponse[] = [];

      // If response has nested data structure
      if (response.data && 'data' in response.data && Array.isArray(response.data.data)) {
        linksData = response.data.data;
      } else if (Array.isArray(response.data)) {
        linksData = response.data;
      }

      // Transform API response to match our ShortLink interface
      return linksData.map(transformLinkResponse);
    } catch (error) {
      throw error;
    }
  },

  // Create a new short link
  createShortLink: async (data: {
    title: string;
    originalUrl: string;
    platform?: string;
    tags?: string[];
    rules?: RedirectRuleDto[];
  }): Promise<ShortLink> => {
    try {
      const request: CreateShortLinkRequest = {
        original_url: data.originalUrl,
        title: data.title,
        platform: data.platform,
        tags: data.tags,
        rules: data.rules
      };
      const response = await api.post<LinkApiResponse>(API_ENDPOINTS.SHORTEN_URL, request);

      return transformLinkResponse(response.data);
    } catch (error) {
      throw error;
    }
  },

  // Delete a short link
  deleteShortLink: async (id: string): Promise<void> => {
    try {
      await api.delete(API_ENDPOINTS.DELETE_BY_KEY(id));
    } catch (error) {
      throw error;
    }
  },

  // Update a short link
  updateShortLink: async (id: string, data: {
    title?: string;
    platform?: string;
    tags?: string[];
    rules?: RedirectRuleDto[];
    shortUrlKey?: string; // Required for update operation
  }): Promise<ShortLink> => {
    try {
      const updatePayload: UpdateShortLinkRequest = {
        short_url_key: data.shortUrlKey || '',
        title: data.title,
        platform: data.platform,
        tags: data.tags,
        rules: data.rules
      };

      const response = await api.put<LinkApiResponse>(API_ENDPOINTS.SHORTEN_URL, updatePayload);

      return transformLinkResponse(response.data);
    } catch (error) {
      throw error;
    }
  },

  // Get a single short link by ID
  getShortlink: async (id: string): Promise<ShortLink> => {
    try {
      // Use the new direct endpoint to get a single link
      const response = await api.get<LinkApiResponse>(API_ENDPOINTS.GET_LINK_BY_ID(id));
      return transformLinkResponse(response.data);
    } catch (error) {
      throw error;
    }
  },

  // Toggle link status
  toggleStatus: async (shortUrlKey: string, active: boolean): Promise<ShortLink> => {
    try {
      const response = await api.patch<LinkApiResponse>(API_ENDPOINTS.TOGGLE_STATUS(shortUrlKey, active));
      return transformLinkResponse(response.data);
    } catch (error) {
      throw error;
    }
  },

  // Delete link by short URL key
  deleteByKey: async (shortUrlKey: string): Promise<void> => {
    try {
      await api.delete(API_ENDPOINTS.DELETE_BY_KEY(shortUrlKey));
    } catch (error) {
      throw error;
    }
  },

  // Get link analytics
  getLinkAnalytics: async (id: string): Promise<void> => {
    throw new Error('Analytics functionality is not yet supported by the backend API');
  },

  // Get user stats
  getUserStats: async (): Promise<StatsResponse> => {
    try {
      const response = await api.get<StatsApiResponse>(API_ENDPOINTS.GET_STATS);
      return transformStatsResponse(response.data);
    } catch (error) {
      throw error;
    }
  }
};
