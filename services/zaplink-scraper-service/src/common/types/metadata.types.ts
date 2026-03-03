export interface MetadataResult {
    title: string | null;
    description: string | null;
    image: string | null;
    images: string[];
    videos: string[];
    price: number | null;
    currency: string | null;
    siteName: string | null;
    favicon: string | null;
    error?: string | null;
}

export const EMPTY_RESULT: MetadataResult = {
    title: null,
    description: null,
    image: null,
    images: [],
    videos: [],
    price: null,
    currency: null,
    siteName: null,
    favicon: null,
};

export interface ParseContext {
    html: string;
    sourceUrl: string;
    getMetaContent: (property: string) => string | null;
    getMetaContentArray: (property: string) => string[];
}
