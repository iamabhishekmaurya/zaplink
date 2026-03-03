import { MetadataResult } from '../../common/types/metadata.types';
import { SitePlugin } from './site-plugin.interface';
import { decodeHtmlEntities } from '../../common/utils/html.utils';

export class FlipkartPlugin implements SitePlugin {
    readonly name = 'Flipkart';

    matches(url: string): boolean {
        return url.includes('flipkart.com');
    }

    enhance(result: MetadataResult, html: string, sourceUrl: string): MetadataResult {
        if (result.title?.includes('reCAPTCHA')) return result;

        // Flipkart Title Override (span with class VU-Tz5)
        const flipkartTitle = html.match(/<span class="VU-Tz5[^>]*>([^<]*)<\/span>/i);
        if (flipkartTitle?.[1] && (!result.title || result.title.length < 5)) {
            result.title = decodeHtmlEntities(flipkartTitle[1]);
        }

        // Flipkart Image Override (img with class DByuf4)
        if (!result.image || result.image.includes('logo')) {
            const flipkartImg = html.match(/<img[^>]*class="DByuf4[^>]*src=["']([^"']*)["']/i);
            if (flipkartImg?.[1]) {
                result.image = flipkartImg[1];
                if (!result.images.includes(result.image)) {
                    result.images.unshift(result.image);
                }
            }
        }

        // Flipkart Price Override (div with class Nx9bqj)
        const flipkartPriceMatch = html.match(/<div[^>]*class="[^"]*Nx9bqj[^"]*"[^>]*>(?:&#8377;|₹)?([\d,]+)<\/div>/i);
        if (flipkartPriceMatch?.[1]) {
            const parsed = parseFloat(flipkartPriceMatch[1].replace(/,/g, ''));
            if (!isNaN(parsed) && parsed > 0) result.price = parsed;
        }

        return result;
    }
}

export const flipkartPlugin = new FlipkartPlugin();
