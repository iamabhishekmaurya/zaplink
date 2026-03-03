"use client";

import { motion } from "framer-motion";
import { BioLink } from "@/services/bioPageService";
import { ThemeConfig } from "@/ui/design-system/theme-utils";
import { cn } from "@/lib/utils";
import {
  Instagram,
  Twitter,
  Youtube,
  Facebook,
  Linkedin,
  Github,
  Music2,
  Video,
  Mail,
  Phone,
  Globe,
  ShoppingBag,
  Calendar,
  Lock,
  Sparkles,
  ExternalLink,
  Music
} from "lucide-react";

interface AdvancedLinkCardProps {
  link: BioLink;
  theme: ThemeConfig;
  previewMode?: boolean;
  index: number;
}

const iconMap: Record<string, React.ElementType> = {
  INSTAGRAM: Instagram,
  TWITTER: Twitter,
  YOUTUBE: Youtube,
  FACEBOOK: Facebook,
  LINKEDIN: Linkedin,
  GITHUB: Github,
  TIKTOK: Music2,
  TWITCH: Video,
  EMAIL: Mail,
  PHONE: Phone,
  WEBSITE: Globe,
  PRODUCT: ShoppingBag,
  SCHEDULED: Calendar,
  GATED: Lock,
  EMBED: Video,
  SOCIAL: Globe,
  LINK: ExternalLink,
};

export function AdvancedLinkCard({ link, theme, previewMode, index }: AdvancedLinkCardProps) {
  // Visibility check
  if (!link.isActive && !previewMode) return null;

  // Schedule check
  const now = new Date();
  if (link.type === 'SCHEDULED' && link.scheduleFrom) {
    if (now < new Date(link.scheduleFrom)) {
      if (previewMode) return <ScheduledPreview title={link.title} status="future" />;
      return null;
    }
    if (link.scheduleTo && now > new Date(link.scheduleTo)) {
      if (previewMode) return <ScheduledPreview title={link.title} status="expired" />;
      return null;
    }
  }

  const Icon = getIconForLink(link);
  const hasThumbnail = !!link.thumbnailUrl;
  const isProduct = link.type === 'PRODUCT' && link.price;
  const isGated = link.type === 'GATED';
  const buttonStyle = theme.layout?.buttonStyle || 'filled';

  return (
    <motion.a
      href={link.url || '#'}
      target="_blank"
      rel="noopener noreferrer"
      initial={{ opacity: 0, y: 20, scale: 0.95 }}
      animate={{ opacity: 1, y: 0, scale: 1 }}
      exit={{ opacity: 0, scale: 0.9 }}
      whileHover={{ scale: 1.02, y: -2 }}
      whileTap={{ scale: 0.98 }}
      transition={{
        duration: 0.3,
        delay: index * 0.05,
        ease: [0.25, 0.46, 0.45, 0.94],
      }}
      className={cn(
        "group relative block w-full overflow-hidden",
        "transition-all duration-300",
        !link.isActive && previewMode && "opacity-50 grayscale",
        isGated && "ring-2 ring-amber-400/50",

        // Shape override
        theme.layout?.buttonShape === 'pill' ? 'rounded-full' :
          theme.layout?.buttonShape === 'square' ? 'rounded-none' :
            theme.layout?.buttonShape === 'hard' ? 'rounded-sm' : 'rounded-xl',

        // Shadows
        theme.layout?.buttonShadow === 'none' ? 'shadow-none' :
          theme.layout?.buttonShadow === 'sm' ? 'shadow-sm' :
            theme.layout?.buttonShadow === 'md' ? 'shadow-md' :
              theme.layout?.buttonShadow === 'lg' ? 'shadow-lg' : 'shadow-[0_0_15px_color-mix(in_srgb,var(--theme-primary),transparent_50%)]'
      )}
      style={{
        background: getBackgroundStyle(buttonStyle),
        border: getBorderStyle(buttonStyle),
        color: getTextColor(buttonStyle),
        backdropFilter: buttonStyle === 'glass' ? 'blur(10px)' : undefined,
      }}
    >
      {/* Animated Gradient for specific styles */}
      {buttonStyle === 'filled' && (
        <div className="absolute inset-0 bg-gradient-to-r from-white/0 via-white/10 to-white/0 opacity-0 group-hover:opacity-100 transition-opacity duration-500" />
      )}

      <div className="relative flex items-center gap-4 p-4">
        {/* Thumbnail or Icon */}
        <div className="relative flex-shrink-0">
          {hasThumbnail ? (
            <div className={cn(
              "relative w-12 h-12 overflow-hidden",
              theme.layout?.buttonShape === 'pill' ? 'rounded-full' : 'rounded-lg'
            )}>
              <img
                src={link.thumbnailUrl}
                alt=""
                className="w-full h-full object-cover transition-transform duration-500 group-hover:scale-110"
              />
            </div>
          ) : (
            <div
              className={cn(
                "w-12 h-12 flex items-center justify-center transition-colors duration-300",
                buttonStyle === 'filled'
                  ? "bg-white/20 text-inherit"
                  : "bg-[var(--theme-primary)]/10 text-[var(--theme-primary)]",
                theme.layout?.buttonShape === 'pill' ? 'rounded-full' : 'rounded-lg'
              )}
            >
              <Icon className="w-6 h-6" />
            </div>
          )}

          {/* Status Indicator */}
          {isGated && (
            <div className="absolute -top-1 -right-1 w-5 h-5 bg-amber-400 rounded-full flex items-center justify-center shadow-sm">
              <Lock className="w-3 h-3 text-amber-900" />
            </div>
          )}
        </div>

        {/* Content */}
        <div className="flex-1 min-w-0 text-left">
          <h3 className="font-semibold text-base truncate transition-colors">
            {link.title}
          </h3>

          {link.url && !isProduct && (
            <p className="text-sm opacity-70 truncate transition-colors">
              {(() => {
                try {
                  return new URL(link.url!).hostname.replace('www.', '');
                } catch {
                  return link.url === '#' ? 'Link' : link.url;
                }
              })()}
            </p>
          )}

          {isProduct && (
            <div className="flex items-center gap-2 mt-1">
              <span className="text-lg font-bold">
                {link.currency || '$'}{link.price?.toFixed(2)}
              </span>
              {link.metadata?.originalPrice && (
                <span className="text-sm opacity-50 line-through">
                  ${link.metadata.originalPrice}
                </span>
              )}
            </div>
          )}
        </div>

        {/* Arrow */}
        <motion.div
          className="flex-shrink-0 opacity-0 group-hover:opacity-100 transition-opacity"
          initial={{ x: -10 }}
          whileHover={{ x: 0 }}
        >
          <ExternalLink className="w-5 h-5 opacity-70" />
        </motion.div>
      </div>
    </motion.a>
  );
}

