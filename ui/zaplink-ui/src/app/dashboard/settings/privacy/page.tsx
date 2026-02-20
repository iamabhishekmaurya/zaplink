import { Separator } from "@/components/ui/separator"
import { PrivacyForm } from "@/features/settings/components/privacy-form"

export default function SettingsPrivacyPage() {
    return (
        <div className="space-y-6">
            <div>
                <h3 className="text-lg font-medium">Privacy</h3>
                <p className="text-sm text-muted-foreground">
                    Manage your privacy settings and data preferences.
                </p>
            </div>
            <Separator />
            <PrivacyForm />
        </div>
    )
}
