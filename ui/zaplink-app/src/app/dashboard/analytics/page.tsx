'use client';

import { useState, useEffect } from 'react';
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from '@/components/ui/card';
import { Button } from '@/components/ui/button';
import {
    BarChart,
    Bar,
    XAxis,
    YAxis,
    CartesianGrid,
    Tooltip,
    ResponsiveContainer,
    AreaChart,
    Area,
    PieChart,
    Pie,
    Cell
} from 'recharts';
import {
    Calendar,
    Download,
    TrendingUp,
    MousePointer2,
    Globe2,
    Smartphone,
    Laptop,
    Chrome,
    Navigation,
    ArrowUpRight,
    ArrowDownRight,
    MoreHorizontal
} from 'lucide-react';
import { motion } from 'framer-motion';
import { useStats } from '@/hooks/useStats';

const COLORS = ['#3b82f6', '#8b5cf6', '#10b981', '#f59e0b', '#ef4444'];

export default function AnalyticsPage() {
    const [timeRange, setTimeRange] = useState('Last 30 Days');
    const { stats, fetchStats, isLoading } = useStats();

    useEffect(() => {
        fetchStats();
    }, []);

    const containerVariants = {
        hidden: { opacity: 0, y: 20 },
        visible: {
            opacity: 1,
            y: 0,
            transition: {
                duration: 0.6,
                staggerChildren: 0.1
            }
        }
    };

    const itemVariants = {
        hidden: { opacity: 0, y: 20 },
        visible: { opacity: 1, y: 0 }
    };

    return (
        <motion.div
            variants={containerVariants}
            initial="hidden"
            animate="visible"
            className="container mx-auto px-4 py-8 flex flex-col gap-8"
        >
            {/* Header */}
            <div className="flex flex-col md:flex-row md:items-center justify-between gap-4">
                <div>
                    <h1 className="text-3xl font-bold font-display tracking-tight text-foreground mb-2">Detailed Analytics</h1>
                    <p className="text-muted-foreground font-medium">Deep dive into your link performance and audience.</p>
                </div>
                <div className="flex items-center gap-3">
                    <Button variant="outline" className="gap-2 font-bold font-display h-11 border-dashed">
                        <Calendar className="h-4 w-4" /> {timeRange}
                    </Button>
                    <Button className="gap-2 font-bold font-display h-11 shadow-lg shadow-primary/20 px-6">
                        <Download className="h-4 w-4" /> Export Report
                    </Button>
                </div>
            </div>

            {/* Top Metrics */}
            <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
                {[
                    { label: 'Total Clicks', val: stats?.totalClicks?.toLocaleString() || '0', trend: '+12.5%', icon: MousePointer2, color: 'text-primary' },
                    { label: 'Unique Visitors', val: 'N/A', trend: '+8.2%', icon: TrendingUp, color: 'text-blue-500' },
                    { label: 'Avg. CTR', val: (stats?.avgCtr || '0') + '%', trend: '-0.4%', icon: Navigation, color: 'text-purple-500', isDown: true },
                    { label: 'Top Region', val: stats?.topRegion || 'N/A', trend: '45%', icon: Globe2, color: 'text-green-500' },
                ].map((stat, i) => (
                    <Card key={i} className="glass-card border-0 shadow-lg overflow-hidden group">
                        <CardContent className="p-6 relative">
                            <div className="flex items-center justify-between mb-4">
                                <div className={`p-3 rounded-xl bg-muted/50 transition-transform group-hover:rotate-12 ${stat.color}`}>
                                    <stat.icon className="h-6 w-6" />
                                </div>
                                <div className={`flex items-center gap-1 text-sm font-bold ${stat.isDown ? 'text-red-500' : 'text-green-500'}`}>
                                    {stat.isDown ? <ArrowDownRight className="h-4 w-4" /> : <ArrowUpRight className="h-4 w-4" />}
                                    {stat.trend}
                                </div>
                            </div>
                            <p className="text-sm font-bold font-display text-muted-foreground uppercase tracking-wider mb-1">{stat.label}</p>
                            <h3 className="text-2xl font-bold font-display tracking-tight text-foreground">{stat.val}</h3>
                        </CardContent>
                    </Card>
                ))}
            </div>

            {/* Click Trends Chart */}
            <Card className="glass-card border-0 shadow-xl lg:col-span-3">
                <CardHeader className="flex flex-row items-center justify-between">
                    <div>
                        <CardTitle className="text-xl font-bold font-display tracking-tight">Click Performance Over Time</CardTitle>
                        <CardDescription className="font-medium">Analysis of total and unique clicks for the selected period</CardDescription>
                    </div>
                    <Button variant="ghost" size="icon"><MoreHorizontal className="h-5 w-5" /></Button>
                </CardHeader>
                <CardContent className="h-[400px] pt-4">
                    <ResponsiveContainer width="100%" height="100%">
                        <AreaChart data={stats?.clickTrend || []}>
                            <defs>
                                <linearGradient id="colorClicks" x1="0" y1="0" x2="0" y2="1">
                                    <stop offset="5%" stopColor="#818cf8" stopOpacity={0.3} />
                                    <stop offset="95%" stopColor="#818cf8" stopOpacity={0} />
                                </linearGradient>
                            </defs>
                            <CartesianGrid strokeDasharray="3 3" vertical={false} stroke="hsl(var(--muted)/0.5)" />
                            <XAxis
                                dataKey="name"
                                axisLine={false}
                                tickLine={false}
                                tick={{ fill: 'currentColor', opacity: 0.7, fontSize: 12, fontWeight: 600, fontFamily: 'var(--font-space-grotesk)' }}
                            />
                            <YAxis
                                axisLine={false}
                                tickLine={false}
                                tick={{ fill: 'currentColor', opacity: 0.7, fontSize: 12, fontWeight: 600, fontFamily: 'var(--font-space-grotesk)' }}
                            />
                            <Tooltip
                                contentStyle={{ borderRadius: '12px', border: 'none', boxShadow: '0 10px 15px -3px rgb(0 0 0 / 0.1)', backgroundColor: 'hsl(var(--card))' }}
                                itemStyle={{ fontWeight: 600, fontFamily: 'var(--font-space-grotesk)' }}
                            />
                            <Area
                                type="monotone"
                                dataKey="clicks"
                                stroke="#818cf8"
                                strokeWidth={4}
                                fillOpacity={1}
                                fill="url(#colorClicks)"
                                animationDuration={2000}
                            />
                        </AreaChart>
                    </ResponsiveContainer>
                </CardContent>
            </Card>

            <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
                {/* Geographic Data */}
                <Card className="lg:col-span-2 glass-card border-0 shadow-xl overflow-hidden">
                    <CardHeader>
                        <CardTitle className="text-xl font-bold font-display tracking-tight">Top Geographic Locations</CardTitle>
                        <CardDescription className="font-medium">Traffic distribution by country and region</CardDescription>
                    </CardHeader>
                    <CardContent className="h-[350px] pt-4">
                        <ResponsiveContainer width="100%" height="100%">
                            <BarChart data={stats?.referrers || []} layout="vertical">
                                <CartesianGrid strokeDasharray="3 3" horizontal={false} stroke="hsl(var(--muted)/0.5)" />
                                <XAxis type="number" hide />
                                <YAxis
                                    dataKey="name"
                                    type="category"
                                    axisLine={false}
                                    tickLine={false}
                                    width={120}
                                    tick={{ fill: 'currentColor', opacity: 0.8, fontSize: 13, fontWeight: 600, fontFamily: 'var(--font-space-grotesk)' }}
                                />
                                <Tooltip
                                    cursor={{ fill: 'hsl(var(--muted)/0.3)' }}
                                    contentStyle={{ borderRadius: '12px', border: 'none', boxShadow: '0 10px 15px -3px rgb(0 0 0 / 0.1)' }}
                                />
                                <Bar dataKey="value" radius={[0, 4, 4, 0]}>
                                    {(stats?.referrers || []).map((entry: any, index: number) => (
                                        <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                                    ))}
                                </Bar>
                            </BarChart>
                        </ResponsiveContainer>
                    </CardContent>
                </Card>

                {/* Device Distribution */}
                <Card className="glass-card border-0 shadow-xl overflow-hidden">
                    <CardHeader>
                        <CardTitle className="text-xl font-bold font-display tracking-tight">Device Breakdown</CardTitle>
                        <CardDescription className="font-medium">Audience preferences by platform</CardDescription>
                    </CardHeader>
                    <CardContent className="flex flex-col items-center justify-center pt-4">
                        <div className="h-[250px] w-full">
                            <ResponsiveContainer width="100%" height="100%">
                                <PieChart>
                                    <Pie
                                        data={stats?.devices || []}
                                        innerRadius={60}
                                        outerRadius={80}
                                        paddingAngle={10}
                                        dataKey="value"
                                    >
                                        {(stats?.devices || []).map((entry: any, index: number) => (
                                            <Cell key={`cell-${index}`} fill={entry.color} strokeWidth={0} />
                                        ))}
                                    </Pie>
                                    <Tooltip />
                                </PieChart>
                            </ResponsiveContainer>
                        </div>
                        <div className="flex gap-6 mt-4 w-full justify-center">
                            {(stats?.devices || []).map((device: any, i: number) => (
                                <div key={i} className="flex flex-col items-center gap-1">
                                    <div className="flex items-center gap-2">
                                        <div className="h-3 w-3 rounded-full" style={{ backgroundColor: device.color }} />
                                        <span className="text-sm font-bold font-display">{device.name}</span>
                                    </div>
                                    <span className="text-lg font-bold">{device.value}%</span>
                                </div>
                            ))}
                        </div>
                    </CardContent>
                </Card>
            </div>

            <div className="grid grid-cols-1 lg:grid-cols-3 gap-8 pb-10">
                {/* Referrers */}
                <Card className="lg:col-span-2 glass-card border-0 shadow-xl overflow-hidden">
                    <CardHeader>
                        <CardTitle className="text-xl font-bold font-display tracking-tight">Top Referral Sources</CardTitle>
                        <CardDescription className="font-medium">Websites and platforms driving traffic</CardDescription>
                    </CardHeader>
                    <CardContent className="p-0">
                        <div className="divide-y divide-border/40">
                            {(stats?.referrers || []).map((ref: any, i: number) => (
                                <div key={i} className="flex items-center justify-between p-4 px-6 group hover:bg-muted/30 transition-colors">
                                    <div className="flex items-center gap-4">
                                        <div className="h-10 w-10 rounded-full bg-primary/10 flex items-center justify-center font-bold font-display text-primary">
                                            {ref.name.charAt(0)}
                                        </div>
                                        <div>
                                            <p className="font-bold font-display text-foreground">{ref.name}</p>
                                            <p className="text-xs text-muted-foreground font-medium">Link source</p>
                                        </div>
                                    </div>
                                    <div className="flex items-center gap-8">
                                        <div className="text-right">
                                            <p className="font-bold font-display">{ref.value}</p>
                                            <p className="text-xs text-muted-foreground font-medium">Clicks</p>
                                        </div>
                                        <div className="w-32 hidden md:block">
                                            <div className="h-2 w-full bg-muted/50 rounded-full overflow-hidden">
                                                <div
                                                    className="h-full bg-primary"
                                                    style={{ width: `${ref.percentage || 0}%` }}
                                                />
                                            </div>
                                        </div>
                                        <div className={`flex items-center gap-1 font-bold ${ref.trend === 'up' ? 'text-green-500' : 'text-red-500'}`}>
                                            {ref.percentage || 0}%
                                        </div>
                                    </div>
                                </div>
                            ))}
                        </div>
                    </CardContent>
                </Card>

                {/* Browser Stats */}
                <Card className="glass-card border-0 shadow-xl overflow-hidden">
                    <CardHeader>
                        <CardTitle className="text-xl font-bold font-display tracking-tight">System Insights</CardTitle>
                        <CardDescription className="font-medium">Browser and OS distribution</CardDescription>
                    </CardHeader>
                    <CardContent className="flex flex-col gap-6 pt-4">
                        {(stats?.browsers || []).map((item: any, i: number) => {
                            const Icon = (item.name === 'Chrome' ? Chrome :
                                item.name === 'Safari' ? Laptop :
                                    item.name === 'Firefox' ? Globe2 : Smartphone);
                            return (
                                <div key={i} className="flex flex-col gap-2">
                                    <div className="flex justify-between items-center text-sm">
                                        <div className="flex items-center gap-2">
                                            <Icon className={`h-4 w-4 ${item.color}`} />
                                            <span className="font-bold font-display">{item.name}</span>
                                        </div>
                                        <span className="font-bold">{item.val}%</span>
                                    </div>
                                    <div className="h-2 w-full bg-muted/50 rounded-full overflow-hidden">
                                        <motion.div
                                            initial={{ width: 0 }}
                                            animate={{ width: `${item.val}%` }}
                                            transition={{ duration: 1, delay: i * 0.1 }}
                                            className="h-full bg-primary"
                                        />
                                    </div>
                                </div>
                            );
                        })}
                    </CardContent>
                </Card>
            </div>
        </motion.div>
    );
}
