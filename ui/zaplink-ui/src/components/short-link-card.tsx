'use client'

import { useState } from 'react'
import { Badge } from '@/components/ui/badge'
import { Button } from '@/components/ui/button'
import { Card } from '@/components/ui/card'
import { Checkbox } from '@/components/ui/checkbox'
import {
    BarChart3,
    Calendar,
    Copy,
    Check,
    Edit3,
    Lock,
    MoreHorizontal,
    Share2,
    Tag,
    Trash2,
    Power,
    PowerOff
} from 'lucide-react'
import { DropdownMenu, DropdownMenuContent, DropdownMenuItem, DropdownMenuTrigger } from '@/components/ui/dropdown-menu'
import { ShortLink } from '@/lib/types/apiRequestType'
import { getPlatformIcon, getPlatformColor } from '@/lib/utils/platformIcons'
import { ShareDropdown } from '@/components/share-dropdown'

interface ShortLinkCardProps {
    link: ShortLink
    isSelected: boolean
    onSelectLink: (linkId: string, checked: boolean) => void
    onCopy: (shortlink: string) => void
    onDelete: (linkId: string) => void
    onToggleActive: (linkId: string) => void
    onEdit: (link: ShortLink) => void
}

export const ShortLinkCard = ({
    link,
    isSelected,
    onSelectLink,
    onCopy,
    onDelete,
    onToggleActive,
    onEdit
}: ShortLinkCardProps) => {
    const [copied, setCopied] = useState(false)
    const Icon = getPlatformIcon(link.platform)
    const platformColor = getPlatformColor(link.platform)

    const handleCopy = async (shortlink: string) => {
        try {
            await navigator.clipboard.writeText(shortlink)
            setCopied(true)
            onCopy(shortlink)
            
            // Reset after 2 seconds
            setTimeout(() => setCopied(false), 2000)
        } catch (error) {
            // Fallback for older browsers
            const textArea = document.createElement('textarea')
            textArea.value = shortlink
            document.body.appendChild(textArea)
            textArea.select()
            document.execCommand('copy')
            document.body.removeChild(textArea)
            
            setCopied(true)
            onCopy(shortlink)
            setTimeout(() => setCopied(false), 2000)
        }
    }

    const formatDate = (dateString: string) => {
        const date = new Date(dateString)
        const months = ['Jan', 'Feb', 'Mar', 'Apr', 'May', 'Jun', 'Jul', 'Aug', 'Sep', 'Oct', 'Nov', 'Dec']
        const month = months[date.getMonth()]
        const day = date.getDate()
        const year = date.getFullYear()
        return `${month} ${day}, ${year}`
    }

    return (
        <Card className="p-4 hover:shadow-md transition-shadow duration-200">
            <div className="flex flex-col gap-3">
                {/* Header with checkbox and actions */}
                <div className="flex items-start justify-between">
                    <div className="flex items-start gap-3 flex-1">
                        <Checkbox
                            className="mt-1"
                            checked={isSelected}
                            onCheckedChange={(checked) => onSelectLink(link.id, !!checked)}
                        />
                        <div className="flex items-center gap-2">
                            <div className={`p-2 rounded-lg ${platformColor}`}>
                                <Icon className="h-4 w-4" />
                            </div>
                            <h3 className="font-semibold text-sm leading-tight line-clamp-2">
                                {link.title}
                            </h3>
                        </div>
                    </div>
                    <div className="flex items-center gap-1">
                        <ShareDropdown 
                            url={link.shortlink} 
                            title={link.title}
                            description={`Check out this ${link.platform} link: ${link.originalUrl}`}
                        />
                        <Button variant="ghost" size="sm" className="h-8 w-8 p-0">
                            <BarChart3 className="h-4 w-4" />
                        </Button>
                        <DropdownMenu>
                            <DropdownMenuTrigger asChild>
                                <Button 
                                    variant="ghost" 
                                    size="sm" 
                                    className="h-8 w-8 p-0"
                                >
                                    <MoreHorizontal className="h-4 w-4" />
                                </Button>
                            </DropdownMenuTrigger>
                            <DropdownMenuContent align="end">
                                <DropdownMenuItem onClick={() => onEdit(link)}>
                                    <Edit3 className="h-4 w-4 mr-2" />
                                    Edit
                                </DropdownMenuItem>
                                <DropdownMenuItem onClick={() => onToggleActive(link.id)}>
                                    {link.isActive ? (
                                        <>
                                            <PowerOff className="h-4 w-4 mr-2" />
                                            Deactivate
                                        </>
                                    ) : (
                                        <>
                                            <Power className="h-4 w-4 mr-2" />
                                            Activate
                                        </>
                                    )}
                                </DropdownMenuItem>
                                <DropdownMenuItem 
                                    className="text-destructive focus:text-destructive"
                                    onClick={() => onDelete(link.id)}
                                >
                                    <Trash2 className="h-4 w-4 mr-2 text-destructive" />
                                    Delete
                                </DropdownMenuItem>
                            </DropdownMenuContent>
                        </DropdownMenu>
                    </div>
                </div>

                {/* Shortlink */}
                <div className="ml-11">
                    <div className="flex items-center gap-2">
                        <span className="text-sm font-medium text-primary">
                            {link.shortlink}
                        </span>
                        <Button
                            variant="ghost"
                            size="sm"
                            className={`h-6 w-6 p-0 hover:bg-muted transition-all duration-200 ${
                                copied ? 'text-green-600 hover:text-green-700' : 'hover:text-primary'
                            }`}
                            onClick={() => handleCopy(link.shortlink)}
                        >
                            <div className={`transition-transform duration-200 ${copied ? 'scale-110' : 'scale-100'}`}>
                                {copied ? (
                                    <Check className="h-3 w-3" />
                                ) : (
                                    <Copy className="h-3 w-3" />
                                )}
                            </div>
                        </Button>
                    </div>
                </div>

                {/* Original URL */}
                <div className="ml-11">
                    <p className="text-xs text-muted-foreground truncate">
                        {link.originalUrl}
                    </p>
                </div>

                {/* Tags */}
                <div className="ml-11 flex flex-wrap items-center gap-2 mt-2">
                    {link.hasAnalytics && (
                        <Badge variant="secondary" className="text-xs flex items-center gap-1">
                            <Lock className="h-3 w-3" />
                            Click data
                        </Badge>
                    )}
                    <Badge variant="outline" className="text-xs flex items-center gap-1">
                        <Calendar className="h-3 w-3" />
                        {formatDate(link.createdAt)}
                    </Badge>
                    <Badge variant="outline" className="text-xs flex items-center gap-1">
                        <Icon className="h-3 w-3" />
                        {link.platform}
                    </Badge>
                    {link.clicks !== undefined && (
                        <Badge variant="outline" className="text-xs">
                            {link.clicks} clicks
                        </Badge>
                    )}
                </div>
            </div>
        </Card>
    )
}
