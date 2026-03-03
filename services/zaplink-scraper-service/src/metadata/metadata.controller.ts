import { Controller, Post, Body, BadRequestException } from '@nestjs/common';
import { MetadataService } from './metadata.service';
import { BrowserService } from '../browser/browser.service';
import { MetadataResult } from '../common/types/metadata.types';

@Controller('metadata')
export class MetadataController {
    constructor(
        private readonly metadataService: MetadataService,
        private readonly browserService: BrowserService,
    ) { }

    @Post()
    async scrape(@Body('url') url: string): Promise<MetadataResult> {
        if (!url) {
            throw new BadRequestException('URL is required');
        }

        try {
            new URL(url);
        } catch {
            throw new BadRequestException('Invalid URL format');
        }

        const html = await this.browserService.fetchHtml(url);
        const result = await this.metadataService.extract(html, url);
        return { ...result, html } as any;
    }
}
