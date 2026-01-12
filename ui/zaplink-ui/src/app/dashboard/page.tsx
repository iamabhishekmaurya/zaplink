"use client"
import { SectionCards } from "@/components/section-cards"
import { Loader2 } from "lucide-react"
import { useDashboardData } from "@/hooks/useDashboardData"
import { CreationHistoryChart, PlatformDistributionChart } from "@/components/dashboard-charts"
import { RecentActivityTable } from "@/components/recent-activity-table"
import { ChartAreaInteractive } from "@/components/chart-area-interactive"

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
        <p className="text-destructive font-medium">{stats.error}</p>
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
