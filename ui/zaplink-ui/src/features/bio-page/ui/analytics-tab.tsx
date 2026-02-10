"use client"

import { Badge } from "@/components/ui/badge";
import { Button } from "@/components/ui/button";
import { Card, CardContent, CardHeader, CardTitle } from "@/components/ui/card";
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
  Cell,
  Pie,
  PieChart,
  ResponsiveContainer,
  Tooltip,
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
  deviceStats: Array<{ device: string; count: number; percentage: number }>;
}

const COLORS = ['#8b5cf6', '#06b6d4', '#f59e0b', '#ef4444', '#10b981'];

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
  const colorClasses: Record<string, string> = {
    blue: 'from-blue-500 to-cyan-500',
    emerald: 'from-emerald-500 to-teal-500',
    amber: 'from-amber-500 to-orange-500',
    violet: 'from-violet-500 to-purple-500',
  };
  
  return (
    <motion.div custom={index} initial="hidden" animate="visible" variants={cardVariants}>
      <Card className="border shadow-sm overflow-hidden hover:shadow-md transition-shadow">
        <CardContent className="p-5">
          <div className="flex items-start justify-between">
            <div className="space-y-1">
              <p className="text-sm font-medium text-muted-foreground">{title}</p>
              <p className="text-2xl font-bold bg-gradient-to-r from-foreground to-muted-foreground bg-clip-text text-transparent">
                {value}
              </p>
              {subtitle && <p className="text-xs text-muted-foreground">{subtitle}</p>}
            </div>
            <div className={cn(
              "p-2.5 rounded-xl bg-gradient-to-br",
              colorClasses[color]
            )}>
              <Icon className="h-5 w-5 text-white" />
            </div>
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
    setIsLoading(true);
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
          { device: 'Mobile', count: 567, percentage: 66.3 },
          { device: 'Desktop', count: 234, percentage: 27.4 },
          { device: 'Tablet', count: 55, percentage: 6.3 }
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
        
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
          {Array.from({ length: 4 }).map((_, i) => (
            <Card key={i} className="animate-pulse border-0 shadow-sm">
              <CardContent className="p-5">
                <div className="h-4 bg-muted rounded w-3/4 mb-2"></div>
                <div className="h-8 bg-muted rounded w-1/2"></div>
              </CardContent>
            </Card>
          ))}
        </div>
        <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
          <Card className="animate-pulse h-80 border-0 shadow-sm" />
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
          <div className="p-2 bg-gradient-to-br from-blue-100 to-cyan-100 rounded-xl">
            <BarChart3 className="w-5 h-5 text-blue-600" />
          </div>
          <div>
            <h3 className="text-lg font-semibold bg-gradient-to-r from-blue-600 to-cyan-600 bg-clip-text text-transparent">
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
                dateRange === range && "bg-white shadow-sm"
              )}
            >
              {range === '7d' ? '7 Days' : range === '30d' ? '30 Days' : '90 Days'}
            </Button>
          ))}
        </div>
      </motion.div>

      {/* Key Metrics */}
      <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-4 gap-4">
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

      {/* Charts Row */}
      <div className="grid grid-cols-1 lg:grid-cols-2 gap-6">
        {/* Daily Views Chart */}
        <motion.div custom={4} initial="hidden" animate="visible" variants={cardVariants}>
          <Card className="border shadow-sm overflow-hidden">
            <CardHeader className="bg-gradient-to-r from-violet-50 to-purple-50 border-b pb-3">
              <div className="flex items-center gap-2">
                <TrendingUp className="w-4 h-4 text-violet-600" />
                <CardTitle className="text-base font-semibold">Daily Views & Clicks</CardTitle>
              </div>
            </CardHeader>
            <CardContent className="pt-4">
              <ResponsiveContainer width="100%" height={280}>
                <AreaChart data={analytics.dailyViews}>
                  <defs>
                    <linearGradient id="colorViews" x1="0" y1="0" x2="0" y2="1">
                      <stop offset="5%" stopColor="#8b5cf6" stopOpacity={0.3}/>
                      <stop offset="95%" stopColor="#8b5cf6" stopOpacity={0}/>
                    </linearGradient>
                    <linearGradient id="colorClicks" x1="0" y1="0" x2="0" y2="1">
                      <stop offset="5%" stopColor="#06b6d4" stopOpacity={0.3}/>
                      <stop offset="95%" stopColor="#06b6d4" stopOpacity={0}/>
                    </linearGradient>
                  </defs>
                  <CartesianGrid strokeDasharray="3 3" stroke="#e5e7eb" />
                  <XAxis dataKey="date" tick={{fontSize: 11}} stroke="#9ca3af" />
                  <YAxis tick={{fontSize: 11}} stroke="#9ca3af" />
                  <Tooltip 
                    contentStyle={{borderRadius: '8px', border: 'none', boxShadow: '0 4px 6px -1px rgb(0 0 0 / 0.1)'}}
                  />
                  <Area type="monotone" dataKey="views" stroke="#8b5cf6" strokeWidth={2} fillOpacity={1} fill="url(#colorViews)" />
                  <Area type="monotone" dataKey="clicks" stroke="#06b6d4" strokeWidth={2} fillOpacity={1} fill="url(#colorClicks)" />
                </AreaChart>
              </ResponsiveContainer>
            </CardContent>
          </Card>
        </motion.div>

        {/* Device Stats */}
        <motion.div custom={5} initial="hidden" animate="visible" variants={cardVariants}>
          <Card className="border shadow-sm overflow-hidden">
            <CardHeader className="bg-gradient-to-r from-cyan-50 to-blue-50 border-b pb-3">
              <div className="flex items-center gap-2">
                <Smartphone className="w-4 h-4 text-cyan-600" />
                <CardTitle className="text-base font-semibold">Traffic by Device</CardTitle>
              </div>
            </CardHeader>
            <CardContent className="pt-4">
              <ResponsiveContainer width="100%" height={280}>
                <PieChart>
                  <Pie
                    data={analytics.deviceStats}
                    cx="50%"
                    cy="50%"
                    innerRadius={60}
                    outerRadius={100}
                    paddingAngle={4}
                    dataKey="count"
                  >
                    {analytics.deviceStats.map((entry, index) => (
                      <Cell key={`cell-${index}`} fill={COLORS[index % COLORS.length]} />
                    ))}
                  </Pie>
                  <Tooltip 
                    contentStyle={{borderRadius: '8px', border: 'none', boxShadow: '0 4px 6px -1px rgb(0 0 0 / 0.1)'}}
                  />
                </PieChart>
              </ResponsiveContainer>
              {/* Device Legend */}
              <div className="flex justify-center gap-4 mt-2">
                {analytics.deviceStats.map((device, index) => (
                  <div key={device.device} className="flex items-center gap-1.5">
                    <div 
                      className="w-3 h-3 rounded-full" 
                      style={{ backgroundColor: COLORS[index % COLORS.length] }}
                    />
                    <span className="text-xs text-muted-foreground">
                      {device.device} ({device.percentage.toFixed(0)}%)
                    </span>
                  </div>
                ))}
              </div>
            </CardContent>
          </Card>
        </motion.div>
      </div>

      {/* Link Performance */}
      <motion.div custom={6} initial="hidden" animate="visible" variants={cardVariants}>
        <Card className="border shadow-sm overflow-hidden">
          <CardHeader className="bg-gradient-to-r from-emerald-50 to-teal-50 border-b pb-3">
            <div className="flex items-center gap-2">
              <BarChart3 className="w-4 h-4 text-emerald-600" />
              <CardTitle className="text-base font-semibold">Link Performance</CardTitle>
            </div>
          </CardHeader>
          <CardContent className="pt-4">
            <div className="space-y-3">
              {analytics.linkPerformance.slice(0, 5).map((link, index) => (
                <div key={index} className="flex items-center gap-4 p-3 rounded-lg border bg-gradient-to-r from-white to-muted/20 hover:shadow-sm transition-shadow">
                  <div className="w-8 h-8 rounded-lg bg-gradient-to-br from-violet-100 to-indigo-100 flex items-center justify-center text-sm font-semibold text-violet-600">
                    {index + 1}
                  </div>
                  <div className="flex-1 min-w-0">
                    <p className="font-medium text-sm truncate">{link.title}</p>
                    <p className="text-xs text-muted-foreground">{link.clicks.toLocaleString()} clicks</p>
                  </div>
                  <div className="flex items-center gap-3">
                    <div className="w-32 bg-muted rounded-full h-2 overflow-hidden">
                      <div 
                        className="h-full rounded-full bg-gradient-to-r from-violet-500 to-cyan-500 transition-all duration-500" 
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
        <Card className="border shadow-sm overflow-hidden">
          <CardHeader className="bg-gradient-to-r from-amber-50 to-orange-50 border-b pb-3">
            <div className="flex items-center gap-2">
              <Globe className="w-4 h-4 text-amber-600" />
              <CardTitle className="text-base font-semibold">Top Countries</CardTitle>
            </div>
          </CardHeader>
          <CardContent className="pt-4">
            <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-3">
              {analytics.topCountries.map((country, index) => (
                <div 
                  key={index} 
                  className="flex items-center justify-between p-3 rounded-lg border bg-gradient-to-r from-white to-muted/20"
                >
                  <div className="flex items-center gap-3">
                    <div className="w-8 h-8 rounded-lg bg-gradient-to-br from-amber-100 to-orange-100 flex items-center justify-center">
                      <Globe className="h-4 w-4 text-amber-600" />
                    </div>
                    <div>
                      <span className="font-medium text-sm">{country.name}</span>
                      <p className="text-xs text-muted-foreground">{country.value.toLocaleString()} visitors</p>
                    </div>
                  </div>
                  <Badge variant="secondary" className="bg-amber-100 text-amber-700 hover:bg-amber-100">
                    {country.percentage.toFixed(1)}%
                  </Badge>
                </div>
              ))}
            </div>
          </CardContent>
        </Card>
      </motion.div>
    </div>
  );
}
