export function escapeRegex(str: string): string {
    return str.replace(/[.*+?^${}()|[\]\\]/g, '\\$&');
}

export function decodeHtmlEntities(str: string | null): string | null {
    if (!str) return null;
    return str
        .replace(/&amp;/g, '&')
        .replace(/&lt;/g, '<')
        .replace(/&gt;/g, '>')
        .replace(/&quot;/g, '"')
        .replace(/&#039;/g, "'")
        .replace(/&#x27;/g, "'")
        .replace(/&#x2F;/g, '/')
        .replace(/&#(\d+);/g, (_, num) => String.fromCharCode(parseInt(num)))
        .replace(/&#x([a-fA-F0-9]+);/g, (_, hex) => String.fromCharCode(parseInt(hex, 16)));
}

export function createMetaHelpers(html: string) {
    const getMetaContent = (property: string): string | null => {
        const escaped = escapeRegex(property);
        const patterns = [
            new RegExp(`<meta[^>]*property\\s*=\\s*["']${escaped}["'][^>]*content\\s*=\\s*["']([^"']*)["']`, 'i'),
            new RegExp(`<meta[^>]*content\\s*=\\s*["']([^"']*)["'][^>]*property\\s*=\\s*["']${escaped}["']`, 'i'),
            new RegExp(`<meta[^>]*name\\s*=\\s*["']${escaped}["'][^>]*content\\s*=\\s*["']([^"']*)["']`, 'i'),
            new RegExp(`<meta[^>]*content\\s*=\\s*["']([^"']*)["'][^>]*name\\s*=\\s*["']${escaped}["']`, 'i'),
            new RegExp(`<meta[^>]*itemprop\\s*=\\s*["']${escaped}["'][^>]*content\\s*=\\s*["']([^"']*)["']`, 'i'),
        ];
        for (const pattern of patterns) {
            const match = html.match(pattern);
            if (match?.[1]) return decodeHtmlEntities(match[1].trim());
        }
        return null;
    };

    const getMetaContentArray = (property: string): string[] => {
        const escaped = escapeRegex(property);
        const patterns = [
            new RegExp(`<meta[^>]*property\\s*=\\s*["']${escaped}["'][^>]*content\\s*=\\s*["']([^"']*)["']`, 'gi'),
            new RegExp(`<meta[^>]*content\\s*=\\s*["']([^"']*)["'][^>]*property\\s*=\\s*["']${escaped}["']`, 'gi'),
            new RegExp(`<meta[^>]*name\\s*=\\s*["']${escaped}["'][^>]*content\\s*=\\s*["']([^"']*)["']`, 'gi'),
            new RegExp(`<meta[^>]*content\\s*=\\s*["']([^"']*)["'][^>]*name\\s*=\\s*["']${escaped}["']`, 'gi'),
        ];
        const results = new Set<string>();
        for (const pattern of patterns) {
            let match;
            while ((match = pattern.exec(html)) !== null) {
                if (match[1]) results.add(decodeHtmlEntities(match[1].trim())!);
            }
        }
        return Array.from(results);
    };

    return { getMetaContent, getMetaContentArray };
}
