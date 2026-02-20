import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
import {
  ChartConfig,
  ChartContainer,
  ChartLegend,
  ChartLegendContent,
  ChartTooltip,
  ChartTooltipContent,
} from "@/components/ui/chart";
import { cn } from "@/lib/utils";
import { BioPage } from "@/services/bioPageService";
import { motion } from "framer-motion";
import {
  Activity,
  BarChart3,
  Clock,
  Globe,
  MousePointer,
  Smartphone,
  TrendingUp,
  Users
} from "lucide-react";
import { useEffect, useState } from "react";
import {
  Area,
  AreaChart,
  CartesianGrid,
  Pie,
  PieChart,
  XAxis,
  YAxis
} from "recharts";

interface AnalyticsTabProps {
  bioPage: BioPage;
}

interface AnalyticsData {
  totalClicks: number;
  uniqueVisitors: number;
  averageTime: number;
  bounceRate: number;
  topCountries: Array<{ name: string; value: number; percentage: number }>;
  dailyViews: Array<{ date: string; views: number; clicks: number }>;
  linkPerformance: Array<{ title: string; clicks: number; percentage: number }>;
  deviceStats: Array<{ device: string; count: number; percentage: number; fill: string }>;
}

const viewsChartConfig = {
  views: {
    label: "Page Views",
    color: "#8b5cf6",
  },
  clicks: {
    label: "Total Clicks",
    color: "#06b6d4",
  },
} satisfies ChartConfig;

const deviceChartConfig = {
  count: {
    label: "Visitors",
  },
  mobile: {
    label: "Mobile",
    color: "#8b5cf6",
  },
  desktop: {
    label: "Desktop",
    color: "#06b6d4",
  },
  tablet: {
    label: "Tablet",
    color: "#f59e0b",
  },
} satisfies ChartConfig;

const cardVariants = {
  hidden: { opacity: 0, y: 20 },
  visible: (i: number) => ({
    opacity: 1,
    y: 0,
    transition: {
      delay: i * 0.1,
      duration: 0.3,
    },
  }),
};

// Stat Card Component
const colorMap: Record<string, string> = {
  blue: "bg-blue-500/10 text-blue-600",
  emerald: "bg-emerald-500/10 text-emerald-600",
  amber: "bg-amber-500/10 text-amber-600",
  violet: "bg-violet-500/10 text-violet-600",
};

function StatCard({
  title,
  value,
  icon: Icon,
  color,
  subtitle,
  index
}: {
  title: string;
  value: string;
  icon: any;
  color: string;
  subtitle?: string;
  index: number;
}) {
  const colorStyles = colorMap[color] || colorMap.blue;

  return (
    <motion.div custom={index} initial="hidden" animate="visible" variants={cardVariants}>
      <Card className="border shadow-sm overflow-hidden hover:shadow-md transition-all duration-200">
        <CardHeader className="flex flex-row items-center justify-between space-y-0 pb-2">
          <CardTitle className="text-sm font-medium text-muted-foreground">
            {title}
          </CardTitle>
          <div className={cn("p-2 rounded-lg transition-colors", colorStyles)}>
            <Icon className="h-4 w-4" />
          </div>
        </CardHeader>
        <CardContent>
          <div className="text-2xl font-bold tracking-tight">{value}</div>
          {subtitle && (
            <p className="text-xs text-muted-foreground mt-1">
              {subtitle}
            </p>
          )}
        </CardContent>
      </Card>
    </motion.div>
  );
}

