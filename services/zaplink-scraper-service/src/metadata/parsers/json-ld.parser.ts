import { MetadataResult, ParseContext } from '../../common/types/metadata.types';

export class JsonLdParser {
    parse(ctx: ParseContext): Partial<MetadataResult> {
        const { html } = ctx;
        const jsonLdBlocks = html.match(/<script[^>]*type\s*=\s*["']application\/ld\+json["'][^>]*>([\s\S]*?)<\/script>/gi);

        const results: Partial<MetadataResult> = {
            images: [],
            price: null,
            currency: null
        };

        if (jsonLdBlocks) {
            for (const block of jsonLdBlocks) {
                try {
                    const jsonContent = block.replace(/<script[^>]*>/i, '').replace(/<\/script>/i, '').trim();
                    const data = JSON.parse(jsonContent);

                    const extractedImages = this.findImagesInJsonLd(data);
                    results.images!.push(...extractedImages);

                    const price = this.findPriceInJsonLd(data);
                    if (price && !results.price) results.price = price;

                    const currency = this.findCurrencyInJsonLd(data);
                    if (currency && !results.currency) results.currency = currency;
                } catch { /* skip */ }
            }
        }

        return results;
    }

    private findImagesInJsonLd(data: any): string[] {
        let images: string[] = [];
        if (!data) return images;

        if (typeof data === 'string' && data.startsWith('http') && data.match(/\.(jpeg|jpg|gif|png|webp|avif)/i)) {
            return [data];
        }

        if (Array.isArray(data)) {
            for (const item of data) {
                images.push(...this.findImagesInJsonLd(item));
            }
        } else if (typeof data === 'object') {
            if (data.image) {
                if (typeof data.image === 'string') {
                    images.push(data.image);
                } else if (Array.isArray(data.image)) {
                    data.image.forEach((img: any) => {
                        if (typeof img === 'string') images.push(img);
                        else if (img?.url) images.push(img.url);
                    });
                } else if (data.image.url) {
                    images.push(data.image.url);
                }
            }

            if (data['@graph']) {
                images.push(...this.findImagesInJsonLd(data['@graph']));
            }
        }
        return images;
    }

    private findPriceInJsonLd(data: any): number | null {
        if (!data) return null;
        if (data['@graph'] && Array.isArray(data['@graph'])) {
            for (const item of data['@graph']) {
                const price = this.findPriceInJsonLd(item);
                if (price) return price;
            }
        }
        if (data['@type'] === 'Product' || data['@type']?.includes?.('Product')) {
            const offers = data.offers;
            if (offers) {
                const offerObj = Array.isArray(offers) ? offers[0] : offers;
                if (offerObj?.price) {
                    const parsed = parseFloat(String(offerObj.price));
                    if (!isNaN(parsed) && parsed > 0) return parsed;
                }
                if (offerObj?.lowPrice) {
                    const parsed = parseFloat(String(offerObj.lowPrice));
                    if (!isNaN(parsed) && parsed > 0) return parsed;
                }
            }
        }
        if (data['@type'] === 'Offer' || data['@type'] === 'AggregateOffer') {
            if (data.price) {
                const parsed = parseFloat(String(data.price));
                if (!isNaN(parsed) && parsed > 0) return parsed;
            }
        }
        return null;
    }

    private findCurrencyInJsonLd(data: any): string | null {
        if (!data) return null;
        if (data['@graph'] && Array.isArray(data['@graph'])) {
            for (const item of data['@graph']) {
                const currency = this.findCurrencyInJsonLd(item);
                if (currency) return currency;
            }
        }
        if (data['@type'] === 'Product' || data['@type']?.includes?.('Product')) {
            const offers = data.offers;
            if (offers) {
                const offerObj = Array.isArray(offers) ? offers[0] : offers;
                if (offerObj?.priceCurrency) return offerObj.priceCurrency;
            }
        }
        if (data['@type'] === 'Offer' || data['@type'] === 'AggregateOffer') {
            if (data.priceCurrency) return data.priceCurrency;
        }
        return null;
    }
}
