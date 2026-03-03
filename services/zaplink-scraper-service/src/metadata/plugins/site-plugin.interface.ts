import { MetadataResult } from '../../common/types/metadata.types';

export interface SitePlugin {
    readonly name: string;
    matches(url: string): boolean;
    enhance(result: MetadataResult, html: string, sourceUrl: string): MetadataResult;
}
