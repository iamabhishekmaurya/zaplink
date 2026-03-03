import { MetadataResult, ParseContext } from '../../common/types/metadata.types';

export class MicrodataParser {
    parse(ctx: ParseContext): Partial<MetadataResult> {
        const { getMetaContent } = ctx;

        return {
            price: parseFloat(getMetaContent('price')?.replace(/[^0-9.]/g, '') || '0') || null,
            currency: getMetaContent('priceCurrency'),
            image: getMetaContent('image'),
        };
    }
}
