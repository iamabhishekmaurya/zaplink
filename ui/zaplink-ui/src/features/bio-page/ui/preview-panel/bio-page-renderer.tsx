import { BioPageWithTheme } from "@/features/bio-page/types/index";
import { LinkRenderer } from "@/features/bio-page/ui/preview-panel/link-renderer";
import { generateThemeVariables } from "@/ui/design-system/theme-utils";
import { useMemo } from "react";

interface BioPageRendererProps {
    page: BioPageWithTheme;
    previewMode?: boolean;
}

export function BioPageRenderer({ page, previewMode = false }: BioPageRendererProps) {
    const theme = page.parsedTheme;

    // Memoize CSS variables to apply to the container
    const cssVariables = useMemo(() => generateThemeVariables(theme), [theme]);

    // Background style
    const backgroundStyle: React.CSSProperties = {
        ...cssVariables,
        minHeight: '100%',
        width: '100%',
        display: 'flex',
        flexDirection: 'column',
        alignItems: 'center',
        padding: '2rem 1rem',
        backgroundColor: 'var(--theme-bg)',
        backgroundImage: 'var(--theme-bg-image)',
        backgroundSize: 'cover',
        backgroundPosition: 'center',
        color: 'var(--theme-text)',
        fontFamily: 'var(--theme-font)',
    };

    const sortedLinks = page.bioLinks ? [...page.bioLinks].sort((a, b) => a.sortOrder - b.sortOrder) : [];

    return (
        <div style={backgroundStyle} className="bio-page-container">
            {/* Avatar */}
            {page.avatarUrl && (
                <div className="mb-4">
                    <img
                        src={page.avatarUrl}
                        alt={page.title || page.username}
                        className="w-24 h-24 object-cover border-4 border-white/20 shadow-lg"
                        style={{ borderRadius: '50%' }} // Avatar style could be themed too (rounded/square)
                    />
                </div>
            )}

            {/* Profile Info */}
            <div className="text-center mb-8 max-w-md">
                <h1 className="text-xl font-bold mb-2 tracking-tight">
                    {page.title || `@${page.username}`}
                </h1>
                {page.bioText && (
                    <p className="opacity-90 whitespace-pre-wrap leading-relaxed">
                        {page.bioText}
                    </p>
                )}
            </div>

            {/* Links */}
            <div className="w-full max-w-md flex-1 space-y-4">
                {sortedLinks.map((link) => (
                    <LinkRenderer
                        key={link.id}
                        link={link}
                        theme={theme}
                        previewMode={previewMode}
                    />
                ))}

                {sortedLinks.length === 0 && previewMode && (
                    <div className="text-center opacity-50 p-4 border border-dashed rounded">
                        No links added yet
                    </div>
                )}
            </div>

            {/* Footer */}
            <div className="mt-12 pt-6 opacity-60 text-xs font-medium">
                <a href="https://zap.link" target="_blank" rel="noopener noreferrer" className="hover:opacity-100 transition-opacity">
                    Powered by Zaplink
                </a>
            </div>
        </div>
    );
}
