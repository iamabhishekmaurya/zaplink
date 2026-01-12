"use client"

import * as React from "react"
import { Bar, BarChart, CartesianGrid, XAxis, PieChart, Pie, Label, Cell } from "recharts"

import {
    Card,
    CardContent,
    CardDescription,
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
import { DashboardStats } from "@/hooks/useDashboardData"

// --- Creation History Chart ---

const historyChartConfig = {
    links: {
        label: "Links",
        color: "var(--chart-1)",
    },
    qrs: {
        label: "QR Codes",
        color: "var(--chart-2)",
    },
} satisfies ChartConfig

export function CreationHistoryChart({ data }: { data: DashboardStats['creationHistory'] }) {
    return (
        <Card className="h-full flex flex-col">
            <CardHeader>
                <CardTitle>Creation History</CardTitle>
                <CardDescription>New content created over the last 7 days</CardDescription>
            </CardHeader>
            <CardContent className="flex-1">
                <ChartContainer config={historyChartConfig} className="h-[300px] w-full">
                    <BarChart accessibilityLayer data={data}>
                        <CartesianGrid vertical={false} />
                        <XAxis
                            dataKey="date"
                            tickLine={false}
                            tickMargin={10}
                            axisLine={false}
                            tickFormatter={(value) => {
                                const date = new Date(value);
                                return date.toLocaleDateString("en-US", { weekday: 'short', day: 'numeric' });
                            }}
                        />
                        <ChartTooltip content={<ChartTooltipContent indicator="dashed" />} />
                        <ChartLegend content={<ChartLegendContent />} />
                        <Bar dataKey="links" fill="var(--color-links)" radius={[4, 4, 0, 0]} />
                        <Bar dataKey="qrs" fill="var(--color-qrs)" radius={[4, 4, 0, 0]} />
                    </BarChart>
                </ChartContainer>
            </CardContent>
        </Card>
    )
}

// --- Platform Distribution Chart ---

const platformChartConfig = {
    count: {
        label: "Links",
    },
} satisfies ChartConfig

export function PlatformDistributionChart({ data }: { data: DashboardStats['platformDistribution'] }) {
    const totalLinks = React.useMemo(() => {
        return data.reduce((acc, curr) => acc + curr.count, 0)
    }, [data])

    return (
        <Card className="h-full flex flex-col">
            <CardHeader className="items-center pb-0">
                <CardTitle>Link Platforms</CardTitle>
                <CardDescription>Distribution of link destinations</CardDescription>
            </CardHeader>
            <CardContent className="flex-1 pb-0 flex items-center justify-center">
                <ChartContainer
                    config={platformChartConfig}
                    className="mx-auto aspect-square h-[300px]"
                >
                    <PieChart>
                        <ChartTooltip
                            cursor={false}
                            content={<ChartTooltipContent hideLabel />}
                        />
                        <Pie
                            data={data}
                            dataKey="count"
                            nameKey="platform"
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
                                                    {totalLinks.toLocaleString()}
                                                </tspan>
                                                <tspan
                                                    x={viewBox.cx}
                                                    y={(viewBox.cy || 0) + 24}
                                                    className="fill-muted-foreground text-xs"
                                                >
                                                    Total Links
                                                </tspan>
                                            </text>
                                        )
                                    }
                                }}
                            />
                            {data.map((entry, index) => (
                                <Cell key={`cell-${index}`} fill={entry.fill} />
                            ))}
                        </Pie>
                        <ChartLegend content={<ChartLegendContent />} className="-translate-y-2 flex-wrap gap-2 [&>*]:basis-1/4 [&>*]:justify-center" />
                    </PieChart>
                </ChartContainer>
            </CardContent>
        </Card>
    )
}
