"use client"

import { cn } from "@/lib/utils"
import {
    AudioWaveform,
    BadgeCheck,
    Bell,
    Command,
    CreditCard,
    FileText,
    GalleryVerticalEnd,
    Image as ImageIcon,
    Laptop,
    LayoutDashboard,
    Link2,
    LogOut,
    Moon,
    Plus,
    ScanLine,
    Sparkles,
    Sun,
    Users,
} from "lucide-react"
import Link from "next/link"
import { usePathname, useRouter } from "next/navigation"
import { useState } from "react"
import { useSelector, useDispatch } from "react-redux"
import { RootState, AppDispatch } from "@/store"
import { logout } from "@/store/slices/authSlice"
import { useTheme } from "next-themes"
import { Avatar, AvatarFallback, AvatarImage } from "@/components/ui/avatar"
import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuGroup,
    DropdownMenuItem,
    DropdownMenuLabel,
    DropdownMenuSeparator,
    DropdownMenuSub,
    DropdownMenuSubContent,
    DropdownMenuSubTrigger,
    DropdownMenuTrigger,
    DropdownMenuPortal,
} from "@/components/ui/dropdown-menu"

const dockItems = [
    { title: "Home", url: "/dashboard", icon: LayoutDashboard },
    { title: "Bio", url: "/dashboard/bio-page", icon: FileText },
    { title: "Links", url: "/dashboard/link/short-link", icon: Link2 },
    { title: "QR", url: "/dashboard/qr", icon: ScanLine },
    { title: "Content", url: "/dashboard/content", icon: ImageIcon },
]

const teams = [
    { name: "Acme Inc", logo: GalleryVerticalEnd, plan: "Enterprise" },
    { name: "Acme Corp.", logo: AudioWaveform, plan: "Startup" },
    { name: "Evil Corp.", logo: Command, plan: "Free" },
]

