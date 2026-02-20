import { Separator } from "@/components/ui/separator"
import { BillingForm } from "@/features/settings/components/billing-form"

export default function SettingsBillingPage() {
    return (
        <div className="space-y-6">
            <div>
                <h3 className="text-lg font-medium">Billing</h3>
                <p className="text-sm text-muted-foreground">
                    Manage your billing information and subscription plan.
                </p>
            </div>
            <Separator />
            <BillingForm />
        </div>
    )
}