function ScheduledPreview({ title, status }: { title: string; status: 'future' | 'expired' }) {
  return (
    <div
      className={cn(
        "w-full p-4 rounded-xl border-2 border-dashed",
        status === 'future' ? "border-yellow-400/50 bg-yellow-400/10 text-yellow-600" : "border-red-400/50 bg-red-400/10 text-red-600"
      )}
    >
      <div className="flex items-center gap-2 text-sm font-medium">
        <Calendar className="w-4 h-4" />
        <span>
          {status === 'future' ? 'Scheduled (Future): ' : 'Scheduled (Expired): '}
          {title}
        </span>
      </div>
    </div>
  );
}

function getBackgroundStyle(style: string): string {
  switch (style) {
    case 'filled':
      return 'var(--theme-btn-bg)';
    case 'glass':
      return 'rgba(255, 255, 255, 0.1)'; // Or make this dynamic based on bg color if possible, but hard for glass
    case 'outline':
      return 'transparent';
    case 'soft':
      return 'rgba(var(--theme-primary-rgb), 0.1)'; // Would need RGB vars, fallback to hardcoded equivalent or similar
    case 'ghost':
      return 'transparent';
    default:
      return 'var(--theme-btn-bg)';
  }
}

function getBorderStyle(style: string): string {
  switch (style) {
    case 'filled':
      return 'none';
    case 'outline':
      return '2px solid var(--theme-btn-bg)';
    case 'glass':
      return '1px solid rgba(255, 255, 255, 0.2)';
    case 'soft':
      return 'none';
    default:
      return 'none';
  }
}

function getTextColor(style: string): string {
  switch (style) {
    case 'filled':
      return 'var(--theme-btn-text)';
    case 'outline':
      return 'var(--theme-btn-bg)'; // Outline color usually matches button color
    case 'glass':
      return 'var(--theme-text)'; // Usually text color on glass
    case 'soft':
      return 'var(--theme-primary)';
    case 'ghost':
      return 'var(--theme-text)';
    default:
      return 'var(--theme-btn-text)';
  }
}

function getIconForLink(link: BioLink): React.ElementType {
  // Check type first
  if (iconMap[link.type]) {
    return iconMap[link.type];
  }

  // Auto-detect from URL
  if (link.url) {
    const url = link.url.toLowerCase();
    if (url.includes('instagram')) return Instagram;
    if (url.includes('twitter') || url.includes('x.com')) return Twitter;
    if (url.includes('youtube')) return Youtube;
    if (url.includes('facebook')) return Facebook;
    if (url.includes('linkedin')) return Linkedin;
    if (url.includes('github')) return Github;
    if (url.includes('tiktok')) return Music2;
    if (url.includes('twitch')) return Video;
    if (url.includes('spotify')) return Music;
    if (url.includes('mailto:')) return Mail;
    if (url.includes('tel:')) return Phone;
  }

  return ExternalLink;
}
