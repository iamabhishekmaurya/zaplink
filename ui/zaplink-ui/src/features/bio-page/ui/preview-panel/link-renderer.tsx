import { cn } from "@/lib/utils";
import { BioLink } from "@/services/bioPageService";
import { ThemeConfig, getRadiusValue } from "@/ui/design-system/theme-utils";
import { Calendar, Lock, Mail, Music, Phone, ShoppingBag, Video } from "lucide-react";
import {
    FaDiscord,
    FaFacebook,
    FaGithub,
    FaInstagram,
    FaLinkedin, FaSpotify,
    FaTiktok,
    FaTwitch,
    FaTwitter,
    FaYoutube
} from "react-icons/fa";

interface LinkRendererProps {
    link: BioLink;
    theme: ThemeConfig;
    previewMode?: boolean;
}

export function LinkRenderer({ link, theme, previewMode = false }: LinkRendererProps) {
    // Visibility check
    if (!link.isActive) {
        if (previewMode) return <div className="opacity-50 border border-dashed border-gray-400 p-2 text-center text-xs">Hidden: {link.title}</div>;
        return null;
    }

    // Schedule check
    const now = new Date();
    if (link.type === 'SCHEDULED' && link.scheduleFrom) {
        if (now < new Date(link.scheduleFrom)) {
            if (previewMode) return <div className="opacity-50 border border-dashed border-yellow-400 p-2 text-center text-xs">Scheduled (Future): {link.title}</div>;
            return null;
        }
        if (link.scheduleTo && now > new Date(link.scheduleTo)) {
            if (previewMode) return <div className="opacity-50 border border-dashed border-red-400 p-2 text-center text-xs">Scheduled (Expired): {link.title}</div>;
            return null;
        }
    }

    // Styles
    const buttonStyle = theme.layout.buttonStyle;
    const buttonShape = theme.layout.buttonShape;
    const shadow = theme.layout.buttonShadow;

    const radiusInfo = getRadiusValue(buttonShape);

    const baseClasses = cn(
        "w-full flex items-center justify-between px-4 py-3 mb-3 transition-transform hover:scale-[1.02] active:scale-[0.98]",
        shadow !== 'none' && `shadow-${shadow}`,
        buttonStyle === 'outline' ? 'border-2' : '',
        buttonStyle === 'filled' || buttonStyle === 'soft' ? '' : 'bg-transparent',
        // We will use inline styles for dynamic theme colors to ensure preview accuracy without generating CSS classes
    );

    const style: React.CSSProperties = {
        borderRadius: radiusInfo,
        backgroundColor: buttonStyle === 'filled' ? 'var(--button-bg)' : buttonStyle === 'soft' ? 'var(--button-bg-soft)' : 'transparent',
        borderColor: buttonStyle === 'outline' ? 'var(--button-border)' : 'transparent',
        color: buttonStyle === 'filled' ? 'var(--button-fg)' : 'var(--button-fg-alt)',
        // boxShadow handled by tailwind classes or manual if needed
    };

    // Icon resolution
    const Icon = getIconForLink(link);

    return (
        <a
            href={link.url || '#'}
            target="_blank"
            rel="noopener noreferrer"
            className={baseClasses}
            style={style}
        >
            <div className="flex items-center space-x-3 overflow-hidden">
                {link.thumbnailUrl ? (
                    <img src={link.thumbnailUrl} alt="" className="w-10 h-10 object-cover rounded" style={{ borderRadius: radiusInfo }} />
                ) : link.iconUrl ? (
                    <img src={link.iconUrl} className="w-5 h-5 flex-shrink-0" alt="" />
                ) : Icon ? (
                    <Icon className="w-5 h-5 flex-shrink-0" />
                ) : null}

                <span className="font-medium truncate">{link.title}</span>
            </div>

            {/* Right side icons or indicators */}
            {link.type === 'GATED' && <Lock className="w-4 h-4 opacity-70" />}
            {link.type === 'PRODUCT' && link.price && (
                <span className="text-sm font-bold">{link.currency || '$'} {link.price}</span>
            )}
        </a>
    );
}

function getIconForLink(link: BioLink): React.ElementType | null {
    // Auto-detect social icons based on type or url?
    // Using type primarily
    if (link.type === 'SOCIAL') {
        const lowerTitle = link.title.toLowerCase();
        if (lowerTitle.includes('instagram')) return FaInstagram;
        if (lowerTitle.includes('twitter') || lowerTitle.includes('x')) return FaTwitter;
        if (lowerTitle.includes('tiktok')) return FaTiktok;
        if (lowerTitle.includes('youtube')) return FaYoutube;
        if (lowerTitle.includes('facebook')) return FaFacebook;
        if (lowerTitle.includes('linkedin')) return FaLinkedin;
        if (lowerTitle.includes('spotify')) return FaSpotify;
        if (lowerTitle.includes('twitch')) return FaTwitch;
        if (lowerTitle.includes('github')) return FaGithub;
        if (lowerTitle.includes('discord')) return FaDiscord;
    }

    switch (link.type) {
        case 'EMAIL': return Mail;
        case 'PHONE': return Phone;
        case 'PRODUCT': return ShoppingBag;
        case 'EMBED': return link.embedCode ? Video : Music; // Generic
        case 'SCHEDULED': return Calendar;
        case 'GATED': return Lock;
        default: return null;
    }
}
