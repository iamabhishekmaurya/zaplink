import { ThemeConfig, defaultTheme } from "@/ui/design-system/theme-utils";

export interface BioPageTemplate {
    id: string;
    name: string;
    description: string;
    theme: ThemeConfig;
    previewImage?: string;
}

export const templates: BioPageTemplate[] = [
    {
        id: 'minimal',
        name: 'Minimal Clean',
        description: 'Clean, professional design suitable for everyone.',
        theme: {
            ...defaultTheme,
            layout: {
                ...defaultTheme.layout,
                contentAlignment: 'left',
            }
        },
    },
    {
        id: 'dark-mode',
        name: 'Cyber Dark',
        description: 'High contrast dark theme with neon accents.',
        theme: {
            ...defaultTheme,
            colors: {
                ...defaultTheme.colors,
                background: '#09090b',
                text: '#fafafa',
                primary: '#8b5cf6',
                button: '#27272a',
                buttonText: '#fafafa',
                cardBg: 'rgba(24, 24, 27, 0.8)',
            },
            layout: {
                ...defaultTheme.layout,
                buttonStyle: 'glass',
                buttonShape: 'hard',
                buttonShadow: 'glow',
                contentAlignment: 'center',
                layoutStyle: 'framed',
            },
            effects: {
                backgroundType: 'solid',
            }
        }
    },
    {
        id: 'gradient-glass',
        name: 'Gradient Glass',
        description: 'Modern glassmorphism with vibrant gradient.',
        theme: {
            ...defaultTheme,
            colors: {
                ...defaultTheme.colors,
                text: '#ffffff',
                button: 'rgba(255, 255, 255, 0.2)',
                buttonText: '#ffffff',
                cardBg: 'rgba(255, 255, 255, 0.1)',
            },
            layout: {
                ...defaultTheme.layout,
                buttonStyle: 'glass',
                buttonShape: 'pill',
                buttonShadow: 'lg',
                contentAlignment: 'center',
            },
            effects: {
                backgroundType: 'gradient',
                backgroundGradient: 'linear-gradient(to bottom right, #4f46e5, #ec4899)',
            }
        }
    },
    {
        id: 'monochrome',
        name: 'Monochrome',
        description: 'Stylish black and white design.',
        theme: {
            ...defaultTheme,
            colors: {
                ...defaultTheme.colors,
                background: '#ffffff',
                text: '#000000',
                primary: '#000000',
                button: '#000000',
                buttonText: '#ffffff',
                cardBg: '#ffffff',
            },
            layout: {
                ...defaultTheme.layout,
                buttonStyle: 'outline',
                buttonShape: 'square',
                buttonShadow: 'none',
                contentAlignment: 'left',
            },
            effects: {
                backgroundType: 'solid',
            }
        }
    },
    {
        id: 'forest',
        name: 'Forest',
        description: 'Nature inspired green palette.',
        theme: {
            ...defaultTheme,
            colors: {
                ...defaultTheme.colors,
                background: '#f0fdf4',
                text: '#14532d',
                primary: '#16a34a',
                button: '#15803d',
                buttonText: '#ffffff',
                cardBg: '#dcfce7',
            },
            layout: {
                ...defaultTheme.layout,
                buttonStyle: 'filled',
                buttonShape: 'rounded',
                buttonShadow: 'sm',
                layoutStyle: 'hero',
            },
            effects: {
                backgroundType: 'solid',
            }
        }
    },
    {
        id: 'influencer',
        name: 'Influencer',
        description: 'Soft pastel colors perfect for creators.',
        theme: {
            ...defaultTheme,
            colors: {
                ...defaultTheme.colors,
                background: '#fff1f2',
                text: '#881337',
                primary: '#f43f5e',
                button: '#ffe4e6',
                buttonText: '#be123c',
                cardBg: 'rgba(255, 255, 255, 0.6)',
            },
            layout: {
                ...defaultTheme.layout,
                buttonStyle: 'filled', // Soft filled
                buttonShape: 'pill',
                buttonShadow: 'sm',
                layoutStyle: 'framed',
            },
            effects: {
                backgroundType: 'gradient',
                backgroundGradient: 'linear-gradient(to bottom, #fff1f2, #ffe4e6)',
            }
        }
    }
];