export function AnalyticsTab({ bioPage }: AnalyticsTabProps) {
  const [analytics, setAnalytics] = useState<AnalyticsData | null>(null);
  const [isLoading, setIsLoading] = useState(true);
  const [dateRange, setDateRange] = useState<'7d' | '30d' | '90d'>('30d');

  useEffect(() => {
    fetchAnalytics();
  }, [bioPage.id, dateRange]);

  const fetchAnalytics = async () => {
    if (!analytics) setIsLoading(true);
    try {
      // Mock data - replace with actual API call
      const mockData: AnalyticsData = {
        totalClicks: 1234,
        uniqueVisitors: 856,
        averageTime: 145, // seconds
        bounceRate: 32.5, // percentage
        topCountries: [
          { name: 'United States', value: 423, percentage: 49.4 },
          { name: 'United Kingdom', value: 156, percentage: 18.2 },
          { name: 'Canada', value: 98, percentage: 11.5 },
          { name: 'Germany', value: 87, percentage: 10.2 },
          { name: 'France', value: 92, percentage: 10.7 }
        ],
        dailyViews: Array.from({ length: 30 }, (_, i) => ({
          date: new Date(Date.now() - (29 - i) * 24 * 60 * 60 * 1000).toISOString().split('T')[0],
          views: Math.floor(Math.random() * 50) + 10,
          clicks: Math.floor(Math.random() * 30) + 5
        })),
        linkPerformance: bioPage.bioLinks?.map(link => ({
          title: link.title,
          clicks: Math.floor(Math.random() * 200) + 10,
          percentage: Math.random() * 100
        })) || [],
        deviceStats: [
          { device: 'Mobile', count: 567, percentage: 66.3, fill: "var(--color-mobile)" },
          { device: 'Desktop', count: 234, percentage: 27.4, fill: "var(--color-desktop)" },
          { device: 'Tablet', count: 55, percentage: 6.3, fill: "var(--color-tablet)" }
        ]
      };

      // Simulate API delay
      await new Promise(resolve => setTimeout(resolve, 1000));
      setAnalytics(mockData);
    } catch {
      // Silent fail for analytics fetch
    } finally {
      setIsLoading(false);
    }
  };

  if (isLoading) {
    return (
      <div className="space-y-6">
        {/* Header Skeleton */}
        <div className="flex items-center gap-3 pb-4 border-b">
          <div className="w-10 h-10 rounded-xl bg-muted animate-pulse" />
          <div className="space-y-2">
            <div className="w-32 h-5 bg-muted rounded animate-pulse" />
            <div className="w-48 h-3 bg-muted rounded animate-pulse" />
          </div>
        </div>

        <div className="grid grid-cols-2 gap-4">
          {Array.from({ length: 4 }).map((_, i) => (
            <Card key={i} className="animate-pulse border-0 shadow-sm">
              <CardContent className="p-5">
                <div className="h-4 bg-muted rounded w-3/4 mb-2"></div>
                <div className="h-8 bg-muted rounded w-1/2"></div>
              </CardContent>
            </Card>
          ))}
        </div>
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
          <Card className="animate-pulse h-80 border-0 shadow-sm lg:col-span-2" />
          <Card className="animate-pulse h-80 border-0 shadow-sm" />
        </div>
      </div>
    );
  }

  if (!analytics) {
    return (
      <motion.div
        initial={{ opacity: 0, scale: 0.95 }}
        animate={{ opacity: 1, scale: 1 }}
        className="text-center py-16 px-4"
      >
        <div className="w-20 h-20 rounded-full bg-gradient-to-br from-slate-100 to-slate-200 flex items-center justify-center mx-auto mb-6">
          <BarChart3 className="w-10 h-10 text-slate-400" />
        </div>
        <h3 className="text-xl font-semibold mb-2">No Analytics Available</h3>
        <p className="text-muted-foreground max-w-md mx-auto">
          Analytics data will appear once your bio page starts receiving traffic. Share your link to get started!
        </p>
      </motion.div>
    );
  }

  return (
    <div className="space-y-6 pb-20">
      {/* Header */}
      <motion.div
        initial={{ opacity: 0, y: -10 }}
        animate={{ opacity: 1, y: 0 }}
        className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4 pb-4 border-b"
      >
        <div className="flex items-center gap-3">
          <div className="p-2 bg-primary/10 rounded-xl">
            <BarChart3 className="w-5 h-5 text-primary" />
          </div>
          <div>
            <h3 className="text-lg font-semibold text-foreground">
              Analytics Dashboard
            </h3>
            <p className="text-sm text-muted-foreground">
              Track your page performance and visitor insights
            </p>
          </div>
        </div>

        {/* Date Range Selector */}
        <div className="flex bg-muted rounded-lg p-1">
          {(['7d', '30d', '90d'] as const).map((range) => (
            <Button
              key={range}
              variant={dateRange === range ? "secondary" : "ghost"}
              size="sm"
              onClick={() => setDateRange(range)}
              className={cn(
                "text-xs",
                dateRange === range && "bg-background shadow-sm"
              )}
            >
              {range === '7d' ? '7 Days' : range === '30d' ? '30 Days' : '90 Days'}
            </Button>
          ))}
        </div>
      </motion.div>

      {/* Key Metrics - 2x2 Grid */}
      <div className="grid grid-cols-2 gap-4">
        <StatCard
          title="Total Clicks"
          value={analytics.totalClicks.toLocaleString()}
          icon={MousePointer}
          color="blue"
          index={0}
        />
        <StatCard
          title="Unique Visitors"
          value={analytics.uniqueVisitors.toLocaleString()}
          icon={Users}
          color="emerald"
          index={1}
        />
        <StatCard
          title="Avg. Time"
          value={`${Math.floor(analytics.averageTime / 60)}m ${analytics.averageTime % 60}s`}
          icon={Clock}
          color="amber"
          index={2}
        />
        <StatCard
          title="Bounce Rate"
          value={`${analytics.bounceRate.toFixed(1)}%`}
          icon={Activity}
          color="violet"
          index={3}
        />
      </div>

      {/* Main Charts Row */}
      <div className="flex flex-col gap-6">
        {/* Daily Views Chart */}
        <motion.div custom={4} initial="hidden" animate="visible" variants={cardVariants}>
          <Card className="border shadow-sm overflow-hidden h-full">
            <CardHeader>
              <CardTitle>Daily Views & Clicks</CardTitle>
            </CardHeader>
            <CardContent className="px-2 sm:px-4">
              <ChartContainer config={viewsChartConfig} className="aspect-auto h-[280px] w-full">
                <AreaChart
                  data={analytics.dailyViews}
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
                    minTickGap={32}
                    tickFormatter={(value) => {
                      const date = new Date(value)
                      return date.toLocaleDateString("en-US", {
                        month: "short",
                        day: "numeric",
                      })
                    }}
                  />
                  <ChartTooltip
                    cursor={false}
                    content={<ChartTooltipContent indicator="dot" />}
                  />
                  <Area
                    dataKey="views"
                    type="natural"
                    fill={viewsChartConfig.views.color}
                    fillOpacity={0.4}
                    stroke={viewsChartConfig.views.color}
                    stackId="a"
                  />
                  <Area
                    dataKey="clicks"
                    type="natural"
                    fill={viewsChartConfig.clicks.color}
                    fillOpacity={0.4}
                    stroke={viewsChartConfig.clicks.color}
                    stackId="a"
                  />
                </AreaChart>
              </ChartContainer>
            </CardContent>
          </Card>
        </motion.div>

        {/* Device Stats */}
        <motion.div custom={5} initial="hidden" animate="visible" variants={cardVariants}>
          <Card className="border shadow-sm overflow-hidden h-full">
            <CardHeader>
              <CardTitle>Traffic by Device</CardTitle>
            </CardHeader>
            <CardContent>
              <ChartContainer
                config={deviceChartConfig}
                className="mx-auto aspect-square max-h-[300px]"
              >
                <PieChart>
                  <ChartTooltip
                    cursor={false}
                    content={<ChartTooltipContent hideLabel />}
                  />
                  <Pie
                    data={analytics.deviceStats}
                    dataKey="count"
                    nameKey="device"
                    innerRadius={60}
                    strokeWidth={5}
                  />
                  <ChartLegend content={<ChartLegendContent />} />
                </PieChart>
              </ChartContainer>
            </CardContent>
          </Card>
        </motion.div>
      </div>

      {/* Bottom Row - Performance & Countries */}
      <div className="flex flex-col gap-6">
        {/* Link Performance */}
        <motion.div custom={6} initial="hidden" animate="visible" variants={cardVariants}>
          <Card className="border shadow-sm overflow-hidden h-full">
            <CardHeader>
              <CardTitle>Link Performance</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="space-y-3">
                {analytics.linkPerformance.slice(0, 5).map((link, index) => (
                  <div key={index} className="flex items-center gap-4 p-3 rounded-lg border bg-card hover:shadow-sm transition-shadow">
                    <div className="w-8 h-8 rounded-lg bg-muted flex items-center justify-center text-sm font-semibold text-foreground">
                      {index + 1}
                    </div>
                    <div className="flex-1 min-w-0">
                      <p className="font-medium text-sm truncate">{link.title}</p>
                      <p className="text-xs text-muted-foreground">{link.clicks.toLocaleString()} clicks</p>
                    </div>
                    <div className="flex items-center gap-3">
                      <div className="w-32 bg-muted rounded-full h-2 overflow-hidden">
                        <div
                          className="h-full rounded-full bg-primary transition-all duration-500"
                          style={{ width: `${Math.max(link.percentage, 5)}%` }}
                        />
                      </div>
                      <span className="text-sm font-medium w-12 text-right text-muted-foreground">
                        {link.percentage.toFixed(0)}%
                      </span>
                    </div>
                  </div>
                ))}
              </div>
            </CardContent>
          </Card>
        </motion.div>

        {/* Top Countries */}
        <motion.div custom={7} initial="hidden" animate="visible" variants={cardVariants}>
          <Card className="border shadow-sm overflow-hidden h-full">
            <CardHeader>
              <CardTitle>Top Countries</CardTitle>
            </CardHeader>
            <CardContent>
              <div className="grid grid-cols-1 sm:grid-cols-2 gap-3">
                {analytics.topCountries.map((country, index) => (
                  <div
                    key={index}
                    className="flex items-center justify-between p-3 rounded-lg border bg-card"
                  >
                    <div className="flex items-center gap-3">
                      <div className="w-8 h-8 rounded-lg bg-muted flex items-center justify-center">
                        <Globe className="h-4 w-4 text-muted-foreground" />
                      </div>
                      <div>
                        <span className="font-medium text-sm">{country.name}</span>
                        <p className="text-xs text-muted-foreground">{country.value.toLocaleString()} visitors</p>
                      </div>
                    </div>
                    <Badge variant="secondary">
                      {country.percentage.toFixed(1)}%
                    </Badge>
                  </div>
                ))}
              </div>
            </CardContent>
          </Card>
        </motion.div>
      </div>
    </div>
  );
}
