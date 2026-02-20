"use client"

import { Separator } from "@/components/ui/separator"
import { SettingsSidebar, sidebarNavItems } from "./settings-sidebar"

interface SettingsLayoutProps {
    children: React.ReactNode
}

export function SettingsLayout({ children }: SettingsLayoutProps) {
    return (
        <div className="container mx-auto px-4 py-6 max-w-6xl space-y-6">
            <div className="space-y-0.5">
                <h2 className="text-3xl font-bold tracking-tight">Settings</h2>
                <p className="text-muted-foreground">
                    Manage your account settings and set e-mail preferences.
                </p>
            </div>
            <Separator className="my-6" />
            <div className="flex flex-col gap-8 lg:flex-row lg:gap-12">
                <aside className="lg:w-1/5 lg:border-solid lg:border-gray-200 dark:lg:border-gray-700">
                    <SettingsSidebar items={sidebarNavItems} />
                </aside>
                <div className="flex-1 lg:max-w-3xl">{children}</div>
            </div>
        </div>
    )
}
