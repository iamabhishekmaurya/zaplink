import { tokens } from './tokens';

export interface ThemeConfig {
    colors: {
        primary: string;
        background: string;
        text: string;
        button: string;
        buttonText: string;
        accent: string;
        // Advanced
        cardBg: string; // Transparent, solid, etc.
        overlay?: string; // Gradient overlay
    };
    typography: {
        fontFamily: string;
        fontSize: 'sm' | 'base' | 'lg';
    };
    layout: {
        buttonStyle: 'filled' | 'outline' | 'ghost' | 'glass' | 'soft';
        buttonShape: 'rounded' | 'square' | 'pill' | 'hard';
        buttonShadow: 'none' | 'sm' | 'md' | 'lg' | 'glow';
        cardStyle?: 'flat' | 'raised' | 'glass' | 'border';
        cardBlur?: number;
    };
    effects: {
        backgroundType: 'solid' | 'gradient' | 'image' | 'video' | 'particles';
        backgroundImage?: string;
        backgroundGradient?: string;
        particles?: boolean;
    };
    customCss?: string;
}

export const defaultTheme: ThemeConfig = {
    colors: {
        primary: tokens.colors.primary,
        background: '#ffffff',
        text: '#000000',
        button: tokens.colors.primary,
        buttonText: '#ffffff',
        accent: tokens.colors.accent,
        cardBg: 'rgba(255, 255, 255, 0.9)',
    },
    typography: {
        fontFamily: 'Inter, sans-serif',
        fontSize: 'base',
    },
    layout: {
        buttonStyle: 'filled',
        buttonShape: 'rounded',
        buttonShadow: 'sm',
    },
    effects: {
        backgroundType: 'solid',
        particles: false,
    },
};

/**
 * Generates CSS variables for the bio page based on theme config
 */
export function generateThemeVariables(theme: ThemeConfig) {
    return {
        '--theme-primary': theme.colors.primary,
        '--theme-bg': theme.effects.backgroundType === 'solid' ? theme.colors.background : 'transparent',
        '--theme-text': theme.colors.text,
        '--theme-btn-bg': theme.colors.button,
        '--theme-btn-text': theme.colors.buttonText,
        '--theme-accent': theme.colors.accent,
        '--theme-card-bg': theme.colors.cardBg,
        '--theme-font': theme.typography.fontFamily,
        '--theme-btn-radius': getRadiusValue(theme.layout.buttonShape),
        '--theme-bg-image': theme.effects.backgroundType === 'image' && theme.effects.backgroundImage ? `url(${theme.effects.backgroundImage})` :
            theme.effects.backgroundType === 'gradient' && theme.effects.backgroundGradient ? theme.effects.backgroundGradient : 'none',
    } as React.CSSProperties;
}

export function getRadiusValue(shape: ThemeConfig['layout']['buttonShape']): string {
    switch (shape) {
        case 'square': return '0px';
        case 'rounded': return '8px';
        case 'pill': return '9999px';
        case 'hard': return '2px';
        default: return '8px';
    }
}

export function parseThemeConfig(configJson?: string): ThemeConfig {
    if (!configJson) return defaultTheme;
    try {
        const parsed = JSON.parse(configJson);
        return { ...defaultTheme, ...parsed }; // Merge with default to handle missing keys
    } catch {
        return defaultTheme;
    }
}

export function serializeThemeConfig(config: ThemeConfig): string {
    return JSON.stringify(config);
}
