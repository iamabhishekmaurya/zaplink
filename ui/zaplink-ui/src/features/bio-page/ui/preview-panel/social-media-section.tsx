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
  Globe
} from "lucide-react";

interface SocialMediaSectionProps {
  links: BioLink[];
  theme: ThemeConfig;
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
};

export function SocialMediaSection({ links, theme }: SocialMediaSectionProps) {
  const buttonStyle = theme.layout?.buttonStyle || 'filled';
  const alignment = theme.layout?.contentAlignment === 'left' ? 'justify-start' : 'justify-center';

  return (
    <div className={cn("flex flex-wrap gap-3", alignment)}>
      {links.map((link, index) => {
        const Icon = getIconForLink(link);
        const platform = getPlatformName(link);

        return (
          <motion.a
            key={link.id}
            href={link.url || '#'}
            target="_blank"
            rel="noopener noreferrer"
            initial={{ opacity: 0, scale: 0 }}
            animate={{ opacity: 1, scale: 1 }}
            transition={{
              duration: 0.3,
              delay: index * 0.1,
              type: "spring",
              stiffness: 200,
            }}
            whileHover={{
              scale: 1.2,
              rotate: 5,
              transition: { duration: 0.2 },
            }}
            whileTap={{ scale: 0.9 }}
            className={cn(
              "group relative flex items-center justify-center",
              "w-12 h-12 rounded-xl",
              "transition-all duration-300",
              "shadow-lg hover:shadow-xl",

              // Shape override
              theme.layout?.buttonShape === 'pill' ? 'rounded-full' :
                theme.layout?.buttonShape === 'square' ? 'rounded-none' :
                  theme.layout?.buttonShape === 'hard' ? 'rounded-sm' : 'rounded-xl',

              // Shadows
              theme.layout?.buttonShadow === 'none' ? 'shadow-none' :
                theme.layout?.buttonShadow === 'sm' ? 'shadow-sm' :
                  theme.layout?.buttonShadow === 'md' ? 'shadow-md' :
                    theme.layout?.buttonShadow === 'lg' ? 'shadow-lg' : 'shadow-[0_0_15px_rgba(var(--theme-primary),0.5)]'
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
              <div className="absolute inset-0 bg-gradient-to-r from-white/0 via-white/10 to-white/0 opacity-0 group-hover:opacity-100 transition-opacity duration-500 rounded-inherit" />
            )}

            <Icon className="w-5 h-5 transition-colors relative z-10" />

            {/* Tooltip */}
            <span
              className="absolute -bottom-8 left-1/2 -translate-x-1/2 px-2 py-1 bg-black/80 text-white text-xs rounded opacity-0 group-hover:opacity-100 transition-opacity whitespace-nowrap pointer-events-none z-20"
            >
              {platform}
            </span>
          </motion.a>
        );
      })}
    </div>
  );
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
  }

  // Default
  return Globe;
}

function getPlatformName(link: BioLink): string {
  if (link.title) return link.title;
  if (link.url) {
    const url = link.url.toLowerCase();
    if (url.includes('instagram')) return 'Instagram';
    if (url.includes('twitter') || url.includes('x.com')) return 'Twitter';
    if (url.includes('youtube')) return 'YouTube';
    if (url.includes('facebook')) return 'Facebook';
    if (url.includes('linkedin')) return 'LinkedIn';
    if (url.includes('github')) return 'GitHub';
    if (url.includes('tiktok')) return 'TikTok';
    if (url.includes('twitch')) return 'Twitch';
  }
  return 'Link';
}

function getBackgroundStyle(style: string): string {
  switch (style) {
    case 'filled':
      return 'var(--theme-btn-bg)';
    case 'glass':
      return 'rgba(255, 255, 255, 0.1)';
    case 'outline':
      return 'transparent';
    case 'soft':
      return 'rgba(var(--theme-primary-rgb), 0.1)';
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
      return 'var(--theme-btn-bg)';
    case 'glass':
      return 'var(--theme-text)';
    case 'soft':
      return 'var(--theme-primary)';
    case 'ghost':
      return 'var(--theme-text)';
    default:
      return 'var(--theme-btn-text)';
  }
}
