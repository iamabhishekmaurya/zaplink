import { normalizeUrl } from '../../common/utils/url.utils';

export class ImageHeuristic {
    private readonly junkWords = [
        'data:image', 'pixel', 'logo', 'icon', 'sprite', 'svg', 'avatar',
        'tracker', 'badge', 'button', 'promo', 'banner', 'nav', 'spinner',
        'placeholder', 'blank', 'transparent', '1x1', 'rating', 'star',
        'arrow', 'video-play', 'thumbnail-play', 'feature', 'a-dynamic', 'captcha'
    ];

    filterAndDedupe(images: string[], sourceUrl: string): string[] {
        const normalized = images
            .filter(Boolean)
            .map(img => normalizeUrl(img, sourceUrl))
            .filter(img => !this.isIrrelevantImage(img));

        return this.deduplicate(normalized);
    }

    private isIrrelevantImage(url: string): boolean {
        const lowerUrl = url.toLowerCase();
        return this.junkWords.some(word => lowerUrl.includes(word));
    }

    private deduplicate(images: string[]): string[] {
        const uniqueImageSets = new Map<string, string>();

        for (const img of images) {
            const baseId = this.getBaseImageId(img);
            // Keep the one with the longer URL (usually contains more details or higher resolution)
            // or just keep the first one if length is same
            if (!uniqueImageSets.has(baseId) || img.length > uniqueImageSets.get(baseId)!.length) {
                uniqueImageSets.set(baseId, img);
            }
        }

        return Array.from(uniqueImageSets.values());
    }

    private getBaseImageId(url: string): string {
        try {
            const urlObj = new URL(url);
            const path = urlObj.pathname;

            // Amazon/Flipkart specific base ID extraction
            if (urlObj.hostname.includes('amazon') || urlObj.hostname.includes('ssl-images-amazon') || urlObj.hostname.includes('media-amazon')) {
                const match = path.match(/\/I\/([^._]+)(?:\..*)?$/);
                if (match) return match[1];
            }

            // Generic base ID: remove resolution modifiers like -300x300, _S120_, etc.
            return path.replace(/[-_][a-zA-Z0-9]+(\.[a-zA-Z]+)$/, '$1');
        } catch {
            return url;
        }
    }
}
