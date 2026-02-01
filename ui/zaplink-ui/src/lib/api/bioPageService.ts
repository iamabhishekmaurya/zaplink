import apiClient from './client';

export interface BioPage {
  id: string;
  username: string;
  ownerId: string;
  bioText?: string;
  avatarUrl?: string;
  themeConfig?: string;
  isActive: boolean;
  publicUrl: string;
  createdAt: string;
  updatedAt: string;
  links?: BioLink[];
}

export interface BioLink {
  id: string;
  bioPageId: string;
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

export interface CreateBioPageRequest {
  username: string;
  ownerId: string;
  bioText?: string;
  avatarUrl?: string;
  themeConfig?: string;
}

export interface UpdateBioPageRequest {
  bioText?: string;
  avatarUrl?: string;
  themeConfig?: string;
  isActive?: boolean;
}

export interface CreateBioLinkRequest {
  bioPageId: number;
  title: string;
  url?: string;
  type: string;
  isActive?: boolean;
  sortOrder?: number;
  price?: number;
  currency?: string;
}

export interface UpdateBioLinkRequest {
  title?: string;
  url?: string;
  type?: string;
  isActive?: boolean;
  sortOrder?: number;
  price?: number;
  currency?: string;
}

export interface ReorderLinksRequest {
  linkOrders: { linkId: number; sortOrder: number }[];
}

class BioPageService {
  private readonly baseUrl = '/api/v1';

  // ========== BioPage Write Operations (Core Service) ==========

  async createBioPage(request: CreateBioPageRequest): Promise<BioPage> {
    const response = await apiClient.post(`${this.baseUrl}/wr/bio-pages`, request);
    return response.data;
  }

  async updateBioPage(pageId: number, request: UpdateBioPageRequest): Promise<BioPage> {
    const response = await apiClient.put(`${this.baseUrl}/wr/bio-pages/${pageId}`, request);
    return response.data;
  }

  async deleteBioPage(pageId: number): Promise<void> {
    await apiClient.delete(`${this.baseUrl}/wr/bio-pages/${pageId}`);
  }

  // ========== BioPage Read Operations (Manager Service) ==========

  async getBioPageById(id: string): Promise<BioPage> {
    const response = await apiClient.get(`${this.baseUrl}/rd/bio-pages/${id}`);
    return response.data;
  }

  async getBioPageByUsername(username: string): Promise<BioPage> {
    const response = await apiClient.get(`${this.baseUrl}/rd/bio-pages/username/${username}`);
    return response.data;
  }

  async getBioPagesByOwnerId(ownerId: string): Promise<BioPage[]> {
    const response = await apiClient.get(`${this.baseUrl}/rd/bio-pages/owner/${ownerId}`);
    return response.data;
  }

  async getAllBioPages(): Promise<BioPage[]> {
    const response = await apiClient.get(`${this.baseUrl}/rd/bio-pages`);
    return response.data;
  }

  // ========== BioLink Write Operations (Core Service) ==========

  async createBioLink(request: CreateBioLinkRequest): Promise<BioLink> {
    const response = await apiClient.post(`${this.baseUrl}/wr/bio-links`, request);
    return response.data;
  }

  async updateBioLink(linkId: number, request: UpdateBioLinkRequest): Promise<BioLink> {
    const response = await apiClient.put(`${this.baseUrl}/wr/bio-links/${linkId}`, request);
    return response.data;
  }

  async deleteBioLink(linkId: number): Promise<void> {
    await apiClient.delete(`${this.baseUrl}/wr/bio-links/${linkId}`);
  }

  async reorderLinks(pageId: number, request: ReorderLinksRequest): Promise<void> {
    await apiClient.put(`${this.baseUrl}/wr/bio-links/${pageId}/reorder`, request);
  }

  // ========== BioLink Read Operations (Manager Service) ==========

  async getBioLinkById(id: number): Promise<BioLink> {
    const response = await apiClient.get(`${this.baseUrl}/rd/bio-links/${id}`);
    return response.data;
  }

  async getBioLinksByPageId(pageId: number): Promise<BioLink[]> {
    const response = await apiClient.get(`${this.baseUrl}/rd/bio-links/page/${pageId}`);
    return response.data;
  }

  async getActiveBioLinksByPageId(pageId: number): Promise<BioLink[]> {
    const response = await apiClient.get(`${this.baseUrl}/rd/bio-links/page/${pageId}/active`);
    return response.data;
  }

  async getAllBioLinks(): Promise<BioLink[]> {
    const response = await apiClient.get(`${this.baseUrl}/rd/bio-links`);
    return response.data;
  }

  // ========== Public Bio Page (Redirect Service) ==========

  async getPublicBioPage(username: string): Promise<BioPage> {
    const response = await apiClient.get(`/v1/bio/${username}`);
    return response.data;
  }
}

export const bioPageService = new BioPageService();
