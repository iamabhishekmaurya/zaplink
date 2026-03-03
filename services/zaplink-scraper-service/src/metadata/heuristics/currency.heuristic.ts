export class CurrencyHeuristic {
    detect(html: string, price: number | null): string | null {
        if (html.includes('&#8377;') || html.includes('₹') || html.match(/\bRs\.?\s*\d/i)) {
            return 'INR';
        }

        if (html.includes('$')) {
            return 'USD';
        }

        if (html.includes('€') || html.includes('&euro;')) {
            return 'EUR';
        }

        if (html.includes('£') || html.includes('&pound;')) {
            return 'GBP';
        }

        return null;
    }
}
