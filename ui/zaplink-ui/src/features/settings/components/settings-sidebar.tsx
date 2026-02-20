"use client"

import Link from "next/link"
import { usePathname } from "next/navigation"
import { cn } from "@/lib/utils"
import { buttonVariants } from "@/components/ui/button"
import {
    User,
    Lock,
    CreditCard,
    Bell,
    Users,
    Shield,
    Sliders,
    HelpCircle
} from "lucide-react"

interface SidebarNavProps extends React.HTMLAttributes<HTMLElement> {
    items: {
        href: string
        title: string
        icon: React.ReactNode
    }[]
}

export function SettingsSidebar({ className, items, ...props }: SidebarNavProps) {
    const pathname = usePathname()

    return (
        <nav
            className={cn(
                "flex space-x-2 lg:flex-col lg:space-x-0 lg:space-y-1",
                className
            )}
            {...props}
        >
            {items.map((item) => (
                <Link
                    key={item.href}
                    href={item.href}
                    className={cn(
                        buttonVariants({ variant: "ghost" }),
                        pathname === item.href
                            ? "bg-muted hover:bg-muted"
                            : "hover:bg-muted/50",
                        "justify-start"
                    )}
                >
                    <span className="mr-2">{item.icon}</span>
                    {item.title}
                </Link>
            ))}
        </nav>
    )
}

export const sidebarNavItems = [
    {
        title: "Account",
        href: "/dashboard/settings/account",
        icon: <User className="h-4 w-4" />,
    },
    {
        title: "Privacy",
        href: "/dashboard/settings/privacy",
        icon: <Lock className="h-4 w-4" />,
    },
    {
        title: "Billing",
        href: "/dashboard/settings/billing",
        icon: <CreditCard className="h-4 w-4" />,
    },
    {
        title: "Notifications",
        href: "/dashboard/settings/notifications",
        icon: <Bell className="h-4 w-4" />,
    },
    {
        title: "Team",
        href: "/dashboard/settings/team",
        icon: <Users className="h-4 w-4" />,
    },
    {
        title: "Security",
        href: "/dashboard/settings/security",
        icon: <Shield className="h-4 w-4" />,
    },
    {
        title: "Advanced",
        href: "/dashboard/settings/advanced",
        icon: <Sliders className="h-4 w-4" />,
    },
    {
        title: "Support",
        href: "/dashboard/settings/support",
        icon: <HelpCircle className="h-4 w-4" />,
    },
]
