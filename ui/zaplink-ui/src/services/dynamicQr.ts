import api from "@/services/client";
import { API_ENDPOINTS } from "@/lib/constants/apiConstant"
import { DynamicQrResponse, DynamicQrApiResponse, PageResponse, QRConfigType, RedirectRuleDto } from "@/lib/types/apiRequestType"

// ============ API Response Types (for PageResponse content) ============
// Spring's Page can return camelCase or snake_case depending on config
interface DynamicQrApiPageResponse {
    content: DynamicQrApiResponse[];
    // Spring Page fields (camelCase)
    totalPages?: number;
    totalElements?: number;
    size?: number;
    number?: number;
    // Alternate snake_case fields (if Spring is configured that way)
    total_pages?: number;
    total_elements?: number;
}

// ============ Transformation Functions ============
function transformDynamicQrResponse(apiQr: DynamicQrApiResponse): DynamicQrResponse {
    return {
        id: apiQr.id,
        qrKey: apiQr.qr_key,
        qrName: apiQr.qr_name,
        currentDestinationUrl: apiQr.current_destination_url,
        qrImageUrl: apiQr.qr_image_url,
        redirectUrl: apiQr.redirect_url,
        campaignId: apiQr.campaign_id,
        userEmail: apiQr.user_email,
        isActive: apiQr.is_active,
        totalScans: apiQr.total_scans,
        createdAt: apiQr.created_at,
        updatedAt: apiQr.updated_at,
        lastScanned: apiQr.last_scanned,
        rules: apiQr.rules,
        qrConfig: apiQr.qr_config,
        allowedDomains: apiQr.allowed_domains,
        password: apiQr.password,
        scanLimit: apiQr.scan_limit,
        expirationDate: apiQr.expiration_date,
        trackAnalytics: apiQr.track_analytics
    };
}

// ============ Request Types ============
export interface CreateDynamicQrRequest {
    qr_name: string;
    destination_url: string;
    qr_config: QRConfigType;
    campaign_id?: string;
    expiration_date?: string;
    password?: string;
    scan_limit?: number;
    allowed_domains?: string[];
    track_analytics?: boolean;
    rules?: RedirectRuleDto[];
}

export interface UpdateDynamicQrRequest {
    qr_name?: string;
    destination_url?: string;
    qr_config?: QRConfigType;
    campaign_id?: string;
    expiration_date?: string;
    password?: string;
    scan_limit?: number;
    allowed_domains?: string[];
    track_analytics?: boolean;
    rules?: RedirectRuleDto[];
}

// ============ Service ============
export const DynamicQrService = {
    createDynamicQr: async (data: {
        qrName: string;
        destinationUrl: string;
        qrConfig: QRConfigType;
        campaignId?: string;
        expirationDate?: string;
        password?: string;
        scanLimit?: number;
        allowedDomains?: string[];
        trackAnalytics?: boolean;
        rules?: RedirectRuleDto[];
    }): Promise<DynamicQrResponse> => {
        const request: CreateDynamicQrRequest = {
            qr_name: data.qrName,
            destination_url: data.destinationUrl,
            qr_config: data.qrConfig,
            campaign_id: data.campaignId,
            expiration_date: data.expirationDate,
            password: data.password,
            scan_limit: data.scanLimit,
            allowed_domains: data.allowedDomains,
            track_analytics: data.trackAnalytics,
            rules: data.rules
        };
        const response = await api.post<DynamicQrApiResponse>(API_ENDPOINTS.DYNAMIC_QR_CREATE, request);
        return transformDynamicQrResponse(response.data);
    },

    getDynamicQrs: async (page = 0, size = 10): Promise<PageResponse<DynamicQrResponse>> => {
        const response = await api.get<DynamicQrApiPageResponse>(`${API_ENDPOINTS.DYNAMIC_QR_LIST}`, {
            params: { page, size }
        });

        return {
            content: response.data.content?.map(transformDynamicQrResponse) || [],
            totalPages: response.data.totalPages ?? response.data.total_pages ?? 0,
            totalElements: response.data.totalElements ?? response.data.total_elements ?? 0,
            size: response.data.size ?? 0,
            number: response.data.number ?? 0
        };
    },

    deleteDynamicQr: async (qrKey: string): Promise<void> => {
        await api.delete(API_ENDPOINTS.DYNAMIC_QR_DELETE(qrKey));
    },

    updateDynamicQr: async (qrKey: string, data: {
        qrName?: string;
        destinationUrl?: string;
        qrConfig?: QRConfigType;
        campaignId?: string;
        expirationDate?: string;
        password?: string;
        scanLimit?: number;
        allowedDomains?: string[];
        trackAnalytics?: boolean;
        rules?: RedirectRuleDto[];
    }): Promise<DynamicQrResponse> => {
        const request: UpdateDynamicQrRequest = {
            qr_name: data.qrName,
            destination_url: data.destinationUrl,
            qr_config: data.qrConfig,
            campaign_id: data.campaignId,
            expiration_date: data.expirationDate,
            password: data.password,
            scan_limit: data.scanLimit,
            allowed_domains: data.allowedDomains,
            track_analytics: data.trackAnalytics,
            rules: data.rules
        };
        const response = await api.put<DynamicQrApiResponse>(`${API_ENDPOINTS.DYNAMIC_QR_CREATE}/${qrKey}`, request);
        return transformDynamicQrResponse(response.data);
    }
};
