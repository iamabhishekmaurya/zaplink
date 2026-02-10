"use client"

import { useEffect, useRef } from "react"
import { BioPageWithTheme } from "@/features/bio-page/types"
import { ModernBioPageRenderer } from "@/features/bio-page/ui/preview-panel/modern-bio-page-renderer"
import { generateThemeVariables } from "@/ui/design-system/theme-utils"

interface PublicBioPageViewerProps {
    bioPage: BioPageWithTheme
}

export function PublicBioPageViewer({ bioPage }: PublicBioPageViewerProps) {
    const previousThemeRef = useRef<string>('');
    const isInitialMount = useRef(true);

    // We can add analytics/tracking here in the future
    // useEffect(() => { trackPageView(bioPage.id) }, [])

    // Ensure theme is applied to document body for full immersion on mobile
    useEffect(() => {
        const currentTheme = JSON.stringify(bioPage.parsedTheme);
        
        // Only update theme if it's different from previous
        if (currentTheme !== previousThemeRef.current) {
            const vars = generateThemeVariables(bioPage.parsedTheme);
            
            // Apply theme variables
            Object.entries(vars).forEach(([key, value]) => {
                document.documentElement.style.setProperty(key, value);
            });

            // Set body background color
            document.body.style.backgroundColor = (vars as any)['--theme-bg'];
            
            previousThemeRef.current = currentTheme;
        }

        // Only set up cleanup on unmount, not on every theme change
        return () => {
            if (!isInitialMount.current) {
                // Cleanup only when component actually unmounts
                document.body.style.removeProperty('background-color');
                if (previousThemeRef.current) {
                    const vars = generateThemeVariables(bioPage.parsedTheme);
                    Object.keys(vars).forEach(key => document.documentElement.style.removeProperty(key));
                }
            }
            isInitialMount.current = false;
        };
    }, [bioPage.parsedTheme]);

    return (
        <main className="min-h-screen w-full">
            <ModernBioPageRenderer page={bioPage} previewMode={false} />
        </main>
    )
}
