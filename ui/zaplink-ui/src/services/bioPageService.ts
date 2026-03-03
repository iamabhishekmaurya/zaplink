import apiClient from "@/services/client";

// ============ API Response Types (snake_case - from backend) ============
export interface BioPageApiResponse {
  id: number;
  username: string;
  owner_id: string;
  bio_text?: string;
  avatar_url?: string;
  theme_config?: string;
  title?: string;
  cover_url?: string;
  seo_meta?: string;
  is_public: boolean;
  created_at: string;
  updated_at: string;
  effects: {
    backgroundType: 'solid' | 'gradient' | 'image' | 'video' | 'particles';
    backgroundImage?: string;
    backgroundGradient?: string;
    particles?: boolean;
  };
  bioLinks?: BioLinkApiResponse[];
}

interface BioLinkApiResponse {
  id: number;
  page_id: number;
  title: string;
  url?: string;
  type: string;
  is_active: boolean;
  sort_order: number;
  price?: number;
  currency?: string;
  metadata?: string; // API returns JSON string
  schedule_from?: string;
  schedule_to?: string;
  icon_url?: string;
  thumbnail_url?: string;
  embedCode?: string;
  gateType?: string;
  gateValue?: string;
  gateMessage?: string;
  created_at: string;
  updated_at: string;
}

// ============ Frontend Types (camelCase) ============
// ============ Frontend Types (camelCase) ============

export enum BioLinkType {
  LINK = 'LINK',
  SOCIAL = 'SOCIAL',
  PRODUCT = 'PRODUCT', // Commerce
  EMBED = 'EMBED', // YouTube, Spotify, etc.
  SCHEDULED = 'SCHEDULED',
  GATED = 'GATED', // Password/Email
  PAYMENT = 'PAYMENT', // Razorpay stub
}

export interface BioPageThemeConfig {
  palette: 'minimal' | 'dark' | 'neon' | 'commerce' | 'portfolio' | 'gradient' | 'editorial' | 'compact' | 'custom';
  customColors?: {
    background: string;
    text: string;
    primary: string;
    secondary: string;
  };
  fontFamily?: string;
  buttonShape?: 'rounded' | 'pill' | 'sharp';
  backgroundStyle?: 'solid' | 'gradient' | 'image';
  backgroundImage?: string;
}

export interface BioPage {
  id: string; // Changed to string for frontend consistency
  username: string;
  ownerId: string;
  bioText?: string;
  avatarUrl?: string;
  themeConfig: BioPageThemeConfig; // Changed from string? to strong type
  title?: string;
  coverUrl?: string;
  seoMeta?: {
    title?: string;
    description?: string;
    keywords?: string[];
    ogImage?: string;
  };
  isPublic: boolean;
  createdAt: string;
  updatedAt: string;
  effects: {
    backgroundType: 'solid' | 'gradient' | 'image' | 'video' | 'particles';
    backgroundImage?: string;
    backgroundGradient?: string;
    particles?: boolean;
  };
  bioLinks?: BioLink[];
}

export interface BioLink {
  id: string; // Changed to string
  pageId: string; // Changed to string
  title: string;
  url?: string;
  type: BioLinkType | string; // Allow string for backward compat but prefer Enum
  isActive: boolean;
  sortOrder: number;
  price?: number;
  currency?: string;
  metadata?: {
    sectionType?: string;
    childLinks?: Array<{
      title: string;
      url?: string;
      thumbnailUrl?: string;
    }>;
    originalPrice?: number;
    [key: string]: any;
  };
  scheduleFrom?: string;
  scheduleTo?: string;
  iconUrl?: string;
  thumbnailUrl?: string;
  embedCode?: string;
  gateType?: string;
  gateValue?: string;
  gateMessage?: string;
  createdAt: string;
  updatedAt: string;
}

// ============ Request Types ============
export interface CreateBioPageRequest {
  username: string;
  owner_id: string;
  bio_text?: string;
  avatar_url?: string;
  theme_config?: string; // Backend expects JSON string
  title?: string;
  cover_url?: string;
  seo_meta?: string;
  is_public?: boolean;
}

