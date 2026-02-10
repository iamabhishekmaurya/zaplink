import { BioPage, BioLink } from "@/services/bioPageService";
import { ThemeConfig } from "@/ui/design-system/theme-utils";

export type BioPageLinkType = 'LINK' | 'SOCIAL' | 'PRODUCT' | 'EMAIL' | 'PHONE' | 'EMBED' | 'SCHEDULED' | 'GATED' | 'PAYMENT';

export interface BioLinkMetadata {
    description?: string;
    sku?: string;
    embedCode?: string;
    gatedContent?: {
        type: 'email' | 'password' | 'payment';
        value?: string;
        gateMessage?: string;
    };
}

export type BioPageWithTheme = BioPage & {
    parsedTheme: ThemeConfig;
};

export interface EditorState {
    isSaving: boolean;
    hasUnsavedChanges: boolean;
    activeTab: 'links' | 'design' | 'settings' | 'analytics';
    selectedLink?: BioLink;
    previewMode: 'mobile' | 'desktop';
}

export interface LinkFormData {
    title: string;
    url: string;
    type: BioPageLinkType;
    isActive: boolean;
    price?: number;
    currency?: string;
    scheduleFrom?: Date | null;
    scheduleTo?: Date | null;
    thumbnailUrl?: string;
    iconUrl?: string;
    // Metadata fields flattened for form
    description?: string;
    sku?: string;
    embedCode?: string;
    gateType?: 'email' | 'password' | 'payment';
    gateValue?: string;
    gateMessage?: string;
}

export const SUPPORTED_SOCIAL_PLATFORMS = [
    'instagram', 'twitter', 'tiktok', 'youtube', 'facebook', 'linkedin', 'spotify', 'twitch', 'github', 'discord'
] as const;

export type SocialPlatform = typeof SUPPORTED_SOCIAL_PLATFORMS[number];
