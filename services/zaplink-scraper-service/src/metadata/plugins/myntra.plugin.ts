import { MetadataResult } from '../../common/types/metadata.types';
import { SitePlugin } from './site-plugin.interface';

export class MyntraPlugin implements SitePlugin {
    readonly name = 'Myntra';

    matches(url: string): boolean {
        return url.includes('myntra.com');
    }

    enhance(result: MetadataResult, html: string, sourceUrl: string): MetadataResult {
        try {
            const myxMatch = html.match(/window\.__myx\s*=\s*({.*?});/i);
            if (!myxMatch) return result;

            // JSON.parse might fail if the HTML match is slightly off (e.g. nested braces)
            // But usually this works for window state variables
            const data = JSON.parse(myxMatch[1]);
            const pdpData = data.pdpData;

            if (pdpData) {
                // Title - Preferred over OG title which often has extra "Buy..." text
                if (pdpData.name) {
                    result.title = pdpData.name;
                }

                // Brand
                if (pdpData.brand?.name) {
                    result.siteName = pdpData.brand.name;
                }

                // Price - Extract the actual selling price from the first available size
                if (pdpData.sizes && pdpData.sizes.length > 0) {
                    const firstSize = pdpData.sizes.find((s: any) => s.available);
                    if (firstSize?.sizeSellerData?.[0]?.discountedPrice) {
                        result.price = firstSize.sizeSellerData[0].discountedPrice;
                        result.currency = 'INR';
                    } else if (pdpData.mrp) {
                        result.price = pdpData.mrp;
                        result.currency = 'INR';
                    }
                }

                // Images - Extract the full gallery
                if (pdpData.media?.albums) {
                    const allImages: string[] = [];
                    for (const album of pdpData.media.albums) {
                        if (album.images) {
                            for (const img of album.images) {
                                let src = img.secureSrc || img.src || img.imageURL;
                                if (src) {
                                    // Replace placeholders with high-res values
                                    src = src.replace(/\(\$height\)/g, '1440')
                                        .replace(/\(\$qualityPercentage\)/g, '75')
                                        .replace(/\(\$width\)/g, '1080');
                                    allImages.push(src);
                                }
                            }
                        }
                    }

                    if (allImages.length > 0) {
                        const uniqueImages = Array.from(new Set(allImages));
                        // Prepend these as they are guaranteed high quality gallery images
                        result.images = [...uniqueImages, ...result.images];
                    }
                }
            }
        } catch (e) {
            // Silently fail and return existing results if parsing fails
            console.warn('MyntraPlugin: Failed to parse state JSON', e);
        }

        return result;
    }
}

export const myntraPlugin = new MyntraPlugin();
