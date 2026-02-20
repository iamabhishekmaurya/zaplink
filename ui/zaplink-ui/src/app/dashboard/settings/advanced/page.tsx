import { Separator } from "@/components/ui/separator"
import { AdvancedForm } from "@/features/settings/components/advanced-form"

export default function SettingsAdvancedPage() {
    return (
        <div className="space-y-6">
            <div>
                <h3 className="text-lg font-medium">Advanced</h3>
                <p className="text-sm text-muted-foreground">
                    Advanced settings and developer options.
                </p>
            </div>
            <Separator />
            <AdvancedForm />
        </div>
    )
}
