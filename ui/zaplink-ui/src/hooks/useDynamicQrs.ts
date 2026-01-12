import { useState, useEffect, useCallback } from 'react'
import { DynamicQrService } from '@/lib/api/dynamicQr'
import { DynamicQrResponse } from '@/lib/types/apiRequestType'
import { toast } from 'sonner'
import api from '@/lib/util/api'

export const useDynamicQrs = () => {
    const [qrs, setQrs] = useState<DynamicQrResponse[]>([])
    const [loading, setLoading] = useState(true)
    const [error, setError] = useState<string | null>(null)

    // Pagination state
    const [page, setPage] = useState(0)
    const [totalPages, setTotalPages] = useState(0)
    const [totalElements, setTotalElements] = useState(0)

    const fetchQrs = useCallback(async () => {
        try {
            setLoading(true)
            const response = await DynamicQrService.getDynamicQrs(page)
            setQrs(response.content)
            setTotalPages(response.totalPages)
            setTotalElements(response.totalElements)
            setError(null)
        } catch (err) {
            console.error('Failed to fetch QRs', err)
            setError('Failed to load QR codes')
        } finally {
            setLoading(false)
        }
    }, [page])

    useEffect(() => {
        fetchQrs()
    }, [fetchQrs])

    const deleteQr = async (qrKey: string) => {
        try {
            await DynamicQrService.deleteDynamicQr(qrKey)
            toast.success('QR Code deleted successfully')
            fetchQrs() // Refresh list
        } catch (err) {
            console.error('Failed to delete QR', err)
            toast.error('Failed to delete QR code')
        }
    }

    const downloadQr = async (qrResponse: DynamicQrResponse) => {
        try {
            // Use authenticated API client to fetch image as blob
            const response = await api.get(qrResponse.qrImageUrl, {
                responseType: 'blob'
            })

            const url = window.URL.createObjectURL(response.data)
            const link = document.createElement('a')
            link.href = url
            link.download = `${qrResponse.qrName || 'qr-code'}.png`
            document.body.appendChild(link)
            link.click()
            document.body.removeChild(link)
            window.URL.revokeObjectURL(url)
        } catch (err) {
            console.error('Failed to download QR', err)
            toast.error('Failed to download QR image')
        }
    }

    return {
        qrs,
        loading,
        error,
        page,
        setPage,
        totalPages,
        totalElements,
        deleteQr,
        downloadQr,
        refresh: fetchQrs
    }
}
