import { Metadata } from 'next'
import { notFound } from 'next/navigation'
import { PublicBioPageViewer } from '@/features/bio-page/ui/public-bio-page-viewer'
import { BioPageApiResponse, BioPage } from '@/services/bioPageService'
import { BioPageWithTheme } from '@/features/bio-page/types'
import { handleApiError } from '@/lib/error-handler'

// Since we cannot use the client-side axios interceptors in a server component easily
// without comprehensive cookie handling, we'll use standard fetch for public data.
// Public bio pages are unauthenticated GET requests, so no token needed.

async function getBioPage(username: string): Promise<BioPageApiResponse | null> {
  try {
    const baseUrl = process.env.NEXT_PUBLIC_API_BASE || process.env.NEXT_PUBLIC_API_URL || 'http://localhost:8090';
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
      const config = JSON.parse(apiData.theme_config);
      // Merge with defaults to ensure structure
      if (config.colors) parsedTheme.colors = { ...parsedTheme.colors, ...config.colors };
      if (config.typography) parsedTheme.typography = { ...parsedTheme.typography, ...config.typography };
      if (config.layout) parsedTheme.layout = { ...parsedTheme.layout, ...config.layout };
      if (config.effects) parsedTheme.effects = { ...parsedTheme.effects, ...config.effects };
    }
  } catch {
    // Silent fail for theme config parsing - use defaults
  }

  // Transform links (using logic similar to BioPageService but minimal)
  const bioLinks = (apiData.bioLinks || []).map(link => ({
    id: link.id,
    pageId: link.page_id,
    title: link.title,
    url: link.url,
    type: link.type,
    isActive: link.is_active,
    sortOrder: link.sort_order,
    price: link.price,
    currency: link.currency,
    metadata: link.metadata,
    scheduleFrom: link.schedule_from,
    scheduleTo: link.schedule_to,
    iconUrl: link.icon_url,
    thumbnailUrl: link.thumbnail_url,
    createdAt: link.created_at,
    updatedAt: link.updated_at,
  }));

  // Construct BioPage object
  const bioPage: BioPage = {
    id: apiData.id,
    username: apiData.username,
    ownerId: apiData.owner_id,
    bioText: apiData.bio_text,
    avatarUrl: apiData.avatar_url,
    themeConfig: apiData.theme_config,
    title: apiData.title,
    coverUrl: apiData.cover_url,
    seoMeta: apiData.seo_meta,
    isPublic: apiData.is_public ?? true,
    createdAt: apiData.created_at,
    updatedAt: apiData.updated_at,
    bioLinks: bioLinks
  };

  return {
    ...bioPage,
    parsedTheme
  };
}

export async function generateMetadata({ params }: { params: { username: string } }): Promise<Metadata> {
  const data = await getBioPage(params.username);

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
}

export default async function Page({ params }: { params: { username: string } }) {
  try {
    const data = await getBioPage(params.username);

    if (!data) {
      notFound();
    }

    const bioPage = transformToBioPageWithTheme(data);

    return <PublicBioPageViewer bioPage={bioPage} />;
  } catch {
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
