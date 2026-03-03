"use client";

import { useMemo } from "react";
import { motion } from "framer-motion";
import { BioPageWithTheme } from "@/features/bio-page/types/index";
import { generateThemeVariables } from "@/ui/design-system/theme-utils";
import { cn } from "@/lib/utils";

// Import all 10 distinct advanced layout components
import { ClassicLayout } from "./layouts/classic-layout";
import { HeroLayout } from "./layouts/hero-layout";
import { FramedLayout } from "./layouts/framed-layout";
import { SplitLayout } from "./layouts/split-layout";
import { BentoGridLayout } from "./layouts/bento-grid-layout";
import { MinimalistLayout } from "./layouts/minimalist-layout";
import { CyberpunkLayout } from "./layouts/cyberpunk-layout";
import { NotionLayout } from "./layouts/notion-layout";
import { TerminalLayout } from "./layouts/terminal-layout";
import { CarouselLayout } from "./layouts/carousel-layout";

interface ModernBioPageRendererProps {
  page: BioPageWithTheme;
  previewMode?: boolean;
}

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

  // Component Map for O(1) rendering and cleaner code
  const LayoutComponent = useMemo(() => {
    switch (layoutStyle) {
      case 'hero': return HeroLayout;
      case 'framed': return FramedLayout;
      case 'split': return SplitLayout;
      case 'grid': return BentoGridLayout;
      case 'minimalist': return MinimalistLayout;
      case 'cyberpunk': return CyberpunkLayout;
      case 'notion': return NotionLayout;
      case 'terminal': return TerminalLayout;
      case 'carousel': return CarouselLayout;
      case 'classic':
      default: return ClassicLayout;
    }
  }, [layoutStyle]);


  // Special handling for layouts that force their own backgrounds/CSS (like Cyberpunk/Terminal)
  const isCustomBgLayout = layoutStyle === 'cyberpunk' || layoutStyle === 'terminal' || layoutStyle === 'notion';

  return (
    <motion.div
      initial="hidden"
      animate="visible"
      variants={containerVariants}
      className={cn(
        previewMode ? "min-h-full" : "min-h-screen",
        "w-full flex flex-col transition-all duration-500 ease-out",
        !isCustomBgLayout && theme.effects?.backgroundType === 'gradient' && "bg-gradient-to-br",
        !isCustomBgLayout && theme.effects?.backgroundType === 'solid' && "bg-[var(--theme-bg)]"
      )}
      style={!isCustomBgLayout ? {
        ...cssVariables,
        backgroundColor: (!theme.effects?.backgroundGradient && !theme.effects?.backgroundImage) ? 'var(--theme-bg)' : undefined,
        backgroundImage: theme.effects?.backgroundImage
          ? `url(${theme.effects.backgroundImage})`
          : theme.effects?.backgroundGradient || undefined,
        backgroundSize: 'cover',
        backgroundPosition: 'center',
        backgroundAttachment: 'fixed',
        color: 'var(--theme-text)',
        fontFamily: 'var(--theme-font)',
      } : { ...cssVariables }} // Still inject css variables so buttons inside can optionally use them, but remove global bg enforcement
    >
      {/* Animated Background Effects */}
      {!isCustomBgLayout && theme.effects?.particles && (
        <div className="fixed inset-0 pointer-events-none overflow-hidden z-0">
          <div className="absolute inset-0 bg-[radial-gradient(circle_at_50%_50%,rgba(255,255,255,0.1)_0%,transparent_50%)]" />
        </div>
      )}

      {/* Dynamic Layout Router */}
      <LayoutComponent {...commonProps} />

    </motion.div>
  );
}
