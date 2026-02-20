import { Separator } from "@/components/ui/separator"
import { TeamForm } from "@/features/settings/components/team-form"

export default function SettingsTeamPage() {
    return (
        <div className="space-y-6">
            <div>
                <h3 className="text-lg font-medium">Team</h3>
                <p className="text-sm text-muted-foreground">
                    Manage your team members and their permissions.
                </p>
            </div>
            <Separator />
            <TeamForm />
        </div>
    )
}
