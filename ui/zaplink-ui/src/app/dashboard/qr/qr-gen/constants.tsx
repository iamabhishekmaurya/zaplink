import * as z from 'zod'
import React from 'react'

// --- SVGs & Icons ---

export const SHAPE_SVGS: Record<string, React.ReactNode> = {
    // Body Shapes
    SQUARE: <rect x="2" y="2" width="20" height="20" fill="currentColor" />,
    ROUNDED: <rect x="2" y="2" width="20" height="20" rx="5" fill="currentColor" />,
    DOT: <circle cx="12" cy="12" r="9" fill="currentColor" />,
    LEAF: <path d="M12 2C6.48 2 2 6.48 2 12C2 17.52 6.48 22 12 22C17.52 22 22 17.52 22 12C22 6.48 17.52 2 12 2ZM12 20C7.58 20 4 16.42 4 12C4 7.58 7.58 4 12 4C16.42 4 20 7.58 20 12C20 16.42 16.42 20 12 20Z" fill="currentColor" />,
    CIRCLE: <circle cx="12" cy="12" r="10" fill="currentColor" />,
    DIAMOND: <rect x="12" y="2.1" width="14" height="14" transform="rotate(45 12 2.1)" fill="currentColor" />,
    LIQUID: <path d="M12 2C8 2 4 6 4 10V14C4 18 8 22 12 22C16 22 20 18 20 14V10C20 6 16 2 12 2Z" fill="currentColor" />,
}

export const BODY_SHAPES = [
    { value: 'SQUARE', label: 'Square', icon: <rect x="3" y="3" width="18" height="18" fill="currentColor" /> },
    { value: 'ROUNDED', label: 'Rounded', icon: <rect x="3" y="3" width="18" height="18" rx="4" fill="currentColor" /> },
    { value: 'DOT', label: 'Dot', icon: <circle cx="12" cy="12" r="8" fill="currentColor" /> },
    { value: 'CIRCLE', label: 'Circle', icon: <circle cx="12" cy="12" r="10" fill="currentColor" /> },
    { value: 'LIQUID', label: 'Liquid', icon: <path d="M12 2 C 7 2 3 6 3 11 C 3 17 7.5 21 12 22 C 16.5 21 21 17 21 11 C 21 6 17 2 12 2 Z" fill="currentColor" /> },
]

export const EYE_SHAPES = [
    { value: 'SQUARE', label: 'Square', icon: <g fill="currentColor"><rect x="3" y="3" width="18" height="18" rx="1" stroke="currentColor" strokeWidth="4" fill="none" /><rect x="9" y="9" width="6" height="6" /></g> },
    { value: 'ROUNDED', label: 'Rounded', icon: <g fill="currentColor"><rect x="3" y="3" width="18" height="18" rx="5" stroke="currentColor" strokeWidth="4" fill="none" /><rect x="9" y="9" width="6" height="6" rx="2" /></g> },
    { value: 'LEAF', label: 'Leaf', icon: <g fill="currentColor"><path d="M 3 12 C 3 7 7 3 12 3 L 21 3 L 21 12 C 21 17 17 21 12 21 L 3 21 L 3 12 Z" stroke="currentColor" strokeWidth="3" fill="none" /><circle cx="12" cy="12" r="3" /></g> },
    { value: 'CIRCLE', label: 'Circle', icon: <g fill="currentColor"><circle cx="12" cy="12" r="9" stroke="currentColor" strokeWidth="4" fill="none" /><circle cx="12" cy="12" r="3" /></g> },
]

export const PRESET_COLORS = [
    { name: 'Black', code: '#000000' },
    { name: 'Zap Blue', code: '#0066FF' },
    { name: 'Success', code: '#10B981' },
    { name: 'Warning', code: '#F59E0B' },
    { name: 'Purple', code: '#8B5CF6' },
    { name: 'Pink', code: '#EC4899' },
]

