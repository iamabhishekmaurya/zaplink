"use client";

import { useMemo } from "react";
import { BioPageTemplate } from "@/features/bio-page/lib/templates";
import { generateThemeVariables, ThemeConfig } from "@/ui/design-system/theme-utils";
import { cn } from "@/lib/utils";

interface ThemePreviewCardProps {
    template: BioPageTemplate;
    isSelected?: boolean;
    onClick?: () => void;
}

export function ThemePreviewCard({ template, isSelected, onClick }: ThemePreviewCardProps) {
    const theme = template.theme;
    const variables = useMemo(() => {
        // Enforce neutral "wireframe" palette for layout selection
        return {
            ...generateThemeVariables(theme),
            // Map to System/Dashboard Theme Variables for valid neutral adaptation
            '--theme-primary': 'var(--primary)',
            '--theme-bg': 'var(--muted)',
            '--theme-text': 'var(--foreground)',
            '--theme-btn-bg': 'var(--secondary)',
            '--theme-btn-text': 'var(--secondary-foreground)',
            '--theme-accent': 'var(--accent)',
            '--theme-card-bg': 'var(--card)',
            '--theme-font': 'Inter, sans-serif',
        } as React.CSSProperties;
    }, [theme]);

    const buttonStyle = theme.layout?.buttonStyle || 'filled';

    // Helper to get button style - force neutral
    const getButtonStyles = () => {
        const base = {
            borderRadius: 'var(--theme-btn-radius)',
        };

        switch (buttonStyle) {
            case 'filled':
                return { ...base, background: 'var(--theme-btn-bg)', color: 'var(--theme-btn-text)', border: 'none' };
            case 'outline':
                return { ...base, background: 'transparent', color: 'var(--theme-btn-text)', border: '2px solid var(--theme-btn-bg)' };
            case 'glass':
                return { ...base, background: 'rgba(255, 255, 255, 0.1)', color: 'var(--theme-btn-text)', border: '1px solid rgba(255,255,255,0.2)', backdropFilter: 'blur(4px)' };
            case 'soft':
                return { ...base, background: 'var(--theme-btn-bg)', opacity: 0.8, color: 'var(--theme-btn-text)', border: 'none' };
            default:
                return { ...base, background: 'var(--theme-btn-bg)', color: 'var(--theme-btn-text)' };
        }
    };

    const btnStyles = getButtonStyles();

    return (
        <div
            onClick={onClick}
            className={cn(
                "cursor-pointer rounded-xl border-2 transition-all overflow-hidden relative group h-full flex flex-col",
                isSelected ? "border-primary ring-2 ring-primary ring-offset-2" : "border-border hover:border-ring/50"
            )}
        >
            <div
                className="aspect-[9/16] relative overflow-hidden bg-muted/30"
                style={{
                    ...variables,
                    color: 'var(--theme-text)',
                } as React.CSSProperties}
            >
                {/* Background Image/Effect Simulation - NEUTRALIZED */}
                {/* We removed the specific background image/gradient to show raw layout */}

                {/* Content Preview */}
                <div className={cn(
                    "absolute inset-0 flex flex-col pointer-events-none",
                    theme.layout?.layoutStyle === 'hero' ? '' : 'p-4 pt-8',
                    theme.layout?.contentAlignment === 'left' ? 'items-start' : 'items-center'
                )}>
                    {/* Hero Header Background */}
                    {theme.layout?.layoutStyle === 'hero' && (
                        <div className="w-full h-16 bg-black/20 mb-[-20px]" />
                    )}

                    {/* Framed Background Container */}
                    <div className={cn(
                        "flex flex-col w-full",
                        theme.layout?.contentAlignment === 'left' ? 'items-start' : 'items-center',
                        theme.layout?.layoutStyle === 'framed' && "bg-white/10 backdrop-blur-sm p-2 rounded-lg border border-white/20 shadow-sm"
                    )}>
                        {/* Avatar */}
                        <div
                            className={cn(
                                "w-16 h-16 mb-3 overflow-hidden border-2 border-white/20 shadow-sm z-10",
                                theme.layout?.layoutStyle === 'hero' && "ml-4"
                            )}
                            style={{
                                borderRadius: theme.layout.buttonShape === 'square' ? '8px' : '9999px',
                                backgroundColor: 'rgba(128,128,128,0.2)'
                            }}
                        >
                            <div className="w-full h-full bg-gradient-to-br from-gray-300 to-gray-400 opacity-50" />
                        </div>

                        {/* Name & Bio lines */}
                        <div className="w-20 h-3 mb-2 rounded opacity-80" style={{ backgroundColor: 'currentColor' }} />
                        <div className="w-32 h-2 mb-6 rounded opacity-50" style={{ backgroundColor: 'currentColor' }} />

                        {/* Buttons */}
                        <div className="w-full space-y-3 px-1">
                            {[1, 2, 3].map((i) => (
                                <div
                                    key={i}
                                    className="w-full h-8 flex items-center justify-center text-[10px] font-medium opacity-90 shadow-sm"
                                    style={btnStyles as React.CSSProperties}
                                >
                                    <div className="w-1/2 h-2 rounded opacity-40 bg-current" />
                                </div>
                            ))}
                        </div>
                    </div>
                </div>

                {/* Selected Overlay */}
                {isSelected && (
                    <div className="absolute inset-0 bg-primary/5 flex items-center justify-center z-10 transition-opacity">
                        <div className="bg-primary text-primary-foreground px-3 py-1 rounded-full text-sm font-medium shadow-sm animate-in fade-in zoom-in duration-200">
                            Selected
                        </div>
                    </div>
                )}

                {/* Hover Overlay */}
                {!isSelected && (
                    <div className="absolute inset-0 bg-black/5 opacity-0 group-hover:opacity-100 transition-opacity z-0" />
                )}
            </div>

            <div className="p-3 border-t bg-card flex-1">
                <h3 className="font-semibold text-sm">{template.name}</h3>
                <p className="text-xs text-muted-foreground mt-1 line-clamp-2">{template.description}</p>
            </div>
        </div>
    );
}
