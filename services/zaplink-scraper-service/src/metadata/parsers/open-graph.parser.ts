import { MetadataResult, ParseContext } from '../../common/types/metadata.types';

export class OpenGraphParser {
    parse(ctx: ParseContext): Partial<MetadataResult> {
        const { getMetaContent, getMetaContentArray } = ctx;

        const price = getMetaContent('og:price:amount') || getMetaContent('product:price:amount');
        const currency = getMetaContent('og:price:currency') || getMetaContent('product:price:currency');

        return {
            title: getMetaContent('og:title'),
            description: getMetaContent('og:description'),
            image: getMetaContent('og:image') || getMetaContent('og:image:url'),
            images: [
                ...getMetaContentArray('og:image'),
                ...getMetaContentArray('og:image:url'),
            ],
            videos: [
                ...getMetaContentArray('og:video'),
                ...getMetaContentArray('og:video:url'),
            ],
            siteName: getMetaContent('og:site_name'),
            price: price ? parseFloat(price.replace(/[^0-9.]/g, '')) : null,
            currency: currency,
        };
    }
}
