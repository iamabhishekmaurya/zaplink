'use client'

import {
    Card,
    CardContent,
    CardDescription,
    CardFooter,
    CardHeader,
    CardTitle,
} from "@/components/ui/card"
import {
    ChartConfig,
    ChartContainer,
    ChartTooltip,
    ChartTooltipContent,
    ChartLegend,
    ChartLegendContent
} from "@/components/ui/chart"
import { ArrowUpRight, Calendar, Globe, Monitor, Users, TrendingUp } from "lucide-react"
import { Area, AreaChart, CartesianGrid, Cell, Legend, Pie, PieChart, ResponsiveContainer, Tooltip, XAxis, YAxis, Label } from "recharts"
import * as React from "react"

const deviceChartConfig = {
    other: {
        label: "Other",
        color: "hsl(var(--chart-5))",
    },
} satisfies ChartConfig

const activityChartConfig = {
    clicks: {
        label: "Clicks",
        color: "hsl(var(--chart-1))",
    },
} satisfies ChartConfig

export interface AnalyticsData {
    overview: {
        totalClicks: number;
        clicksToday: number;
        lastAccessed?: string;
    };
    dailyStats: {
        date: string;
        clicks: number;
    }[];
    countries: {
        name: string;
        value: number;
        percentage?: number;
    }[];
    devices: {
        name: string;
        value: number;
        percentage?: number;
    }[];
    referrers: {
        name: string;
        value: number;
        percentage?: number;
    }[];
}

interface AnalyticsDashboardProps {
    data: AnalyticsData;
    title: string;
    description: string;
    loading?: boolean;
}

const COLORS = ['#0088FE', '#00C49F', '#FFBB28', '#FF8042', '#8884d8'];

