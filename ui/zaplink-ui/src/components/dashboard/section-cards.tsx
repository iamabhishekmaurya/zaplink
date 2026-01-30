import { IconTrendingDown, IconTrendingUp } from "@tabler/icons-react"

import { Badge } from "@/components/ui/badge"
import {
  Card,
  CardAction,
  CardDescription,
  CardFooter,
  CardHeader,
  CardTitle,
} from "@/components/ui/card"
import { DollarSign, Link2, MousePointerClick, ScanLine } from "lucide-react"
import { DashboardStats } from "@/hooks/useDashboardData"

interface SectionCardsProps {
  stats: DashboardStats;
}

export function SectionCards({ stats }: SectionCardsProps) {
  const cards = [
    {
      title: "Total Clicks",
      value: stats.totalClicks,
      change: "+12.5%", // Placeholder for now as we don't have historical data
      icon: <MousePointerClick className="size-5 text-primary" />,
      shortDetail: {
        heading: "All time clicks",
        icon: <IconTrendingUp className="size-4 text-green-500" />,
        info: "Across all links"
      }
    },
    {
      title: "Active Links",
      value: `${stats.activeLinks} / ${stats.totalLinks}`,
      change: stats.totalLinks > 0 ? `${Math.round((stats.activeLinks / stats.totalLinks) * 100)}%` : "0%",
      icon: <Link2 className="size-5 text-blue-500" />,
      shortDetail: {
        heading: "Active Rate",
        icon: null,
        info: "Percentage of active links"
      }
    },
    {
      title: "Total QRs",
      value: stats.totalQrs,
      change: "+5.2%", // Placeholder
      icon: <ScanLine className="size-5 text-purple-500" />,
      shortDetail: {
        heading: "Total Scans",
        icon: <IconTrendingUp className="size-4 text-green-500" />,
        info: `${stats.totalScans} total scans captured`
      }
    },
    {
      title: "Avg. Clicks/Link",
      value: stats.totalLinks > 0 ? Math.round(stats.totalClicks / stats.totalLinks) : 0,
      change: "+5.2%",
      icon: <DollarSign className="size-5 text-yellow-500" />,
      shortDetail: {
        heading: "Engagement",
        icon: <IconTrendingUp className="size-4 text-green-500" />,
        info: "Average clicks per link"
      }
    }
  ]
  return (
    <div className="grid grid-cols-1 gap-4 px-4 lg:px-6 md:grid-cols-2 xl:grid-cols-4">
      {
        cards.map((stat, index) => (
          <Card key={index} className="relative overflow-hidden border-border/50 bg-background/50 backdrop-blur-sm hover:border-primary/50 hover:shadow-[0_0_20px_-12px_var(--color-primary)] transition-all duration-300">
            <div className="absolute inset-x-0 top-0 h-px bg-gradient-to-r from-transparent via-primary/30 to-transparent opacity-0 transition-opacity group-hover:opacity-100" />
            <CardHeader className="pb-2">
              <CardDescription className="font-medium">{stat.title}</CardDescription>
              <CardTitle className="flex flex-row items-center gap-2 text-2xl font-bold tabular-nums">
                <div className="p-2 rounded-lg bg-primary/10">
                  {stat.icon}
                </div>
                {stat.value}
              </CardTitle>
            </CardHeader>
            <CardFooter className="flex-col items-start gap-1 pb-4 text-sm">
              <div className="flex items-center gap-1 font-medium text-foreground">
                {stat.shortDetail.icon}
                <span>{stat.shortDetail.heading}</span>
              </div>
              <div className="text-muted-foreground text-xs">{stat.shortDetail.info}</div>
            </CardFooter>
          </Card>
        ))
      }
    </div>
  )
}
