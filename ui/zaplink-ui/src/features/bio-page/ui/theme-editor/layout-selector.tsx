"use client";

import { cn } from "@/lib/utils";
import { ThemeConfig } from "@/ui/design-system/theme-utils";
import {
    LayoutDashboard, Minimize, Type, UserSquare2,
    Columns, LayoutGrid, Cpu, FileText, Terminal as TerminalIcon, GalleryHorizontalEnd
} from "lucide-react";

interface LayoutSelectorProps {
    theme: ThemeConfig;
    onChange: (theme: ThemeConfig) => void;
}

const layouts = [
    {
        id: 'classic',
        label: 'Classic',
        description: 'Centered profile with links below',
        icon: UserSquare2,
        preview: (
            <div className="w-full h-full flex flex-col items-center justify-center gap-2 p-4 bg-muted/20">
                <div className="w-8 h-8 rounded-full bg-primary/20" />
                <div className="w-16 h-2 rounded bg-primary/10" />
                <div className="w-full h-8 rounded-lg bg-primary/5" />
                <div className="w-full h-8 rounded-lg bg-primary/5" />
            </div>
        )
    },
    {
        id: 'hero',
        label: 'Hero',
        description: 'Large cover image with overlapping content',
        icon: LayoutDashboard,
        preview: (
            <div className="w-full h-full flex flex-col bg-muted/20">
                <div className="h-1/3 bg-primary/20 w-full" />
                <div className="flex-1 px-4 -mt-4">
                    <div className="w-full h-full bg-background rounded-t-xl shadow-sm p-2 flex flex-col items-center gap-2 border-t border-x">
                        <div className="w-8 h-8 rounded-full bg-primary/20 -mt-6 border-2 border-background" />
                        <div className="w-16 h-2 rounded bg-primary/10" />
                        <div className="w-full h-6 rounded-lg bg-primary/5" />
                    </div>
                </div>
            </div>
        )
    },
    {
        id: 'framed',
        label: 'Framed',
        description: 'Modern card layout with floating effect',
        icon: Minimize,
        preview: (
            <div className="w-full h-full flex items-center justify-center p-4 bg-muted/20">
                <div className="w-full h-full rounded-xl border bg-background shadow-lg p-2 flex flex-col items-center gap-2">
                    <div className="w-6 h-6 rounded-full bg-primary/20" />
                    <div className="w-12 h-1.5 rounded bg-primary/10" />
                    <div className="w-full h-6 rounded-lg bg-primary/5" />
                </div>
            </div>
        )
    },
    {
        id: 'split',
        label: 'Split',
        description: 'Two-column design for modern desktop viewing',
        icon: Columns,
        preview: (
            <div className="w-full h-full flex gap-2 p-2 bg-muted/20">
                <div className="w-1/3 h-full rounded-lg bg-primary/10 flex flex-col items-center justify-center p-2">
                    <div className="w-8 h-8 rounded-full bg-primary/20" />
                </div>
                <div className="w-2/3 h-full flex flex-col gap-2">
                    <div className="w-full h-6 rounded-lg bg-primary/5" />
                    <div className="w-full h-6 rounded-lg bg-primary/5" />
                    <div className="w-full h-6 rounded-lg bg-primary/5" />
                </div>
            </div>
        )
    },
    {
        id: 'grid',
        label: 'Bento Grid',
        description: 'High visual impact masonry tile grid',
        icon: LayoutGrid,
        preview: (
            <div className="w-full h-full flex flex-col p-2 bg-muted/20 gap-2">
                <div className="w-full flex items-center gap-2">
                    <div className="w-6 h-6 rounded-full bg-primary/20 shrink-0" />
                    <div className="w-20 h-2 rounded bg-primary/10" />
                </div>
                <div className="flex-1 grid grid-cols-2 gap-2">
                    <div className="bg-primary/5 rounded-lg h-full" />
                    <div className="flex flex-col gap-2 h-full">
                        <div className="bg-primary/5 rounded-lg flex-1" />
                        <div className="bg-primary/5 rounded-lg flex-1" />
                    </div>
                </div>
            </div>
        )
    },
    {
        id: 'minimalist',
        label: 'Minimalist Air',
        description: 'Extreme whitespace and huge typography',
        icon: Type,
        preview: (
            <div className="w-full h-full flex flex-col p-4 bg-background">
                <div className="w-6 h-6 rounded-full bg-muted-foreground/20" />
                <div className="w-24 h-4 rounded bg-foreground/80 mt-2" />
                <div className="w-32 h-1 rounded bg-muted-foreground/30 mt-1 mb-4" />
                <div className="w-full h-4 rounded bg-transparent border-b border-muted-foreground/20" />
                <div className="w-full h-4 rounded bg-transparent border-b border-muted-foreground/20 mt-2" />
            </div>
        )
    },
    {
        id: 'cyberpunk',
        label: 'Cyberpunk Neon',
        description: 'Forced dark mode with neon borders and glitch effects',
        icon: Cpu,
        preview: (
            <div className="w-full h-full flex flex-col p-4 bg-zinc-950 items-center justify-center gap-2">
                <div className="w-8 h-8 rounded-sm bg-pink-500/20 border border-pink-500/50" />
                <div className="w-full h-8 rounded-sm bg-transparent border border-cyan-500/50 shadow-[0_0_5px_rgba(6,182,212,0.5)]" />
                <div className="w-full h-8 rounded-sm bg-transparent border border-pink-500/50 shadow-[0_0_5px_rgba(236,72,153,0.5)]" />
            </div>
        )
    },
    {
        id: 'notion',
        label: 'Notion Doc',
        description: 'Clean document aesthetic, flush left alignment',
        icon: FileText,
        preview: (
            <div className="w-full h-full flex flex-col p-4 bg-stone-50 gap-2">
                <div className="w-8 h-8 rounded bg-stone-200" />
                <div className="w-24 h-3 rounded bg-stone-800" />
                <div className="w-full h-1 bg-stone-200 my-1" />
                <div className="w-full h-6 rounded bg-stone-100 flex items-center px-1"><div className="w-4 h-4 rounded bg-stone-200" /></div>
                <div className="w-full h-6 rounded bg-stone-100 flex items-center px-1"><div className="w-4 h-4 rounded bg-stone-200" /></div>
            </div>
        )
    },
    {
        id: 'terminal',
        label: 'Retro Terminal',
        description: 'MS-DOS hacker aesthetic with monospace font',
        icon: TerminalIcon,
        preview: (
            <div className="w-full h-full flex flex-col p-4 bg-black gap-2 border-[4px] border-zinc-800">
                <div className="w-20 h-2 bg-green-500/80" />
                <div className="w-full h-6 border border-green-500/50 bg-black flex items-center px-1">
                    <div className="w-2 h-2 bg-green-500/80 animate-pulse" />
                </div>
                <div className="w-full h-6 border border-green-500/50 bg-black" />
            </div>
        )
    },
    {
        id: 'carousel',
        label: 'Horizontal Swiper',
        description: 'Horizontal scrollable cards like stories',
        icon: GalleryHorizontalEnd,
        preview: (
            <div className="w-full h-full flex flex-col items-center justify-center p-2 bg-muted/20 overflow-hidden">
                <div className="w-6 h-6 rounded-full bg-primary/20 mb-2" />
                <div className="flex gap-2 w-full ml-8">
                    <div className="w-16 h-20 rounded-lg bg-primary/10 shrink-0" />
                    <div className="w-16 h-20 rounded-lg bg-primary/10 shrink-0" />
                    <div className="w-16 h-20 rounded-lg bg-primary/5 shrink-0 opacity-50" />
                </div>
            </div>
        )
    }
] as const;

