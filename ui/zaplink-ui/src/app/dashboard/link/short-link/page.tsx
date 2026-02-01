'use client'

import { SearchAndFilters } from '@/features/shortlinks/ui/search-and-filters'
import { ShortLinkCard } from '@/features/shortlinks/ui/short-link-card'
import { ShortLinkEmpty } from '@/features/shortlinks/ui/short-link-empty'
import { ShortLinkError } from '@/features/shortlinks/ui/short-link-error'
import { ShortLinkSkeletonList } from '@/features/shortlinks/ui/short-link-skeleton'
import { Button } from '@/components/ui/button'
import { useShortlinks } from '@/hooks/useShortlinks'
import type { ShortLink } from '@/lib/types/apiRequestType'
import { Plus } from 'lucide-react'
import { useRouter } from 'next/navigation'
import { useState } from 'react'

const ShortLink = () => {
    const router = useRouter()
    const {
        shortlinks = [],
        loading,
        error,
        selectedLinks = [],
        setSelectedLinks,
        copyToClipboard,
        deleteShortlink,
        toggleActive,
        refetch
    } = useShortlinks()

    // Search and filter states
    const [searchQuery, setSearchQuery] = useState('')
    const [selectedPlatform, setSelectedPlatform] = useState<string>('all')
    const [selectedDateRange, setSelectedDateRange] = useState<string>('all')
    const [showFilters, setShowFilters] = useState(false)

    const handleSelectLink = (linkId: string, checked: boolean) => {
        setSelectedLinks(prev =>
            checked
                ? [...prev, linkId]
                : prev.filter(id => id !== linkId)
        )
    }

    // Filter shortlinks based on search and filters
    const filteredShortlinks = shortlinks.filter(link => {
        // Search filter
        const matchesSearch = searchQuery === '' ||
            link.title.toLowerCase().includes(searchQuery.toLowerCase()) ||
            link.shortlink.toLowerCase().includes(searchQuery.toLowerCase()) ||
            link.originalUrl.toLowerCase().includes(searchQuery.toLowerCase()) ||
            link.tags?.some((tag: string) => tag.toLowerCase().includes(searchQuery.toLowerCase()))

        // Platform filter
        const matchesPlatform = selectedPlatform === 'all' || link.platform === selectedPlatform

        // Date range filter
        const matchesDateRange = () => {
            if (selectedDateRange === 'all') return true

            const linkDate = new Date(link.createdAt)
            const now = new Date()

            switch (selectedDateRange) {
                case 'today':
                    return linkDate.toDateString() === now.toDateString()
                case 'week': {
                    const weekAgo = new Date(now.getTime() - 7 * 24 * 60 * 60 * 1000)
                    return linkDate >= weekAgo
                }
                case 'month': {
                    const monthAgo = new Date(now.getTime() - 30 * 24 * 60 * 60 * 1000)
                    return linkDate >= monthAgo
                }
                case 'year': {
                    const yearAgo = new Date(now.getTime() - 365 * 24 * 60 * 60 * 1000)
                    return linkDate >= yearAgo
                }
                default:
                    return true
            }
        }

        return matchesSearch && matchesPlatform && matchesDateRange();
    });

    // Clear all filters
    const clearFilters = () => {
        setSearchQuery('')
        setSelectedPlatform('all')
        setSelectedDateRange('all')
    }

    // Check if any filters are active
    const hasActiveFilters = Boolean(searchQuery) || selectedPlatform !== 'all' || selectedDateRange !== 'all'

    const handleCreateFirstLink = () => {
        router.push('/dashboard/link/create-short-link')
    }

    const handleCreateNewLink = () => {
        router.push('/dashboard/link/create-short-link')
    }

    const handleEdit = (link: ShortLink) => {
        // Navigate to create page with edit mode and link ID
        router.push(`/dashboard/link/create-short-link?edit=true&id=${link.id}`)
    }

    if (loading) {
        return (
            <div className="container mx-auto px-4 py-6 max-w-6xl">
                <div className="flex flex-col gap-6">
                    <div className="flex flex-col gap-2">
                        <h1 className="text-3xl font-bold tracking-tight">Short Links</h1>
                        <p className="text-muted-foreground">Manage and track your shortened links</p>
                    </div>
                    <ShortLinkSkeletonList />
                </div>
            </div>
        )
    }

    if (error) {
        return (
            <div className="container mx-auto px-4 py-6 max-w-6xl">
                <div className="flex flex-col gap-6">
                    <div className="flex flex-col gap-2">
                        <h1 className="text-3xl font-bold tracking-tight">Short Links</h1>
                        <p className="text-muted-foreground">Manage and track your shortened links</p>
                    </div>
                    <ShortLinkError
                        error={error}
                        onRetry={refetch}
                    />
                </div>
            </div>
        )
    }

    return (
        <div className="container mx-auto px-4 py-6 max-w-6xl">
            <div className="flex flex-col gap-6">
                {/* Header */}
                <div className="flex items-center justify-between">
                    <div className="flex flex-col gap-2">
                        <h1 className="text-3xl font-bold tracking-tight">Short Links</h1>
                        <p className="text-muted-foreground">Manage and track your shortened links</p>
                    </div>
                    <Button onClick={handleCreateNewLink} className="flex items-center gap-2">
                        <Plus className="h-4 w-4" />
                        Create New Short Link
                    </Button>
                </div>

                {/* Search and Filters */}
                <SearchAndFilters
                    searchQuery={searchQuery}
                    setSearchQuery={setSearchQuery}
                    selectedPlatform={selectedPlatform}
                    setSelectedPlatform={setSelectedPlatform}
                    selectedDateRange={selectedDateRange}
                    setSelectedDateRange={setSelectedDateRange}
                    showFilters={showFilters}
                    setShowFilters={setShowFilters}
                    shortlinks={shortlinks}
                    filteredShortlinks={filteredShortlinks}
                    hasActiveFilters={hasActiveFilters}
                    clearFilters={clearFilters}
                />

                {/* Links Grid */}
                {filteredShortlinks.length > 0 ? (
                    <div className="grid gap-4 grid-cols-1">
                        {filteredShortlinks.map((link) => (
                            <ShortLinkCard
                                key={link.id}
                                link={link}
                                isSelected={selectedLinks.includes(link.id)}
                                onSelectLink={handleSelectLink}
                                onCopy={copyToClipboard}
                                onDelete={deleteShortlink}
                                onToggleActive={toggleActive}
                                onEdit={handleEdit}
                            />
                        ))}
                    </div>
                ) : (
                    <ShortLinkEmpty
                        hasActiveFilters={hasActiveFilters}
                        onClearFilters={clearFilters}
                        onCreateFirstLink={handleCreateFirstLink}
                    />
                )}
            </div>
        </div>
    )
}

export default ShortLink