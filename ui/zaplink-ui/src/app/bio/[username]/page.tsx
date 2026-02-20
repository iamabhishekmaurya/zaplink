import { Metadata, ResolvingMetadata } from 'next';
import { notFound } from 'next/navigation';

import { BioPageWithTheme } from '@/features/bio-page/types';
import { PublicBioPageViewer } from '@/features/bio-page/ui/public-bio-page-viewer';
import { BioPage, bioPageService as bioService } from '@/services/bioPageService';
import { parseThemeConfig } from '@/ui/design-system/theme-utils';

// Page Props Interface
interface Props {
    params: Promise<{ username: string }>;
    searchParams: Promise<{ [key: string]: string | string[] | undefined }>;
}

// Fetch data logic separate for reusability
async function getBioPage(username: string): Promise<BioPage | null> {
    try {
        const data = await bioService.getPublicBioPage(username);
        if (!data || !data.username) return null;
        return data;
    } catch (error) {
        console.error('Failed to fetch bio page:', error);
        return null;
    }
}

// Generate Dynamic Metadata
export async function generateMetadata(
    { params }: Props,
    parent: ResolvingMetadata
): Promise<Metadata> {
    // read route params
    const { username } = await params;

    // fetch data
    const page = await getBioPage(username);

    if (!page) {
        return {
            title: 'Page Not Found',
        };
    }

    // Access SEO meta
    const seo = page.seoMeta || {};
    const currentParent = await parent;

    return {
        title: seo.title || `${page.title || page.username} | ZapLink`,
        description: seo.description || page.bioText || `Check out ${page.username}'s links on ZapLink.`,
        openGraph: {
            title: seo.title || `${page.title || page.username} | ZapLink`,
            description: seo.description || page.bioText,
            images: [seo.ogImage || page.avatarUrl || '/default-og.png'],
        },
        twitter: {
            card: 'summary_large_image',
            title: seo.title || page.title,
            description: seo.description || page.bioText,
            images: [seo.ogImage || page.avatarUrl || ''],
        },
    };
}

// Main Page Component
export default async function PublicBioPage({ params }: Props) {
    const { username } = await params;
    const page = await getBioPage(username);

    if (!page || !page.isPublic) {
        notFound();
    }

    // Adapt to BioPageWithTheme
    // We assume themeConfig is convertible to ThemeConfig or fallback to default
    const parsedTheme = parseThemeConfig(JSON.stringify(page.themeConfig));

    const bioPageWithTheme: BioPageWithTheme = {
        ...page,
        parsedTheme
    };

    return (
        <PublicBioPageViewer bioPage={bioPageWithTheme} />
    );
}