export function LayoutSelector({ theme, onChange }: LayoutSelectorProps) {
    const currentLayout = theme.layout?.layoutStyle || 'classic';

    const handleSelect = (layoutId: string) => {
        onChange({
            ...theme,
            layout: {
                ...theme.layout,
                layoutStyle: layoutId as any
            }
        });
    };

    return (
        <div className="space-y-6 pb-20">
            <div className="flex items-center gap-4 pb-6 border-b">
                <div className="p-3 bg-gradient-to-br from-primary/20 to-primary/5 rounded-2xl shadow-sm border border-primary/10">
                    <LayoutDashboard className="w-6 h-6 text-primary" />
                </div>
                <div>
                    <h3 className="text-xl font-bold bg-clip-text text-transparent bg-gradient-to-r from-foreground to-foreground/70">
                        Page Layout
                    </h3>
                    <p className="text-sm text-muted-foreground mt-1">
                        Choose how your content is structured and presented
                    </p>
                </div>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-5">
                {layouts.map((layout) => (
                    <div
                        key={layout.id}
                        onClick={() => handleSelect(layout.id)}
                        className={cn(
                            "group cursor-pointer rounded-2xl border-2 transition-all duration-300 overflow-hidden relative flex flex-col bg-card shadow-sm hover:shadow-md",
                            currentLayout === layout.id
                                ? "border-primary ring-2 ring-primary/20 shadow-md scale-[1.01]"
                                : "border-border/60 hover:border-primary/50 hover:bg-muted/30"
                        )}
                    >
                        {/* Active Indicator positioned top-right over preview */}
                        {currentLayout === layout.id && (
                            <div className="absolute top-3 right-3 z-10 w-6 h-6 bg-primary rounded-full flex items-center justify-center shadow-md">
                                <div className="w-2 h-2 bg-primary-foreground rounded-full animate-pulse" />
                            </div>
                        )}

                        {/* Preview Area */}
                        <div className={cn(
                            "aspect-[4/3] w-full border-b bg-muted/20 relative overflow-hidden transition-colors",
                            currentLayout === layout.id ? "bg-primary/5 border-primary/20" : "group-hover:bg-muted/40"
                        )}>
                            {/* Make the internal preview un-clickable / scale on hover */}
                            <div className="w-full h-full pointer-events-none transform group-hover:scale-105 transition-transform duration-500 ease-out p-1">
                                {layout.preview}
                            </div>
                        </div>

                        {/* Content */}
                        <div className="p-4 flex flex-col justify-between flex-1 gap-2">
                            <div className="flex items-center gap-2.5">
                                <div className={cn(
                                    "p-1.5 rounded-lg transition-colors",
                                    currentLayout === layout.id ? "bg-primary/10" : "bg-muted"
                                )}>
                                    <layout.icon className={cn(
                                        "w-4 h-4",
                                        currentLayout === layout.id ? "text-primary" : "text-muted-foreground"
                                    )} />
                                </div>
                                <span className={cn(
                                    "font-semibold",
                                    currentLayout === layout.id ? "text-primary" : "text-foreground"
                                )}>
                                    {layout.label}
                                </span>
                            </div>
                            <p className="text-[13px] text-muted-foreground leading-snug line-clamp-2">
                                {layout.description}
                            </p>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
}