export function MobileBottomDock() {
    const pathname = usePathname()
    const router = useRouter()
    const dispatch = useDispatch<AppDispatch>()
    const { setTheme } = useTheme()
    const user = useSelector((state: RootState) => state.auth.user)
    const [activeTeam, setActiveTeam] = useState(teams[0])

    const displayName = user?.firstName && user?.lastName
        ? `${user.firstName} ${user.lastName}`
        : user?.username ?? "User"
    const displayEmail = user?.email ?? "user@example.com"
    const initials = displayName.slice(0, 2).toUpperCase()

    const handleLogout = () => {
        dispatch(logout())
        router.replace("/login")
    }

    return (
        <div className="md:hidden fixed bottom-0 left-0 right-0 z-50">
            {/* Frosted glass bar */}
            <div className="bg-background/85 backdrop-blur-2xl border-t border-border/40">
                <nav className="flex items-center justify-around h-14 px-2">
                    {dockItems.map((item) => {
                        const isActive =
                            item.url === "/dashboard"
                                ? pathname === "/dashboard"
                                : pathname.startsWith(item.url)
                        const Icon = item.icon

                        return (
                            <Link
                                key={item.url}
                                href={item.url}
                                className="relative flex flex-col items-center justify-center flex-1 h-full gap-0.5 touch-manipulation active:scale-90 transition-transform"
                            >
                                <Icon
                                    className={cn(
                                        "w-[20px] h-[20px] transition-colors",
                                        isActive ? "text-primary" : "text-muted-foreground"
                                    )}
                                    strokeWidth={isActive ? 2.4 : 1.8}
                                />
                                <span
                                    className={cn(
                                        "text-[9px] leading-none",
                                        isActive ? "text-primary font-semibold" : "text-muted-foreground/70 font-medium"
                                    )}
                                >
                                    {item.title}
                                </span>
                            </Link>
                        )
                    })}

                    {/* User Avatar with Dropdown */}
                    <DropdownMenu>
                        <DropdownMenuTrigger asChild>
                            <button className="relative flex flex-col items-center justify-center flex-1 h-full gap-0.5 touch-manipulation outline-none active:scale-90 transition-transform">
                                <Avatar className="w-6 h-6 ring-1.5 ring-border/40">
                                    <AvatarImage src="https://github.com/shadcn.png" alt={displayName} />
                                    <AvatarFallback className="text-[8px] font-bold bg-muted text-muted-foreground">{initials}</AvatarFallback>
                                </Avatar>
                                <span className="text-[9px] leading-none text-muted-foreground/70 font-medium">
                                    Me
                                </span>
                            </button>
                        </DropdownMenuTrigger>
                        <DropdownMenuContent
                            className="w-56 rounded-xl mb-1"
                            side="top"
                            align="end"
                            sideOffset={8}
                        >
                            {/* User Identity */}
                            <DropdownMenuLabel className="p-0 font-normal">
                                <div className="flex items-center gap-2.5 px-3 py-2.5 text-left text-sm">
                                    <Avatar className="h-8 w-8 rounded-lg">
                                        <AvatarImage src="https://github.com/shadcn.png" alt={displayName} />
                                        <AvatarFallback className="rounded-lg text-[10px]">{initials}</AvatarFallback>
                                    </Avatar>
                                    <div className="grid flex-1 text-left leading-tight min-w-0">
                                        <span className="truncate font-semibold text-sm">{displayName}</span>
                                        <span className="truncate text-[11px] text-muted-foreground">{displayEmail}</span>
                                    </div>
                                </div>
                            </DropdownMenuLabel>
                            <DropdownMenuSeparator />

                            {/* Switch Team */}
                            <DropdownMenuGroup>
                                <DropdownMenuSub>
                                    <DropdownMenuSubTrigger className="text-xs">
                                        <Users className="mr-2 h-3.5 w-3.5" />
                                        <span className="flex-1">Switch Team</span>
                                    </DropdownMenuSubTrigger>
                                    <DropdownMenuPortal>
                                        <DropdownMenuSubContent className="rounded-xl w-48">
                                            <DropdownMenuLabel className="text-muted-foreground text-[10px]">
                                                Teams
                                            </DropdownMenuLabel>
                                            {teams.map((team) => {
                                                const Logo = team.logo
                                                return (
                                                    <DropdownMenuItem
                                                        key={team.name}
                                                        onClick={() => setActiveTeam(team)}
                                                        className={cn("gap-2 py-1.5", activeTeam.name === team.name && "bg-accent")}
                                                    >
                                                        <div className="flex size-5 items-center justify-center rounded border bg-background">
                                                            <Logo className="size-3 shrink-0" />
                                                        </div>
                                                        <div className="flex flex-col min-w-0">
                                                            <span className="text-xs font-medium truncate">{team.name}</span>
                                                            <span className="text-[9px] text-muted-foreground">{team.plan}</span>
                                                        </div>
                                                    </DropdownMenuItem>
                                                )
                                            })}
                                            <DropdownMenuSeparator />
                                            <DropdownMenuItem className="gap-2 py-1.5">
                                                <div className="flex size-5 items-center justify-center rounded border">
                                                    <Plus className="size-3" />
                                                </div>
                                                <span className="text-muted-foreground font-medium text-xs">Add team</span>
                                            </DropdownMenuItem>
                                        </DropdownMenuSubContent>
                                    </DropdownMenuPortal>
                                </DropdownMenuSub>
                            </DropdownMenuGroup>
                            <DropdownMenuSeparator />

                            {/* Upgrade */}
                            <DropdownMenuItem className="text-xs">
                                <Sparkles className="mr-2 h-3.5 w-3.5" />
                                Upgrade to Pro
                            </DropdownMenuItem>
                            <DropdownMenuSeparator />

                            {/* Account Options */}
                            <DropdownMenuGroup>
                                <DropdownMenuItem className="text-xs" onClick={() => router.push('/dashboard/settings/account')}>
                                    <BadgeCheck className="mr-2 h-3.5 w-3.5" />
                                    Account
                                </DropdownMenuItem>
                                <DropdownMenuItem className="text-xs" onClick={() => router.push('/dashboard/settings/billing')}>
                                    <CreditCard className="mr-2 h-3.5 w-3.5" />
                                    Billing
                                </DropdownMenuItem>
                                <DropdownMenuItem className="text-xs" onClick={() => router.push('/dashboard/settings/notifications')}>
                                    <Bell className="mr-2 h-3.5 w-3.5" />
                                    Notifications
                                </DropdownMenuItem>
                            </DropdownMenuGroup>
                            <DropdownMenuSeparator />

                            {/* Theme */}
                            <DropdownMenuGroup>
                                <DropdownMenuSub>
                                    <DropdownMenuSubTrigger className="text-xs">
                                        <Sun className="mr-2 h-3.5 w-3.5 rotate-0 scale-100 transition-all dark:-rotate-90 dark:scale-0" />
                                        <Moon className="absolute ml-0 h-3.5 w-3.5 rotate-90 scale-0 transition-all dark:rotate-0 dark:scale-100" />
                                        <span className="ml-5">Theme</span>
                                    </DropdownMenuSubTrigger>
                                    <DropdownMenuPortal>
                                        <DropdownMenuSubContent className="rounded-xl">
                                            <DropdownMenuItem className="text-xs" onClick={() => setTheme("light")}>
                                                <Sun className="mr-2 h-3.5 w-3.5" />
                                                Light
                                            </DropdownMenuItem>
                                            <DropdownMenuItem className="text-xs" onClick={() => setTheme("dark")}>
                                                <Moon className="mr-2 h-3.5 w-3.5" />
                                                Dark
                                            </DropdownMenuItem>
                                            <DropdownMenuItem className="text-xs" onClick={() => setTheme("system")}>
                                                <Laptop className="mr-2 h-3.5 w-3.5" />
                                                System
                                            </DropdownMenuItem>
                                        </DropdownMenuSubContent>
                                    </DropdownMenuPortal>
                                </DropdownMenuSub>
                            </DropdownMenuGroup>
                            <DropdownMenuSeparator />

                            {/* Logout */}
                            <DropdownMenuItem onClick={handleLogout} className="text-xs text-red-500 focus:text-red-500">
                                <LogOut className="mr-2 h-3.5 w-3.5" />
                                Log out
                            </DropdownMenuItem>
                        </DropdownMenuContent>
                    </DropdownMenu>
                </nav>

                {/* Safe area spacer for notched devices */}
                <div className="h-[env(safe-area-inset-bottom,0px)]" />
            </div>
        </div>
    )
}
