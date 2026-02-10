import { DynamicQrResponse, ShortLink } from '@/lib/types/apiRequestType';
import { DynamicQrService } from '@/services/dynamicQr';
import { shortlinkService } from '@/services/shortlinkService';
import { bioPageService } from '@/services/bioPageService';
import { useAuth } from '@/hooks/useAuth';
import { useCallback, useEffect, useRef, useState } from 'react';

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
    recentActivity: ((ShortLink & { type: 'LINK' }) | (DynamicQrResponse & { type: 'QR' }) | (any & { type: 'BIOPAGE' }))[];
    platformDistribution: { platform: string; count: number; fill: string }[];
    creationHistory: { date: string; links: number; qrs: number }[];
    visitorTrend: { date: string; visitors: number }[];
    avgCtr: number;
    topRegion: string;
    referrers: { name: string; value: number }[];
    refetch: () => void;
}

export function useDashboardData() {
    const { user } = useAuth();
    const [refreshKey, setRefreshKey] = useState(0);
    const isFetching = useRef(false);
    const hasFetched = useRef(false);
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
        hasFetched.current = false; // Allow refetch
        setStats(prev => ({ ...prev, isLoading: true, error: null, isNetworkError: false }));
        setRefreshKey(prev => prev + 1);
    }, []);

    useEffect(() => {
        async function fetchData() {
            // Guard against duplicate fetches and missing user
            if (isFetching.current || (hasFetched.current && refreshKey === 0) || !user?.id) {
                return;
            }
            isFetching.current = true;

            try {
                const [links, qrsPage, statsData, bioPages] = await Promise.all([
                    shortlinkService.getUserLinks().catch(() => {
                        return [];
                    }),
                    DynamicQrService.getDynamicQrs(0, 50).catch(() => {
                        return { content: [], totalElements: 0 };
                    }),
                    shortlinkService.getUserStats().catch(() => {
                        return { totalClicks: 0, clickTrend: [], referrers: [], avgCtr: 0, topRegion: 'Unknown' };
                    }),
                    bioPageService.getBioPagesByOwnerId(user.id.toString()).catch(() => {
                        // Return empty array as fallback so dashboard doesn't break
                        return [];
                    })
                ]);

                const qrs = qrsPage.content || [];
                // 1. Calculate Basic Stats
                const totalLinks = links.length;
                const activeLinks = links.filter(l => l.isActive).length;
                const totalClicks = statsData.totalClicks || links.reduce((acc, l) => acc + (l.clicks || 0), 0);

                const totalQrs = qrsPage.totalElements || qrs.length;
                const totalScans = qrs.reduce((acc, q) => acc + (q.totalScans || 0), 0);

                const bioPagesCount = bioPages.length;

                // 2. Prepare Recent Activity
                // Combine links, QRs, and bio pages, sort by date desc, take top 10
                const linksWithEpoch = links.map(l => ({ ...l, type: 'LINK' as const, epoch: new Date(l.createdAt).getTime() }));
                const qrsWithEpoch = qrs.map(q => ({ ...q, type: 'QR' as const, epoch: new Date(q.createdAt).getTime(), title: q.qrName, clicks: q.totalScans }));
                const bioPagesWithEpoch = bioPages.map(bp => ({ ...bp, type: 'BIOPAGE' as const, epoch: new Date(bp.createdAt).getTime(), title: `@${bp.username}`, clicks: 0 }));

                const recentActivity = [...linksWithEpoch, ...qrsWithEpoch, ...bioPagesWithEpoch]
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
                                return; // Skip this entry
                            }
                            dateKey = fallbackDate.toISOString().split('T')[0];
                        } else {
                            dateKey = date.toISOString().split('T')[0];
                        }
                    } catch {
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
                                return; // Skip this entry
                            }
                            dateKey = fallbackDate.toISOString().split('T')[0];
                        } else {
                            dateKey = date.toISOString().split('T')[0];
                        }
                    } catch {
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
                const visitorTrend = (statsData.clickTrend || []).map((item: { name: string; value: number }) => ({
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
                    bioPages: bioPagesCount,
                    isLoading: false,
                    error: null,
                    isNetworkError: false,
                    recentActivity: recentActivity as ((ShortLink & { type: 'LINK' }) | (DynamicQrResponse & { type: 'QR' }) | any & { type: 'BIOPAGE' })[],
                    platformDistribution,
                    creationHistory,
                    visitorTrend,
                    avgCtr: statsData.avgCtr || 0,
                    topRegion: statsData.topRegion || 'Unknown',
                    referrers,
                    refetch
                });

                hasFetched.current = true;
                isFetching.current = false;

            } catch (err: any) {
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

                isFetching.current = false;
            }
        }

        fetchData();
    }, [refreshKey, refetch, user?.id]);

    return stats;
}
