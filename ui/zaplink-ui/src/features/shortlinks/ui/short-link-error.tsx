'use client'

import { Button } from '@/components/ui/button'
import { Card } from '@/components/ui/card'
import { AlertCircle, WifiOff, RefreshCw } from 'lucide-react'

interface ShortLinkErrorProps {
    error: string
    onRetry: () => void
}

export const ShortLinkError = ({ error, onRetry }: ShortLinkErrorProps) => {
    // Detect if it's a network error
    const isNetworkError = error.toLowerCase().includes('network') ||
        error.toLowerCase().includes('connect') ||
        error.toLowerCase().includes('server');

    return (
        <Card className="p-8">
            <div className="flex flex-col items-center gap-4 text-center max-w-md mx-auto">
                {isNetworkError ? (
                    <div className="rounded-full bg-orange-100 dark:bg-orange-900/20 p-4">
                        <WifiOff className="h-10 w-10 text-orange-600 dark:text-orange-400" />
                    </div>
                ) : (
                    <div className="rounded-full bg-destructive/10 p-4">
                        <AlertCircle className="h-10 w-10 text-destructive" />
                    </div>
                )}

                <div className="space-y-2">
                    <h3 className="text-lg font-semibold">
                        {isNetworkError ? "Connection Failed" : "Something went wrong"}
                    </h3>
                    <p className="text-sm text-muted-foreground">
                        {error}
                    </p>
                    {isNetworkError && (
                        <p className="text-xs text-muted-foreground mt-2">
                            Make sure your backend services are running and try again.
                        </p>
                    )}
                </div>

                <Button onClick={onRetry} variant="outline" className="gap-2 mt-2">
                    <RefreshCw className="h-4 w-4" />
                    Try Again
                </Button>
            </div>
        </Card>
    )
}
