import { Injectable } from '@nestjs/common';
import { MetadataResult, EMPTY_RESULT } from '../common/types/metadata.types';
import { createMetaHelpers } from '../common/utils/html.utils';
import { OpenGraphParser } from './parsers/open-graph.parser';
import { JsonLdParser } from './parsers/json-ld.parser';
import { MicrodataParser } from './parsers/microdata.parser';
import { HtmlFallbackParser } from './parsers/html-fallback.parser';
import { PriceHeuristic } from './heuristics/price.heuristic';
import { ImageHeuristic } from './heuristics/image.heuristic';
import { CurrencyHeuristic } from './heuristics/currency.heuristic';
import { SitePlugin } from './plugins/site-plugin.interface';
import { amazonPlugin } from './plugins/amazon.plugin';
import { flipkartPlugin } from './plugins/flipkart.plugin';
import { myntraPlugin } from './plugins/myntra.plugin';

@Injectable()
export class MetadataService {
    private readonly ogParser = new OpenGraphParser();
    private readonly jsonLdParser = new JsonLdParser();
    private readonly microParser = new MicrodataParser();
    private readonly fallbackParser = new HtmlFallbackParser();

    private readonly priceHeuristic = new PriceHeuristic();
    private readonly imageHeuristic = new ImageHeuristic();
    private readonly currencyHeuristic = new CurrencyHeuristic();

    private readonly plugins: SitePlugin[] = [
        amazonPlugin,
        flipkartPlugin,
        myntraPlugin,
    ];

    extract(html: string, sourceUrl: string): MetadataResult {
        if (!html || html.length < 100) return EMPTY_RESULT;

        const { getMetaContent, getMetaContentArray } = createMetaHelpers(html);
        const ctx = { html, sourceUrl, getMetaContent, getMetaContentArray };

        // Layer 1: Generic Parsers
        const ogData = this.ogParser.parse(ctx);
        const jsonLdData = this.jsonLdParser.parse(ctx);
        const microData = this.microParser.parse(ctx);
        const fallbackData = this.fallbackParser.parse(ctx);

        // Merge Layer 1 results (prioritize OG > JSON-LD > Microdata > Fallback)
        let result: MetadataResult = { ...EMPTY_RESULT };

        const layers = [fallbackData, microData, jsonLdData, ogData];
        for (const layer of layers) {
            if (layer.title) result.title = layer.title;
            if (layer.description) result.description = layer.description;
            if (layer.image) result.image = layer.image;
            if (layer.price !== undefined && layer.price !== null) result.price = layer.price;
            if (layer.currency) result.currency = layer.currency;
            if (layer.siteName) result.siteName = layer.siteName;
            if (layer.favicon) result.favicon = layer.favicon;
        }
        // Ensure images from all layers are combined
        result.images = Array.from(new Set([
            ...(ogData.images || []),
            ...(jsonLdData.images || []),
            ...(microData.image ? [microData.image] : []),
            ...(fallbackData.image ? [fallbackData.image] : []),
            ...(result.image ? [result.image] : []),
        ])).filter(Boolean);

        // Layer 2: Heuristics for missing fields
        if (!result.price) {
            result.price = this.priceHeuristic.detect(html);
        }

        if (!result.currency) {
            result.currency = this.currencyHeuristic.detect(html, result.price);
        }

        // Layer 3: Site Specific Overrides
        for (const plugin of this.plugins) {
            if (plugin.matches(sourceUrl)) {
                result = plugin.enhance(result, html, sourceUrl);
                break;
            }
        }

        // Final cleanup: Deduplicate images after all enhancements
        result.images = this.imageHeuristic.filterAndDedupe(result.images, sourceUrl);
        if (result.images.length > 0) {
            result.image = result.images[0];
        }

        return result;
    }
}
