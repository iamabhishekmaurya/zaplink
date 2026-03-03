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
import { motion, Variants } from "framer-motion";
import {
  Activity,
  BarChart3,
  Clock,
  Globe,
  MousePointer,
  Smartphone,
  TrendingUp,
  Users,
  Calendar
} from "lucide-react";
import { useEffect, useState } from "react";
import {
  Area,
  AreaChart,
  CartesianGrid,
  Pie,
  PieChart,
  XAxis,
  YAxis,
  Cell
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
    color: "var(--color-primary)",
  },
  clicks: {
    label: "Total Clicks",
    color: "var(--color-chart-2)",
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

const cardVariants: Variants = {
  hidden: { opacity: 0, y: 20 },
  visible: (i: number) => ({
    opacity: 1,
    y: 0,
    transition: {
      delay: i * 0.1,
      duration: 0.4,
      ease: "easeOut"
    },
  }),
};

// Stat Card Component
const colorStyles: Record<string, { bg: string, iconBg: string, iconText: string }> = {
  blue: { bg: "bg-blue-50/50 dark:bg-blue-500/10", iconBg: "bg-blue-100 dark:bg-blue-500/20", iconText: "text-blue-500 dark:text-blue-400" },
  emerald: { bg: "bg-emerald-50/50 dark:bg-emerald-500/10", iconBg: "bg-emerald-100 dark:bg-emerald-500/20", iconText: "text-emerald-500 dark:text-emerald-400" },
  amber: { bg: "bg-orange-50/50 dark:bg-orange-500/10", iconBg: "bg-orange-100 dark:bg-orange-500/20", iconText: "text-orange-500 dark:text-orange-400" },
  violet: { bg: "bg-purple-50/50 dark:bg-purple-500/10", iconBg: "bg-purple-100 dark:bg-purple-500/20", iconText: "text-purple-500 dark:text-purple-400" },
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
  const styles = colorStyles[color] || colorStyles.blue;

  return (
    <motion.div custom={index} initial="hidden" animate="visible" variants={cardVariants} className="h-full">
      <Card className={cn(
        "border border-black/5 dark:border-white/5 shadow-none overflow-hidden hover:scale-[1.02] transition-transform duration-300 relative group rounded-3xl h-full",
        styles.bg
      )}>
        <CardContent className="p-6 sm:p-7 flex flex-col justify-start h-full relative z-10 gap-6">
          <div className={cn("w-12 h-12 rounded-2xl flex items-center justify-center shrink-0", styles.iconBg)}>
            <Icon className={cn("h-5 w-5", styles.iconText)} />
          </div>
          <div className="flex flex-col flex-1 pb-2">
            <div className="text-4xl sm:text-[40px] font-black tracking-tight mb-3 text-foreground leading-none">
              {value}
            </div>
            <p className="text-[11px] sm:text-xs font-bold text-muted-foreground/80 tracking-widest uppercase mt-auto">
              {title}
            </p>
            {subtitle && (
              <p className="text-xs text-muted-foreground mt-3 font-medium">
                {subtitle}
              </p>
            )}
          </div>
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
        <div className="flex items-center justify-between pb-6 border-b">
          <div className="flex items-center gap-4">
            <div className="w-12 h-12 rounded-2xl bg-muted animate-pulse" />
            <div className="space-y-2">
              <div className="w-40 h-6 bg-muted rounded-md animate-pulse" />
              <div className="w-64 h-4 bg-muted/50 rounded-md animate-pulse" />
            </div>
          </div>
          <div className="w-32 h-10 rounded-xl bg-muted animate-pulse" />
        </div>

        <div className="grid grid-cols-2 lg:grid-cols-4 gap-4">
          {Array.from({ length: 4 }).map((_, i) => (
            <Card key={i} className="animate-pulse border-0 shadow-sm rounded-2xl h-36">
              <CardContent className="p-5 h-full flex flex-col justify-between">
                <div className="w-10 h-10 bg-muted/50 rounded-xl" />
                <div className="space-y-2 mt-auto">
                  <div className="h-8 bg-muted rounded-md w-3/4" />
                  <div className="h-4 bg-muted/50 rounded-md w-1/2" />
                </div>
              </CardContent>
            </Card>
          ))}
        </div>
        <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
          <Card className="animate-pulse h-96 border-0 shadow-sm rounded-2xl lg:col-span-2" />
          <Card className="animate-pulse h-96 border-0 shadow-sm rounded-2xl" />
        </div>
      </div>
    );
  }

  if (!analytics) {
    return (
      <motion.div
        initial={{ opacity: 0, scale: 0.98 }}
        animate={{ opacity: 1, scale: 1 }}
        className="text-center py-20 px-4"
      >
        <div className="w-24 h-24 rounded-[2rem] bg-gradient-to-br from-muted/50 to-muted flex items-center justify-center mx-auto mb-6 shadow-inner border border-border/50">
          <BarChart3 className="w-10 h-10 text-muted-foreground/60" />
        </div>
        <h3 className="text-xl sm:text-2xl font-bold bg-clip-text text-transparent bg-gradient-to-r from-foreground to-foreground/70 mb-3">
          No Analytics Yet
        </h3>
        <p className="text-muted-foreground/80 max-w-md mx-auto text-sm leading-relaxed">
          Share your bio link with your audience. Once visitors start landing on your page, beautifully detailed insights will appear here.
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
        className="flex flex-col sm:flex-row justify-between items-start sm:items-center gap-4 pb-6 border-b"
      >
        <div className="flex items-center gap-4">
          <div className="p-3 bg-purple-50 dark:bg-purple-500/10 rounded-2xl shadow-sm border-0 border-purple-500/10">
            <BarChart3 className="w-6 h-6 text-purple-600 dark:text-purple-400" />
          </div>
          <div>
            <h3 className="text-xl font-bold bg-clip-text text-transparent bg-gradient-to-r from-foreground to-foreground/70">
              Analytics Dashboard
            </h3>
            <p className="text-sm text-muted-foreground mt-1">
              Deep dive into your audience engagement and page performance
            </p>
          </div>
        </div>

        {/* Date Range Selector */}
        <div className="flex items-center gap-1 bg-transparent p-1 rounded-2xl border border-border/80 shadow-sm">
          <div className="pl-3 pr-2 text-muted-foreground">
            <Calendar className="w-4 h-4" />
          </div>
          {(['7d', '30d', '90d'] as const).map((range) => (
            <Button
              key={range}
              variant={dateRange === range ? "default" : "ghost"}
              size="sm"
              onClick={() => setDateRange(range)}
              className={cn(
                "text-xs px-3 sm:px-4 h-9 shadow-none transition-all rounded-xl",
                dateRange === range ? "bg-muted text-foreground shadow-sm hover:translate-y-0" : "hover:bg-muted/50 font-medium hover:text-foreground text-muted-foreground"
              )}
            >
              {range === '7d' ? '7 Days' : range === '30d' ? '30 Days' : '90 Days'}
            </Button>
          ))}
        </div>
      </motion.div>

      {/* Key Metrics - 4 Columns on large, 2 on small */}
      <div className="grid grid-cols-2 lg:grid-cols-4 gap-4">
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
          subtitle={`+${Math.floor(Math.random() * 20 + 5)}% from last period`}
        />
        <StatCard
          title="Avg. Time on Page"
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
          subtitle="-2.4% from last period"
        />
      </div>

      {/* Main Charts Row */}
      <div className="grid grid-cols-1 gap-6">
        {/* Daily Views Chart */}
        <motion.div custom={4} initial="hidden" animate="visible" variants={cardVariants}>
          <Card className="border border-border/60 shadow-sm rounded-3xl overflow-hidden h-full">
            <CardHeader className="border-b-0 pb-2 pt-6 px-6">
              <CardTitle className="text-base font-bold text-foreground">Traffic Trends</CardTitle>
            </CardHeader>
            <CardContent className="p-6">
              <ChartContainer config={viewsChartConfig} className="w-full h-[300px]">
                <AreaChart
                  data={analytics.dailyViews}
                  margin={{
                    left: 0,
                    right: 0,
                    top: 10,
                    bottom: 0,
                  }}
                >
                  <defs>
                    <linearGradient id="colorViews" x1="0" y1="0" x2="0" y2="1">
                      <stop offset="5%" stopColor={viewsChartConfig.views.color} stopOpacity={0.3} />
                      <stop offset="95%" stopColor={viewsChartConfig.views.color} stopOpacity={0} />
                    </linearGradient>
                    <linearGradient id="colorClicks" x1="0" y1="0" x2="0" y2="1">
                      <stop offset="5%" stopColor={viewsChartConfig.clicks.color} stopOpacity={0.3} />
                      <stop offset="95%" stopColor={viewsChartConfig.clicks.color} stopOpacity={0} />
                    </linearGradient>
                  </defs>
                  <CartesianGrid vertical={false} strokeDasharray="3 3" stroke="var(--border)" opacity={0.5} />
                  <XAxis
                    dataKey="date"
                    tickLine={false}
                    axisLine={false}
                    tickMargin={12}
                    minTickGap={32}
                    tickFormatter={(value) => {
                      const date = new Date(value)
                      return date.toLocaleDateString("en-US", {
                        month: "short",
                        day: "numeric",
                      })
                    }}
                    stroke="var(--muted-foreground)"
                    fontSize={12}
                  />
                  <YAxis
                    tickLine={false}
                    axisLine={false}
                    tickMargin={12}
                    stroke="var(--muted-foreground)"
                    fontSize={12}
                    tickFormatter={(val) => val.toLocaleString()}
                  />
                  <ChartTooltip
                    cursor={{ stroke: 'var(--border)', strokeWidth: 1, strokeDasharray: '3 3' }}
                    content={<ChartTooltipContent indicator="dot" className="rounded-xl shadow-lg border-border/50" />}
                  />
                  <Area
                    dataKey="views"
                    type="monotone"
                    fill="url(#colorViews)"
                    fillOpacity={1}
                    stroke={viewsChartConfig.views.color}
                    strokeWidth={3}
                    activeDot={{ r: 6, strokeWidth: 0, fill: viewsChartConfig.views.color }}
                  />
                  <Area
                    dataKey="clicks"
                    type="monotone"
                    fill="url(#colorClicks)"
                    fillOpacity={1}
                    stroke={viewsChartConfig.clicks.color}
                    strokeWidth={3}
                    activeDot={{ r: 6, strokeWidth: 0, fill: viewsChartConfig.clicks.color }}
                  />
                </AreaChart>
              </ChartContainer>
            </CardContent>
          </Card>
        </motion.div>
      </div>

      {/* Bottom Row - Devices, Performance & Countries */}
      <div className="grid grid-cols-1 lg:grid-cols-3 gap-6">
        {/* Device Stats */}
        <motion.div custom={5} initial="hidden" animate="visible" variants={cardVariants}>
          <Card className="border border-border/60 shadow-sm rounded-3xl overflow-hidden h-full">
            <CardHeader className="border-b-0 pb-2 pt-6 px-6">
              <CardTitle className="text-base font-bold text-foreground">Audience Devices</CardTitle>
            </CardHeader>
            <CardContent className="p-6 flex flex-col justify-center">
              <ChartContainer
                config={deviceChartConfig}
                className="mx-auto aspect-square max-h-[250px] w-full"
              >
                <PieChart>
                  <ChartTooltip
                    cursor={false}
                    content={<ChartTooltipContent hideLabel className="rounded-xl shadow-lg border-border/50 bg-background/95 backdrop-blur-sm" />}
                  />
                  <Pie
                    data={analytics.deviceStats}
                    dataKey="count"
                    nameKey="device"
                    innerRadius={70}
                    outerRadius={100}
                    strokeWidth={4}
                    stroke="var(--background)"
                    paddingAngle={5}
                  >
                    {analytics.deviceStats.map((entry, index) => (
                      <Cell key={`cell-${index}`} fill={entry.fill} />
                    ))}
                  </Pie>
                  <ChartLegend
                    content={<ChartLegendContent className="text-sm mt-4 text-muted-foreground" />}
                    iconType="circle"
                  />
                </PieChart>
              </ChartContainer>
            </CardContent>
          </Card>
        </motion.div>

        {/* Link Performance */}
        <motion.div custom={6} initial="hidden" animate="visible" variants={cardVariants} className="flex flex-col h-full">
          <Card className="border border-border/60 shadow-sm rounded-3xl overflow-hidden flex-1">
            <CardHeader className="border-b-0 pb-2 pt-6 px-6">
              <CardTitle className="text-base font-bold text-foreground">Link Engagement</CardTitle>
            </CardHeader>
            <CardContent className="p-0">
              <div className="divide-y divide-border/50">
                {analytics.linkPerformance.slice(0, 5).map((link, index) => (
                  <div key={index} className="flex items-center gap-4 p-4 hover:bg-muted/30 transition-colors">
                    <div className="w-10 h-10 rounded-xl bg-primary/10 flex items-center justify-center text-sm font-bold text-primary shrink-0 border border-primary/20">
                      {index + 1}
                    </div>
                    <div className="flex-1 min-w-0 pr-4">
                      <p className="font-semibold text-sm truncate text-foreground leading-tight mb-1">{link.title}</p>
                      <div className="flex items-center gap-2">
                        <TrendingUp className="w-3.5 h-3.5 text-emerald-500" />
                        <p className="text-xs text-muted-foreground font-medium">{link.clicks.toLocaleString()} clicks</p>
                      </div>
                    </div>
                    <div className="flex flex-col items-end gap-1.5 shrink-0 w-24">
                      <span className="text-sm font-bold text-foreground">
                        {link.percentage.toFixed(0)}%
                      </span>
                      <div className="w-full bg-muted rounded-full h-1.5 overflow-hidden">
                        <div
                          className="h-full rounded-full bg-gradient-to-r from-primary/80 to-primary transition-all duration-1000 ease-out"
                          style={{ width: `${Math.max(link.percentage, 5)}%` }}
                        />
                      </div>
                    </div>
                  </div>
                ))}
                {analytics.linkPerformance.length === 0 && (
                  <div className="p-8 text-center text-muted-foreground text-sm">No links found to analyze.</div>
                )}
              </div>
            </CardContent>
          </Card>
        </motion.div>

        {/* Top Countries */}
        <motion.div custom={7} initial="hidden" animate="visible" variants={cardVariants} className="flex flex-col h-full">
          <Card className="border border-border/60 shadow-sm rounded-3xl overflow-hidden flex-1">
            <CardHeader className="border-b-0 pb-2 pt-6 px-6">
              <CardTitle className="text-base font-bold text-foreground">Global Reach</CardTitle>
            </CardHeader>
            <CardContent className="p-5">
              <div className="grid gap-3">
                {analytics.topCountries.map((country, index) => (
                  <div
                    key={index}
                    className="flex items-center justify-between p-3.5 rounded-xl border border-border/50 bg-card hover:bg-muted/30 transition-colors group"
                  >
                    <div className="flex items-center gap-3.5">
                      <div className="w-10 h-10 rounded-xl bg-blue-500/10 border border-blue-500/20 flex items-center justify-center group-hover:scale-105 transition-transform">
                        <Globe className="h-5 w-5 text-blue-500" />
                      </div>
                      <div>
                        <span className="font-semibold text-sm text-foreground block mb-0.5">{country.name}</span>
                        <p className="text-xs font-medium text-muted-foreground">{country.value.toLocaleString()} visitors</p>
                      </div>
                    </div>
                    <Badge variant="outline" className="bg-background shadow-xs font-bold text-primary border-primary/20">
                      {country.percentage.toFixed(1)}%
                    </Badge>
                  </div>
                ))}
                {analytics.topCountries.length === 0 && (
                  <div className="p-8 text-center text-muted-foreground text-sm border border-dashed rounded-xl">No geographic data available yet.</div>
                )}
              </div>
            </CardContent>
          </Card>
        </motion.div>
      </div>
    </div>
  );
}
