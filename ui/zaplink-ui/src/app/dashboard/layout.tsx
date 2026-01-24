"use client"

import { AppSidebar } from "@/components/app-sidebar"
import { SiteHeader } from "@/components/site-header"
import {
  SidebarInset,
  SidebarProvider,
} from "@/components/ui/sidebar"

export default function DashboardLayout({
  children,
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <SidebarProvider
      style={
        {
          "--sidebar-width": "calc(var(--spacing) * 62)",
          "--header-height": "calc(var(--spacing) * 12)",
        } as React.CSSProperties
      }
    >
      <AppSidebar variant="inset" />
      <SidebarInset className="relative overflow-hidden bg-background">
        {/* Background Gradients */}
        <div className="pointer-events-none absolute inset-0 flex justify-center">
          <div className="h-[500px] w-[500px] bg-primary/5 rounded-full blur-3xl opacity-50 absolute -top-40 right-0" />
          <div className="h-[500px] w-[500px] bg-[#ff8904]/5 rounded-full blur-3xl opacity-50 absolute top-40 -left-20" />
        </div>
        <SiteHeader />
        <div className="relative z-10">
          {children}
        </div>
      </SidebarInset>
    </SidebarProvider>
  );
}