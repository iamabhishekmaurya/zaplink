'use client'

import { Badge } from '@/components/ui/badge'
import { Button } from '@/components/ui/button'
import { Card, CardContent, CardFooter, CardHeader, CardTitle } from '@/components/ui/card'
import { DropdownMenu, DropdownMenuContent, DropdownMenuItem, DropdownMenuTrigger } from '@/components/ui/dropdown-menu'
import { DynamicQrResponse } from '@/lib/types/apiRequestType'
import {
    BarChart3,
    Calendar,
    Download,
    ExternalLink,
    MoreHorizontal,
    Pencil,
    QrCode,
    ScanLine,
    Trash2
} from 'lucide-react'
import { useRouter } from 'next/navigation'
import { useEffect, useState } from 'react'

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
    const router = useRouter()

    useEffect(() => {
        const controller = new AbortController();
        let objectUrl: string | null = null;

        const fetchImage = async () => {
            try {
                // Use qrImageUrl directly - Next.js proxy handles /api/* routing to Gateway
                const token = localStorage.getItem('token')
                const response = await fetch(qr.qrImageUrl, {
                    headers: token ? {
                        'Authorization': `Bearer ${token}`,
                        'X-API-Version': '1'
                    } : { 'X-API-Version': '1' },
                    signal: controller.signal
                })

                if (!response.ok) {

                    throw new Error(`Failed to load QR image: ${response.status}`)
                }

                const blob = await response.blob()
                const url = URL.createObjectURL(blob)
                objectUrl = url
                setImageUrl(url)
            } catch (error: any) {
                if (error.name !== 'AbortError') {
                    console.error('Failed to load QR image', error)
                }
            }
        }

        if (qr.qrImageUrl) {
            fetchImage()
        }

        // Cleanup blob URL on unmount
        return () => {
            controller.abort()
            if (objectUrl) {
                URL.revokeObjectURL(objectUrl)
            }
        }
    }, [qr.qrImageUrl])

    const formatDate = (dateString: string) => {
        const date = new Date(dateString)
        const months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']
        return `${months[date.getMonth()]} ${date.getDate()}, ${date.getFullYear()}`
    }

    return (
        <Card className="hover:shadow-md transition-shadow duration-200 overflow-hidden p-2">
            <div className="flex">
                {/* QR Image Preview */}
                <div className="hidden md:flex flex-shrink-0 w-32 bg-muted/20 items-center justify-center border-r p-4">
                    {imageUrl ? (
                        <div className="relative w-24 h-24 bg-white rounded-lg border p-2 shadow-sm">
                            <img
                                src={imageUrl}
                                alt={qr.qrName}
                                className="w-full h-full object-contain"
                                onError={(e) => {
                                    (e.target as HTMLImageElement).style.display = 'none';
                                    (e.target as HTMLImageElement).nextElementSibling?.classList.remove('hidden');
                                }}
                            />
                            <div className="hidden absolute inset-0 items-center justify-center text-muted-foreground">
                                <QrCode className="w-8 h-8 opacity-20" />
                            </div>
                        </div>
                    ) : (
                        <div className="w-24 h-24 bg-muted rounded animate-pulse" />
                    )}
                </div>

                <div className="flex-1 min-w-0 flex flex-col">
                    <CardHeader className="p-4 pb-2 space-y-0">
                        <div className="flex items-start justify-between gap-4">
                            <div className="min-w-0 space-y-1">
                                <CardTitle className="font-semibold text-lg leading-tight truncate">
                                    {qr.qrName}
                                </CardTitle>
                                <a
                                    href={qr.redirectUrl}
                                    target="_blank"
                                    rel="noreferrer"
                                    className="text-sm text-muted-foreground hover:text-primary flex items-center gap-1 truncate"
                                    title={qr.redirectUrl}
                                >
                                    <span className="truncate">{qr.redirectUrl}</span>
                                    <ExternalLink className="w-3 h-3 flex-shrink-0" />
                                </a>
                            </div>

                            <div className="flex items-center gap-1 flex-shrink-0">
                                <DropdownMenu>
                                    <DropdownMenuTrigger asChild>
                                        <Button variant="ghost" size="icon" className="h-8 w-8">
                                            <MoreHorizontal className="h-4 w-4" />
                                        </Button>
                                    </DropdownMenuTrigger>
                                    <DropdownMenuContent align="end">
                                        <DropdownMenuItem className="cursor-pointer" onClick={() => router.push(`/dashboard/analytics/${qr.qrKey}?type=qr`)}>
                                            <BarChart3 className="h-4 w-4 mr-2" />
                                            Analytics
                                        </DropdownMenuItem>
                                        <DropdownMenuItem
                                            className="cursor-pointer"
                                            onClick={() => router.push(`/dashboard/qr/qr-gen?edit=${qr.qrKey}&data=${encodeURIComponent(JSON.stringify(qr))}`)}
                                        >
                                            <Pencil className="h-4 w-4 mr-2" />
                                            Edit
                                        </DropdownMenuItem>
                                        <DropdownMenuItem className="cursor-pointer" onClick={() => onDownload(qr)}>
                                            <Download className="h-4 w-4 mr-2" />
                                            Download
                                        </DropdownMenuItem>
                                        <DropdownMenuItem
                                            className="text-destructive focus:text-destructive cursor-pointer"
                                            onClick={() => onDelete(qr.qrKey)}
                                        >
                                            <Trash2 className="h-4 w-4 mr-2" />
                                            Delete
                                        </DropdownMenuItem>
                                    </DropdownMenuContent>
                                </DropdownMenu>
                            </div>
                        </div>
                    </CardHeader>

                    <CardContent className="p-4 py-2">
                        <div className="w-full min-w-0">
                            <p className="text-xs text-muted-foreground truncate w-full select-all" title={qr.currentDestinationUrl}>
                                <span className="font-medium">Target: </span>{qr.currentDestinationUrl}
                            </p>
                        </div>
                    </CardContent>

                    <CardFooter className="p-4 pt-0">
                        <div className="flex flex-wrap items-center gap-2">
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
                    </CardFooter>
                </div>
            </div>
        </Card>
    )
}

