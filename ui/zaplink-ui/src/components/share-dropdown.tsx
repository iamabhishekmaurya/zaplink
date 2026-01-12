'use client'

import { Button } from '@/components/ui/button';
import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuItem,
    DropdownMenuSeparator,
    DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu';
import {
    Check,
    Copy,
    Download,
    Mail,
    QrCode,
    Share2
} from 'lucide-react';
import { useState } from 'react';
import { FaFacebook, FaInstagram, FaLinkedin, FaTelegram, FaTwitter, FaWhatsapp } from "react-icons/fa";
import { toast } from 'sonner';

interface ShareDropdownProps {
    url: string
    title?: string
    description?: string
    className?: string
    buttonClassName?: string
    showQRCode?: boolean
    showDownload?: boolean
}

export const ShareDropdown = ({
    url,
    title = '',
    description = '',
    className = '',
    buttonClassName = 'h-8 w-8 p-0',
    showQRCode = true,
    showDownload = true
}: ShareDropdownProps) => {
    const [copied, setCopied] = useState(false)

    const handleCopyLink = async () => {
        try {
            await navigator.clipboard.writeText(url)
            setCopied(true)
            toast.success('Link copied to clipboard!')
            setTimeout(() => setCopied(false), 2000)
        } catch (error) {
            // Fallback for older browsers
            const textArea = document.createElement('textarea')
            textArea.value = url
            document.body.appendChild(textArea)
            textArea.select()
            document.execCommand('copy')
            document.body.removeChild(textArea)

            setCopied(true)
            toast.success('Link copied to clipboard!')
            setTimeout(() => setCopied(false), 2000)
        }
    }

    const shareToTwitter = () => {
        const text = title ? `${title} - ${url}` : `Check this out: ${url}`
        const twitterUrl = `https://twitter.com/intent/tweet?text=${encodeURIComponent(text)}`
        window.open(twitterUrl, '_blank', 'width=550,height=420')
    }

    const shareToFacebook = () => {
        const facebookUrl = `https://www.facebook.com/sharer/sharer.php?u=${encodeURIComponent(url)}`
        window.open(facebookUrl, '_blank', 'width=550,height=420')
    }

    const shareToLinkedIn = () => {
        const linkedInUrl = `https://www.linkedin.com/sharing/share-offsite/?url=${encodeURIComponent(url)}`
        window.open(linkedInUrl, '_blank', 'width=550,height=420')
    }

    const shareToInstagram = () => {
        // Instagram doesn't support direct URL sharing, so we copy the link
        handleCopyLink()
        toast.info('Link copied! Open Instagram and paste in your story or bio')
    }

    const shareViaEmail = () => {
        const subject = title ? `Check out: ${title}` : 'Check this out'
        const body = description ? `${description}\n\n${url}` : `I thought you might find this interesting:\n\n${url}`
        const mailtoUrl = `mailto:?subject=${encodeURIComponent(subject)}&body=${encodeURIComponent(body)}`
        window.location.href = mailtoUrl
    }

    const shareViaWhatsApp = () => {
        const text = title ? `${title} - ${url}` : url
        const whatsappUrl = `https://wa.me/?text=${encodeURIComponent(text)}`
        window.open(whatsappUrl, '_blank')
    }

    const shareViaTelegram = () => {
        const text = title ? `${title} - ${url}` : url
        const telegramUrl = `https://t.me/share/url?url=${encodeURIComponent(url)}&text=${encodeURIComponent(title)}`
        window.open(telegramUrl, '_blank')
    }

    const generateQRCode = () => {
        // This would typically open a QR code modal or generate QR code
        toast.info('QR code feature coming soon!')
        // For now, we can use a QR code API
        const qrUrl = `https://api.qrserver.com/v1/create-qr-code/?size=200x200&data=${encodeURIComponent(url)}`
        window.open(qrUrl, '_blank')
    }

    const downloadQRCode = () => {
        const qrUrl = `https://api.qrserver.com/v1/create-qr-code/?size=300x300&data=${encodeURIComponent(url)}`
        const link = document.createElement('a')
        link.href = qrUrl
        link.download = `qr-code-${Date.now()}.png`
        document.body.appendChild(link)
        link.click()
        document.body.removeChild(link)
        toast.success('QR code downloaded!')
    }

    return (
        <DropdownMenu>
            <DropdownMenuTrigger asChild>
                <Button variant="ghost" size="sm" className={buttonClassName}>
                    <Share2 className="h-4 w-4" />
                </Button>
            </DropdownMenuTrigger>
            <DropdownMenuContent align="end" className={`w-56 ${className}`}>
                {/* Quick Actions */}
                <DropdownMenuItem
                    onClick={handleCopyLink}
                    className="cursor-pointer hover:bg-accent transition-colors"
                >
                    {copied ? (
                        <>
                            <Check className="h-4 w-4 mr-2 text-green-600" />
                            <span className="text-green-600">Copied!</span>
                        </>
                    ) : (
                        <>
                            <Copy className="h-4 w-4 mr-2" />
                            Copy Link
                        </>
                    )}
                </DropdownMenuItem>

                <DropdownMenuSeparator />

                {/* Social Media */}
                <div className="px-2 py-1.5 text-sm font-semibold text-muted-foreground">
                    Share on Social Media
                </div>

                <DropdownMenuItem
                    onClick={shareToTwitter}
                    className="cursor-pointer hover:bg-accent transition-colors"
                >
                    <FaTwitter className="h-4 w-4 mr-2 text-blue-400" />
                    Twitter
                </DropdownMenuItem>

                <DropdownMenuItem
                    onClick={shareToFacebook}
                    className="cursor-pointer hover:bg-accent transition-colors"
                >
                    <FaFacebook className="h-4 w-4 mr-2 text-blue-600" />
                    Facebook
                </DropdownMenuItem>

                <DropdownMenuItem
                    onClick={shareToLinkedIn}
                    className="cursor-pointer hover:bg-accent transition-colors"
                >
                    <FaLinkedin className="h-4 w-4 mr-2 text-blue-700" />
                    LinkedIn
                </DropdownMenuItem>

                <DropdownMenuItem
                    onClick={shareToInstagram}
                    className="cursor-pointer hover:bg-accent transition-colors"
                >
                    <FaInstagram className="h-4 w-4 mr-2 text-pink-600" />
                    Instagram
                </DropdownMenuItem>

                <DropdownMenuSeparator />

                {/* Messaging Apps */}
                <div className="px-2 py-1.5 text-sm font-semibold text-muted-foreground">
                    Share via Messaging
                </div>

                <DropdownMenuItem
                    onClick={shareViaWhatsApp}
                    className="cursor-pointer hover:bg-accent transition-colors"
                >
                    <FaWhatsapp className="h-4 w-4 mr-2 text-green-500" />
                    WhatsApp
                </DropdownMenuItem>

                <DropdownMenuItem
                    onClick={shareViaTelegram}
                    className="cursor-pointer hover:bg-accent transition-colors"
                >
                    <FaTelegram className="h-4 w-4 mr-2 text-blue-400" />
                    Telegram
                </DropdownMenuItem>

                <DropdownMenuItem
                    onClick={shareViaEmail}
                    className="cursor-pointer hover:bg-accent transition-colors"
                >
                    <Mail className="h-4 w-4 mr-2" />
                    Email
                </DropdownMenuItem>

                {/* QR Code Options */}
                {(showQRCode || showDownload) && (
                    <>
                        <DropdownMenuSeparator />
                        <div className="px-2 py-1.5 text-sm font-semibold text-muted-foreground">
                            QR Code
                        </div>

                        {showQRCode && (
                            <DropdownMenuItem
                                onClick={generateQRCode}
                                className="cursor-pointer hover:bg-accent transition-colors"
                            >
                                <QrCode className="h-4 w-4 mr-2" />
                                Show QR Code
                            </DropdownMenuItem>
                        )}

                        {showDownload && (
                            <DropdownMenuItem
                                onClick={downloadQRCode}
                                className="cursor-pointer hover:bg-accent transition-colors"
                            >
                                <Download className="h-4 w-4 mr-2" />
                                Download QR Code
                            </DropdownMenuItem>
                        )}
                    </>
                )}
            </DropdownMenuContent>
        </DropdownMenu>
    )
}
