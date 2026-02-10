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
  return (
    <div className="flex flex-wrap justify-center gap-3">
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
              "bg-white/10 backdrop-blur-sm",
              "border border-white/10",
              "transition-all duration-300",
              "hover:bg-white/20 hover:border-white/30",
              "shadow-lg hover:shadow-xl"
            )}
          >
            <Icon className="w-5 h-5 text-white/80 group-hover:text-white transition-colors" />

            {/* Tooltip */}
            <span className="absolute -bottom-8 left-1/2 -translate-x-1/2 px-2 py-1 bg-black/80 text-white text-xs rounded opacity-0 group-hover:opacity-100 transition-opacity whitespace-nowrap pointer-events-none">
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
