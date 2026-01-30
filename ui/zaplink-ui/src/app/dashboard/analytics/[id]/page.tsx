'use client'

import { AnalyticsDashboard, AnalyticsData } from '@/components/analytics/analytics-dashboard'
import { API_ENDPOINTS } from '@/lib/constants/apiConstant'
import api from "@/lib/api/client";
import { AlertCircle } from 'lucide-react'
import { useParams, useSearchParams } from 'next/navigation'
import { Suspense, useEffect, useState } from 'react'
import { toast } from 'sonner'

interface LinkAnalyticsResponse {
    shortUrlKey: string;
    originalUrl: string;
    totalClicks: number;
    clicksToday: number;
    topCountries: { name: string; value: number; percentage: number }[];
    topBrowsers: { name: string; value: number; percentage: number }[];
    topReferrers: { name: string; value: number; percentage: number }[];
    dailyClicks: { name: string; value: number }[];
}

interface QrAnalyticsResponse {
    qrKey: string;
    qrName: string;
    totalScans: number;
    scansToday: number;
    countryStats: { country: string; count: number; percentage: number }[];
    deviceStats: { deviceType: string; count: number; percentage: number }[];
    dailyStats: { date: string; scans: number }[];
    // Note: Qr endpoint in backend might not contain referrers yet or named differently, checking interface...
    // Assuming backend structure as seen in DyQrController:
    // QrAnalyticsResponse: countryStats, deviceStats, browserStats, dailyStats
    browserStats: { browser: string; count: number; percentage: number }[];
}


const AnalyticsPageContent = () => {
    const params = useParams()
    const searchParams = useSearchParams()

    // id could be shortUrlKey (for links) or qrKey (for QRs)
    const id = params.id as string
    const type = searchParams.get('type') || 'link' // 'link' | 'qr'

    const [data, setData] = useState<AnalyticsData | null>(null)
    const [loading, setLoading] = useState(true)
    const [error, setError] = useState<string | null>(null)
    const [meta, setMeta] = useState<{ title: string, subtitle: string }>({ title: 'Analytics', subtitle: '' })

    useEffect(() => {
        const fetchData = async () => {
            if (!id) return;
            setLoading(true)
            setError(null)

            try {
                if (type === 'link') {
                    // Fetch Link Analytics
                    const res = await api.get<LinkAnalyticsResponse>(API_ENDPOINTS.ANALYTICS_LINK(id))
                    const d = res.data;

                    // Normalize
                    const normalized: AnalyticsData = {
                        overview: {
                            totalClicks: d.totalClicks,
                            clicksToday: d.clicksToday,
                        },
                        dailyStats: d.dailyClicks.map(item => ({ date: item.name, clicks: item.value })),
                        countries: d.topCountries,
                        devices: d.topBrowsers.map(b => ({ ...b, name: cleanUserAgent(b.name) })), // Use browsers as 'devices' for now
                        referrers: d.topReferrers
                    }
                    setData(normalized)
                    setMeta({ title: `Link Analytics: /${d.shortUrlKey}`, subtitle: d.originalUrl })
                } else if (type === 'qr') {
                    // Fetch QR Analytics
                    // Note: API_ENDPOINTS.DYNAMIC_QR_ANALYTICS uses DYNAMIC_QR_READ base path
                    const res = await api.get<QrAnalyticsResponse>(API_ENDPOINTS.DYNAMIC_QR_ANALYTICS(id))
                    const d = res.data;

                    const normalized: AnalyticsData = {
                        overview: {
                            totalClicks: d.totalScans,
                            clicksToday: d.scansToday,
                        },
                        dailyStats: d.dailyStats.map(item => ({ date: item.date, clicks: item.scans })),
                        countries: d.countryStats.map(c => ({ name: c.country, value: c.count, percentage: c.percentage })),
                        devices: d.deviceStats.map(dev => ({ name: dev.deviceType, value: dev.count, percentage: dev.percentage })),
                        referrers: [] // QR usually scanned physically, referrers unlikely or N/A
                    }
                    setData(normalized)
                    setMeta({ title: `QR Analytics: ${d.qrName}`, subtitle: `Tracking Key: ${d.qrKey}` })
                }
            } catch (err: any) {
                console.error("Failed to fetch analytics", err)
                setError(err.response?.data?.message || "Failed to load analytics data")
                toast.error("Could not load analytics")
            } finally {
                setLoading(false)
            }
        }

        fetchData()
    }, [id, type])

    // Helper to clean up raw User Agent strings if backend sends raw
    const cleanUserAgent = (ua: string) => {
        if (!ua) return "Unknown";
        if (ua.includes("Chrome")) return "Chrome";
        if (ua.includes("Firefox")) return "Firefox";
        if (ua.includes("Safari") && !ua.includes("Chrome")) return "Safari";
        if (ua.includes("Edge")) return "Edge";
        if (ua.includes("iPhone")) return "iPhone";
        if (ua.includes("Android")) return "Android";

        // Truncate if too long (likely raw string)
        if (ua.length > 20) return ua.substring(0, 17) + "..."
        return ua;
    }

    if (error) {
        return (
            <div className="p-8">
                <div className="bg-destructive/15 text-destructive p-4 rounded-md flex items-center gap-3 border border-destructive/20">
                    <AlertCircle className="h-5 w-5" />
                    <div>
                        <h3 className="font-semibold">Error</h3>
                        <p className="text-sm opacity-90">{error}</p>
                    </div>
                </div>
            </div>
        )
    }

    if (!data && !loading) return null;

    return (
        <div className="flex flex-col gap-4 p-4 md:gap-6 md:py-6">
            <AnalyticsDashboard
                data={data || { overview: { totalClicks: 0, clicksToday: 0 }, dailyStats: [], countries: [], devices: [], referrers: [] }}
                title={meta.title}
                description={meta.subtitle}
                loading={loading}
            />
        </div>
    )
}

const AnalyticsPage = () => {
    return (
        <Suspense fallback={<div>Loading...</div>}>
            <AnalyticsPageContent />
        </Suspense>
    )
}

export default AnalyticsPage
