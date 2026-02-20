import { Metadata } from 'next'
import { notFound } from 'next/navigation'
import { PublicBioPageViewer } from '@/features/bio-page/ui/public-bio-page-viewer'
import { BioPageApiResponse, BioPage } from '@/services/bioPageService'
import { BioPageWithTheme } from '@/features/bio-page/types'

// Helper for safe JSON parsing
function safeJsonParse(value: any, fallback: any = undefined) {
  if (!value) return fallback;
  if (typeof value !== 'string') return value;
  try {
    return JSON.parse(value);
  } catch (e) {
    console.warn('JSON Parse Error:', e, value);
    return fallback;
  }
}

async function getBioPage(username: string): Promise<BioPageApiResponse | null> {
  try {
    // Server-side fetch needs absolute URL.
    // Use env var if absolute, otherwise fallback to localhost service
    let baseUrl = process.env.NEXT_PUBLIC_API_BASE || process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8090';

    // If baseUrl is relative (starts with /), prepend localhost for server-side fetch
    if (baseUrl.startsWith('/')) {
      baseUrl = `http://localhost:8090${baseUrl}`;
    }

    const res = await fetch(`${baseUrl}/b/${username}`, {
      next: { revalidate: 60 }, // Revalidate every 60 seconds (ISR)
      headers: {
        'Content-Type': 'application/json',
      },
    });

    if (!res.ok) {
      if (res.status === 404) {
        return null;
      }
      throw new Error(`Failed to fetch bio page: ${res.status} ${res.statusText}`);
    }

    return res.json();
  } catch (error) {
    throw error;
  }
}

// Transform API response to our BioPageWithTheme type
function transformToBioPageWithTheme(apiData: BioPageApiResponse): BioPageWithTheme {
  // Parse theme config safely
  let parsedTheme: any = {
    colors: {
      primary: '#000000',
      background: '#ffffff',
      text: '#000000',
      button: '#000000',
      buttonText: '#ffffff'
    },
    typography: {
      fontFamily: 'Inter'
    },
    layout: {
      style: 'stack', // default
      spacing: 'md',
      alignment: 'center'
    },
    effects: {}
  };

  try {
    if (apiData.theme_config) {
      const config = safeJsonParse(apiData.theme_config, {});
      // Merge with defaults to ensure structure
      if (config.colors) parsedTheme.colors = { ...parsedTheme.colors, ...config.colors };
      if (config.typography) parsedTheme.typography = { ...parsedTheme.typography, ...config.typography };
      if (config.layout) parsedTheme.layout = { ...parsedTheme.layout, ...config.layout };
      if (config.effects) parsedTheme.effects = { ...parsedTheme.effects, ...config.effects };
    }
  } catch (e) {
    console.error("Theme parsing error", e);
    // Silent fail for theme config parsing - use defaults
  }

  // Transform links (using logic similar to BioPageService but minimal)
  const bioLinks = (apiData.bioLinks || []).map(link => ({
    id: String(link.id),
    pageId: String(link.page_id),
    title: link.title,
    url: link.url,
    type: link.type,
    isActive: link.is_active,
    sortOrder: link.sort_order,
    price: link.price,
    currency: link.currency,
    metadata: safeJsonParse(link.metadata, {}),
    scheduleFrom: link.schedule_from,
    scheduleTo: link.schedule_to,
    iconUrl: link.icon_url,
    thumbnailUrl: link.thumbnail_url,
    createdAt: link.created_at,
    updatedAt: link.updated_at,
  }));

  // Construct BioPage object
  const bioPage: BioPage = {
    id: String(apiData.id),
    username: apiData.username,
    ownerId: apiData.owner_id,
    bioText: apiData.bio_text,
    avatarUrl: apiData.avatar_url,
    themeConfig: parsedTheme as any, // Cast to any or match type. parsedTheme is effectively BioPageThemeConfig
    title: apiData.title,
    coverUrl: apiData.cover_url,
    seoMeta: safeJsonParse(apiData.seo_meta),
    isPublic: apiData.is_public ?? true,
    createdAt: apiData.created_at,
    updatedAt: apiData.updated_at,
    effects: apiData.effects || {
      backgroundType: 'solid',
      particles: false,
    },
    bioLinks: bioLinks
  };

  return {
    ...bioPage,
    parsedTheme
  };
}

export async function generateMetadata({ params }: { params: Promise<{ username: string }> }): Promise<Metadata> {
  try {
    const { username } = await params;
    const data = await getBioPage(username);

    if (!data) {
      return {
        title: 'Page Not Found',
      };
    }

    const title = data.title || `@${data.username} | Zaplink`;
    const description = data.bio_text || `Check out @${data.username}'s bio page on Zaplink.`;

    return {
      title: title,
      description: description,
      openGraph: {
        title: title,
        description: description,
        images: data.avatar_url ? [data.avatar_url] : [],
      },
      twitter: {
        card: 'summary_large_image',
        title: title,
        description: description,
        images: data.avatar_url ? [data.avatar_url] : [],
      }
    };
  } catch (e) {
    return {
      title: 'Error',
    };
  }
}

export default async function Page({ params }: { params: Promise<{ username: string }> }) {
  try {
    const { username } = await params;
    const data = await getBioPage(username);

    if (!data) {
      notFound();
    }

    const bioPage = transformToBioPageWithTheme(data);

    return <PublicBioPageViewer bioPage={bioPage} />;
  } catch (error) {
    console.error("Error loading bio page:", error);
    // Return error page instead of crashing
    return (
      <div className="min-h-screen flex items-center justify-center bg-background">
        <div className="text-center space-y-4 p-8">
          <h1 className="text-2xl font-bold text-foreground">Something went wrong</h1>
          <p className="text-muted-foreground">Unable to load this bio page. Please try again later.</p>
          <a
            href="/"
            className="inline-flex items-center px-4 py-2 bg-primary text-primary-foreground rounded-md hover:bg-primary/90 transition-colors"
          >
            Go Home
          </a>
        </div>
      </div>
    );
  }
}
