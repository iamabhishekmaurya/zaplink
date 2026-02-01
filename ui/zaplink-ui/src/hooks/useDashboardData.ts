import { useState, useEffect, useCallback } from 'react';
import { shortlinkService, StatsResponse } from '@/lib/api/shortlinkService';
import { DynamicQrService } from '@/lib/api/dynamicQr';
import { ShortLink, DynamicQrResponse } from '@/lib/types/apiRequestType';
import { log } from 'console';

export interface DashboardStats {
    totalLinks: number;
    activeLinks: number;
    totalClicks: number;
    totalQrs: number;
    totalScans: number;
    bioPages: number;
    isLoading: boolean;
    error: string | null;
    isNetworkError: boolean;
    recentActivity: ((ShortLink & { type: 'LINK' }) | (DynamicQrResponse & { type: 'QR' }))[];
    platformDistribution: { platform: string; count: number; fill: string }[];
    creationHistory: { date: string; links: number; qrs: number }[];
    visitorTrend: { date: string; visitors: number }[];
    avgCtr: number;
    topRegion: string;
    referrers: { name: string; value: number }[];
    refetch: () => void;
}

export function useDashboardData() {
    const [refreshKey, setRefreshKey] = useState(0);
    const [stats, setStats] = useState<DashboardStats>({
        totalLinks: 0,
        activeLinks: 0,
        totalClicks: 0,
        totalQrs: 0,
        totalScans: 0,
        bioPages: 0,
        isLoading: true,
        error: null,
        isNetworkError: false,
        recentActivity: [],
        platformDistribution: [],
        creationHistory: [],
        visitorTrend: [],
        avgCtr: 0,
        topRegion: '-',
        referrers: [],
        refetch: () => { }
    });

    const refetch = useCallback(() => {
        setStats(prev => ({ ...prev, isLoading: true, error: null, isNetworkError: false }));
        setRefreshKey(prev => prev + 1);
    }, []);

    useEffect(() => {
        async function fetchData() {
            try {
                const [links, qrsPage, statsData] = await Promise.all([
                    shortlinkService.getUserLinks(),
                    DynamicQrService.getDynamicQrs(0, 50),
                    shortlinkService.getUserStats()
                ]);

                const qrs = qrsPage.content || [];
                // 1. Calculate Basic Stats
                const totalLinks = links.length;
                const activeLinks = links.filter(l => l.isActive).length;
                const totalClicks = statsData.total_clicks || links.reduce((acc, l) => acc + (l.clicks || 0), 0);

                const totalQrs = qrsPage.totalElements || qrs.length;
                const totalScans = qrs.reduce((acc, q) => acc + (q.totalScans || 0), 0);

                // 2. Prepare Recent Activity
                // Combine links and QRs, sort by date desc, take top 10
                const linksWithEpoch = links.map(l => ({ ...l, type: 'LINK' as const, epoch: new Date(l.createdAt).getTime() }));
                const qrsWithEpoch = qrs.map(q => ({ ...q, type: 'QR' as const, epoch: new Date(q.createdAt).getTime(), title: q.qrName, clicks: q.totalScans }));

                const recentActivity = [...linksWithEpoch, ...qrsWithEpoch]
                    .sort((a, b) => b.epoch - a.epoch)
                    .slice(0, 10);

                // 3. Platform Distribution (Top 5 + Others)
                const platformCounts: Record<string, number> = {};
                links.forEach(l => {
                    const p = l.platform || 'Other';
                    platformCounts[p] = (platformCounts[p] || 0) + 1;
                });

                // Sort by count desc
                const sortedPlatforms = Object.entries(platformCounts)
                    .sort(([, a], [, b]) => b - a);

                const topPlatforms = sortedPlatforms.slice(0, 4);
                const otherCount = sortedPlatforms.slice(4).reduce((acc, [, c]) => acc + c, 0);

                const chartColors = [
                    "var(--chart-1)", "var(--chart-2)", "var(--chart-3)", "var(--chart-4)", "var(--chart-5)"
                ];

                const platformDistribution = topPlatforms.map(([platform, count], index) => ({
                    platform,
                    count,
                    fill: chartColors[index % 5]
                }));

                if (otherCount > 0) {
                    platformDistribution.push({ platform: 'Other', count: otherCount, fill: "var(--chart-5)" });
                }


                // 4. Creation History (Last 7 days)
                const days = 7;
                const historyMap: Record<string, { links: number; qrs: number }> = {};

                // Initialize last 7 days with 0
                for (let i = 6; i >= 0; i--) {
                    const d = new Date();
                    d.setDate(d.getDate() - i);
                    const dateKey = d.toISOString().split('T')[0];
                    historyMap[dateKey] = { links: 0, qrs: 0 };
                }

                links.forEach(l => {
                    // Robust date parsing with fallback
                    let dateKey: string;
                    try {
                        const date = new Date(l.createdAt);
                        if (isNaN(date.getTime())) {
                            // If invalid date, try parsing as string format
                            const fallbackDate = new Date(l.createdAt.replace(' ', 'T'));
                            if (isNaN(fallbackDate.getTime())) {
                                console.warn('Invalid date format:', l.createdAt);
                                return; // Skip this entry
                            }
                            dateKey = fallbackDate.toISOString().split('T')[0];
                        } else {
                            dateKey = date.toISOString().split('T')[0];
                        }
                    } catch (error) {
                        console.warn('Date parsing error:', error, l.createdAt);
                        return; // Skip this entry
                    }

                    if (historyMap[dateKey]) historyMap[dateKey].links++;
                });

                qrs.forEach(q => {
                    // Robust date parsing with fallback
                    let dateKey: string;
                    try {
                        const date = new Date(q.createdAt);
                        if (isNaN(date.getTime())) {
                            // If invalid date, try parsing as string format
                            const fallbackDate = new Date(q.createdAt.replace(' ', 'T'));
                            if (isNaN(fallbackDate.getTime())) {
                                console.warn('Invalid date format:', q.createdAt);
                                return; // Skip this entry
                            }
                            dateKey = fallbackDate.toISOString().split('T')[0];
                        } else {
                            dateKey = date.toISOString().split('T')[0];
                        }
                    } catch (error) {
                        console.warn('Date parsing error:', error, q.createdAt);
                        return; // Skip this entry
                    }

                    if (historyMap[dateKey]) historyMap[dateKey].qrs++;
                });

                const creationHistory = Object.entries(historyMap)
                    .sort(([a], [b]) => a.localeCompare(b))
                    .map(([date, counts]) => ({
                        date,
                        links: counts.links,
                        qrs: counts.qrs
                    }));

                // 5. Visitor Trend (from StatsResponse)
                // Map backend clickTrend to chart format
                const visitorTrend = (statsData.click_trend || []).map((item: { name: string; value: number }) => ({
                    date: item.name,
                    visitors: item.value
                }));

                // 6. Detailed Stats
                const referrers = (statsData.referrers || []).map((item: { name: string; value: number | string }) => ({
                    name: item.name,
                    value: Number(item.value)
                }));

                setStats({
                    totalLinks,
                    activeLinks,
                    totalClicks,
                    totalQrs,
                    totalScans,
                    bioPages: 0, // Not available in statsData
                    isLoading: false,
                    error: null,
                    isNetworkError: false,
                    recentActivity,
                    platformDistribution,
                    creationHistory,
                    visitorTrend,
                    avgCtr: statsData.avg_ctr || 0,
                    topRegion: statsData.top_region || 'Unknown',
                    referrers,
                    refetch
                });

            } catch (err: any) {
                console.error("Failed to fetch dashboard data", err);

                // Detect network errors vs other errors
                const isNetworkError = err?.code === 'ERR_NETWORK' ||
                    err?.message?.toLowerCase().includes('network') ||
                    !navigator.onLine;

                const errorMessage = isNetworkError
                    ? 'Unable to connect to the server. Please check if the backend services are running.'
                    : 'Failed to load dashboard data. Please try again.';

                setStats(prev => ({
                    ...prev,
                    isLoading: false,
                    error: errorMessage,
                    isNetworkError,
                    refetch
                }));
            }
        }

        fetchData();
    }, [refreshKey, refetch]);

    return stats;
}
