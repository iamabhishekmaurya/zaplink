import { SettingsLayout } from "@/features/settings/components/settings-layout"

export default function Layout({ children }: { children: React.ReactNode }) {
    return <SettingsLayout>{children}</SettingsLayout>
}
