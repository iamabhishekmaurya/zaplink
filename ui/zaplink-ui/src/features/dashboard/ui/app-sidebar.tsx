"use client"

import {
  AudioWaveform,
  CalendarDays,
  Command,
  FileText,
  GalleryVerticalEnd,
  Image as ImageIcon,
  LayoutDashboard,
  Link2,
  PieChart,
  ScanLine,
  Settings2,
  Target,
  Users,
  Wallet,
  Workflow
} from 'lucide-react'
import * as React from 'react'

import { NavGeneral } from '@/components/(nav)/nav-general'
import { NavMain } from '@/components/(nav)/nav-main'
import { NavUser } from '@/components/(nav)/nav-user'
import { Separator } from '@/components/ui/separator'
import {
  Sidebar,
  SidebarContent,
  SidebarFooter,
  SidebarHeader,
  SidebarRail,
} from '@/components/ui/sidebar'
import { TeamSwitcher } from '@/features/dashboard/ui/team-switcher'

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
      title: "Content",
      url: "/dashboard/content",
      icon: ImageIcon,
      items: [
        {
          title: "Library",
          url: "/dashboard/content",
        },
        {
          title: "Favorites",
          url: "/dashboard/content/favorites",
        },
        {
          title: "Trash",
          url: "/dashboard/content/trash",
        }
      ]
    },
    // {
    //   title: "Team Management",
    //   url: "/dashboard/team-management",
    //   icon: Users,
    //   items: [
    //     {
    //       title: "Team Members",
    //       url: "/dashboard/team-management",
    //     },
    //     {
    //       title: "Workflow",
    //       url: "/dashboard/workflow-management",
    //     },
    //   ],
    // },
    // {
    //   title: "Workflow",
    //   url: "/dashboard/workflow-management",
    //   icon: Workflow,
    //   items: [
    //     {
    //       title: "My Posts",
    //       url: "/dashboard/workflow-management",
    //     },
    //     {
    //       title: "Pending Approval",
    //       url: "/dashboard/workflow-management?tab=pending",
    //     },
    //   ],
    // },
    // {
    //   title: "Influencer",
    //   url: "/dashboard/influencer-management",
    //   icon: Target,
    //   items: [
    //     {
    //       title: "My Campaigns",
    //       url: "/dashboard/influencer-management",
    //     },
    //   ],
    // },
    // {
    //   title: "Calendar",
    //   url: "/dashboard/calendar",
    //   icon: CalendarDays,
    //   items: [
    //     {
    //       title: "Scheduler",
    //       url: "/dashboard/calendar",
    //     }
    //   ]
    // },
    // {
    //   title: "Finance",
    //   url: "#",
    //   icon: Wallet,
    //   items: [
    //     {
    //       title: "Billing",
    //       url: "/dashboard/billing",
    //     }
    //   ]
    // },
    // {
    //   title: "Settings",
    //   url: "/dashboard/settings",
    //   icon: Settings2,
    //   items: [],
    // },
  ],
}

// Add imports
import { useSelector } from 'react-redux';
import { RootState } from '@/store';

export function AppSidebar({ ...props }: React.ComponentProps<typeof Sidebar>) {
  const user = useSelector((state: RootState) => state.auth.user);

  // Fallback to sample data if user is not loaded
  const sidebarUser = user ? {
    name: user.firstName && user.lastName ? `${user.firstName} ${user.lastName}` : user.username,
    email: user.email,
    avatar: "https://github.com/shadcn.png" // Fallback avatar as UserInfo doesn't have it yet
  } : data.user;

  return (
    <Sidebar collapsible="icon" className="bg-background/60 font-bold backdrop-blur-xl border-r border-border/50 supports-[backdrop-filter]:bg-background/60" {...props}>
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
        <NavUser user={sidebarUser} />
      </SidebarFooter>
      <SidebarRail />
    </Sidebar>
  )
}