export const LOGO_PRESETS = [
    { name: 'GitHub', url: 'https://cdn-icons-png.flaticon.com/512/3291/3291695.png' },
    { name: 'Twitter', url: 'https://cdn-icons-png.flaticon.com/512/733/733579.png' },
    { name: 'LinkedIn', url: 'https://cdn-icons-png.flaticon.com/512/174/174857.png' },
    { name: 'Instagram', url: 'https://cdn-icons-png.flaticon.com/512/2111/2111463.png' },
    { name: 'Facebook', url: 'https://cdn-icons-png.flaticon.com/512/5968/5968764.png' },
    { name: 'YouTube', url: 'https://cdn-icons-png.flaticon.com/512/1384/1384060.png' },
    { name: 'WhatsApp', url: 'https://cdn-icons-png.flaticon.com/512/733/733585.png' },
    { name: 'TikTok', url: 'https://cdn-icons-png.flaticon.com/512/3046/3046121.png' },
    { name: 'Telegram', url: 'https://cdn-icons-png.flaticon.com/512/2111/2111646.png' },
    { name: 'Discord', url: 'https://cdn-icons-png.flaticon.com/512/5968/5968756.png' },
]

// --- Form Schema ---

export const formSchema = z.object({
    data: z.string().min(1, 'Content is required').max(2000, 'Content matches limit'),
    bodyShape: z.string(),
    eyeShape: z.string(),
    bodyColor: z.string(),
    bodyColorDark: z.string().optional(),
    eyeColorOuter: z.string(),
    eyeColorInner: z.string(),
    backgroundColor: z.string(),
    gradientLinear: z.boolean(),
    transparentBackground: z.boolean(),
    margin: z.number().min(0).max(10),
    logo: z.object({
        logoPath: z.string().optional(),
        sizeRatio: z.number().min(0.1).max(0.5).optional(),
        padding: z.number().min(0).max(20).optional(),
        backgroundColor: z.string().optional(),
        backgroundEnabled: z.boolean().optional(),
        backgroundRounded: z.boolean().optional(),
        marginSize: z.number().min(0).max(10).optional(),
        removeQuietZone: z.boolean().optional(),
        backgroundCornerRadius: z.number().optional(),
    }).optional(),
    // Advanced Dynamic QR Settings
    enableTracking: z.boolean().optional(),
    trackAnalytics: z.boolean().optional(),
    expirationDays: z.number().min(0).max(365).optional(),
    passwordProtection: z.boolean().optional(),
    password: z.string().optional(), // Actual password value
    apiGateway: z.string().optional(),
    customRedirect: z.string().optional(),
    domainRestriction: z.string().optional(),
    allowedDomains: z.string().optional(), // Comma separated domains
    scanLimit: z.number().min(0).max(10000).optional(),
    rules: z.array(z.any()).optional(),
})

export type FormValues = z.infer<typeof formSchema>

export const Defaults: FormValues = {
    data: 'https://zaipmeio',
    bodyShape: 'SQUARE',
    eyeShape: 'SQUARE',
    bodyColor: '#000000',
    bodyColorDark: '#000000',
    eyeColorOuter: '#000000',
    eyeColorInner: '#000000',
    backgroundColor: '#FFFFFF',
    gradientLinear: false,
    transparentBackground: false,
    margin: 1,
    logo: {
        logoPath: '',
        sizeRatio: 0.2,
        padding: 2,
        backgroundColor: '#FFFFFF',
        backgroundEnabled: true,
        backgroundRounded: true,
        marginSize: 0,
        removeQuietZone: true,
        backgroundCornerRadius: 20
    },
    // Advanced Dynamic QR Settings
    enableTracking: true,
    trackAnalytics: false,
    expirationDays: 0,
    passwordProtection: false,
    password: '',
    apiGateway: 'api-gateway',
    customRedirect: '',
    domainRestriction: 'none',
    allowedDomains: '',
    scanLimit: 1000,
    rules: []
}
