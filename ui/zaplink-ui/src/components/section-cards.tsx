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
      icon: <MousePointerClick />,
      shortDetail: {
        heading: "All time clicks",
        icon: <IconTrendingUp className="size-4 text-green-600" />,
        info: "Across all links"
      }
    },
    {
      title: "Active Links",
      value: `${stats.activeLinks} / ${stats.totalLinks}`,
      change: stats.totalLinks > 0 ? `${Math.round((stats.activeLinks / stats.totalLinks) * 100)}%` : "0%",
      icon: <Link2 />,
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
      icon: <ScanLine />,
      shortDetail: {
        heading: "Total Scans",
        icon: <IconTrendingUp className="size-4 text-green-600" />,
        info: `${stats.totalScans} total scans captured`
      }
    },
    {
      title: "Avg. Clicks/Link",
      value: stats.totalLinks > 0 ? Math.round(stats.totalClicks / stats.totalLinks) : 0,
      change: "+5.2%",
      icon: <DollarSign />,
      shortDetail: {
        heading: "Engagement",
        icon: <IconTrendingUp className="size-4 text-green-600" />,
        info: "Average clicks per link"
      }
    }
  ]
  return (
    <div className="*:data-[slot=card]:from-primary/5 *:data-[slot=card]:to-card dark:*:data-[slot=card]:bg-card grid grid-cols-1 gap-4 px-4 *:data-[slot=card]:bg-gradient-to-t *:data-[slot=card]:shadow-xs lg:px-6 @xl/main:grid-cols-2 @5xl/main:grid-cols-4">
      {
        cards.map((stat, index) => (
          <Card key={index} className="@container/card">
            <CardHeader>
              <CardDescription>{stat.title}</CardDescription>
              <CardTitle className="flex flex-row items-center text-2xl font-semibold tabular-nums @[250px]/card:text-3xl">
                {stat.icon}{stat.value}
              </CardTitle>
              {/* <CardAction>
                <Badge variant="outline" className="text-green-600">
                  {stat.shortDetail.icon}
                  {stat.change}
                </Badge>
              </CardAction> */}
            </CardHeader>
            <CardFooter className="flex-col items-start gap-1.5 text-sm">
              <div className="line-clamp-1 flex gap-2 font-medium">
                {stat.shortDetail.heading}{stat.shortDetail.icon}
              </div>
              <div className="text-muted-foreground">{stat.shortDetail.info}</div>
            </CardFooter>
          </Card>
        ))
      }
    </div>
  )
}
