import { useState, useCallback, useMemo } from 'react';
import { ThemeConfig, defaultTheme, parseThemeConfig, serializeThemeConfig, generateThemeVariables } from "@/ui/design-system/theme-utils";

export function useTheme(initialConfigJson?: string) {
    const [theme, setTheme] = useState<ThemeConfig>(() => parseThemeConfig(initialConfigJson));

    const updateTheme = useCallback((updates: Partial<ThemeConfig> | ((prev: ThemeConfig) => ThemeConfig)) => {
        setTheme((prev) => {
            if (typeof updates === 'function') {
                return updates(prev);
            }
            return { ...prev, ...updates };
        });
    }, []);

    const resetTheme = useCallback(() => {
        setTheme(defaultTheme);
    }, []);

    const serializedTheme = useMemo(() => serializeThemeConfig(theme), [theme]);
    const cssVariables = useMemo(() => generateThemeVariables(theme), [theme]);

    return {
        theme,
        updateTheme,
        resetTheme,
        serializedTheme,
        cssVariables,
    };
}
