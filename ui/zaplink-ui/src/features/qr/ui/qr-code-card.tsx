'use client'

import { Badge } from '@/components/ui/badge'
import { Button } from '@/components/ui/button'
import { Card, CardContent, CardFooter, CardHeader, CardTitle } from '@/components/ui/card'
import { DropdownMenu, DropdownMenuContent, DropdownMenuItem, DropdownMenuTrigger } from '@/components/ui/dropdown-menu'
import { DynamicQrResponse } from '@/lib/types/apiRequestType'
import { QRService } from '@/services/QRServerApi'
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

// Global cache for QR image URLs - persists across component lifecycles
const qrImageCache = new Map<string, string>();
// Track in-flight requests to prevent duplicate API calls
const pendingRequests = new Map<string, Promise<string>>();

// Helper to ensure all required fields have default values
const normalizeQrConfig = (config: any) => ({
    data: config.data || '',
    size: config.size || 1024,
    margin: config.margin ?? 1,
    error_correction_level: config.error_correction_level || 'H',
    transparent_background: config.transparent_background ?? false,
    background_color: config.background_color || '#FFFFFF',
    body: {
        shape: config.body?.shape || 'SQUARE',
        color: config.body?.color || '#000000',
        color_dark: config.body?.color_dark,
        gradient_linear: config.body?.gradient_linear ?? false,
    },
    eye: {
        shape: config.eye?.shape || 'SQUARE',
        color_outer: config.eye?.color_outer || '#000000',
        color_inner: config.eye?.color_inner || '#000000',
    },
    logo: config.logo,
});

export const QrCodeCard = ({
    qr,
    onDelete,
    onDownload
}: QrCodeCardProps) => {
    const [imageUrl, setImageUrl] = useState<string | null>(() => {
        // Initialize from cache if available
        return qrImageCache.get(qr.qrKey) || null;
    })
    const router = useRouter()

    useEffect(() => {
        let isCancelled = false;

        const generateImage = async () => {
            // Check cache first
            const cachedUrl = qrImageCache.get(qr.qrKey);
            if (cachedUrl) {
                setImageUrl(cachedUrl);
                return;
            }

            // Check if there's already a pending request for this qrKey
            const pendingRequest = pendingRequests.get(qr.qrKey);
            if (pendingRequest) {
                try {
                    const url = await pendingRequest;
                    if (!isCancelled) {
                        setImageUrl(url);
                    }
                } catch {
                    // Request failed, will be handled by the original requestor
                }
                return;
            }

            if (!qr.qrConfig) {
                console.warn('No QR config available for', qr.qrKey);
                return;
            }

            // Normalize config to ensure all required fields have default values
            const normalizedConfig = normalizeQrConfig(qr.qrConfig);

            // Create a new request and store it
            const requestPromise = QRService.generateStyledQR(normalizedConfig);
            pendingRequests.set(qr.qrKey, requestPromise);

            try {
                const url = await requestPromise;

                // Cache the result
                qrImageCache.set(qr.qrKey, url);
                pendingRequests.delete(qr.qrKey);

                if (!isCancelled) {
                    setImageUrl(url);
                }
            } catch (error: any) {
                pendingRequests.delete(qr.qrKey);
                if (!isCancelled) {
                    console.error('Failed to generate QR image', error);
                }
            }
        };

        generateImage();

        return () => {
            isCancelled = true;
        };
    }, [qr.qrKey]) // Only depend on qrKey

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
                                            onClick={() => {
                                                // Store QR data in sessionStorage to avoid long URLs
                                                sessionStorage.setItem(`qr-edit-${qr.qrKey}`, JSON.stringify(qr));
                                                router.push(`/dashboard/qr/qr-gen?edit=${qr.qrKey}`);
                                            }}
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

