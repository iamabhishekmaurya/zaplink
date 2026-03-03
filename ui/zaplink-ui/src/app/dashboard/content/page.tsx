import { Metadata } from "next"
import ContentManager from "@/features/content-management/ui/content-manager"

export const metadata: Metadata = {
    title: "Content Management | Zaplink",
    description: "Manage your folders and media files.",
}

export default function ContentPage() {
    return (
        <div className="flex flex-col h-[calc(100vh-4rem)] p-4 sm:p-8 pt-4 sm:pt-6 gap-3 sm:gap-4">
            <div className="flex items-center justify-between shrink-0">
                <h2 className="text-2xl sm:text-3xl font-bold tracking-tight">Content Library</h2>
            </div>
            <div className="flex-1 min-h-0 flex flex-col">
                <ContentManager />
            </div>
        </div>
    )
}
