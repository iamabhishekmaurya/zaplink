"use client"

import { Separator } from "@/components/ui/separator"
import { SidebarTrigger } from "@/components/ui/sidebar"
import { Link, Moon, QrCodeIcon, Sun } from "lucide-react"
import { useTheme } from "next-themes"
import { usePathname } from "next/navigation"
import * as React from "react"
import { Badge } from "./ui/badge"
import { Breadcrumb, BreadcrumbItem, BreadcrumbLink, BreadcrumbList, BreadcrumbPage, BreadcrumbSeparator } from "./ui/breadcrumb"

export function SiteHeader() {
  const { theme, setTheme } = useTheme()
  const pathname = usePathname()

  // Generate breadcrumb items from pathname
  const generateBreadcrumbs = () => {
    const pathSegments = pathname.split('/').filter(segment => segment)
    const breadcrumbs = []

    // Add Dashboard as the first item
    breadcrumbs.push({
      label: 'Dashboard',
      href: '/dashboard'
    })

    // Add other segments
    pathSegments.forEach((segment, index) => {
      if (segment !== 'dashboard') {
        const href = '/' + pathSegments.slice(0, index + 1).join('/')
        const label = segment.charAt(0).toUpperCase() + segment.slice(1).replace(/-/g, ' ')
        breadcrumbs.push({
          label,
          href
        })
      }
    })

    return breadcrumbs
  }

  const breadcrumbs = generateBreadcrumbs()

  return (
    <header className="flex h-(--header-height) shrink-0 items-center gap-2 border-b transition-[width,height] ease-linear group-has-data-[collapsible=icon]/sidebar-wrapper:h-(--header-height)">
      <div className="flex w-full items-center gap-1 px-4 lg:gap-2 lg:px-6">
        <SidebarTrigger className="-ml-1" />
        <Separator
          orientation="vertical"
          className="mx-2 data-[orientation=vertical]:h-4"
        />
        <Breadcrumb>
          <BreadcrumbList>
            {breadcrumbs.map((breadcrumb, index) => (
              <React.Fragment key={breadcrumb.href}>
                <BreadcrumbItem className={index === breadcrumbs.length - 1 ? '' : 'hidden md:block'}>
                  {index === breadcrumbs.length - 1 ? (
                    <BreadcrumbPage>{breadcrumb.label}</BreadcrumbPage>
                  ) : (
                    <BreadcrumbLink href={breadcrumb.href}>
                      {breadcrumb.label}
                    </BreadcrumbLink>
                  )}
                </BreadcrumbItem>
                {index < breadcrumbs.length - 1 && (
                  <BreadcrumbSeparator className="hidden md:block" />
                )}
              </React.Fragment>
            ))}
          </BreadcrumbList>
        </Breadcrumb>
        <div className="ml-auto flex items-center gap-2">
          <Badge variant="outline" className="flex gap-2 cursor-pointer">
            <div onClick={() => setTheme("dark")}>
              <Link width={16} height={16} />
            </div>
            <div onClick={() => setTheme("light")}>
              <QrCodeIcon width={16} height={16} />
            </div>
          </Badge>
          <Badge variant="outline" className="flex gap-2 cursor-pointer">
            <div onClick={() => setTheme("dark")}>
              <Moon width={16} height={16} />
            </div>
            <div onClick={() => setTheme("light")}>
              <Sun width={16} height={16} />
            </div>
          </Badge>
        </div>
      </div>
    </header>
  )
}
