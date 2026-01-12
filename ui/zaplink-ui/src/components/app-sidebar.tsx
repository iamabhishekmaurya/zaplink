"use client"

import {
  AudioWaveform,
  BookOpen,
  Command,
  Frame,
  GalleryVerticalEnd,
  LayoutDashboard,
  Link2,
  Map,
  PieChart,
  ScanLine,
  Settings2
} from "lucide-react"
import * as React from "react"

import { NavMain } from "@/components/(nav)/nav-main"
import { NavProjects } from "@/components/(nav)/nav-projects"
import { NavUser } from "@/components/(nav)/nav-user"
import { TeamSwitcher } from "@/components/team-switcher"
import { Separator } from "@/components/ui/separator"
import {
  Sidebar,
  SidebarContent,
  SidebarFooter,
  SidebarHeader,
  SidebarRail,
} from "@/components/ui/sidebar"
import { NavGeneral } from "./(nav)/nav-general"
import { ToastDemo } from "./demo/ToastDemo"

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
    }
  ],
  navMain: [
    {
      title: "Link",
      url: "#",
      icon: Link2,
      isActive: true,
      items: [
        {
          title: "Short Link",
          url: "/dashboard/link/short-link",
        },
        {
          title: "Dynamic Short Link",
          url: "/dashboard/link/dynamic-short-link",
        },
        {
          title: "Analytic",
          url: "#",
        },
      ],
    },
    {
      title: "QR",
      url: "#",
      icon: ScanLine,
      items: [
        {
          title: "QR Generator",
          url: "/dashboard/qr/qr-gen",
        },
        {
          title: "Dynamic QR Generator",
          url: "/dashboard/qr/dynamic-qr-gen",
        },
        {
          title: "Analytic",
          url: "#",
        },
      ],
    },
    {
      title: "Documentation",
      url: "#",
      icon: BookOpen,
      items: [
        {
          title: "Introduction",
          url: "#",
        },
        {
          title: "Get Started",
          url: "#",
        },
        {
          title: "Tutorials",
          url: "#",
        },
        {
          title: "Changelog",
          url: "#",
        },
      ],
    },
    {
      title: "Settings",
      url: "#",
      icon: Settings2,
      items: [
        {
          title: "General",
          url: "#",
        },
        {
          title: "Team",
          url: "#",
        },
        {
          title: "Billing",
          url: "#",
        },
        {
          title: "Limits",
          url: "#",
        },
      ],
    },
  ],
  projects: [
    {
      name: "Design Engineering",
      url: "#",
      icon: Frame,
    },
    {
      name: "Sales & Marketing",
      url: "#",
      icon: PieChart,
    },
    {
      name: "Travel",
      url: "#",
      icon: Map,
    },
  ],
}

export function AppSidebar({ ...props }: React.ComponentProps<typeof Sidebar>) {
  return (
    <Sidebar collapsible="icon" {...props}>
      <SidebarHeader>
        <TeamSwitcher teams={data.teams} />
      </SidebarHeader>
      <SidebarContent>
        <NavGeneral general={data.general} />
        <NavMain items={data.navMain} />
        <NavProjects projects={data.projects} />
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
