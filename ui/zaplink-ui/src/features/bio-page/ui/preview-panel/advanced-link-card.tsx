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
        "group relative block w-full overflow-hidden rounded-2xl",
        "transition-all duration-300",
        !link.isActive && previewMode && "opacity-50 grayscale",
        isGated && "ring-2 ring-amber-400/50"
      )}
      style={{
        background: getBackgroundStyle(theme),
        backdropFilter: 'blur(10px)',
        border: '1px solid rgba(255, 255, 255, 0.1)',
      }}
    >
      {/* Animated Gradient Border */}
      <div className="absolute inset-0 rounded-2xl bg-gradient-to-r from-transparent via-white/10 to-transparent opacity-0 group-hover:opacity-100 transition-opacity duration-500" />

      {/* Shine Effect */}
      <motion.div
        className="absolute inset-0 -translate-x-full group-hover:translate-x-full transition-transform duration-1000"
        style={{
          background: 'linear-gradient(90deg, transparent, rgba(255,255,255,0.2), transparent)',
        }}
      />

      <div className="relative flex items-center gap-4 p-4">
        {/* Thumbnail or Icon */}
        <div className="relative flex-shrink-0">
          {hasThumbnail ? (
            <div className="relative w-14 h-14 rounded-xl overflow-hidden">
              <img
                src={link.thumbnailUrl}
                alt=""
                className="w-full h-full object-cover transition-transform duration-500 group-hover:scale-110"
              />
              <div className="absolute inset-0 bg-black/20 group-hover:bg-black/10 transition-colors" />
            </div>
          ) : (
            <div
              className={cn(
                "w-14 h-14 rounded-xl flex items-center justify-center",
                "bg-white/10 backdrop-blur-sm",
                "transition-colors duration-300 group-hover:bg-white/20"
              )}
            >
              <Icon className="w-6 h-6 text-white/90" />
            </div>
          )}

          {/* Status Indicator */}
          {isGated && (
            <div className="absolute -top-1 -right-1 w-5 h-5 bg-amber-400 rounded-full flex items-center justify-center">
              <Lock className="w-3 h-3 text-amber-900" />
            </div>
          )}
        </div>

        {/* Content */}
        <div className="flex-1 min-w-0">
          <h3 className="font-semibold text-white text-base truncate group-hover:text-white transition-colors">
            {link.title}
          </h3>
          
          {link.url && !isProduct && (
            <p className="text-sm text-white/60 truncate group-hover:text-white/80 transition-colors">
              {new URL(link.url).hostname.replace('www.', '')}
            </p>
          )}

          {isProduct && (
            <div className="flex items-center gap-2 mt-1">
              <span className="text-lg font-bold text-emerald-400">
                {link.currency || '$'}{link.price?.toFixed(2)}
              </span>
              {link.metadata?.originalPrice && (
                <span className="text-sm text-white/40 line-through">
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
          <ExternalLink className="w-5 h-5 text-white/70" />
        </motion.div>
      </div>

      {/* Scheduled Badge */}
      {link.type === 'SCHEDULED' && (
        <div className="absolute top-2 right-2 px-2 py-1 bg-blue-500/80 backdrop-blur-sm rounded-full text-xs font-medium text-white">
          <Calendar className="w-3 h-3 inline mr-1" />
          Scheduled
        </div>
      )}
    </motion.a>
  );
}

function ScheduledPreview({ title, status }: { title: string; status: 'future' | 'expired' }) {
  return (
    <div
      className={cn(
        "w-full p-4 rounded-2xl border-2 border-dashed",
        status === 'future' ? "border-yellow-400/50 bg-yellow-400/10" : "border-red-400/50 bg-red-400/10"
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

function getBackgroundStyle(theme: ThemeConfig): string {
  const buttonStyle = theme.layout?.buttonStyle || 'filled';
  
  switch (buttonStyle) {
    case 'filled':
      return 'linear-gradient(135deg, rgba(255,255,255,0.15) 0%, rgba(255,255,255,0.05) 100%)';
    case 'glass':
      return 'rgba(255, 255, 255, 0.1)';
    case 'outline':
      return 'transparent';
    case 'soft':
      return 'rgba(255, 255, 255, 0.08)';
    case 'ghost':
      return 'transparent';
    default:
      return 'rgba(255, 255, 255, 0.1)';
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
