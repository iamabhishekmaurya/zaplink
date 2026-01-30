"use client"
import dynamic from "next/dynamic"
import { SectionCards } from "@/components/dashboard/section-cards"
import { Loader2, AlertCircle, WifiOff, RefreshCw } from "lucide-react"
import { useDashboardData } from "@/hooks/useDashboardData"
import { Button } from "@/components/ui/button"
import { Skeleton } from "@/components/ui/skeleton"
import { Card, CardContent, CardHeader } from "@/components/ui/card"

// Loading skeleton for charts
const ChartSkeleton = () => (
  <Card>
    <CardHeader>
      <Skeleton className="h-5 w-32" />
    </CardHeader>
    <CardContent>
      <Skeleton className="h-[300px] w-full" />
    </CardContent>
  </Card>
)

// Loading skeleton for table
const TableSkeleton = () => (
  <Card>
    <CardHeader>
      <Skeleton className="h-5 w-40" />
    </CardHeader>
    <CardContent className="space-y-3">
      {Array.from({ length: 5 }).map((_, i) => (
        <Skeleton key={i} className="h-12 w-full" />
      ))}
    </CardContent>
  </Card>
)

// Lazy load heavy chart components
const ChartAreaInteractive = dynamic(
  () => import("@/components/dashboard/chart-area-interactive").then(mod => ({ default: mod.ChartAreaInteractive })),
  { loading: () => <ChartSkeleton />, ssr: false }
)

const CreationHistoryChart = dynamic(
  () => import("@/components/dashboard/dashboard-charts").then(mod => ({ default: mod.CreationHistoryChart })),
  { loading: () => <ChartSkeleton />, ssr: false }
)

const PlatformDistributionChart = dynamic(
  () => import("@/components/dashboard/dashboard-charts").then(mod => ({ default: mod.PlatformDistributionChart })),
  { loading: () => <ChartSkeleton />, ssr: false }
)

const RecentActivityTable = dynamic(
  () => import("@/components/dashboard/recent-activity-table").then(mod => ({ default: mod.RecentActivityTable })),
  { loading: () => <TableSkeleton />, ssr: false }
)

export default function Page() {
  const stats = useDashboardData()

  if (stats.isLoading) {
    return (
      <div className="flex flex-1 items-center justify-center h-[50vh]">
        <div className="flex flex-col items-center gap-4">
          <Loader2 className="h-8 w-8 animate-spin text-primary" />
          <p className="text-xs text-muted-foreground">Loading dashboard...</p>
        </div>
      </div>
    )
  }

  if (stats.error) {
    return (
      <div className="flex flex-1 items-center justify-center h-[50vh]">
        <div className="flex flex-col items-center gap-4 max-w-md text-center px-4">
          {stats.isNetworkError ? (
            <div className="rounded-full bg-orange-100 dark:bg-orange-900/20 p-4">
              <WifiOff className="h-10 w-10 text-orange-600 dark:text-orange-400" />
            </div>
          ) : (
            <div className="rounded-full bg-destructive/10 p-4">
              <AlertCircle className="h-10 w-10 text-destructive" />
            </div>
          )}

          <div className="space-y-2">
            <h3 className="text-lg font-semibold">
              {stats.isNetworkError ? "Connection Failed" : "Something went wrong"}
            </h3>
            <p className="text-sm text-muted-foreground">
              {stats.error}
            </p>
            {stats.isNetworkError && (
              <p className="text-xs text-muted-foreground mt-2">
                Make sure your backend services are running and try again.
              </p>
            )}
          </div>

          <Button
            onClick={stats.refetch}
            variant="outline"
            className="gap-2 mt-2"
          >
            <RefreshCw className="h-4 w-4" />
            Try Again
          </Button>
        </div>
      </div>
    )
  }

  return (
    <div className="flex flex-1 flex-col">
      <div className="@container/main flex flex-1 flex-col gap-2">
        <div className="flex flex-col gap-4 py-4 md:gap-6 md:py-6">
          <SectionCards stats={stats} />

          <div className="grid grid-cols-1 lg:grid-cols-3 gap-6 px-4 lg:px-6">
            <div className="lg:col-span-3">
              <ChartAreaInteractive data={stats.visitorTrend} />
            </div>
            <div className="lg:col-span-2">
              <CreationHistoryChart data={stats.creationHistory} />
            </div>
            <div className="lg:col-span-1">
              <PlatformDistributionChart data={stats.platformDistribution} />
            </div>
          </div>

          <div className="px-4 lg:px-6">
            <RecentActivityTable items={stats.recentActivity} />
          </div>
        </div>
      </div>
    </div>
  )
}
