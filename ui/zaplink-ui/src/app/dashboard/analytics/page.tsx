'use client'

import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import { useDashboardData } from "@/hooks/useDashboardData"
import { Area, AreaChart, Bar, BarChart, CartesianGrid, Cell, Legend, Pie, PieChart, ResponsiveContainer, Tooltip, XAxis, YAxis } from "recharts"
import { ArrowUpRight, MousePointerClick, Globe, Activity, Loader2, Calendar } from "lucide-react"

export default function AnalyticsPage() {
    const {
        totalClicks,
        avgCtr,
        topRegion,
        activeLinks,
        visitorTrend,
        referrers,
        creationHistory,
        isLoading
    } = useDashboardData()

    if (isLoading) {
        return (
            <div className="flex items-center justify-center min-h-[500px]">
                <Loader2 className="h-8 w-8 animate-spin text-primary" />
            </div>
        )
    }

    // Prepare data for Heatmap (simulation using creationHistory or mock if needed)
    // For now we use creationHistory to visualize activity intensity
    const heatmapData = creationHistory.map(day => ({
        ...day,
        intensity: day.links + day.qrs
    }));

    return (
        <div className="flex flex-1 flex-col">
            <div className="@container/main flex flex-1 flex-col gap-2">
                <div className="flex flex-col gap-4 py-4 md:gap-6 md:py-6 px-4 md:px-6">
                    <div className="flex items-center justify-between">
                        <div>
                            <h2 className="text-3xl font-bold tracking-tight">Analytics Overview</h2>
                            <p className="text-muted-foreground">Deep dive into your link performance and audience insights.</p>
                        </div>
                        <div className="flex items-center gap-2 bg-background/50 backdrop-blur-sm border border-border/50 p-2 rounded-lg text-sm text-muted-foreground">
                            <Calendar className="h-4 w-4" />
                            <span>Last 7 Days</span>
                        </div>
                    </div>

                    {/* Key Metrics Grid */}
                    <div className="grid gap-4 md:grid-cols-2 lg:grid-cols-4">
                        <Card className="bg-background/50 backdrop-blur-sm border-border/50 shadow-sm hover:border-primary/50 hover:shadow-[0_0_20px_-12px_var(--color-primary)] transition-all duration-300">
                            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                                <CardTitle className="text-sm font-medium">Total Clicks</CardTitle>
                                <MousePointerClick className="h-4 w-4 text-muted-foreground" />
                            </CardHeader>
                            <CardContent>
                                <div className="text-2xl font-bold">{totalClicks.toLocaleString()}</div>
                                <p className="text-xs text-muted-foreground flex items-center gap-1 mt-1">
                                    <span className="text-green-500 flex items-center"><ArrowUpRight className="h-3 w-3" /> +12%</span> from last week
                                </p>
                            </CardContent>
                        </Card>
                        <Card className="bg-background/50 backdrop-blur-sm border-border/50 shadow-sm hover:border-primary/50 hover:shadow-[0_0_20px_-12px_var(--color-primary)] transition-all duration-300">
                            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                                <CardTitle className="text-sm font-medium">Avg. CTR</CardTitle>
                                <Activity className="h-4 w-4 text-muted-foreground" />
                            </CardHeader>
                            <CardContent>
                                <div className="text-2xl font-bold">{avgCtr}%</div>
                                <p className="text-xs text-muted-foreground mt-1">Click-through rate</p>
                            </CardContent>
                        </Card>
                        <Card className="bg-background/50 backdrop-blur-sm border-border/50 shadow-sm hover:border-primary/50 hover:shadow-[0_0_20px_-12px_var(--color-primary)] transition-all duration-300">
                            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                                <CardTitle className="text-sm font-medium">Top Region</CardTitle>
                                <Globe className="h-4 w-4 text-muted-foreground" />
                            </CardHeader>
                            <CardContent>
                                <div className="text-2xl font-bold">{topRegion}</div>
                                <p className="text-xs text-muted-foreground mt-1">Most generic traffic source</p>
                            </CardContent>
                        </Card>
                        <Card className="bg-background/50 backdrop-blur-sm border-border/50 shadow-sm hover:border-primary/50 hover:shadow-[0_0_20px_-12px_var(--color-primary)] transition-all duration-300">
                            <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                                <CardTitle className="text-sm font-medium">Active Links</CardTitle>
                                <Activity className="h-4 w-4 text-muted-foreground" />
                            </CardHeader>
                            <CardContent>
                                <div className="text-2xl font-bold">{activeLinks}</div>
                                <p className="text-xs text-muted-foreground mt-1">Currently active campaigns</p>
                            </CardContent>
                        </Card>
                    </div>

                    {/* Main Charts Section */}
                    <div className="grid gap-8 lg:grid-cols-7">

                        {/* Traffic Overview - Area Chart */}
                        <Card className="lg:col-span-4 bg-background/50 backdrop-blur-sm border-border/50 shadow-sm hover:border-primary/50 hover:shadow-[0_0_20px_-12px_var(--color-primary)] transition-all duration-300">
                            <CardHeader>
                                <CardTitle>Traffic Overview</CardTitle>
                                <CardDescription>Daily visitor trends over the past week</CardDescription>
                            </CardHeader>
                            <CardContent className="pl-0">
                                <div className="h-[350px] w-full">
                                    <ResponsiveContainer width="100%" height="100%">
                                        <AreaChart data={visitorTrend}>
                                            <defs>
                                                <linearGradient id="colorVisitors" x1="0" y1="0" x2="0" y2="1">
                                                    <stop offset="5%" stopColor="hsl(var(--primary))" stopOpacity={0.3} />
                                                    <stop offset="95%" stopColor="hsl(var(--primary))" stopOpacity={0} />
                                                </linearGradient>
                                            </defs>
                                            <CartesianGrid vertical={false} strokeDasharray="3 3" stroke="hsl(var(--border))" opacity={0.4} />
                                            <XAxis
                                                dataKey="date"
                                                stroke="#888888"
                                                fontSize={12}
                                                tickLine={false}
                                                axisLine={false}
                                            />
                                            <YAxis
                                                stroke="#888888"
                                                fontSize={12}
                                                tickLine={false}
                                                axisLine={false}
                                                tickFormatter={(value) => `${value}`}
                                            />
                                            <Tooltip
                                                contentStyle={{ backgroundColor: 'hsl(var(--background))', borderColor: 'hsl(var(--border))' }}
                                                itemStyle={{ color: 'hsl(var(--foreground))' }}
                                                cursor={{ stroke: 'hsl(var(--muted-foreground))', strokeWidth: 1, strokeDasharray: '4 4' }}
                                            />
                                            <Area
                                                type="monotone"
                                                dataKey="visitors"
                                                stroke="hsl(var(--primary))"
                                                fillOpacity={1}
                                                fill="url(#colorVisitors)"
                                                strokeWidth={3}
                                            />
                                        </AreaChart>
                                    </ResponsiveContainer>
                                </div>
                            </CardContent>
                        </Card>

                        {/* Referrer Sources - Bar Chart */}
                        <Card className="lg:col-span-3 bg-background/50 backdrop-blur-sm border-border/50 shadow-sm hover:border-primary/50 hover:shadow-[0_0_20px_-12px_var(--color-primary)] transition-all duration-300">
                            <CardHeader>
                                <CardTitle>Top Referrers</CardTitle>
                                <CardDescription>Where your traffic is coming from</CardDescription>
                            </CardHeader>
                            <CardContent>
                                <div className="h-[350px] w-full">
                                    <ResponsiveContainer width="100%" height="100%">
                                        <BarChart layout="vertical" data={referrers} margin={{ left: 0, right: 0, top: 0, bottom: 0 }}>
                                            <CartesianGrid horizontal={false} strokeDasharray="3 3" stroke="hsl(var(--border))" opacity={0.4} />
                                            <XAxis type="number" hide />
                                            <YAxis
                                                dataKey="name"
                                                type="category"
                                                width={100}
                                                tick={{ fontSize: 12, fill: '#888888' }}
                                                axisLine={false}
                                                tickLine={false}
                                            />
                                            <Tooltip
                                                cursor={{ fill: 'hsl(var(--muted)/0.2)' }}
                                                contentStyle={{ backgroundColor: 'hsl(var(--background))', borderColor: 'hsl(var(--border))' }}
                                            />
                                            <Bar dataKey="value" fill="hsl(var(--chart-2))" radius={[0, 4, 4, 0]} barSize={20} />
                                        </BarChart>
                                    </ResponsiveContainer>
                                </div>
                            </CardContent>
                        </Card>
                    </div>

                    {/* Activity Heatmap & Insights */}
                    <div className="grid gap-8 lg:grid-cols-3">
                        <Card className="lg:col-span-2 bg-background/50 backdrop-blur-sm border-border/50 shadow-sm hover:border-primary/50 hover:shadow-[0_0_20px_-12px_var(--color-primary)] transition-all duration-300">
                            <CardHeader>
                                <CardTitle>Activity Heatmap</CardTitle>
                                <CardDescription>Creation intensity (Links + QR Codes) over the last 7 days</CardDescription>
                            </CardHeader>
                            <CardContent>
                                <div className="flex items-center gap-2 py-4">
                                    {heatmapData.map((day, i) => (
                                        <div key={i} className="flex flex-col items-center gap-2 flex-1">
                                            <div
                                                className="w-full aspect-square rounded-md transition-all hover:scale-105 cursor-help"
                                                style={{
                                                    backgroundColor: `hsl(var(--primary) / ${Math.max(0.1, Math.min(1, day.intensity * 0.2))})`
                                                }}
                                                title={`${day.date}: ${day.intensity} items`}
                                            />
                                            <span className="text-xs text-muted-foreground">{new Date(day.date).toLocaleDateString('en-US', { weekday: 'short' })}</span>
                                        </div>
                                    ))}
                                </div>
                            </CardContent>
                        </Card>

                        <Card className="bg-gradient-to-br from-primary/10 to-background border-border/50 shadow-sm hover:border-primary/50 hover:shadow-[0_0_20px_-12px_var(--color-primary)] transition-all duration-300">
                            <CardHeader>
                                <CardTitle>Smart Insights</CardTitle>
                                <CardDescription>AI-driven performance tips</CardDescription>
                            </CardHeader>
                            <CardContent className="space-y-4">
                                <div className="flex gap-3 items-start">
                                    <div className="mt-1 bg-green-500/10 p-2 rounded-full">
                                        <ArrowUpRight className="h-4 w-4 text-green-500" />
                                    </div>
                                    <div>
                                        <p className="text-sm font-medium">Traffic Spike!</p>
                                        <p className="text-xs text-muted-foreground">Your campaigns are performing 12% better than average on weekends.</p>
                                    </div>
                                </div>
                                <div className="flex gap-3 items-start">
                                    <div className="mt-1 bg-blue-500/10 p-2 rounded-full">
                                        <Globe className="h-4 w-4 text-blue-500" />
                                    </div>
                                    <div>
                                        <p className="text-sm font-medium">Global Reach</p>
                                        <p className="text-xs text-muted-foreground">You're getting traction in {topRegion}. Consider localizing your content.</p>
                                    </div>
                                </div>
                            </CardContent>
                        </Card>
                    </div>
                </div>
            </div>
        </div>
    )
}
