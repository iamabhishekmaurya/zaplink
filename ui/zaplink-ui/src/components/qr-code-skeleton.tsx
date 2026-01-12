import { Skeleton } from "@/components/ui/skeleton"
import { Card } from "@/components/ui/card"

export const QrCodeSkeleton = () => {
    return (
        <Card className="p-4">
            <div className="flex gap-4 items-start">
                <Skeleton className="hidden md:block w-24 h-24 rounded-lg flex-shrink-0" />
                <div className="flex-1 space-y-3 w-full min-w-0">
                    <div className="flex justify-between">
                        <div className="space-y-2 w-1/2">
                            <Skeleton className="h-6 w-3/4" />
                            <Skeleton className="h-4 w-1/2" />
                        </div>
                        <div className="flex gap-2">
                            <Skeleton className="h-8 w-8 rounded-md" />
                            <Skeleton className="h-8 w-8 rounded-md" />
                        </div>
                    </div>
                    <div className="flex gap-2">
                        <Skeleton className="h-5 w-16" />
                        <Skeleton className="h-5 w-24" />
                        <Skeleton className="h-5 w-20" />
                    </div>
                </div>
            </div>
        </Card>
    )
}

export const QrCodeSkeletonList = () => {
    return (
        <div className="grid gap-4">
            {[1, 2, 3].map((i) => (
                <QrCodeSkeleton key={i} />
            ))}
        </div>
    )
}
