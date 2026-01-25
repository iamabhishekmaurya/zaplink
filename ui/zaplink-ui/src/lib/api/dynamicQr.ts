import api from "@/lib/api/client";
import { API_ENDPOINTS } from '../constant/apiConstant'
import { DynamicQrResponse, PageResponse, QRConfigType } from '../types/apiRequestType'

interface CreateDynamicQrRequest {
    qrName: string
    destinationUrl: string
    qrConfig: QRConfigType
    campaignId?: string
    // Advanced Features
    expirationDate?: string // ISO date string
    password?: string
    scanLimit?: number
    allowedDomains?: string[]
    trackAnalytics?: boolean
}

export const DynamicQrService = {
    createDynamicQr: async (data: CreateDynamicQrRequest) => {
        const response = await api.post<DynamicQrResponse>(API_ENDPOINTS.DYNAMIC_QR_CREATE, data)
        return response.data
    },

    getDynamicQrs: async (page = 0, size = 10) => {
        const response = await api.get<PageResponse<DynamicQrResponse>>(`${API_ENDPOINTS.DYNAMIC_QR_LIST}`, {
            params: { page, size }
        })
        return response.data
    },

    deleteDynamicQr: async (qrKey: string) => {
        await api.delete(API_ENDPOINTS.DYNAMIC_QR_DELETE(qrKey))
    }
}

