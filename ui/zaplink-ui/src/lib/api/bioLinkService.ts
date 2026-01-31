import apiClient from './client';
import { BioLink, CreateBioLinkRequest, UpdateBioLinkRequest, ReorderLinksRequest } from './bioPageService';

class BioLinkService {
  private readonly baseUrl = '/v1/api';

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
}

export const bioLinkService = new BioLinkService();
