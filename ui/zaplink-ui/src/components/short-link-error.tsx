'use client'

import { Button } from '@/components/ui/button'
import { Card } from '@/components/ui/card'

interface ShortLinkErrorProps {
    error: string
    onRetry: () => void
}

export const ShortLinkError = ({ error, onRetry }: ShortLinkErrorProps) => {
    return (
        <Card className="p-8 text-center">
            <div className="flex flex-col items-center gap-4">
                <p className="text-destructive">Error: {error}</p>
                <Button onClick={onRetry}>
                    Try Again
                </Button>
            </div>
        </Card>
    )
}