export interface UpdateBioPageRequest {
  bio_text?: string;
  avatar_url?: string;
  theme_config?: string;
  is_active?: boolean;
  title?: string;
  cover_url?: string;
  seo_meta?: string;
  is_public?: boolean;
}

export interface CreateBioLinkRequest {
  page_id: number;
  title: string;
  url?: string;
  type: string;
  is_active?: boolean;
  sort_order?: number;
  price?: number;
  currency?: string;
  metadata?: string;
  schedule_from?: string;
  schedule_to?: string;
  icon_url?: string;
  thumbnail_url?: string;
}

export interface UpdateBioLinkRequest {
  title?: string;
  url?: string;
  type?: string;
  is_active?: boolean;
  sort_order?: number;
  price?: number;
  currency?: string;
  metadata?: string;
  schedule_from?: string;
  schedule_to?: string;
  icon_url?: string;
  thumbnail_url?: string;
}

export interface ReorderLinksRequest {
  link_orders: { link_id: number; sort_order: number }[];
}

// ============ Transformation Functions ============
function transformBioLink(apiLink: BioLinkApiResponse): BioLink {
  let parsedMetadata: BioLink['metadata'] = undefined;

  if (apiLink.metadata) {
    try {
      parsedMetadata = JSON.parse(apiLink.metadata);
    } catch {
      parsedMetadata = undefined;
    }
  }

  return {
    id: String(apiLink.id),
    pageId: String(apiLink.page_id),
    title: apiLink.title,
    url: apiLink.url,
    type: apiLink.type,
    isActive: apiLink.is_active,
    sortOrder: apiLink.sort_order,
    price: apiLink.price,
    currency: apiLink.currency,
    metadata: parsedMetadata,
    scheduleFrom: apiLink.schedule_from,
    scheduleTo: apiLink.schedule_to,
    iconUrl: apiLink.icon_url,
    thumbnailUrl: apiLink.thumbnail_url,
    embedCode: parsedMetadata?.embedCode,
    gateType: parsedMetadata?.gateType,
    gateValue: parsedMetadata?.gateValue,
    gateMessage: parsedMetadata?.gateMessage,
    createdAt: apiLink.created_at,
    updatedAt: apiLink.updated_at,
  };
}

function transformBioPage(apiPage: BioPageApiResponse): BioPage {
  let parsedThemeConfig: BioPageThemeConfig = {
    palette: 'minimal',
    customColors: { background: '#ffffff', text: '#000000', primary: '#000000', secondary: '#888888' }
  };
  let parsedSeoMeta: BioPage['seoMeta'] = undefined;

  if (apiPage.theme_config) {
    try {
      parsedThemeConfig = JSON.parse(apiPage.theme_config);
    } catch {
      console.error('Failed to parse theme_config');
    }
  }

  if (apiPage.seo_meta) {
    try {
      parsedSeoMeta = JSON.parse(apiPage.seo_meta);
    } catch {
      parsedSeoMeta = undefined;
    }
  }

  return {
    id: String(apiPage.id),
    username: apiPage.username,
    ownerId: apiPage.owner_id,
    bioText: apiPage.bio_text,
    avatarUrl: apiPage.avatar_url,
    themeConfig: parsedThemeConfig,
    title: apiPage.title,
    coverUrl: apiPage.cover_url,
    seoMeta: parsedSeoMeta,
    isPublic: apiPage.is_public ?? true,
    createdAt: apiPage.created_at,
    updatedAt: apiPage.updated_at,
    effects: apiPage.effects || {
      backgroundType: 'solid',
      particles: false,
    },
    bioLinks: apiPage.bioLinks?.map(transformBioLink),
  };
}

// ============ Service Class ============
class BioPageService {
  private readonly baseUrl = ''; // Remove /api prefix since apiClient already has base URL

  // ========== BioPage Write Operations (Core Service) ==========
  // Core BioController: /page (Singular) + /link (Singular)

  async createBioPage(request: CreateBioPageRequest): Promise<BioPage> {
    try {
      const response = await apiClient.post<BioPageApiResponse>(`/wr/bio/page`, request);
      return transformBioPage(response.data);
    } catch (error) {
      throw error;
    }
  }

