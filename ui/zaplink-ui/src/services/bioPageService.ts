import apiClient from "@/services/client";

// ============ API Response Types (snake_case - from backend) ============
interface BioPageApiResponse {
  id: number;
  username: string;
  owner_id: string;
  bio_text?: string;
  avatar_url?: string;
  theme_config?: string;
  created_at: string;
  updated_at: string;
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
  created_at: string;
  updated_at: string;
}

// ============ Frontend Types (camelCase) ============
export interface BioPage {
  id: number;
  username: string;
  ownerId: string;
  bioText?: string;
  avatarUrl?: string;
  themeConfig?: string;
  createdAt: string;
  updatedAt: string;
  bioLinks?: BioLink[];
}

export interface BioLink {
  id: number;
  pageId: number;
  title: string;
  url?: string;
  type: string;
  isActive: boolean;
  sortOrder: number;
  price?: number;
  currency?: string;
  createdAt: string;
  updatedAt: string;
}

// ============ Request Types ============
export interface CreateBioPageRequest {
  username: string;
  owner_id: string;
  bio_text?: string;
  avatar_url?: string;
  theme_config?: string;
}

export interface UpdateBioPageRequest {
  bio_text?: string;
  avatar_url?: string;
  theme_config?: string;
  is_active?: boolean;
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
}

export interface UpdateBioLinkRequest {
  title?: string;
  url?: string;
  type?: string;
  is_active?: boolean;
  sort_order?: number;
  price?: number;
  currency?: string;
}

export interface ReorderLinksRequest {
  linkOrders: { linkId: number; sortOrder: number }[];
}

// ============ Transformation Functions ============
function transformBioLink(apiLink: BioLinkApiResponse): BioLink {
  return {
    id: apiLink.id,
    pageId: apiLink.page_id,
    title: apiLink.title,
    url: apiLink.url,
    type: apiLink.type,
    isActive: apiLink.is_active,
    sortOrder: apiLink.sort_order,
    price: apiLink.price,
    currency: apiLink.currency,
    createdAt: apiLink.created_at,
    updatedAt: apiLink.updated_at,
  };
}

function transformBioPage(apiPage: BioPageApiResponse): BioPage {
  return {
    id: apiPage.id,
    username: apiPage.username,
    ownerId: apiPage.owner_id,
    bioText: apiPage.bio_text,
    avatarUrl: apiPage.avatar_url,
    themeConfig: apiPage.theme_config,
    createdAt: apiPage.created_at,
    updatedAt: apiPage.updated_at,
    bioLinks: apiPage.bioLinks?.map(transformBioLink),
  };
}

// ============ Service Class ============
class BioPageService {
  private readonly baseUrl = '/api';

  // ========== BioPage Write Operations (Core Service) ==========

  async createBioPage(request: CreateBioPageRequest): Promise<BioPage> {
    const response = await apiClient.post<BioPageApiResponse>(`${this.baseUrl}/wr/bio-pages`, request);
    return transformBioPage(response.data);
  }

  async updateBioPage(pageId: number, request: UpdateBioPageRequest): Promise<BioPage> {
    const response = await apiClient.put<BioPageApiResponse>(`${this.baseUrl}/wr/bio-pages/${pageId}`, request);
    return transformBioPage(response.data);
  }

  async deleteBioPage(pageId: number): Promise<void> {
    await apiClient.delete(`${this.baseUrl}/wr/bio-pages/${pageId}`);
  }

  // ========== BioPage Read Operations (Manager Service) ==========

  async getBioPageById(id: string | number): Promise<BioPage> {
    const response = await apiClient.get<BioPageApiResponse>(`${this.baseUrl}/rd/bio-pages/${id}`);
    return transformBioPage(response.data);
  }

  async getBioPageByUsername(username: string): Promise<BioPage> {
    const response = await apiClient.get<BioPageApiResponse>(`${this.baseUrl}/rd/bio-pages/username/${username}`);
    return transformBioPage(response.data);
  }

  async getBioPagesByOwnerId(ownerId: string): Promise<BioPage[]> {
    const response = await apiClient.get<BioPageApiResponse[]>(`${this.baseUrl}/rd/bio-pages/owner/${ownerId}`);
    return response.data.map(transformBioPage);
  }

  async getAllBioPages(): Promise<BioPage[]> {
    const response = await apiClient.get<BioPageApiResponse[]>(`${this.baseUrl}/rd/bio-pages`);
    return response.data.map(transformBioPage);
  }

  // ========== BioLink Write Operations (Core Service) ==========

  async createBioLink(request: CreateBioLinkRequest): Promise<BioLink> {
    const response = await apiClient.post<BioLinkApiResponse>(`${this.baseUrl}/wr/bio-links`, request);
    return transformBioLink(response.data);
  }

  async updateBioLink(linkId: number, request: UpdateBioLinkRequest): Promise<BioLink> {
    const response = await apiClient.put<BioLinkApiResponse>(`${this.baseUrl}/wr/bio-links/${linkId}`, request);
    return transformBioLink(response.data);
  }

  async deleteBioLink(linkId: number): Promise<void> {
    await apiClient.delete(`${this.baseUrl}/wr/bio-links/${linkId}`);
  }

  async reorderLinks(pageId: number, linkIds: number[]): Promise<void> {
    await apiClient.put(`${this.baseUrl}/wr/bio-pages/${pageId}/links/reorder`, linkIds);
  }

  // ========== BioLink Read Operations (Manager Service) ==========

  async getBioLinkById(id: number): Promise<BioLink> {
    const response = await apiClient.get<BioLinkApiResponse>(`${this.baseUrl}/rd/bio-links/${id}`);
    return transformBioLink(response.data);
  }

  async getBioLinksByPageId(pageId: number): Promise<BioLink[]> {
    const response = await apiClient.get<BioLinkApiResponse[]>(`${this.baseUrl}/rd/bio-links/page/${pageId}`);
    return response.data.map(transformBioLink);
  }

  async getActiveBioLinksByPageId(pageId: number): Promise<BioLink[]> {
    const response = await apiClient.get<BioLinkApiResponse[]>(`${this.baseUrl}/rd/bio-links/page/${pageId}/active`);
    return response.data.map(transformBioLink);
  }

  async getAllBioLinks(): Promise<BioLink[]> {
    const response = await apiClient.get<BioLinkApiResponse[]>(`${this.baseUrl}/rd/bio-links`);
    return response.data.map(transformBioLink);
  }

  // ========== Public Bio Page (Redirect Service) ==========

  async getPublicBioPage(username: string): Promise<BioPage> {
    const response = await apiClient.get<BioPageApiResponse>(`/bio/${username}`);
    return transformBioPage(response.data);
  }
}

export const bioPageService = new BioPageService();
