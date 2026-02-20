export const tokens = {
    colors: {
        primary: 'var(--primary)', // defined in global css
        secondary: 'var(--secondary)',
        background: 'var(--background)',
        foreground: 'var(--foreground)',
        muted: 'var(--muted)',
        mutedForeground: 'var(--muted-foreground)',
        border: 'var(--border)',
        input: 'var(--input)',
        ring: 'var(--ring)',
        accent: 'var(--accent)',
        // Bio Page Specific Palettes (for templates)
        palettes: {
            minimal: {
                bg: '#ffffff',
                text: '#1a1a1a',
                accent: '#000000',
            },
            dark: {
                bg: '#1a1a1a',
                text: '#ffffff',
                accent: '#3b82f6',
            },
            neon: {
                bg: '#09090b',
                text: '#e4e4e7',
                accent: '#22c55e',
            },
        }
    },
    spacing: {
        xs: '0.25rem',
        sm: '0.5rem',
        md: '1rem',
        lg: '1.5rem',
        xl: '2rem',
        container: '42rem', // Max width for bio page
    },
    borderRadius: {
        sm: '0.25rem',
        md: '0.5rem',
        lg: '0.75rem',
        xl: '1rem',
        full: '9999px',
    },
    typography: {
        fonts: {
            sans: 'var(--font-sans)',
            heading: 'var(--font-heading)',
        }
    }
} as const;
