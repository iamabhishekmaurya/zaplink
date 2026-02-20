import { BioPageWithTheme } from "@/features/bio-page/types/index";
import { generateThemeVariables, getRadiusValue } from "@/ui/design-system/theme-utils";
import { cn } from "@/lib/utils";
import { useMemo } from "react";

interface MiniBioPreviewProps {
    page: BioPageWithTheme;
    className?: string;
}

export function MiniBioPreview({ page, className }: MiniBioPreviewProps) {
    const theme = page.parsedTheme;
    const cssVariables = useMemo(() => generateThemeVariables(theme), [theme]);

    // Abstract representation of content
    const linkCount = Math.min(page.bioLinks?.length || 0, 4);
    const hasSocials = (page.bioLinks?.filter(l => l.type === 'SOCIAL').length || 0) > 0;

    const buttonStyle = {
        backgroundColor: 'var(--theme-btn-bg)',
        color: 'var(--theme-btn-text)',
        borderRadius: 'var(--theme-btn-radius)',
        fontFamily: 'var(--theme-font)',
    };

    return (
        <div
            className={cn(
                "relative w-full aspect-[9/16] overflow-hidden rounded-lg shadow-inner select-none pointer-events-none",
                className
            )}
            style={{
                ...cssVariables,
                backgroundColor: 'var(--theme-bg)',
                backgroundImage: 'var(--theme-bg-image)',
                backgroundSize: 'cover',
                backgroundPosition: 'center',
                color: 'var(--theme-text)',
            } as React.CSSProperties}
        >
            {/* Overlay for specific themes if needed */}
            {theme.effects?.backgroundType === 'particles' && (
                <div className="absolute inset-0 bg-white/5" />
            )}

            <div className="flex flex-col items-center p-3 h-full scale-[0.8] origin-top">
                {/* Avatar */}
                <div className="w-12 h-12 rounded-full bg-muted/20 mb-2 overflow-hidden border-2 border-[var(--theme-text)]/10">
                    {page.avatarUrl ? (
                        <img src={page.avatarUrl} alt="" className="w-full h-full object-cover" />
                    ) : (
                        <div className="w-full h-full bg-[var(--theme-primary)]/20" />
                    )}
                </div>

                {/* Title & Bio Lines */}
                <div className="w-20 h-2 rounded bg-[var(--theme-text)]/20 mb-1" />
                <div className="w-14 h-1.5 rounded bg-[var(--theme-text)]/10 mb-3" />

                {/* Socials Row */}
                {hasSocials && (
                    <div className="flex gap-1 mb-3">
                        {[1, 2, 3].map(i => (
                            <div key={i} className="w-4 h-4 rounded-full bg-[var(--theme-text)]/20" />
                        ))}
                    </div>
                )}

                {/* Links Stack */}
                <div className="w-full flex flex-col gap-1.5">
                    {Array.from({ length: Math.max(linkCount, 3) }).map((_, i) => (
                        <div
                            key={i}
                            className="w-full h-7 flex items-center justify-center text-[6px] opacity-80"
                            style={buttonStyle}
                        >
                            <div className="w-1/2 h-1 bg-current opacity-30 rounded-full" />
                        </div>
                    ))}
                </div>
            </div>
        </div>
    );
}
