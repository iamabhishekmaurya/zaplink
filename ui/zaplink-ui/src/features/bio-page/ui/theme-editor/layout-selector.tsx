"use client";

import { cn } from "@/lib/utils";
import { ThemeConfig } from "@/ui/design-system/theme-utils";
import { LayoutDashboard, Minimize, Type, UserSquare2 } from "lucide-react";

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
            <div className="flex items-center gap-3 pb-4 border-b">
                <div className="p-2 bg-primary/10 rounded-xl">
                    <LayoutDashboard className="w-5 h-5 text-primary" />
                </div>
                <div>
                    <h3 className="text-lg font-semibold text-foreground">
                        Page Layout
                    </h3>
                    <p className="text-sm text-muted-foreground">
                        Choose how your content is structured
                    </p>
                </div>
            </div>

            <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
                {layouts.map((layout) => (
                    <div
                        key={layout.id}
                        onClick={() => handleSelect(layout.id)}
                        className={cn(
                            "group cursor-pointer rounded-xl border-2 transition-all duration-200 overflow-hidden relative",
                            currentLayout === layout.id
                                ? "border-primary bg-primary/5 ring-1 ring-primary/20"
                                : "border-muted hover:border-primary/50 hover:bg-muted/50"
                        )}
                    >
                        {/* Preview Area */}
                        <div className="aspect-[4/3] w-full border-b bg-background/50 group-hover:bg-background/80 transition-colors">
                            {layout.preview}
                        </div>

                        {/* Content */}
                        <div className="p-4 space-y-1">
                            <div className="flex items-center gap-2">
                                <layout.icon className={cn(
                                    "w-4 h-4",
                                    currentLayout === layout.id ? "text-primary" : "text-muted-foreground"
                                )} />
                                <span className={cn(
                                    "font-medium",
                                    currentLayout === layout.id ? "text-primary" : "text-foreground"
                                )}>
                                    {layout.label}
                                </span>
                            </div>
                            <p className="text-xs text-muted-foreground line-clamp-2">
                                {layout.description}
                            </p>
                        </div>
                    </div>
                ))}
            </div>
        </div>
    );
}
