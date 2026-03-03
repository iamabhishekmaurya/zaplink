import { MetadataResult } from '../../common/types/metadata.types';
import { SitePlugin } from './site-plugin.interface';
import { decodeHtmlEntities } from '../../common/utils/html.utils';

export class AmazonPlugin implements SitePlugin {
    readonly name = 'Amazon';

    matches(url: string): boolean {
        return url.includes('amazon') || url.includes('amzn.');
    }

    enhance(result: MetadataResult, html: string, sourceUrl: string): MetadataResult {
        if (result.title?.includes('Bot Check')) return result;

        // Amazon Title Override
        const amazonTitle = html.match(/<span[^>]*id="productTitle"[^>]*>([^<]+)<\/span>/i) ||
            html.match(/<h1[^>]*id="title"[^>]*>.*?<span[^>]*>([^<]+)<\/span>.*?<\/h1>/i);

        if (amazonTitle?.[1] && (!result.title || result.title.length < 5 || result.title.includes('Amazon'))) {
            result.title = decodeHtmlEntities(amazonTitle[1].trim());
        }

        // Amazon Images (from landing image)
        const amazonImg = html.match(/<img[^>]*id="landingImage"[^>]*src=["']([^"']*)["']/i) ||
            html.match(/<img[^>]*id="imgBlkFront"[^>]*src=["']([^"']*)["']/i) ||
            html.match(/data-old-hires=["']([^"']*)["']/i);

        if (amazonImg?.[1]) {
            if (!result.images.includes(amazonImg[1])) {
                result.images.unshift(amazonImg[1]);
                result.image = amazonImg[1];
            }
        }

        // Amazon High-res dynamic images
        const dynImgMatches = html.match(/data-a-dynamic-image=["']([^"']*)["']/gi);
        if (dynImgMatches) {
            for (const match of dynImgMatches) {
                const payload = match.match(/data-a-dynamic-image=["']([^"']*)["']/i)?.[1];
                if (payload) {
                    try {
                        const decoded = decodeHtmlEntities(payload);
                        const imgObj = JSON.parse(decoded!);
                        const highResImages = Object.entries(imgObj).filter(([_, dims]: [string, any]) => {
                            return Array.isArray(dims) && dims.some((dim: number) => dim >= 600);
                        }).map(([url]) => url);

                        result.images.push(...highResImages);
                    } catch { /* skip */ }
                }
            }
        }

        // Amazon Price Override
        const amazonPriceMatch = html.match(/<span[^>]*class="[^"]*a-price-whole[^"]*"[^>]*>([^<]+)<\/span>/i);
        if (amazonPriceMatch?.[1]) {
            const cleaned = amazonPriceMatch[1].replace(/,/g, '').replace(/\.$/, '').trim();
            const parsed = parseFloat(cleaned);
            if (!isNaN(parsed) && parsed > 0) result.price = parsed;
        }

        return result;
    }
}

export const amazonPlugin = new AmazonPlugin();
