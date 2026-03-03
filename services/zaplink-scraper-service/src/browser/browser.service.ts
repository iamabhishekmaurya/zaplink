import { Injectable, Logger } from '@nestjs/common';
import puppeteer from 'puppeteer-extra';
import StealthPlugin from 'puppeteer-extra-plugin-stealth';

puppeteer.use(StealthPlugin());

@Injectable()
export class BrowserService {
    private readonly logger = new Logger(BrowserService.name);

    async fetchHtml(url: string): Promise<string> {
        const puppeteerHtml = await this.fetchWithPuppeteer(url);
        if (puppeteerHtml && puppeteerHtml.length > 500) {
            return puppeteerHtml;
        }

        this.logger.warn(`Puppeteer fetch failed or returned too little data for ${url}, falling back to direct fetch`);
        return this.fetchDirect(url);
    }

    private async fetchWithPuppeteer(url: string): Promise<string | null> {
        let browser = null;
        try {
            browser = await puppeteer.launch({
                headless: true,
                args: [
                    '--no-sandbox',
                    '--disable-setuid-sandbox',
                    '--disable-web-security',
                    '--disable-features=IsolateOrigins,site-per-process',
                    '--disable-blink-features=AutomationControlled',
                ],
            });

            const page = await browser.newPage();
            await page.setUserAgent('Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/121.0.0.0 Safari/537.36');
            await page.setViewport({ width: 1920, height: 1080 });

            // Go to the URL and wait for DOM content
            await page.goto(url, { waitUntil: 'load', timeout: 15000 });

            // Wait for dynamic content
            await new Promise(resolve => setTimeout(resolve, 3000));

            return await page.content();
        } catch (error) {
            this.logger.error(`Puppeteer error fetching ${url}: ${error.message}`);
            return null;
        } finally {
            if (browser) {
                await browser.close().catch(err => this.logger.error(`Error closing browser: ${err.message}`));
            }
        }
    }

    private async fetchDirect(url: string): Promise<string> {
        try {
            const response = await fetch(url, {
                headers: {
                    'User-Agent': 'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/120.0.0.0 Safari/537.36',
                    'Accept': 'text/html,application/xhtml+xml,application/xml;q=0.9,image/avif,*/*;q=0.8',
                },
            });
            return await response.text();
        } catch (error) {
            this.logger.error(`Direct fetch error for ${url}: ${error.message}`);
            return '';
        }
    }
}
