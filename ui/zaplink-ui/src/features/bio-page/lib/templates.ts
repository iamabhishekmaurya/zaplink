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
        id: 'classic-clean',
        name: 'Classic Clean',
        description: 'A timeless, centered layout perfect for personal brands.',
        theme: {
            ...defaultTheme,
            colors: {
                ...defaultTheme.colors,
                background: '#fafafa',
                text: '#171717',
                primary: '#2563eb', // Blue
                button: '#ffffff',
                buttonText: '#171717',
                cardBg: '#ffffff',
            },
            layout: {
                ...defaultTheme.layout,
                buttonStyle: 'outline',
                buttonShape: 'rounded',
                buttonShadow: 'sm',
                layoutStyle: 'classic',
                contentAlignment: 'center',
            },
            effects: { backgroundType: 'solid' }
        },
    },
    {
        id: 'hero-cover',
        name: 'Hero Cover',
        description: 'Make a bold statement with a large header image.',
        theme: {
            ...defaultTheme,
            colors: {
                ...defaultTheme.colors,
                background: '#09090b',
                text: '#ffffff',
                primary: '#10b981', // Emerald
            },
            layout: {
                ...defaultTheme.layout,
                buttonStyle: 'filled',
                buttonShape: 'pill',
                layoutStyle: 'hero',
            },
            effects: {
                backgroundType: 'solid',
                backgroundImage: 'https://images.unsplash.com/photo-1550684848-fac1c5b4e853?q=80&w=2070&auto=format&fit=crop',
            }
        },
    },
    {
        id: 'framed-glass',
        name: 'Framed Glass',
        description: 'Modern glassmorphism framed beautifully on screen.',
        theme: {
            ...defaultTheme,
            colors: {
                ...defaultTheme.colors,
                text: '#ffffff',
                button: 'rgba(255, 255, 255, 0.1)',
                buttonText: '#ffffff',
                cardBg: 'rgba(255, 255, 255, 0.1)',
            },
            layout: {
                ...defaultTheme.layout,
                buttonStyle: 'glass',
                buttonShape: 'rounded',
                layoutStyle: 'framed',
            },
            effects: {
                backgroundType: 'gradient',
                backgroundGradient: 'linear-gradient(135deg, #a855f7 0%, #ec4899 100%)', // Purple to Pink
            }
        },
    },
    {
        id: 'split-screen',
        name: 'Split Screen',
        description: 'Sticky profile on the left, scrollable links on the right.',
        theme: {
            ...defaultTheme,
            colors: {
                ...defaultTheme.colors,
                background: '#f8fafc',
                text: '#0f172a',
                primary: '#3b82f6',
            },
            layout: {
                ...defaultTheme.layout,
                buttonStyle: 'filled',
                buttonShape: 'square',
                layoutStyle: 'split',
            },
            effects: { backgroundType: 'solid' }
        },
    },
    {
        id: 'bento-grid',
        name: 'Bento Grid',
        description: 'A trendy masonry grid layout for all your content.',
        theme: {
            ...defaultTheme,
            colors: {
                ...defaultTheme.colors,
                background: '#18181b', // Zinc 900
                text: '#fafafa',
                primary: '#f43f5e', // Rose
                cardBg: '#27272a', // Zinc 800
            },
            layout: {
                ...defaultTheme.layout,
                buttonStyle: 'soft',
                buttonShape: 'rounded',
                layoutStyle: 'grid',
            },
            effects: { backgroundType: 'solid' }
        },
    },
    {
        id: 'minimalist-air',
        name: 'Minimalist Air',
        description: 'Maximum whitespace, elegant typography, minimal noise.',
        theme: {
            ...defaultTheme,
            typography: {
                ...defaultTheme.typography,
                fontFamily: 'font-serif',
            },
            colors: {
                ...defaultTheme.colors,
                background: '#ffffff',
                text: '#27272a',
                primary: '#000000',
            },
            layout: {
                ...defaultTheme.layout,
                buttonStyle: 'ghost',
                buttonShape: 'hard',
                layoutStyle: 'minimalist',
            },
            effects: { backgroundType: 'solid' }
        },
    },
    {
        id: 'cyberpunk-neon',
        name: 'Cyberpunk',
        description: 'High-tech, low-life aesthetic. Neon colors and dark backgrounds.',
        theme: {
            ...defaultTheme,
            colors: {
                ...defaultTheme.colors,
                background: '#0a0a0c',
                text: '#00ff41',
                primary: '#ff003c',
                cardBg: '#121215',
            },
            layout: {
                ...defaultTheme.layout,
                buttonStyle: 'outline',
                buttonShape: 'hard',
                layoutStyle: 'cyberpunk',
            },
            effects: { backgroundType: 'solid' }
        },
    },
    {
        id: 'notion-doc',
        name: 'Notion Doc',
        description: 'Clean, document-style layout with serif fonts.',
        theme: {
            ...defaultTheme,
            typography: {
                ...defaultTheme.typography,
                fontFamily: 'font-serif',
            },
            colors: {
                ...defaultTheme.colors,
                background: '#ffffff',
                text: '#37352f', // Notion dark gray
                primary: '#37352f',
                cardBg: '#f7f6f3', // Notion light gray
            },
            layout: {
                ...defaultTheme.layout,
                buttonStyle: 'ghost',
                buttonShape: 'rounded',
                layoutStyle: 'notion',
            },
            effects: { backgroundType: 'solid' }
        },
    },
    {
        id: 'retro-terminal',
        name: 'Retro Terminal',
        description: 'MS-DOS inspired command-line interface.',
        theme: {
            ...defaultTheme,
            typography: {
                ...defaultTheme.typography,
                fontFamily: 'font-mono',
            },
            colors: {
                ...defaultTheme.colors,
                background: '#000000',
                text: '#33ff00', // Terminal green
                primary: '#33ff00',
            },
            layout: {
                ...defaultTheme.layout,
                buttonStyle: 'outline',
                buttonShape: 'hard',
                layoutStyle: 'terminal',
            },
            effects: { backgroundType: 'solid' }
        },
    },
    {
        id: 'horizontal-carousel',
        name: 'Carousel',
        description: 'Swipe through your links horizontally. Mobile-first.',
        theme: {
            ...defaultTheme,
            colors: {
                ...defaultTheme.colors,
                background: '#0f172a', // Slate 900
                text: '#f8fafc',
                primary: '#38bdf8', // Light blue
                cardBg: '#1e293b', // Slate 800
            },
            layout: {
                ...defaultTheme.layout,
                buttonStyle: 'filled',
                buttonShape: 'rounded',
                layoutStyle: 'carousel',
            },
            effects: { backgroundType: 'solid' }
        },
    }
];
