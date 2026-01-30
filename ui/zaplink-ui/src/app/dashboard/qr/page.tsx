'use client'

import { useState } from 'react'
import { useRouter } from 'next/navigation'
import { Button } from '@/components/ui/button'
import { useDynamicQrs } from '@/hooks/useDynamicQrs'
import { QrCodeCard } from '@/components/qr/qr-code-card'
import { QrCodeSkeletonList } from '@/components/qr/qr-code-skeleton'
import { QrSearchAndFilters } from '@/components/qr/qr-search-and-filters'
import { Plus, QrCode } from 'lucide-react'
import { Card } from '@/components/ui/card'
import type { DynamicQrResponse } from '@/lib/types/apiRequestType'

const QrListPage = () => {
    const router = useRouter()
    const {
        qrs,
        loading,
        error,
        deleteQr,
        downloadQr
    } = useDynamicQrs()

    // Search and filter states
    const [searchQuery, setSearchQuery] = useState('')
    const [selectedStatus, setSelectedStatus] = useState<string>('all')
    const [selectedDateRange, setSelectedDateRange] = useState<string>('all')
    const [showFilters, setShowFilters] = useState(false)

    const handleCreateNew = () => {
        router.push('/dashboard/qr/qr-gen')
    }

    // Filter QRs based on search and filters
    const filteredQrs = qrs.filter(qr => {
        // Search filter
        const matchesSearch = searchQuery === '' ||
            qr.qrName.toLowerCase().includes(searchQuery.toLowerCase()) ||
            qr.currentDestinationUrl.toLowerCase().includes(searchQuery.toLowerCase()) ||
            qr.redirectUrl.toLowerCase().includes(searchQuery.toLowerCase())

        // Status filter
        const matchesStatus = selectedStatus === 'all' ||
            (selectedStatus === 'active' && qr.isActive) ||
            (selectedStatus === 'inactive' && !qr.isActive)

        // Date range filter
        const matchesDateRange = () => {
            if (selectedDateRange === 'all') return true

            const qrDate = new Date(qr.createdAt)
            const now = new Date()

            switch (selectedDateRange) {
                case 'today':
                    return qrDate.toDateString() === now.toDateString()
                case 'week': {
                    const weekAgo = new Date(now.getTime() - 7 * 24 * 60 * 60 * 1000)
                    return qrDate >= weekAgo
                }
                case 'month': {
                    const monthAgo = new Date(now.getTime() - 30 * 24 * 60 * 60 * 1000)
                    return qrDate >= monthAgo
                }
                case 'year': {
                    const yearAgo = new Date(now.getTime() - 365 * 24 * 60 * 60 * 1000)
                    return qrDate >= yearAgo
                }
                default:
                    return true
            }
        }

        return matchesSearch && matchesStatus && matchesDateRange();
    })

    // Clear all filters
    const clearFilters = () => {
        setSearchQuery('')
        setSelectedStatus('all')
        setSelectedDateRange('all')
    }

    // Check if any filters are active
    const hasActiveFilters = Boolean(searchQuery) || selectedStatus !== 'all' || selectedDateRange !== 'all'

    if (loading) {
        return (
            <div className="container mx-auto px-4 py-6 max-w-6xl space-y-6">
                <div className="space-y-2">
                    <h1 className="text-3xl font-bold tracking-tight">QR Codes</h1>
                    <p className="text-muted-foreground">Manage your saved QR codes and track their performance</p>
                </div>
                <QrCodeSkeletonList />
            </div>
        )
    }

    return (
        <div className="container mx-auto px-4 py-6 max-w-6xl space-y-6">
            {/* Header */}
            <div className="flex flex-col sm:flex-row sm:items-center justify-between gap-4">
                <div className="space-y-2">
                    <h1 className="text-3xl font-bold tracking-tight">QR Codes</h1>
                    <p className="text-muted-foreground">Manage your saved QR codes and track their performance</p>
                </div>
                <Button onClick={handleCreateNew} className="flex items-center gap-2 w-full sm:w-auto">
                    <Plus className="h-4 w-4" />
                    Create New QR
                </Button>
            </div>

            {/* Error State */}
            {error && (
                <Card className="p-6 text-center text-red-500 bg-red-50 border-red-100 dark:bg-red-900/10 dark:border-red-900/20">
                    <p>{error}</p>
                    <Button variant="link" onClick={() => window.location.reload()}>Try again</Button>
                </Card>
            )}

            {/* Search and Filters */}
            {!loading && !error && (
                <QrSearchAndFilters
                    searchQuery={searchQuery}
                    setSearchQuery={setSearchQuery}
                    selectedStatus={selectedStatus}
                    setSelectedStatus={setSelectedStatus}
                    selectedDateRange={selectedDateRange}
                    setSelectedDateRange={setSelectedDateRange}
                    showFilters={showFilters}
                    setShowFilters={setShowFilters}
                    qrs={qrs}
                    filteredQrs={filteredQrs}
                    hasActiveFilters={hasActiveFilters}
                    clearFilters={clearFilters}
                />
            )}

            {/* Empty State */}
            {!loading && !error && filteredQrs.length === 0 && (
                <Card className="p-12 text-center flex flex-col items-center gap-4 border-dashed">
                    <div className="p-4 rounded-full bg-muted">
                        <QrCode className="h-8 w-8 text-muted-foreground" />
                    </div>
                    <div>
                        <h3 className="text-lg font-semibold">No QR Codes found</h3>
                        <p className="text-muted-foreground">
                            {hasActiveFilters
                                ? 'Try adjusting your filters or search terms'
                                : 'Create your first dynamic QR code to start tracking.'
                            }
                        </p>
                    </div>
                    <Button onClick={handleCreateNew}>
                        <Plus className="h-4 w-4 mr-2" /> Create QR Code
                    </Button>
                </Card>
            )}

            {/* List */}
            {filteredQrs.length > 0 && (
                <div className="grid gap-4">
                    {filteredQrs.map((qr) => (
                        <QrCodeCard
                            key={qr.qrKey}
                            qr={qr}
                            onDelete={deleteQr}
                            onDownload={downloadQr}
                        />
                    ))}
                </div>
            )}
        </div>
    )
}

export default QrListPage
