'use client'

import { Badge } from '@/components/ui/badge'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Label } from '@/components/ui/label'
import {
    Select,
    SelectContent,
    SelectItem,
    SelectTrigger,
    SelectValue,
} from "@/components/ui/select"
import {
    Calendar,
    Filter,
    Search,
    Tag,
    X,
    BarChart3
} from 'lucide-react'
import { DynamicQrResponse } from '@/lib/types/apiRequestType'

interface QrSearchAndFiltersProps {
    searchQuery: string
    setSearchQuery: (query: string) => void
    selectedStatus: string
    setSelectedStatus: (status: string) => void
    selectedDateRange: string
    setSelectedDateRange: (range: string) => void
    showFilters: boolean
    setShowFilters: (show: boolean) => void
    qrs: DynamicQrResponse[]
    filteredQrs: DynamicQrResponse[]
    hasActiveFilters: boolean
    clearFilters: () => void
}

export const QrSearchAndFilters = ({
    searchQuery,
    setSearchQuery,
    selectedStatus,
    setSelectedStatus,
    selectedDateRange,
    setSelectedDateRange,
    showFilters,
    setShowFilters,
    qrs,
    filteredQrs,
    hasActiveFilters,
    clearFilters
}: QrSearchAndFiltersProps) => {

    // Calculate stats
    const totalScans = qrs.reduce((sum, qr) => sum + qr.totalScans, 0)
    const activeQrs = qrs.filter(qr => qr.isActive).length
    const filteredScans = filteredQrs.reduce((sum, qr) => sum + qr.totalScans, 0)

    return (
        <div className="bg-card border rounded-lg p-6 space-y-4">
            {/* Search Bar and Filter Controls in same row */}
            <div className="flex flex-col sm:flex-row gap-4 items-start sm:items-center justify-between">
                <div className="relative flex-1 max-w-md">
                    <Search className="absolute left-3 top-1/2 transform -translate-y-1/2 h-4 w-4 text-muted-foreground z-10" />
                    <Input
                        placeholder="Search by QR name or destination URL..."
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
                                {[searchQuery, selectedStatus !== 'all', selectedDateRange !== 'all'].filter(Boolean).length}
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
                        {filteredQrs.length} / {qrs.length}
                    </div>
                </div>
            </div>

            {/* Expanded Filters */}
            {showFilters && (
                <div className="border-t pt-6">
                    <div className="grid grid-cols-1 md:grid-cols-3 gap-6">
                        <div className="space-y-2">
                            <Label className="flex items-center gap-2">
                                <BarChart3 className="h-4 w-4" />
                                Status
                            </Label>
                            <Select value={selectedStatus} onValueChange={setSelectedStatus}>
                                <SelectTrigger className="h-10">
                                    <SelectValue placeholder="Select status" />
                                </SelectTrigger>
                                <SelectContent>
                                    <SelectItem value="all">All Status</SelectItem>
                                    <SelectItem value="active">Active</SelectItem>
                                    <SelectItem value="inactive">Inactive</SelectItem>
                                </SelectContent>
                            </Select>
                        </div>

                        <div className="space-y-2">
                            <Label className="flex items-center gap-2">
                                <Calendar className="h-4 w-4" />
                                Date Range
                            </Label>
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
                            <Label>Quick Stats</Label>
                            <div className="flex flex-wrap gap-2 text-xs">
                                <Badge variant="secondary" className="px-2 py-1">
                                    Total: {qrs.length}
                                </Badge>
                                <Badge variant="secondary" className="px-2 py-1">
                                    Active: {activeQrs}
                                </Badge>
                                <Badge variant="secondary" className="px-2 py-1">
                                    Total Scans: {totalScans}
                                </Badge>
                                {searchQuery && (
                                    <Badge variant="outline" className="px-2 py-1">
                                        Search: {filteredQrs.length}
                                    </Badge>
                                )}
                                {selectedStatus !== 'all' && (
                                    <Badge variant="outline" className="px-2 py-1">
                                        {selectedStatus}: {filteredQrs.length}
                                    </Badge>
                                )}
                                <Badge variant="outline" className="px-2 py-1">
                                    Filtered Scans: {filteredScans}
                                </Badge>
                            </div>
                        </div>
                    </div>
                </div>
            )}
        </div>
    )
}
