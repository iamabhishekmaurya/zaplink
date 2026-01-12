'use client'

import { useState, useEffect } from 'react'
import { Badge } from '@/components/ui/badge'
import { Button } from '@/components/ui/button'
import { Card } from '@/components/ui/card'
import api from '@/lib/util/api'
import {
    Calendar,
    Download,
    MoreHorizontal,
    QrCode,
    ScanLine,
    Trash2,
    ExternalLink
} from 'lucide-react'
import { DropdownMenu, DropdownMenuContent, DropdownMenuItem, DropdownMenuTrigger } from '@/components/ui/dropdown-menu'
import { DynamicQrResponse } from '@/lib/types/apiRequestType'

interface QrCodeCardProps {
    qr: DynamicQrResponse
    onDelete: (qrKey: string) => void
    onDownload: (qr: DynamicQrResponse) => void
}

export const QrCodeCard = ({
    qr,
    onDelete,
    onDownload
}: QrCodeCardProps) => {
    const [imageUrl, setImageUrl] = useState<string | null>(null)

    useEffect(() => {
        const fetchImage = async () => {
            try {
                // Fetch image as blob with auth headers
                const response = await api.get(qr.qrImageUrl, {
                    responseType: 'blob'
                })
                const url = URL.createObjectURL(response.data)
                setImageUrl(url)
            } catch (error) {
                console.error('Failed to load QR image', error)
            }
        }

        if (qr.qrImageUrl) {
            fetchImage()
        }

        // Cleanup blob URL on unmount
        return () => {
            if (imageUrl) {
                URL.revokeObjectURL(imageUrl)
            }
        }
    }, [qr.qrImageUrl])

    const formatDate = (dateString: string) => {
        const date = new Date(dateString)
        const months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']
        return `${months[date.getMonth()]} ${date.getDate()}, ${date.getFullYear()}`
    }

    return (
        <Card className="p-4 hover:shadow-md transition-shadow duration-200">
            <div className="flex gap-4 items-start">
                {/* QR Image Preview */}
                <div className="hidden md:flex flex-shrink-0 w-24 h-24 bg-white rounded-lg border p-2 items-center justify-center">
                    {/* Using Blob URL for image */}
                    {imageUrl ? (
                        <img
                            src={imageUrl}
                            alt={qr.qrName}
                            className="w-full h-full object-contain"
                            onError={(e) => {
                                // Fallback if image fails to load
                                (e.target as HTMLImageElement).style.display = 'none';
                                (e.target as HTMLImageElement).nextElementSibling?.classList.remove('hidden');
                            }}
                        />
                    ) : (
                        <div className="animate-pulse w-full h-full bg-muted rounded" />
                    )}
                    <div className="hidden text-muted-foreground">
                        <QrCode className="w-8 h-8 opacity-20" />
                    </div>
                </div>

                <div className="flex-1 min-w-0 space-y-2">
                    {/* Header */}
                    <div className="flex items-start justify-between gap-2">
                        <div className="min-w-0">
                            <h3 className="font-semibold text-lg leading-tight truncate">
                                {qr.qrName}
                            </h3>
                            <a
                                href={qr.redirectUrl}
                                target="_blank"
                                rel="noreferrer"
                                className="text-sm text-muted-foreground hover:text-primary flex items-center gap-1 mt-1 truncate"
                            >
                                <span className="truncate">{qr.redirectUrl}</span>
                                <ExternalLink className="w-3 h-3 flex-shrink-0" />
                            </a>
                        </div>
                        <div className="flex items-center gap-1 flex-shrink-0">
                            <Button variant="ghost" size="sm" className="h-8 w-8 p-0" onClick={() => onDownload(qr)}>
                                <Download className="h-4 w-4" />
                            </Button>
                            <DropdownMenu>
                                <DropdownMenuTrigger asChild>
                                    <Button variant="ghost" size="sm" className="h-8 w-8 p-0">
                                        <MoreHorizontal className="h-4 w-4" />
                                    </Button>
                                </DropdownMenuTrigger>
                                <DropdownMenuContent align="end">
                                    <DropdownMenuItem
                                        className="text-destructive focus:text-destructive"
                                        onClick={() => onDelete(qr.qrKey)}
                                    >
                                        <Trash2 className="h-4 w-4 mr-2" />
                                        Delete
                                    </DropdownMenuItem>
                                </DropdownMenuContent>
                            </DropdownMenu>
                        </div>
                    </div>

                    <a
                        href={qr.currentDestinationUrl}
                        target="_blank"
                        rel="noreferrer" className="text-xs text-muted-foreground truncate">
                        {qr.currentDestinationUrl}
                    </a>
                    {/* Stats & Info */}
                    <div className="flex flex-wrap items-center gap-2 mt-2">
                        <Badge variant={qr.isActive ? "default" : "secondary"} className="text-xs">
                            {qr.isActive ? 'Active' : 'Inactive'}
                        </Badge>
                        <Badge variant="outline" className="text-xs flex items-center gap-1">
                            <Calendar className="h-3 w-3" />
                            {formatDate(qr.createdAt)}
                        </Badge>
                        <Badge variant="outline" className="text-xs flex items-center gap-1">
                            <ScanLine className="h-3 w-3" />
                            {qr.totalScans} scans
                        </Badge>
                    </div>
                </div>
            </div>
        </Card>
    )
}

