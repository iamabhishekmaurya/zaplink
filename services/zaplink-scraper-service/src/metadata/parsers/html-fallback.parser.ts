import { MetadataResult, ParseContext } from '../../common/types/metadata.types';
import { decodeHtmlEntities } from '../../common/utils/html.utils';

export class HtmlFallbackParser {
    parse(ctx: ParseContext): Partial<MetadataResult> {
        const { html, sourceUrl, getMetaContent } = ctx;

        const title =
            getMetaContent('twitter:title') || getMetaContent('title') ||
            html.match(/<title[^>]*>([^<]*)<\/title>/i)?.[1]?.trim() || null;

        const description = getMetaContent('twitter:description') || getMetaContent('description');

        const image = getMetaContent('twitter:image') || getMetaContent('twitter:image:src') || getMetaContent('image');

        return {
            title: title ? decodeHtmlEntities(title) : null,
            description: description ? decodeHtmlEntities(description) : null,
            image: image,
            favicon: this.extractFavicon(html, sourceUrl),
        };
    }

    private extractFavicon(html: string, sourceUrl: string): string | null {
        const patterns = [
            /<link[^>]*rel\s*=\s*["'](?:shortcut )?icon["'][^>]*href\s*=\s*["']([^"']*)["']/i,
            /<link[^>]*href\s*=\s*["']([^"']*)["'][^>]*rel\s*=\s*["'](?:shortcut )?icon["']/i,
            /<link[^>]*rel\s*=\s*["']apple-touch-icon["'][^>]*href\s*=\s*["']([^"']*)["']/i,
        ];
        for (const pattern of patterns) {
            const match = html.match(pattern);
            if (match?.[1]) {
                let fav = match[1];
                if (!fav.startsWith('http')) {
                    try {
                        fav = new URL(fav, sourceUrl).toString();
                    } catch {
                        continue;
                    }
                }
                return fav;
            }
        }
        try {
            return new URL('/favicon.ico', sourceUrl).toString();
        } catch {
            return null;
        }
    }
}
