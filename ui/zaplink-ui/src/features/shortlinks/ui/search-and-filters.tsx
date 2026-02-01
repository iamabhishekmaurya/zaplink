'use client'

import { Badge } from '@/components/ui/badge'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import {
    Select,
    SelectContent,
    SelectItem,
    SelectTrigger,
    SelectValue,
} from '@/components/ui/select'
import {
    Calendar,
    Filter,
    Search,
    Tag,
    X
} from 'lucide-react'
import { ShortLink } from '@/lib/types/apiRequestType'

interface SearchAndFiltersProps {
    searchQuery: string
    setSearchQuery: (query: string) => void
    selectedPlatform: string
    setSelectedPlatform: (platform: string) => void
    selectedDateRange: string
    setSelectedDateRange: (range: string) => void
    showFilters: boolean
    setShowFilters: (show: boolean) => void
    shortlinks: ShortLink[]
    filteredShortlinks: ShortLink[]
    hasActiveFilters: boolean
    clearFilters: () => void
}

export const SearchAndFilters = ({
    searchQuery,
    setSearchQuery,
    selectedPlatform,
    setSelectedPlatform,
    selectedDateRange,
    setSelectedDateRange,
    showFilters,
    setShowFilters,
    shortlinks,
    filteredShortlinks,
    hasActiveFilters,
    clearFilters
}: SearchAndFiltersProps) => {

    // Get unique platforms from shortlinks
    const getUniquePlatforms = () => {
        const platforms = [...new Set(shortlinks.map(link => link.platform))]
        return platforms.filter(Boolean)
    }

    return (
        <div className="bg-card border rounded-lg p-6 space-y-4">
            {/* Search Bar and Filter Controls in same row */}
            <div className="flex flex-col sm:flex-row gap-4 items-start sm:items-center justify-between">
                <div className="relative flex-1 max-w-md">
                    <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-muted-foreground z-10" />
                    <Input
                        placeholder="Search by title, shortlink, URL, or tags..."
                        value={searchQuery}
                        onChange={(e) => setSearchQuery(e.target.value)}
                        className="pl-10 pr-10 h-11 bg-background"
                    />
                    {searchQuery && (
                        <Button
                            variant="ghost"
                            size="sm"
                            className="absolute right-1 top-1/2 transform -translate-y-1/2 h-7 w-7 p-0 hover:bg-muted"
                            onClick={() => setSearchQuery('')}
                        >
                            <X className="h-4 w-4" />
                        </Button>
                    )}
                </div>

                <div className="flex items-center gap-3">
                    <Button
                        variant={showFilters ? "default" : "outline"}
                        size="sm"
                        onClick={() => setShowFilters(!showFilters)}
                        className="flex items-center gap-2 h-9"
                    >
                        <Filter className="h-4 w-4" />
                        Filters
                        {hasActiveFilters && (
                            <Badge variant="secondary" className="ml-1 h-5 px-1.5 text-xs">
                                {[searchQuery, selectedPlatform !== 'all', selectedDateRange !== 'all'].filter(Boolean).length}
                            </Badge>
                        )}
                    </Button>

                    {hasActiveFilters && (
                        <Button
                            variant="ghost"
                            size="sm"
                            onClick={clearFilters}
                            className="text-muted-foreground hover:text-foreground h-9"
                        >
                            Clear all
                        </Button>
                    )}

                    <div className="text-sm text-muted-foreground font-medium">
                        {filteredShortlinks.length} / {shortlinks.length}
                    </div>
                </div>
            </div>

            {/* Expanded Filters */}
            {showFilters && (
                <div className="border-t pt-6">
                    <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
                        <div className="space-y-2">
                            <label className="text-sm font-semibold text-foreground flex items-center gap-2">
                                <Tag className="h-4 w-4" />
                                Platform
                            </label>
                            <Select value={selectedPlatform} onValueChange={setSelectedPlatform}>
                                <SelectTrigger className="h-10">
                                    <SelectValue placeholder="Select platform" />
                                </SelectTrigger>
                                <SelectContent>
                                    <SelectItem value="all">All Platforms</SelectItem>
                                    {getUniquePlatforms().map(platform => (
                                        <SelectItem key={platform} value={platform}>
                                            {platform}
                                        </SelectItem>
                                    ))}
                                </SelectContent>
                            </Select>
                        </div>

                        <div className="space-y-2">
                            <label className="text-sm font-semibold text-foreground flex items-center gap-2">
                                <Calendar className="h-4 w-4" />
                                Date Range
                            </label>
                            <Select value={selectedDateRange} onValueChange={setSelectedDateRange}>
                                <SelectTrigger className="h-10">
                                    <SelectValue placeholder="Select date range" />
                                </SelectTrigger>
                                <SelectContent>
                                    <SelectItem value="all">All Time</SelectItem>
                                    <SelectItem value="today">Today</SelectItem>
                                    <SelectItem value="week">Past Week</SelectItem>
                                    <SelectItem value="month">Past Month</SelectItem>
                                    <SelectItem value="year">Past Year</SelectItem>
                                </SelectContent>
                            </Select>
                        </div>

                        <div className="space-y-2">
                            <label className="text-sm font-semibold text-foreground">Quick Stats</label>
                            <div className="flex flex-wrap gap-2 text-xs">
                                <Badge variant="secondary" className="px-2 py-1">
                                    Total: {shortlinks.length}
                                </Badge>
                                {searchQuery && (
                                    <Badge variant="outline" className="px-2 py-1">
                                        Search: {filteredShortlinks.length}
                                    </Badge>
                                )}
                                {selectedPlatform !== 'all' && (
                                    <Badge variant="outline" className="px-2 py-1">
                                        {selectedPlatform}: {filteredShortlinks.length}
                                    </Badge>
                                )}
                            </div>
                        </div>
                    </div>
                </div>
            )}
        </div>
    )
}
