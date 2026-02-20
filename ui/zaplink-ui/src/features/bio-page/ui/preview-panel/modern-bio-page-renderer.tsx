"use client";

import { useMemo } from "react";
import { motion, AnimatePresence } from "framer-motion";
import { BioPageWithTheme } from "@/features/bio-page/types/index";
import { AdvancedLinkCard } from '@/features/bio-page/ui/preview-panel/advanced-link-card';
import { ProfileHeader } from '@/features/bio-page/ui/preview-panel/profile-header';
import { PortalsSection } from '@/features/bio-page/ui/preview-panel/portals-section';
import { SocialMediaSection } from '@/features/bio-page/ui/preview-panel/social-media-section';
import { generateThemeVariables } from "@/ui/design-system/theme-utils";
import { Sparkles, Zap, ExternalLink } from "lucide-react";
import { cn } from "@/lib/utils";

interface ModernBioPageRendererProps {
  page: BioPageWithTheme;
  previewMode?: boolean;
}

import { HeroLayout } from "./layouts/hero-layout";
import { FramedLayout } from "./layouts/framed-layout";

export function ModernBioPageRenderer({ page, previewMode = false }: ModernBioPageRendererProps) {
  const theme = page.parsedTheme;

  const cssVariables = useMemo(() => generateThemeVariables(theme), [theme]);

  const sortedLinks = useMemo(() =>
    page.bioLinks ? [...page.bioLinks].sort((a, b) => a.sortOrder - b.sortOrder) : [],
    [page.bioLinks]
  );

  // Separate social links from regular links
  const { socialLinks, regularLinks, portalLinks } = useMemo(() => {
    const social: typeof sortedLinks = [];
    const regular: typeof sortedLinks = [];
    const portals: typeof sortedLinks = [];

    sortedLinks.forEach(link => {
      if (link.type === 'SOCIAL') {
        social.push(link);
      } else if (link.type === 'SECTION' || link.metadata?.sectionType) {
        portals.push(link);
      } else {
        regular.push(link);
      }
    });

    return { socialLinks: social, regularLinks: regular, portalLinks: portals };
  }, [sortedLinks]);

  const containerVariants = {
    hidden: { opacity: 0 },
    visible: {
      opacity: 1,
      transition: {
        staggerChildren: 0.1,
        delayChildren: 0.2,
      },
    },
  };

  const itemVariants = {
    hidden: { opacity: 0, y: 20 },
    visible: {
      opacity: 1,
      y: 0,
      transition: {
        duration: 0.5,
        ease: "easeOut" as const,
      },
    },
  };

  const commonProps = {
    page,
    previewMode,
    socialLinks,
    regularLinks,
    portalLinks,
    itemVariants
  };

  const layoutStyle = theme.layout?.layoutStyle || 'classic';

  return (
    <motion.div
      initial="hidden"
      animate="visible"
      variants={containerVariants}
      className={cn(
        previewMode ? "min-h-full" : "min-h-screen",
        "w-full flex flex-col transition-all duration-500 ease-out",
        layoutStyle === 'classic' && (theme.layout?.contentAlignment === 'left' ? 'items-start' : 'items-center'),
        layoutStyle === 'classic' && "px-4 py-8 sm:px-6 lg:px-8",
        theme.effects?.backgroundType === 'gradient' && "bg-gradient-to-br",
        theme.effects?.backgroundType === 'solid' && "bg-[var(--theme-bg)]"
      )}
      style={{
        ...cssVariables,
        background: theme.effects?.backgroundGradient || 'var(--theme-bg)',
        backgroundImage: theme.effects?.backgroundImage ? `url(${theme.effects.backgroundImage})` : undefined,
        backgroundSize: 'cover',
        backgroundPosition: 'center',
        backgroundAttachment: 'fixed',
        color: 'var(--theme-text)',
        fontFamily: 'var(--theme-font)',
      }}
    >
      {/* Animated Background Effects */}
      {theme.effects?.particles && (
        <div className="fixed inset-0 pointer-events-none overflow-hidden">
          <div className="absolute inset-0 bg-[radial-gradient(circle_at_50%_50%,rgba(255,255,255,0.1)_0%,transparent_50%)]" />
        </div>
      )}

      {/* Layout Dispatcher */}
      {layoutStyle === 'hero' ? (
        <HeroLayout {...commonProps} />
      ) : layoutStyle === 'framed' ? (
        <FramedLayout {...commonProps} />
      ) : (
        // Classic Layout (Default)
        <div className="w-full max-w-2xl mx-auto relative z-10 w-full">
          {/* Profile Header */}
          <motion.div variants={itemVariants}>
            <ProfileHeader
              avatarUrl={page.avatarUrl}
              title={page.title}
              username={page.username}
              bioText={page.bioText}
              verified={page.isPublic}
              theme={theme}
            />
          </motion.div>

          {/* Social Media Icons */}
          {socialLinks.length > 0 && (
            <motion.div variants={itemVariants} className="mt-6">
              <SocialMediaSection links={socialLinks} theme={theme} />
            </motion.div>
          )}

          {/* Portals/Sections */}
          {portalLinks.length > 0 && (
            <motion.div variants={itemVariants} className="mt-8">
              <PortalsSection links={portalLinks} theme={theme} previewMode={previewMode} />
            </motion.div>
          )}

          {/* Main Links */}
          <motion.div variants={itemVariants} className="mt-8 space-y-3">
            <AnimatePresence mode="popLayout">
              {regularLinks.map((link, index) => (
                <AdvancedLinkCard
                  key={link.id}
                  link={link}
                  theme={theme}
                  previewMode={previewMode}
                  index={index}
                />
              ))}
            </AnimatePresence>

            {regularLinks.length === 0 && previewMode && (
              <motion.div
                initial={{ opacity: 0, scale: 0.9 }}
                animate={{ opacity: 1, scale: 1 }}
                className="text-center py-12 border-2 border-dashed rounded-2xl"
                style={{ borderColor: 'color-mix(in srgb, var(--theme-text), transparent 80%)' }}
              >
                <Sparkles className="w-12 h-12 mx-auto mb-4 opacity-50" />
                <p className="text-lg font-medium opacity-70">No links added yet</p>
                <p className="text-sm opacity-50 mt-1">Add your first link to get started</p>
              </motion.div>
            )}
          </motion.div>

          {/* Footer */}
          <motion.div
            variants={itemVariants}
            className="mt-16 pt-8 text-center"
          >
            <div
              className="inline-flex items-center gap-2 px-4 py-2 rounded-full backdrop-blur-sm"
              style={{ backgroundColor: 'color-mix(in srgb, var(--theme-text), transparent 90%)' }}
            >
              <Zap className="w-4 h-4" />
              <span className="text-sm font-medium">Powered by</span>
              <a
                href="https://zap.link"
                target="_blank"
                rel="noopener noreferrer"
                className="text-sm font-bold hover:underline inline-flex items-center gap-1"
              >
                Zaplink
                <ExternalLink className="w-3 h-3" />
              </a>
            </div>
          </motion.div>
        </div>
      )}
    </motion.div>
  );
}