  async updateBioPage(pageId: string | number, request: UpdateBioPageRequest): Promise<BioPage> {
    const response = await apiClient.put<BioPageApiResponse>(`/wr/bio/page/${pageId}`, request);
    return transformBioPage(response.data);
  }

  async deleteBioPage(pageId: string | number): Promise<void> {
    await apiClient.delete(`/wr/bio/page/${pageId}`);
  }

  // ========== BioPage Read Operations (Manager Service) ==========
  // Manager BioController: /pages (Plural)

  async getBioPageById(id: string | number): Promise<BioPage> {
    const response = await apiClient.get<BioPageApiResponse>(`/rd/bio/pages/id/${id}`);
    return transformBioPage(response.data);
  }

  async getBioPageByUsername(username: string): Promise<BioPage> {
    const response = await apiClient.get<BioPageApiResponse>(`/rd/bio/pages/${username}`);
    return transformBioPage(response.data);
  }

  async getBioPagesByOwnerId(ownerId: string): Promise<BioPage[]> {
    const response = await apiClient.get<BioPageApiResponse[]>(`/rd/bio/pages/owner/${ownerId}`);
    return response.data.map(transformBioPage);
  }

  async getAllBioPages(): Promise<BioPage[]> {
    const response = await apiClient.get<BioPageApiResponse[]>(`/rd/bio/pages`);
    return response.data.map(transformBioPage);
  }

  // ========== BioLink Write Operations (Core Service) ==========

  async createBioLink(pageId: string | number, request: Omit<CreateBioLinkRequest, 'page_id'>): Promise<BioLink> {
    const payload: CreateBioLinkRequest = { ...request, page_id: Number(pageId) };
    console.log('[BioPageService.createBioLink] Sending request:', JSON.stringify(payload, null, 2));
    const response = await apiClient.post<BioLinkApiResponse>(`/wr/bio/link`, payload);
    console.log('[BioPageService.createBioLink] Response:', response.data);
    return transformBioLink(response.data);
  }

  async updateBioLink(linkId: string | number, request: UpdateBioLinkRequest): Promise<BioLink> {
    const response = await apiClient.put<BioLinkApiResponse>(`/wr/bio/link/${linkId}`, request);
    return transformBioLink(response.data);
  }

  async deleteBioLink(linkId: string | number): Promise<void> {
    await apiClient.delete(`/wr/bio/link/${linkId}`);
  }

  async reorderLinks(pageId: string | number, linkIds: (string | number)[]): Promise<void> {
    const numericLinkIds = linkIds.map(id => Number(id)).filter(id => !isNaN(id));
    await apiClient.put(`/wr/bio/link/${pageId}/reorder`, { link_orders: numericLinkIds.map((id, index) => ({ link_id: id, sort_order: index })) });
  }

  async trackClick(linkId: string | number): Promise<void> {
    try {
      await apiClient.post(`/wr/bio/link/${linkId}/click`, {});
    } catch (e) {
      console.error('Failed to track click', e);
    }
  }

  // ========== BioLink Read Operations (Manager Service) ==========
  // Manager BioController: /links (Plural)

  async getBioLinkById(id: number): Promise<BioLink> {
    const response = await apiClient.get<BioLinkApiResponse>(`/rd/bio/links/${id}`);
    return transformBioLink(response.data);
  }

  async getBioLinksByPageId(pageId: number): Promise<BioLink[]> {
    const response = await apiClient.get<BioLinkApiResponse[]>(`/rd/bio/links/page/${pageId}`);
    return response.data.map(transformBioLink);
  }

  async getActiveBioLinksByPageId(pageId: number): Promise<BioLink[]> {
    const response = await apiClient.get<BioLinkApiResponse[]>(`/rd/bio/links/page/${pageId}/active`);
    return response.data.map(transformBioLink);
  }

  async getAllBioLinks(): Promise<BioLink[]> {
    const response = await apiClient.get<BioLinkApiResponse[]>(`/rd/bio/links`);
    return response.data.map(transformBioLink);
  }

  // ========== Public Bio Page (Redirect Service) ==========

  async getPublicBioPage(username: string): Promise<BioPage> {
    const response = await apiClient.get<BioPageApiResponse>(`/b/${username}`);
    return transformBioPage(response.data);
  }
}

export const bioPageService = new BioPageService();
