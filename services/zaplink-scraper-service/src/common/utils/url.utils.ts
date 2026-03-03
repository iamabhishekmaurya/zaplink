export function normalizeUrl(url: string, baseUrl: string): string {
    if (!url) return '';
    if (url.startsWith('http')) return url;

    try {
        return new URL(url, baseUrl).toString();
    } catch {
        return url;
    }
}

export function isValidImageUrl(url: string | null): boolean {
    if (!url) return false;
    return url.match(/\.(jpeg|jpg|gif|png|webp|avif|svg)/i) !== null || url.startsWith('data:image');
}
