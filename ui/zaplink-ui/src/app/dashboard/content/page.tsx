import { Metadata } from "next"
import ContentManager from "@/features/content-management/ui/content-manager"

export const metadata: Metadata = {
    title: "Content Management | Zaplink",
    description: "Manage your folders and media files.",
}

export default function ContentPage() {
    return (
        <div className="flex-1 space-y-4 p-8 pt-6">
            <div className="flex items-center justify-between space-y-2">
                <h2 className="text-3xl font-bold tracking-tight">Content Library</h2>
            </div>
            <ContentManager />
        </div>
    )
}
