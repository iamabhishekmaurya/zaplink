"use client"

import { Badge } from "@/components/ui/badge"
import { Card, CardContent, CardDescription, CardHeader, CardTitle } from "@/components/ui/card"
import {
    Table,
    TableBody,
    TableCell,
    TableHead,
    TableHeader,
    TableRow,
} from "@/components/ui/table"
import { DashboardStats } from "@/hooks/useDashboardData"
import { formatDistanceToNow } from "date-fns"
import { BarChart3, Copy, Link2, MousePointerClick, QrCode, ScanLine } from "lucide-react"

import { Button } from "@/components/ui/button"
import {
    Tooltip,
    TooltipContent,
    TooltipProvider,
    TooltipTrigger,
} from "@/components/ui/tooltip"
import { useRouter } from "next/navigation"
import { toast } from "sonner"

export function RecentActivityTable({ items }: { items: DashboardStats['recentActivity'] }) {
    const router = useRouter()

    const handleCreateQr = (url: string) => {
        router.push(`/dashboard/qr/qr-gen?url=${encodeURIComponent(url)}`)
    }

    const handleCopyLink = (shortlink: string) => {
        navigator.clipboard.writeText(shortlink)
        toast.success("Link copied to clipboard")
    }

    return (
        <Card className="col-span-1 lg:col-span-2 glass-card hover:border-primary/20 transition-colors">
            <CardHeader>
                <CardTitle>Recent Activity</CardTitle>
                <CardDescription>
                    Latest links and QR codes created
                </CardDescription>
            </CardHeader>
            <CardContent>
                <Table>
                    <TableHeader>
                        <TableRow>
                            <TableHead className="w-[100px]">Type</TableHead>
                            <TableHead>Title / Name</TableHead>
                            <TableHead>Created</TableHead>
                            <TableHead>Engagement</TableHead>
                            <TableHead className="text-right">Status</TableHead>
                            <TableHead className="text-right">Actions</TableHead>
                        </TableRow>
                    </TableHeader>
                    <TableBody>
                        {items.length === 0 ? (
                            <TableRow>
                                <TableCell colSpan={6} className="text-center py-8 text-muted-foreground">
                                    No recent activity found.
                                </TableCell>
                            </TableRow>
                        ) : (
                            items.map((item) => {
                                const isLink = item.type === 'LINK';
                                const icon = isLink ? <Link2 className="h-4 w-4" /> : <ScanLine className="h-4 w-4" />;
                                const title = isLink ? (item as any).title || (item as any).originalUrl : (item as any).qrName;
                                const engagement = isLink ? (item as any).clicks : (item as any).totalScans || 0;
                                const originalUrl = isLink ? (item as any).originalUrl : null;
                                const shortlink = isLink ? (item as any).shortlink : null;

                                return (
                                    <TableRow key={`${item.type}-${(item as any).id}`}>
                                        <TableCell className="font-medium">
                                            <div className="flex items-center gap-2">
                                                <div className="p-2 bg-muted rounded-md text-muted-foreground">
                                                    {icon}
                                                </div>
                                                <span className="md:hidden text-xs text-muted-foreground">{item.type}</span>
                                            </div>
                                        </TableCell>
                                        <TableCell>
                                            <div className="flex flex-col">
                                                <span className="font-medium line-clamp-1">{title}</span>
                                                {isLink && <span className="text-xs text-muted-foreground line-clamp-1">{shortlink}</span>}
                                            </div>
                                        </TableCell>
                                        <TableCell className="text-muted-foreground text-sm">
                                            {formatDistanceToNow(new Date(item.createdAt), { addSuffix: true })}
                                        </TableCell>
                                        <TableCell>
                                            <div className="flex items-center gap-1">
                                                <MousePointerClick className="h-3 w-3 text-muted-foreground" />
                                                <span>{engagement}</span>
                                            </div>
                                        </TableCell>
                                        <TableCell className="text-right">
                                            <Badge variant={(item as any).isActive ? "default" : "secondary"} className="text-xs">
                                                {(item as any).isActive ? 'Active' : 'Inactive'}
                                            </Badge>
                                        </TableCell>
                                        <TableCell className="text-right">
                                            <div className="flex items-center justify-end gap-1">
                                                {isLink && (
                                                    <>
                                                        <TooltipProvider>
                                                            <Tooltip>
                                                                <TooltipTrigger asChild>
                                                                    <Button variant="ghost" size="icon" className="h-8 w-8" onClick={() => {
                                                                        if ((item as any).shortUrlKey) {
                                                                            router.push(`/dashboard/analytics/${(item as any).shortUrlKey}?type=link`)
                                                                        }
                                                                    }}
                                                                        disabled={!(item as any).shortUrlKey}
                                                                    >
                                                                        <BarChart3 className="h-4 w-4 text-muted-foreground hover:text-foreground" />
                                                                        <span className="sr-only">Analytics</span>
                                                                    </Button>
                                                                </TooltipTrigger>
                                                                <TooltipContent>Analytics</TooltipContent>
                                                            </Tooltip>
                                                        </TooltipProvider>

                                                        <TooltipProvider>
                                                            <Tooltip>
                                                                <TooltipTrigger asChild>
                                                                    <Button variant="ghost" size="icon" className="h-8 w-8" onClick={() => handleCopyLink(shortlink)}>
                                                                        <Copy className="h-4 w-4 text-muted-foreground hover:text-foreground" />
                                                                        <span className="sr-only">Copy Link</span>
                                                                    </Button>
                                                                </TooltipTrigger>
                                                                <TooltipContent>Copy Link</TooltipContent>
                                                            </Tooltip>
                                                        </TooltipProvider>

                                                        <TooltipProvider>
                                                            <Tooltip>
                                                                <TooltipTrigger asChild>
                                                                    <Button variant="ghost" size="icon" className="h-8 w-8" onClick={() => handleCreateQr(originalUrl)}>
                                                                        <QrCode className="h-4 w-4 text-muted-foreground hover:text-foreground" />
                                                                        <span className="sr-only">Create QR</span>
                                                                    </Button>
                                                                </TooltipTrigger>
                                                                <TooltipContent>Create QR Code</TooltipContent>
                                                            </Tooltip>
                                                        </TooltipProvider>
                                                    </>
                                                )}
                                                {!isLink && (
                                                    <TooltipProvider>
                                                        <Tooltip>
                                                            <TooltipTrigger asChild>
                                                                <Button variant="ghost" size="icon" className="h-8 w-8" onClick={() => router.push(`/dashboard/analytics/${(item as any).qrKey}?type=qr`)}>
                                                                    <BarChart3 className="h-4 w-4 text-muted-foreground hover:text-foreground" />
                                                                    <span className="sr-only">Analytics</span>
                                                                </Button>
                                                            </TooltipTrigger>
                                                            <TooltipContent>View Analytics</TooltipContent>
                                                        </Tooltip>
                                                    </TooltipProvider>
                                                )}
                                            </div>
                                        </TableCell>
                                    </TableRow>
                                )
                            })
                        )}
                    </TableBody>
                </Table>
            </CardContent>
        </Card>
    )
}
