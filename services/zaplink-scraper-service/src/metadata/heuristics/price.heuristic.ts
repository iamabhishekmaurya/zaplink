export class PriceHeuristic {
    detect(html: string): number | null {
        // Extract price from visible elements containing currency symbols (useful for many e-commerce sites)
        // Matches patterns like: >₹ 12,499< or >$49.99< or >Rs. 500<
        const genericPriceMatch = html.match(/>(?:&#8377;|₹|Rs\.?\s*)([1-9]\d{0,2}(?:,\d{2,3})*(?:\.\d{1,2})?)</i);
        if (genericPriceMatch?.[1]) {
            const parsed = parseFloat(genericPriceMatch[1].replace(/,/g, ''));
            if (!isNaN(parsed) && parsed > 0) return parsed;
        }

        // Common CSS class price patterns (too broad but helpful as last resort)
        // Looking for things like <span class="price">49.99</span>
        const classPriceMatch = html.match(/class=["'][^"']*(?:price|amount)[^"']*["]\s*>\s*(?:&#8377;|₹|\$|Rs\.?\s*)?([\d,]+\.?\d*)\s*</i);
        if (classPriceMatch?.[1]) {
            const parsed = parseFloat(classPriceMatch[1].replace(/,/g, ''));
            if (!isNaN(parsed) && parsed > 0) return parsed;
        }

        return null;
    }
}
