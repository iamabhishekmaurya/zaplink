'use client'

import { Card } from '@/components/ui/card'
import { Skeleton } from '@/components/ui/skeleton'

export const ShortLinkSkeleton = () => {
    return (
        <Card className="p-4">
            <div className="flex flex-col gap-3">
                <div className="flex items-start justify-between">
                    <div className="flex items-start gap-3 flex-1">
                        <Skeleton className="h-4 w-4 mt-1" />
                        <div className="flex items-center gap-2">
                            <Skeleton className="h-8 w-8 rounded-lg" />
                        </div>
                    </div>
                    <div className="flex items-center gap-1">
                        {[...Array(4)].map((_, j) => (
                            <Skeleton key={j} className="h-8 w-8 rounded" />
                        ))}
                    </div>
                </div>
                <div className="ml-11">
                    <Skeleton className="h-4 w-48 mb-2" />
                    <Skeleton className="h-3 w-64 mb-2" />
                    <Skeleton className="h-3 w-32" />
                </div>
            </div>
        </Card>
    )
}

export const ShortLinkSkeletonList = ({ count = 3 }: { count?: number }) => {
    return (
        <div className="grid gap-4 grid-cols-1">
            {[...Array(count)].map((_, i) => (
                <ShortLinkSkeleton key={i} />
            ))}
        </div>
    )
}
