import MediaManager from "@/components/media/MediaManager";
import { Metadata } from "next";

export const metadata: Metadata = {
    title: "Media Manager | Zaplink",
    description: "Manage your digital assets",
};

export default function MediaPage() {
    return (
        <div className="h-full flex flex-col">
            <div className="flex items-center justify-between p-6 pb-0">
                <h1 className="text-2xl font-bold tracking-tight">Media Library</h1>
            </div>
            <MediaManager />
        </div>
    );
}
