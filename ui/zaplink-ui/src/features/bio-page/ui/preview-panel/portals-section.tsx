"use client";

import { useState } from "react";
import { motion, AnimatePresence } from "framer-motion";
import { BioLink } from "@/services/bioPageService";
import { ThemeConfig } from "@/ui/design-system/theme-utils";
import { cn } from "@/lib/utils";
import { ChevronDown, Grid, List, LayoutGrid } from "lucide-react";

interface PortalsSectionProps {
  links: BioLink[];
  theme: ThemeConfig;
  previewMode?: boolean;
}

export function PortalsSection({ links, theme, previewMode }: PortalsSectionProps) {
  const [expandedSections, setExpandedSections] = useState<Set<number>>(new Set());

  const toggleSection = (linkId: number) => {
    setExpandedSections(prev => {
      const newSet = new Set(prev);
      if (newSet.has(linkId)) {
        newSet.delete(linkId);
      } else {
        newSet.add(linkId);
      }
      return newSet;
    });
  };

  return (
    <div className="space-y-4">
      {links.map((link, index) => (
        <PortalCard
          key={link.id}
          link={link}
          theme={theme}
          previewMode={previewMode}
          isExpanded={expandedSections.has(link.id)}
          onToggle={() => toggleSection(link.id)}
          index={index}
        />
      ))}
    </div>
  );
}

interface PortalCardProps {
  link: BioLink;
  theme: ThemeConfig;
  previewMode?: boolean;
  isExpanded: boolean;
  onToggle: () => void;
  index: number;
}

function PortalCard({ link, theme, previewMode, isExpanded, onToggle, index }: PortalCardProps) {
  const sectionType = link.metadata?.sectionType || 'default';
  const childLinks = link.metadata?.childLinks || [];

  return (
    <motion.div
      initial={{ opacity: 0, y: 20 }}
      animate={{ opacity: 1, y: 0 }}
      transition={{ delay: index * 0.1 }}
      className={cn(
        "relative overflow-hidden rounded-2xl",
        "bg-white/5 backdrop-blur-sm",
        "border border-white/10",
        "transition-all duration-300"
      )}
    >
      {/* Section Header */}
      <button
        onClick={onToggle}
        className="w-full flex items-center justify-between p-4 hover:bg-white/5 transition-colors"
      >
        <div className="flex items-center gap-3">
          <div className={cn(
            "w-10 h-10 rounded-xl flex items-center justify-center",
            "bg-gradient-to-br from-purple-500/20 to-pink-500/20"
          )}>
            <LayoutGrid className="w-5 h-5 text-white/70" />
          </div>
          <div className="text-left">
            <h3 className="font-semibold text-white">{link.title}</h3>
            {link.url && (
              <p className="text-sm text-white/50">{childLinks.length} items</p>
            )}
          </div>
        </div>

        <motion.div
          animate={{ rotate: isExpanded ? 180 : 0 }}
          transition={{ duration: 0.3 }}
        >
          <ChevronDown className="w-5 h-5 text-white/50" />
        </motion.div>
      </button>

      {/* Expandable Content */}
      <AnimatePresence>
        {isExpanded && (
          <motion.div
            initial={{ height: 0, opacity: 0 }}
            animate={{ height: "auto", opacity: 1 }}
            exit={{ height: 0, opacity: 0 }}
            transition={{ duration: 0.3 }}
            className="overflow-hidden"
          >
            <div className="p-4 pt-0 grid grid-cols-2 sm:grid-cols-3 gap-3">
              {childLinks.map((childLink: any, childIndex: number) => (
                <motion.a
                  key={childIndex}
                  href={childLink.url || '#'}
                  target="_blank"
                  rel="noopener noreferrer"
                  initial={{ opacity: 0, scale: 0.9 }}
                  animate={{ opacity: 1, scale: 1 }}
                  transition={{ delay: childIndex * 0.05 }}
                  whileHover={{ scale: 1.05 }}
                  whileTap={{ scale: 0.95 }}
                  className={cn(
                    "flex flex-col items-center p-3 rounded-xl",
                    "bg-white/5 hover:bg-white/10",
                    "border border-white/5 hover:border-white/10",
                    "transition-all duration-200"
                  )}
                >
                  {childLink.thumbnailUrl && (
                    <img
                      src={childLink.thumbnailUrl}
                      alt=""
                      className="w-full h-20 object-cover rounded-lg mb-2"
                    />
                  )}
                  <span className="text-sm font-medium text-white/80 text-center line-clamp-2">
                    {childLink.title}
                  </span>
                </motion.a>
              ))}

              {childLinks.length === 0 && link.url && (
                <a
                  href={link.url}
                  target="_blank"
                  rel="noopener noreferrer"
                  className={cn(
                    "col-span-full flex items-center justify-center p-4 rounded-xl",
                    "bg-gradient-to-r from-purple-500/20 to-pink-500/20",
                    "border border-white/10",
                    "hover:from-purple-500/30 hover:to-pink-500/30",
                    "transition-all duration-200"
                  )}
                >
                  <span className="font-medium text-white">Visit {link.title}</span>
                </a>
              )}
            </div>
          </motion.div>
        )}
      </AnimatePresence>
    </motion.div>
  );
}
