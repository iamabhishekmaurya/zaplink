"use client"

import {
  AudioWaveform,
  Command,
  GalleryVerticalEnd,
  LayoutDashboard,
  Link2,
  ScanLine,
  Settings2
} from "lucide-react"
import * as React from "react"

import { NavMain } from "@/components/(nav)/nav-main"
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
      title: "Links",
      url: "#",
      icon: Link2,
      isActive: true,
      items: [
        {
          title: "My Links",
          url: "/dashboard/link/short-link",
        },
        {
          title: "Create Link",
          url: "/dashboard/link/create-short-link",
        },
        {
          title: "Dynamic Links",
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
          title: "QR Generator",
          url: "/dashboard/qr/qr-gen",
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
          url: "/dashboard/settings",
        },
      ],
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