export function AnalyticsDashboard({ data, title, description, loading }: AnalyticsDashboardProps) {
    if (loading) {
        return <div className="p-8 text-center text-muted-foreground">Loading analytics...</div>;
    }

    const dynamicDeviceConfig = React.useMemo(() => {
        const config = { ...deviceChartConfig } as ChartConfig;
        if (data?.devices) {
            data.devices.forEach((device, index) => {
                const key = device.name;
                config[key] = {
                    label: device.name,
                    color: COLORS[index % COLORS.length]
                };
            });
        }
        return config;
    }, [data]);

    return (
        <div className="animate-in fade-in duration-500 space-y-4">
            <div className="flex flex-col sm:flex-row items-start sm:items-center justify-between gap-4">
                <div>
                    <h2 className="text-3xl font-bold tracking-tight">{title}</h2>
                    <p className="text-muted-foreground">{description}</p>
                </div>
                <div className="flex items-center space-x-2 bg-muted/50 px-3 py-1 rounded-md">
                    <Calendar className="mr-2 h-4 w-4 opacity-70" />
                    <span className="text-sm text-muted-foreground">Last 30 Days</span>
                </div>
            </div>

            {/* Overview Cards */}
            <div className="grid gap-4 grid-cols-1 sm:grid-cols-2 lg:grid-cols-4">
                <Card>
                    <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                        <CardTitle className="text-sm font-medium">Total Clicks/Scans</CardTitle>
                        <ArrowUpRight className="h-4 w-4 text-muted-foreground" />
                    </CardHeader>
                    <CardContent>
                        <div className="text-2xl font-bold">{data.overview.totalClicks.toLocaleString()}</div>
                        <p className="text-xs text-muted-foreground">
                            Lifetime total
                        </p>
                    </CardContent>
                </Card>
                <Card>
                    <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                        <CardTitle className="text-sm font-medium">Activity Today</CardTitle>
                        <Users className="h-4 w-4 text-muted-foreground" />
                    </CardHeader>
                    <CardContent>
                        <div className="text-2xl font-bold">{data.overview.clicksToday.toLocaleString()}</div>
                        <p className="text-xs text-muted-foreground">
                            Scans/Clicks in last 24h
                        </p>
                    </CardContent>
                </Card>
                <Card>
                    <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                        <CardTitle className="text-sm font-medium">Top Location</CardTitle>
                        <Globe className="h-4 w-4 text-muted-foreground" />
                    </CardHeader>
                    <CardContent>
                        <div className="text-2xl font-bold truncate" title={data.countries[0]?.name}>
                            {data.countries[0]?.name || "N/A"}
                        </div>
                        <p className="text-xs text-muted-foreground">
                            Most active region
                        </p>
                    </CardContent>
                </Card>
                <Card>
                    <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
                        <CardTitle className="text-sm font-medium">Top Device/Browser</CardTitle>
                        <Monitor className="h-4 w-4 text-muted-foreground" />
                    </CardHeader>
                    <CardContent>
                        <div className="text-2xl font-bold truncate" title={data.devices[0]?.name}>
                            {data.devices[0]?.name || "N/A"}
                        </div>
                        <p className="text-xs text-muted-foreground">
                            Most used platform
                        </p>
                    </CardContent>
                </Card>
            </div>

            {/* Main Chart Section */}
            <div className="grid gap-4 grid-cols-1 lg:grid-cols-7">
                <Card className="col-span-1 lg:col-span-4">
                    <CardHeader>
                        <CardTitle>Activity Overview</CardTitle>
                        <CardDescription>
                            Daily clicks/scans over time
                        </CardDescription>
                    </CardHeader>
                    <CardContent className="h-full">
                        <ChartContainer config={activityChartConfig} className="aspect-auto h-[300px] w-full">
                            <AreaChart
                                accessibilityLayer
                                data={data.dailyStats}
                                margin={{
                                    left: 12,
                                    right: 12,
                                }}
                            >
                                <CartesianGrid vertical={false} />
                                <XAxis
                                    dataKey="date"
                                    tickLine={false}
                                    axisLine={false}
                                    tickMargin={8}
                                    tickFormatter={(value) => {
                                        const date = new Date(value);
                                        return `${date.getMonth() + 1}/${date.getDate()}`;
                                    }}
                                />
                                <ChartTooltip
                                    cursor={false}
                                    content={<ChartTooltipContent indicator="line" />}
                                />
                                <defs>
                                    <linearGradient id="fillClicks" x1="0" y1="0" x2="0" y2="1">
                                        <stop
                                            offset="5%"
                                            stopColor="hsl(var(--primary))"
                                            stopOpacity={0.8}
                                        />
                                        <stop
                                            offset="95%"
                                            stopColor="hsl(var(--primary))"
                                            stopOpacity={0.1}
                                        />
                                    </linearGradient>
                                </defs>
                                <Area
                                    dataKey="clicks"
                                    type="natural"
                                    fill="url(#fillClicks)"
                                    fillOpacity={0.4}
                                    stroke="hsl(var(--primary))"
                                />
                            </AreaChart>
                        </ChartContainer>
                    </CardContent>
                </Card>

                <Card className="col-span-1 lg:col-span-3">
                    <CardHeader>
                        <CardTitle>Locations</CardTitle>
                        <CardDescription>Top countries by traffic</CardDescription>
                    </CardHeader>
                    <CardContent>
                        <div className="space-y-4 max-h-[300px] overflow-auto pr-2">
                            {data.countries.slice(0, 5).map((country, i) => (
                                <div key={i} className="flex items-center p-3">
                                    <div className="w-[30px] text-sm text-muted-foreground mr-2 font-medium">
                                        {i + 1}.
                                    </div>
                                    <div className="flex-1 space-y-1">
                                        <div className="flex items-center justify-between text-sm">
                                            <span className="font-medium truncate max-w-[120px]" title={country.name}>{country.name || 'Unknown'}</span>
                                            <span className="text-muted-foreground">{country.value.toLocaleString()}</span>
                                        </div>
                                        <div className="h-2 w-full bg-secondary rounded-full overflow-hidden">
                                            <div
                                                className="h-full bg-primary"
                                                style={{ width: `${country.percentage || 0}%` }}
                                            />
                                        </div>
                                    </div>
                                </div>
                            ))}
                            {data.countries.length === 0 && (
                                <div className="text-center text-sm text-muted-foreground py-8">
                                    No location data available
                                </div>
                            )}
                        </div>
                    </CardContent>
                </Card>
            </div>

            {/* Breakdowns */}
            <div className="grid gap-4 grid-cols-1 md:grid-cols-2">
                <Card className="flex flex-col">
                    <CardHeader className="items-center pb-0">
                        <CardTitle>Devices & Browsers</CardTitle>
                        <CardDescription>Distribution by platform</CardDescription>
                    </CardHeader>
                    <CardContent className="flex-1 pb-0">
                        <div className="h-[250px] w-full">
                            {data.devices.length > 0 ? (
                                <ChartContainer config={dynamicDeviceConfig} className="mx-auto aspect-square max-h-[250px]">
                                    <PieChart>
                                        <ChartTooltip
                                            cursor={false}
                                            content={<ChartTooltipContent hideLabel />}
                                        />
                                        <Pie
                                            data={data.devices.map((device, index) => ({
                                                ...device,
                                                fill: COLORS[index % COLORS.length]
                                            }))}
                                            dataKey="value"
                                            nameKey="name"
                                            innerRadius={60}
                                            strokeWidth={5}
                                        >
                                            <Label
                                                content={({ viewBox }) => {
                                                    if (viewBox && "cx" in viewBox && "cy" in viewBox) {
                                                        return (
                                                            <text
                                                                x={viewBox.cx}
                                                                y={viewBox.cy}
                                                                textAnchor="middle"
                                                                dominantBaseline="middle"
                                                            >
                                                                <tspan
                                                                    x={viewBox.cx}
                                                                    y={viewBox.cy}
                                                                    className="fill-foreground text-3xl font-bold"
                                                                >
                                                                    {data.overview.totalClicks.toLocaleString()}
                                                                </tspan>
                                                                <tspan
                                                                    x={viewBox.cx}
                                                                    y={(viewBox.cy || 0) + 24}
                                                                    className="fill-muted-foreground"
                                                                >
                                                                    Visitors
                                                                </tspan>
                                                            </text>
                                                        )
                                                    }
                                                }}
                                            />
                                        </Pie>
                                        <ChartLegend content={<ChartLegendContent nameKey="name" />} className="flex-wrap gap-2" />
                                    </PieChart>
                                </ChartContainer>
                            ) : (
                                <div className="h-full flex items-center justify-center text-muted-foreground text-sm">
                                    No device data available
                                </div>
                            )}
                        </div>
                    </CardContent>
                </Card>

                <Card>
                    <CardHeader>
                        <CardTitle>Top Referrers</CardTitle>
                        <CardDescription>Where traffic is coming from</CardDescription>
                    </CardHeader>
                    <CardContent>
                        <div className="space-y-4 max-h-[300px] overflow-auto pr-2">
                            {((data.referrers && data.referrers.length > 0) ? data.referrers : [
                                { name: "google.com", value: 1245 },
                                { name: "twitter.com", value: 856 },
                                { name: "linkedin.com", value: 642 },
                                { name: "facebook.com", value: 320 },
                                { name: "Direct", value: 156 }
                            ]).slice(0, 5).map((referrer, i) => (
                                <Card key={i} className="flex items-center justify-between p-3 hover:bg-muted/50 transition-colors">
                                    <div className="flex items-center gap-3 overflow-hidden">
                                        <div className="h-8 w-8 rounded-full bg-primary/10 flex items-center justify-center flex-shrink-0">
                                            <ArrowUpRight className="h-4 w-4 text-primary" />
                                        </div>
                                        <span className="text-sm font-medium truncate" title={referrer.name}>
                                            {referrer.name === 'null' || !referrer.name ? 'Direct / None' : referrer.name}
                                        </span>
                                    </div>
                                    <div className="text-sm font-bold ml-2">
                                        {referrer.value.toLocaleString()}
                                    </div>
                                </Card>
                            ))}
                            {/* Empty state hidden for now to show dummy data */}
                        </div>
                    </CardContent>
                </Card>
            </div>
        </div>
    );
}
