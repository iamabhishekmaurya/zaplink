"use client"

import {
  AudioWaveform,
  Command,
  GalleryVerticalEnd,
  LayoutDashboard,
  Link2,
  ScanLine,
  Settings2,
  PieChart,
  CreditCard,
  User,
  Bell,
  Wallet,
  Globe,
  Image as ImageIcon,
  CalendarDays,
  FileText,
  Plus,
  Users,
  Workflow,
  Target
} from "lucide-react"
import * as React from "react"

import { NavMain } from "@/components/(nav)/nav-main"
import { NavUser } from "@/components/(nav)/nav-user"
import { TeamSwitcher } from "./team-switcher"
import { Separator } from "@/components/ui/separator"
import {
  Sidebar,
  SidebarContent,
  SidebarFooter,
  SidebarHeader,
  SidebarRail,
} from "@/components/ui/sidebar"
import { NavGeneral } from "@/components/(nav)/nav-general"

// This is sample data.
const data = {
  user: {
    name: "shadcn",
    email: "m@example.com",
    avatar: "https://ui.shadcn.com/avatars/shadcn.jpg",
  },
  teams: [
    {
      name: "Acme Inc",
      logo: GalleryVerticalEnd,
      plan: "Enterprise",
    },
    {
      name: "Acme Corp.",
      logo: AudioWaveform,
      plan: "Startup",
    },
    {
      name: "Evil Corp.",
      logo: Command,
      plan: "Free",
    },
  ],
  general: [
    {
      title: "Dashboard",
      url: "/dashboard",
      icon: LayoutDashboard,
      isActive: true
    },
    {
      title: "Analytics",
      url: "/dashboard/analytics",
      icon: PieChart,
    }
  ],
  navMain: [
    {
      title: "Bio Pages",
      url: "/dashboard/bio-page",
      icon: FileText,
      isActive: true,
      items: [
        {
          title: "Manage Pages",
          url: "/dashboard/bio-page",
        },
        {
          title: "Create Page",
          url: "/dashboard/bio-page/create",
        },
      ],
    },
    {
      title: "Short Links",
      url: "/dashboard/link/short-link",
      icon: Link2,
      isActive: false,
      items: [
        {
          title: "Manage Short Links",
          url: "/dashboard/link/short-link",
        },
        {
          title: "Create Short Link",
          url: "/dashboard/link/create-short-link",
        },
        {
          title: "Dynamic QR Links",
          url: "/dashboard/link/dynamic-short-link",
        },
      ],
    },
    {
      title: "QR Codes",
      url: "#",
      icon: ScanLine,
      items: [
        {
          title: "My QR Codes",
          url: "/dashboard/qr",
        },
        {
          title: "QR Generator",
          url: "/dashboard/qr/qr-gen",
        },
      ],
    },
    {
      title: "Media",
      url: "/dashboard/media",
      icon: ImageIcon,
      items: [
        {
          title: "Library",
          url: "/dashboard/media",
        }
      ]
    },
    {
      title: "Team Management",
      url: "/dashboard/team-management",
      icon: Users,
      items: [
        {
          title: "Team Members",
          url: "/dashboard/team-management",
        },
        {
          title: "Workflow",
          url: "/dashboard/workflow-management",
        },
      ],
    },
    {
      title: "Workflow",
      url: "/dashboard/workflow-management",
      icon: Workflow,
      items: [
        {
          title: "My Posts",
          url: "/dashboard/workflow-management",
        },
        {
          title: "Pending Approval",
          url: "/dashboard/workflow-management?tab=pending",
        },
      ],
    },
    {
      title: "Influencer",
      url: "/dashboard/influencer-management",
      icon: Target,
      items: [
        {
          title: "My Campaigns",
          url: "/dashboard/influencer-management",
        },
      ],
    },
    {
      title: "Calendar",
      url: "/dashboard/calendar",
      icon: CalendarDays,
      items: [
        {
          title: "Scheduler",
          url: "/dashboard/calendar",
        }
      ]
    },
    {
      title: "Finance",
      url: "#",
      icon: Wallet,
      items: [
        {
          title: "Billing",
          url: "/dashboard/billing",
        }
      ]
    },
    {
      title: "Settings",
      url: "#",
      icon: Settings2,
      items: [
        {
          title: "General",
          url: "/dashboard/settings",
        },
        {
          title: "Account",
          url: "/dashboard/account",
        },
        {
          title: "Notifications",
          url: "/dashboard/notification",
        },
      ],
    },
  ],
}

export function AppSidebar({ ...props }: React.ComponentProps<typeof Sidebar>) {
  return (
    <Sidebar collapsible="icon" className="bg-background/60 backdrop-blur-xl border-r border-border/50 supports-[backdrop-filter]:bg-background/60" {...props}>
      <SidebarHeader>
        <TeamSwitcher teams={data.teams} />
      </SidebarHeader>
      <SidebarContent>
        <NavGeneral general={data.general} />
        <NavMain items={data.navMain} />
      </SidebarContent>
      <Separator
        orientation="horizontal"
        className="mb-2 data-[orientation=vertical]:h-4"
      />
      <SidebarFooter>
        <NavUser user={data.user} />
      </SidebarFooter>
      <SidebarRail />
    </Sidebar>
  )
}
