'use client'

import { Button } from '@/components/ui/button'
import { Card } from '@/components/ui/card'

interface ShortLinkEmptyProps {
    hasActiveFilters: boolean
    onClearFilters: () => void
    onCreateFirstLink: () => void
}

export const ShortLinkEmpty = ({
    hasActiveFilters,
    onClearFilters,
    onCreateFirstLink
}: ShortLinkEmptyProps) => {
    return (
        <Card className="p-8 text-center">
            <div className="flex flex-col items-center gap-4">
                <p className="text-muted-foreground">
                    {hasActiveFilters 
                        ? `No links found matching your filters` 
                        : 'No short links created yet'
                    }
                </p>
                {hasActiveFilters ? (
                    <Button onClick={onClearFilters}>
                        Clear filters
                    </Button>
                ) : (
                    <Button onClick={onCreateFirstLink}>
                        Create your first short link
                    </Button>
                )}
            </div>
        </Card>
    )
}
