import { Separator } from "@/components/ui/separator"
import { SupportForm } from "@/features/settings/components/support-form"

export default function SettingsSupportPage() {
    return (
        <div className="space-y-6">
            <div>
                <h3 className="text-lg font-medium">Support</h3>
                <p className="text-sm text-muted-foreground">
                    Contact our support team or send feedback.
                </p>
            </div>
            <Separator />
            <SupportForm />
        </div>
    )
}
